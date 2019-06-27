package com.zm.friendCircle.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.zm.entity.ChatFriendDAO;
import com.zm.entity.KeyContentDAO;
import com.zm.entity.UserInfo;
import com.zm.entity.UserInfoDAO;
import com.zm.friendCircle.entity.ZmFriendCircle;
import com.zm.friendCircle.entity.ZmFriendCircleComment;
import com.zm.friendCircle.entity.ZmFriendCircleCommentDAO;
import com.zm.friendCircle.entity.ZmFriendCircleDAO;
import com.zm.friendCircle.entity.ZmFriendCircleLike;
import com.zm.friendCircle.entity.ZmFriendCircleLikeDAO;
import com.zm.friendCircle.entity.ZmNetworkArticle;
import com.zm.friendCircle.entity.ZmNetworkArticleDAO;


@Service
@Transactional
public class NetworkArticleService {

	@Autowired
	private KeyContentDAO  keyContentDao; //获取工人工作类型
	@Autowired
	private ZmFriendCircleDAO friendCircleDao; //总表Dao
	@Autowired
	private ZmNetworkArticleDAO networkArticleDao; //说说Dao
	@Autowired
	private ZmFriendCircleCommentDAO friendCircleCommentDao; //评论Dao
	@Autowired
	private ZmFriendCircleLikeDAO  friendCircleLikeDao; //点赞Dao
	@Autowired
	private ChatFriendDAO  chatFriendDao; //朋友列表
	@Autowired
	private UserInfoDAO    userInfoDao; //用户信息
	
	
	@Test 
	public void test(){
		
		ApplicationContext context = new FileSystemXmlApplicationContext("/src/main/resources/spring/spring-dao.xml" );
		keyContentDao = (KeyContentDAO) context.getBean("keyContentDao");
		friendCircleDao = (ZmFriendCircleDAO) context.getBean("friendCircleDao");
		friendCircleCommentDao = (ZmFriendCircleCommentDAO) context.getBean("friendCircleCommentDao");
		friendCircleLikeDao = (ZmFriendCircleLikeDAO) context.getBean("friendCircleLikeDao");
		chatFriendDao = (ChatFriendDAO)context.getBean("chatFriendDao");
		userInfoDao = (UserInfoDAO)context.getBean("userInfoDao");
		networkArticleDao = (ZmNetworkArticleDAO)context.getBean("networkArticleDao");
//		List list = getWorkClassify();
//		System.out.println( list );
		
//		String str = addEmployee( 1793, "1234565", new Date(),  19 ); //木工
//		System.out.println( str );
		
//		String str = addLike(2, 1792);
//		System.out.println( str );	
		
//		addNetworkArticle(4, 1793, "这是一篇网络文章", "http://www.baidu.com");
		
		List list = getNetworArticleList(4, 1793, 0, 10);
		System.out.println( list );
	}
	
	
	/**
	 * 1.获取网络文章说说
	 * */
	public List getNetworArticleList( int index, int userId , int startPage, int length ){
		
		//1. 获取自己的user_id,获取自己的好友user_id
				//2. 根据user_id 去查询说说记录
				//3. 获取到说说记录之后，再根据说说记录，获取点赞列表，评论列表，图片列表	
				List articleList =  networkArticleDao.findByPagenation(startPage, length);
				if( articleList != null ){
					for( int i=0; i<articleList.size(); i++ ){
						
						ZmNetworkArticle employee = (ZmNetworkArticle)articleList.get(i);
						fillWithOtherDataInArticle(employee, index );
					}
				}
				return articleList;
	}
	
