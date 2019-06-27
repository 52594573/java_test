package com.zm.friendCircle.Service;

import com.zm.entity.ChatFriendDAO;
import com.zm.entity.UserInfo;
import com.zm.entity.UserInfoDAO;
import com.zm.friendCircle.entity.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class FriendCircleService {
	
	@Autowired
	private ZmFriendCircleDAO friendCircleDao; //总表Dao
	@Autowired
	private ZmFriendCircleShuoshuoDAO friendCircleShuoshuoDao; //说说Dao
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
	@Autowired
	private ZmRefusedLookDAO refusedLookDao; //拒绝查看
	
	public static int INT_SHUOSHUO_ID  = 3; //说说
	public static int INT_EMPLOYEE_ID = 2; //找工
	public static int INT_EMPLOYER_ID = 1; //招工
	public static int INT_NETWORK_ARTICLE_ID =4;
	
	@Test
	public void test(){
		
		ApplicationContext context = new FileSystemXmlApplicationContext("/src/main/resources/spring/spring-dao.xml" );
		friendCircleDao = (ZmFriendCircleDAO) context.getBean("friendCircleDao");
		friendCircleShuoshuoDao = (ZmFriendCircleShuoshuoDAO) context.getBean("friendCircleShuoshuoDao");
		friendCircleAlbumDao = (ZmFriendCircleAlbumDAO) context.getBean("friendCircleAlbumDao");
		friendCircleCommentDao = (ZmFriendCircleCommentDAO) context.getBean("friendCircleCommentDao");
		friendCircleLikeDao = (ZmFriendCircleLikeDAO) context.getBean("friendCircleLikeDao");
		chatFriendDao = (ChatFriendDAO)context.getBean("chatFriendDao");
		refusedLookDao = (ZmRefusedLookDAO)context.getBean("refusedLookDao");
		userInfoDao = (UserInfoDAO)context.getBean("userInfoDao");
		
		List<String> list = new ArrayList<String>();
		list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527574215491&di=5aaa777e2b5b0f84121d2091fd2fd6de&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F0df3d7ca7bcb0a4646843bed6163f6246a60af44.jpg");
		list.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1062685056,3399887883&fm=27&gp=0.jpg");
		list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527574288455&di=2eba1acb3bcbea6144d63f32ecf325b2&imgtype=0&src=http%3A%2F%2Fi0.sinaimg.cn%2FIT%2Fcr%2F2014%2F0728%2F183664150.jpg");
		list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527574288454&di=e24c23808d65a86739974965af000f9a&imgtype=0&src=http%3A%2F%2Fimg1.gtimg.com%2Fwuxi_house%2Fpics%2Fhv1%2F191%2F173%2F81%2F5311331.jpg");
		list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527574288454&di=b5f7bde8d0393637e437852d0616011b&imgtype=0&src=http%3A%2F%2Fimgqn.koudaitong.com%2Fupload_files%2F2015%2F03%2F04%2FFtVOoNSfEK1nQFiMPqLSeYJWom0P.jpg%2521580x580.jpg");
		list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527574288451&di=508dae5463491e0c6077f79db19bbe2f&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fitbbs%2F1507%2F03%2Fc1%2F9222508_1435854169969_mthumb.jpg");
		list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527574288451&di=c4e850cd6331a31e558826119a4b8b23&imgtype=0&src=http%3A%2F%2Ffile27.mafengwo.net%2FM00%2F36%2F6A%2FwKgB6lS_ky-AWZH-AA_XfWQ4qcg55.groupinfo.w680.jpeg");
//		addNewShuoShuo( INT_SHUOSHUO_ID, 20000, "20000这是一条假的说说",list );
		
//		deleteShuoShuo( 6, 1793);
		
//		getShuoshuo( 1793, 1, 0, 10);
		
//		addLike( 7, 1793 );
//		addLike( 7, 1792 );
//		addLike( 7, 1791 );
//		deleteComment(1, 1793, 0);
//		addComment(7, 1793, 0, "自己评论自己一下下");
//		addComment(7, 1792, 1793, "机器人1号");
//		addComment(7, 1794, 1793, "机器人2号");
		
//		List temp = getAlbumList(3, 1793);
//		System.out.println( temp );
		
//		refusedLookAboutOnePerson(1793, 1792);
		refusedLookAboutOneShuoShuo(1793, 0, 8);
	}

    /**
     * 1.0 获取全部
     */
    public List getAll(int userId, int startPage, int length) {

        return friendCircleDao.findByPagenation(userId, startPage, length);

    }

	/**
	 * 1.0 获取全部
	 */
	public List getAll(int index, int userId, int startPage, int length) {

		return friendCircleDao.findByPagenation(index, userId, startPage, length);

	}
	
	/**
	 * 1.1获取我自己的
	 * */
	public List getMy( int userId, int startPage, int length ){
		
		return friendCircleDao.findByUserIdAndPagenation( userId, startPage, length );
	}
	
	
	/**
	 * 1.1 获取说说  (获取自己以及朋友的说说)
	 * 		type: image
	 * 		username: 名字
	 * 		avater:   用户的图片
	 * 		content:  说说的内容
	 * 		time:     发布时间
	 * 		picList:  图片的数组
	 * 		likeList: 赞列表
	 * 		commentList : 评论列表 
	 * */
	public List getShuoshuo( int index, int userId, int startPage, int length ){ //type参数  0表示全部， 1表示招工  2表示找工  3表示说说 4网络文章
		
		//1. 获取自己的user_id,获取自己的好友user_id
		//2. 根据user_id 去查询说说记录
		//3. 获取到说说记录之后，再根据说说记录，获取点赞列表，评论列表，图片列表
//		List chatFriendList =  chatFriendDao.findFriendList(userId, 2); //获取好友列表
//		List<Integer> userIdList   = new ArrayList<Integer>();
//		userIdList.add( userId );
//		for( int i=0; i<chatFriendList.size(); i++ ){
//			
//			ChatFriend chatFriend = (ChatFriend)chatFriendList.get(i);
//			userIdList.add(chatFriend.getRightUid());	
//		} 
//		
		List shoushuoList = friendCircleShuoshuoDao.findByPage(userId, startPage, length);
		if( shoushuoList != null ){
			for( int i=0; i<shoushuoList.size(); i++ ){
				
				ZmFriendCircleShuoshuo shuoshuo = (ZmFriendCircleShuoshuo) shoushuoList.get(i);
				fillWithOtherDataInShuoshuo(shuoshuo);
			}
		}
			return shoushuoList;
	}


	public ZmFriendCircleShuoshuo getShuoshuoById(int userId, int shuoshuoId) {

		ZmFriendCircleShuoshuo shuoshuo = friendCircleShuoshuoDao.findById(userId, shuoshuoId);
		return fillWithOtherDataInShuoshuo(shuoshuo);
	}

	public ZmFriendCircleShuoshuo getShuoshuoById(int shuoshuoId) {

		ZmFriendCircleShuoshuo shuoshuo = friendCircleShuoshuoDao.findById(shuoshuoId);
		return fillWithOtherDataInShuoshuo(shuoshuo);
	}
	
	public ZmFriendCircleShuoshuo  fillWithOtherDataInShuoshuo(  ZmFriendCircleShuoshuo  shuoshuo ){
		
		if(shuoshuo == null ){ return null; }
		shuoshuo.setIndex( FriendCircleService.INT_SHUOSHUO_ID ); 
		//根据userId获取userInfo
		UserInfo userInfo = userInfoDao.findById(shuoshuo.getUserId());
		
		shuoshuo.setUserInfo(userInfo);
		//根据说说id 获取 图片列表，评论列表，点赞列表
		List picList = friendCircleAlbumDao.findByIndexAndIndexId( INT_SHUOSHUO_ID ,shuoshuo.getId() );
		List likeList = friendCircleLikeDao.findByIndexAndIndexId( INT_SHUOSHUO_ID, shuoshuo.getId());
		
		if( likeList != null ){
			
			for( int j=0; j<likeList.size(); j++ ){
				ZmFriendCircleLike like = (ZmFriendCircleLike) likeList.get(j);
				UserInfo likeUserInfo =userInfoDao.findById( like.getUserId() );
				like.setUserInfo(likeUserInfo);
			}
		}
		
		List commentList = friendCircleCommentDao.findByIndexAndIndexId( INT_SHUOSHUO_ID, shuoshuo.getId());
		if( commentList != null ){
			for( int j=0; j<commentList.size(); j++ ){
				ZmFriendCircleComment comment = (ZmFriendCircleComment) commentList.get(j);
				UserInfo fromUserInfo = userInfoDao.findById( comment.getFromUserId() );
				UserInfo toUserInfo   = userInfoDao.findById( comment.getToUserId() );
				comment.setFromUserInfo(fromUserInfo);
				comment.setToUserInfo(toUserInfo);
			}
		}
		shuoshuo.setPicList( picList );
		shuoshuo.setLikeList(likeList);
		shuoshuo.setCommentList(commentList);
		return shuoshuo;
	}
	
	
	
	
	/**
	 * 2.1 上传新的说说
	 * 		int type,  说说的类型
	 * 		int userId,   用户id
	 * 		String content,   说说的内容	
	 * 		Timestamp initTime  时间
	 * */
	public String addNewShuoShuo( int type, int userId, String content, String[] picList ){
		
		String resultMsg = null;
		try {
			
			/*
			 * 1. 先存储数据到说说表中:  type, userId, content, initTime
			 * 2. 再存储图片到图片表中:  shuoshuo_id, pic_url
			 * */
			//1.存储说说到 说说表中
			ZmFriendCircleShuoshuo friendCircleShuoshuo = new ZmFriendCircleShuoshuo();
			friendCircleShuoshuo.setUserId(userId);  
			friendCircleShuoshuo.setContent(content);
			friendCircleShuoshuo.setInitTime( new java.sql.Timestamp( new Date().getTime() ) );
			friendCircleShuoshuo.setIndex(INT_SHUOSHUO_ID);
			friendCircleShuoshuo.setvState(0);
			friendCircleShuoshuoDao.save(friendCircleShuoshuo);
			
			//1.1 获取主键id
			int keyId = friendCircleShuoshuo.getId();
			
			//2.存储说说中的图片 到 图片表中
			if( picList != null ){
				for( int i=0; i<picList.length; i++ ){
					
					String picUrl = (String) picList[i];
					ZmFriendCircleAlbum friendCircleAlbum = new ZmFriendCircleAlbum();
					friendCircleAlbum.setPicUrl(picUrl);
					friendCircleAlbum.setShuoshuoId(keyId);
					friendCircleAlbum.setUserId(userId);
					friendCircleAlbum.setIndex(INT_SHUOSHUO_ID);
					friendCircleAlbumDao.save(friendCircleAlbum);
				}
			}
			
			//3.存储说说 到 总表中
			ZmFriendCircle friendCircle = new ZmFriendCircle();
			friendCircle.setIndex( INT_SHUOSHUO_ID );
			friendCircle.setIndexId(keyId);
			friendCircle.setUserId(userId);
			friendCircle.setStatus(0);
			friendCircle.setCircleDel(1);
			friendCircleDao.save(friendCircle);
			
		} catch( Exception e ){
			
			resultMsg = "数据库存储失败";
			
		} finally {
		}
		return resultMsg;
	}
	
	
	
	
	
	/**
	 * 2.2 删除说说
	 * 		int shuoshuoId,  说说的id
	 * 		int userId       用户的id
	 * */
	public String deleteShuoShuo( int index,  int indexId, int userId ){
		
		String resultMsg = null;
		try {
			
			//1.先查询是否该说说是否存在
			ZmFriendCircleShuoshuo friendCircleShuoshuo = friendCircleShuoshuoDao.findById( indexId );
			//2.如果存在，那么删除该说说
			if( friendCircleShuoshuo != null ){ //该说说记录存在
				
				//2.1 删除该说说    
				friendCircleShuoshuoDao.delete(friendCircleShuoshuo);
				
				//2.2 删除相片列表      根据shuoshuo_id删除相片
				List albums =  friendCircleAlbumDao.findByShuoshuoId(indexId);
				if( albums != null ){
					for( int i =0; i<albums.size(); i++ ){
						ZmFriendCircleAlbum album = (ZmFriendCircleAlbum) albums.get(i);
						friendCircleAlbumDao.delete(album);
					}
				}
				
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
	
	
	
	
	
	
	/**
	 * 3.1 增加点赞 / 删除点赞
	 * 		shuoshuoId 被赞的说说ID
	 * 		userId  赞的人的id
	 * */
	public String addLike( int  index ,int shuoshuoId, int userId ){
		
		//检查点赞记录是否存在，如果不存在，则进行增加； 如果存在，那么进行删除
		//检查说说是否存在
		String resultStr = null;
		List likeList = friendCircleLikeDao.findByIndexAndIndexIdAndUserId( index,shuoshuoId, userId);
		if( likeList == null || likeList.size() == 0 ){ //赞不存在，那么增加赞
			
			ZmFriendCircleLike like = new ZmFriendCircleLike();
			like.setIndexId(shuoshuoId);
			like.setIndex( index );
			like.setUserId(userId);
			like.setCreateTime(new Timestamp(System.currentTimeMillis()));
			friendCircleLikeDao.save(like);
			resultStr = "点赞成功";
			
		}else { //赞存在，那么删除该条赞的记录，表示取消赞
			
			for( int i=0; i<likeList.size(); i++ ){ //实际上只有一条。
				
				ZmFriendCircleLike like =  (ZmFriendCircleLike)likeList.get(i);
				friendCircleLikeDao.delete( like );
				resultStr = "取消点赞成功";
			}
		}
		return resultStr;
	} 
	


	/**
	 * 4.1 增加评论
	 * 		int shuoshuoId,  说说的id
	 * 		int fromUserId,  评论的id
	 * 		int toUserId,    被评论的id，如果为0，那么表示直接评论的说说；
	 * 		String comment   评论的内容
	 * */
	public ZmFriendCircleComment addComment( int index ,int shuoshuoId, int fromUserId, int toUserId, String content  ){
		
		try {
			
			ZmFriendCircleComment comment = new ZmFriendCircleComment();
			comment.setFromUserId( fromUserId );
			comment.setToUserId( toUserId );
			comment.setIndex(index);
			comment.setIndexId(shuoshuoId);
			comment.setComment(content);
			comment.setCreateTime(new Timestamp(System.currentTimeMillis()));
			friendCircleCommentDao.save(comment);
			return comment;
			
		} catch( Exception e ){
			
			return null;
		}
	}
	
	
	
	/**
	 * 4.2 删除评论
	 * 		int shuoshuoId,  说说的id
	 * 		int fromUserId,  评论的id
	 * 		int toUserId,    被评论的id，如果为0，那么表示直接评论的说说；
	 * */
	public String deleteComment( int index, int indexId, int commentId, int fromUserId ){
		
		String resultStr = null;
		try {
			
			List list = friendCircleCommentDao.finByIndexIndexIdFromUserId( index, indexId,commentId,fromUserId );
			if( list != null  ){
				for( int i=0; i<list.size(); i++ ){
					ZmFriendCircleComment comment = (ZmFriendCircleComment) list.get(i);
					friendCircleCommentDao.delete(comment);
				}
			}
		}catch( Exception e ){
			
			resultStr = "删除评论失败";
		}
		return resultStr;
	}
	
	
	/**
	 * 
	 * */
	public Boolean isRefusedLookAboutOnePerson( int fromUserId, int toUserId  ){
		
		Boolean result= false;
		try {
			
			List list = refusedLookDao.findByFromUserIdAndToUserId(fromUserId, toUserId);
			if( !(list == null || list.size()==0) ){
				
				result = true;
			}
			
		}catch( Exception e ){
			
		}
		return result;
	}
	
	
	/**
	 * 4.3 屏蔽/取消屏蔽某人的朋友圈
	 * */
	public String refusedLookAboutOnePerson( int fromUserId, int toUserId  ){
		
		String resultStr = null;
		try {
			
			List list = refusedLookDao.findByFromUserIdAndToUserId(fromUserId, toUserId);
			if( list == null || list.size()==0 ){
				
				ZmRefusedLook refusedLook = new ZmRefusedLook();
				refusedLook.setFromUserId(fromUserId);
				refusedLook.setToUserId(toUserId);
				refusedLook.setIndex(0);
				refusedLook.setIndexId(0);
				refusedLookDao.save(refusedLook);
				
			}else {
				
				for( int i=0; i<list.size(); i++ ){
					
					ZmRefusedLook refusedLook = (ZmRefusedLook) list.get(i);
					refusedLookDao.delete(refusedLook);
				}
			}
			
		}catch( Exception e ){
			
			resultStr = "操作失败";
		}
		return resultStr;
	}
	
	/**
	 * 4.4 屏蔽/取消屏蔽 某一条说说
	 * */
	public String refusedLookAboutOneShuoShuo( int fromUserId, int index, int indexId ){
		
		String resultStr =  null;
		try {
			
			List list = refusedLookDao.findByFromUserIdAndIndex( fromUserId, index, indexId );
			if( list == null || list.size() == 0 ){
				
				ZmRefusedLook refusedLook = new ZmRefusedLook();
				refusedLook.setFromUserId(fromUserId);
				refusedLook.setToUserId(0);
				refusedLook.setIndex(index);
				refusedLook.setIndexId(indexId);
				refusedLookDao.save(refusedLook);
				
			}else {
				
				for( int i=0; i<list.size(); i++ ){
					
					ZmRefusedLook refusedLook = (ZmRefusedLook) list.get(i);
					refusedLookDao.delete(refusedLook);
				}
			}
			
		}catch( Exception e ){
			
			resultStr = "操作失败";
		}
		return resultStr;
	}
	
	
	
	/**
	 * 4.4 获取相册图片集合
	 * */
	public List getAlbumList(  int  index, int userId ){
		
		List list = friendCircleAlbumDao.findByUserIdAndPagination(userId, 4);
		
		return list;
	}
	
}
