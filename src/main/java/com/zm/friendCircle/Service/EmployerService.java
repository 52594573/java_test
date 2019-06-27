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

import com.zm.entity.ChatFriendDAO;
import com.zm.entity.KeyContentDAO;
import com.zm.entity.UserInfo;
import com.zm.entity.UserInfoDAO;
import com.zm.friendCircle.entity.ZmEmployerCircle;
import com.zm.friendCircle.entity.ZmEmployerCircleDAO;
import com.zm.friendCircle.entity.ZmFriendCircle;
import com.zm.friendCircle.entity.ZmFriendCircleAlbumDAO;
import com.zm.friendCircle.entity.ZmFriendCircleComment;
import com.zm.friendCircle.entity.ZmFriendCircleCommentDAO;
import com.zm.friendCircle.entity.ZmFriendCircleDAO;
import com.zm.friendCircle.entity.ZmFriendCircleLike;
import com.zm.friendCircle.entity.ZmFriendCircleLikeDAO;


/**
 * 招工Service
 * */

@Service
@Transactional
public class EmployerService {

	private static int WORK_KEY_ID = 4; //工种的key值
	
	@Autowired
	private KeyContentDAO  keyContentDao; //获取工人工作类型
	@Autowired
	private ZmFriendCircleDAO friendCircleDao; //总表Dao
	@Autowired
	private ZmEmployerCircleDAO employerCircleDao; //说说Dao
	@Autowired
	private ZmFriendCircleAlbumDAO friendCircleAlbumDao; //相册Dao
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
		employerCircleDao = (ZmEmployerCircleDAO) context.getBean("employerCircleDao");
		friendCircleAlbumDao = (ZmFriendCircleAlbumDAO) context.getBean("friendCircleAlbumDao");
		friendCircleCommentDao = (ZmFriendCircleCommentDAO) context.getBean("friendCircleCommentDao");
		friendCircleLikeDao = (ZmFriendCircleLikeDAO) context.getBean("friendCircleLikeDao");
		chatFriendDao = (ChatFriendDAO)context.getBean("chatFriendDao");
		userInfoDao = (UserInfoDAO)context.getBean("userInfoDao");
//		List list = getWorkClassify();
//		System.out.println( list );
		
	}
	
	
	
	/**
	 * 2.获取招工列表
	 * 		username: 名字
	 * 		avater:   用户的图片
	 * 		content:  说说的内容
	 * 		createdTime:   创建时间
	 * 		work      工种
	 * 		amount    数量
	 * 		address   地址
	 * 		likeList: 赞列表
	 * 		commentList : 评论列表 
	 * */
	public List getEmployerList( int index, int userId , int startPage, int length ){
	
		//1. 获取自己的user_id,获取自己的好友user_id
		//2. 根据user_id 去查询说说记录
		//3. 获取到说说记录之后，再根据说说记录，获取点赞列表，评论列表，图片列表	
		List emplyerList =  employerCircleDao.findByPagenation(startPage, length);
		if( emplyerList != null ){
			for( int i=0; i<emplyerList.size(); i++ ){
				
				ZmEmployerCircle employer = (ZmEmployerCircle)emplyerList.get(i);
				fillWithOtherDataInEmployer( employer, index );
			}
		}
		return emplyerList;
	}
	
	
	public ZmEmployerCircle  getEmployerById( int  employerId , int index){
		
		ZmEmployerCircle employer = employerCircleDao.findById(employerId);
		return fillWithOtherDataInEmployer(employer, index);
	}
	
	
	public ZmEmployerCircle  fillWithOtherDataInEmployer(  ZmEmployerCircle  employer, int index ){
		
		if(employer == null ){ return null; }
		employer.setIndex( index ); 
		//根据userId获取userInfo
		UserInfo userInfo = userInfoDao.findById(employer.getUserId());
		employer.setUserInfo(userInfo);
		//根据说说id 获取 图片列表，评论列表，点赞列表
		List picList = friendCircleAlbumDao.findByIndexAndIndexId( index ,employer.getId() );
		List likeList = friendCircleLikeDao.findByIndexAndIndexId( index, employer.getId());
		
		if( likeList != null ){
			
			for( int j=0; j<likeList.size(); j++ ){
				ZmFriendCircleLike like = (ZmFriendCircleLike) likeList.get(j);
				UserInfo likeUserInfo =userInfoDao.findById( like.getUserId() );
				like.setUserInfo(likeUserInfo);
			}
		}
		List commentList = friendCircleCommentDao.findByIndexAndIndexId( index, employer.getId());
		if( commentList != null ){
			for( int j=0; j<commentList.size(); j++ ){
				ZmFriendCircleComment comment = (ZmFriendCircleComment) commentList.get(j);
				UserInfo fromUserInfo = userInfoDao.findById( comment.getFromUserId() );
				UserInfo toUserInfo   = userInfoDao.findById( comment.getToUserId() );
				comment.setFromUserInfo(fromUserInfo);
				comment.setToUserInfo(toUserInfo);
			}
		}
		employer.setLikeList(likeList);
		employer.setCommentList(commentList);
		return employer;
	}
	
	
	
	/**
	 * 3.发送招工信息 
	 *    userId  包含了名字，用户头像
	 *    content 说说的内容
	 *    workIds    工种列表
	 *    amount  数量
	 *    address : 地址	
	 *    createDate  创建时间
	 * */
	public String postEmployer( int index, int userId, String content, String workType,String works, int amount, String address ){
		
		String resultMsg = null;
		try {
			//1.存储说说到 说说表中
			ZmEmployerCircle  employerCircle = new ZmEmployerCircle();
			employerCircle.setUserId(userId);
			employerCircle.setContent(content);
			employerCircle.setWorks( works );
			employerCircle.setWorkType(workType);
			employerCircle.setCreateTime( new Timestamp( new Date().getTime() ) );
			employerCircle.setAddress(address);
			employerCircle.setIndex( index );
			employerCircle.setAmount(amount);
			employerCircleDao.save( employerCircle );
			
			//1.1 获取主键id
			int keyId = employerCircle.getId();
			
			//3.存储说说 到 总表中
			ZmFriendCircle friendCircle = new ZmFriendCircle();
			friendCircle.setIndex( index );
			friendCircle.setIndexId(keyId);
			friendCircle.setUserId(userId);
			friendCircleDao.save(friendCircle);
			
			
		} catch( Exception e ){
			
			resultMsg = "数据库存储失败";
		} 
		return resultMsg;
	}
	
	

	/**
	 *  删除找工人
	 * 		int index        值为1；
	 * 		int indexId      招工信息的id
	 * 		int userId       用户的id
	 * */
	public String deleteEmployerShare( int index,  int indexId, int userId ){
		
		String resultMsg = null;
		try {
			
			//1.先查询是否该说说是否存在
			ZmEmployerCircle  employer = employerCircleDao.findById(indexId);
			//2.如果存在，那么删除该说说
			if( employer != null ){ //该说说记录存在
				
				//2.1 删除该说说    
				employerCircleDao.delete(employer);

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
			
			resultMsg = "该说说记录删除失败";
		}
		
		return resultMsg;
	}
	
}