	public ZmNetworkArticle  getNetworkArticleById( int  articleId, int index ){
		
		ZmNetworkArticle employee = networkArticleDao.findById( articleId );
		return fillWithOtherDataInArticle(employee, index );
	}
	
	
	public ZmNetworkArticle  fillWithOtherDataInArticle(  ZmNetworkArticle  article, int index ){
		
		if( article == null ){ return null; }
		article.setIndex( index ); 
		//根据userId获取userInfo
		UserInfo userInfo = userInfoDao.findById( article.getUserId());
	    article.setUserInfo(userInfo);
		//根据说说id 获取 图片列表，评论列表，点赞列表
		List likeList = friendCircleLikeDao.findByIndexAndIndexId( index, article.getId());
		
		if( likeList != null ){
			
			for( int j=0; j<likeList.size(); j++ ){
				ZmFriendCircleLike like = (ZmFriendCircleLike) likeList.get(j);
				UserInfo likeUserInfo =userInfoDao.findById( like.getUserId() );
				like.setUserInfo(likeUserInfo);
			}
		}
		
		List commentList = friendCircleCommentDao.findByIndexAndIndexId( index, article.getId());
		if( commentList != null ){
			for( int j=0; j<commentList.size(); j++ ){
				ZmFriendCircleComment comment = (ZmFriendCircleComment) commentList.get(j);
				UserInfo fromUserInfo = userInfoDao.findById( comment.getFromUserId() );
				UserInfo toUserInfo   = userInfoDao.findById( comment.getToUserId() );
				comment.setFromUserInfo(fromUserInfo);
				comment.setToUserInfo(toUserInfo);
			}
		}
		article.setLikeList(likeList);
		article.setCommentList(commentList);
		return article;
	}
	
	
	/**
	 *  2.上传网络文章说说
	 *     url:  url链接
	 *     content: 内容
	 *     user_id
	 *     index
	 *     init_time
	 * @throws Exception 
	 * */
	public String addNetworkArticle( int index,  int userId, String content, String url, String picUrl, String summary ) {
		
		String resultMsg = null;
		try {
			/*
			 * 1. 先存储数据到说说表中:  type, userId, content, initTime
			 * 2. 再存储图片到图片表中:  shuoshuo_id, pic_url
			 * */
			//1.存储说说到 说说表中
			ZmNetworkArticle networkArticle = new ZmNetworkArticle();
			networkArticle.setUserId(userId);
			networkArticle.setContent(content);
			networkArticle.setUrl(url);
			networkArticle.setInitTime( new Timestamp( new Date().getTime() ) );
			networkArticle.setIndex( index );
			networkArticle.setPicUrl(picUrl);
			networkArticle.setSummary(summary);
			networkArticleDao.save( networkArticle );
			
			//1.1 获取主键id
			int keyId = networkArticle.getId();
			
			//3.存储说说 到 总表中
			ZmFriendCircle friendCircle = new ZmFriendCircle();
			friendCircle.setIndex( index );
			friendCircle.setIndexId(keyId);
			friendCircle.setUserId(userId);
			friendCircleDao.save(friendCircle);
			
		} catch( Exception e ){
			
			e.printStackTrace();
			resultMsg = "操作失败，请重新操作";
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
		} 
		return resultMsg;
	}
	
	

	/**
	 *  删除找工作
	 * 		int index        值为2；
	 * 		int indexId      招工信息的id
	 * 		int userId       用户的id
	 * */
	public String deleteNetworkAriticsShare( int index,  int indexId, int userId ){
		
		String resultMsg = null;
		try {
			
			//1.先查询是否该说说是否存在
			ZmNetworkArticle  networkArticle = networkArticleDao.findById(indexId);
			//2.如果存在，那么删除该说说
			if( networkArticle != null ){ //该说说记录存在
				
				//2.1 删除该说说    
				networkArticleDao.delete(networkArticle);
				
				//2.3 删除点赞列表      根据shuoshuo_id删除点赞
				List likes = friendCircleLikeDao.findByIndexAndIndexId(index,indexId);
				if( likes != null ){
					for( int i=0; i<likes.size(); i++ ){
						ZmFriendCircleLike like = (ZmFriendCircleLike) likes.get( i );
						friendCircleLikeDao.delete(like);
					}
				}
				
				//2.4 删除评论列表      根据shuoshuo_id删除评论
				List comments = friendCircleCommentDao.findByIndexAndIndexId(index, indexId);
				if( comments != null ){
					
					for( int i=0; i<comments.size(); i++ ){
						ZmFriendCircleComment comment = (ZmFriendCircleComment) comments.get(i);
						friendCircleCommentDao.delete(comment);
					}
				}
				
				//2.5 删除总表中的说说   根据typeId删除总表
			   
				List alls = friendCircleDao.findByIndexAndIndexId( index , indexId);
				if( alls != null ){
					for( int i=0; i<alls.size(); i++ ){
						ZmFriendCircle circle = (ZmFriendCircle) alls.get(i);
						friendCircleDao.delete(circle);
					}
				}
				
			}else {//该说说记录不存在
				
				resultMsg = "该说说记录不存在";
				
			}
			
		}catch( Exception e ){
			
			e.printStackTrace();
			resultMsg = "该说说记录删除失败";
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return resultMsg;
	}
	
}
