package com.zm.friendCircle.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.zm.friendCircle.entity.ZmEmployeeCircle;
import com.zm.friendCircle.entity.ZmEmployeeCircleDAO;
import com.zm.friendCircle.entity.ZmFriendCircle;
import com.zm.friendCircle.entity.ZmFriendCircleAlbumDAO;
import com.zm.friendCircle.entity.ZmFriendCircleComment;
import com.zm.friendCircle.entity.ZmFriendCircleCommentDAO;
import com.zm.friendCircle.entity.ZmFriendCircleDAO;
import com.zm.friendCircle.entity.ZmFriendCircleLike;
import com.zm.friendCircle.entity.ZmFriendCircleLikeDAO;


/**
 *  找工Service
 * */
@Service
@Transactional
public class EmployeeService {

	private static int WORK_KEY_ID = 4;
	
	@Autowired
	private KeyContentDAO  keyContentDao; //获取工人工作类型
	@Autowired
	private ZmFriendCircleDAO friendCircleDao; //总表Dao
	@Autowired
	private ZmEmployeeCircleDAO  employeeCircleDao;
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
		employeeCircleDao = (ZmEmployeeCircleDAO) context.getBean("employeeCircleDao");
		friendCircleAlbumDao = (ZmFriendCircleAlbumDAO) context.getBean("friendCircleAlbumDao");
		friendCircleCommentDao = (ZmFriendCircleCommentDAO) context.getBean("friendCircleCommentDao");
		friendCircleLikeDao = (ZmFriendCircleLikeDAO) context.getBean("friendCircleLikeDao");
		chatFriendDao = (ChatFriendDAO)context.getBean("chatFriendDao");
		userInfoDao = (UserInfoDAO)context.getBean("userInfoDao");
//		List list = getWorkClassify();
//		System.out.println( list );
		
//		String str = addEmployee( 1793, "1234565", new Date(),  19 ); //木工
//		System.out.println( str );
		
//		String str = addLike(2, 1792);
//		System.out.println( str );	
		
		List  tempList =  getEmployeeList( 2, 1793 ,  0 , 10 );
		System.out.println( tempList );
	}
	
	
	/**
	 *  1.获取建筑工人的职业类型
	 *      木工，铁工，混凝土，外加，粗装修，其他
	 * */
	public List getWorkClassify(){
		
		List list = keyContentDao.findByKeyId( WORK_KEY_ID );
		return list;
	}
	
	
	/**
	 * 2.获取找工作列表
	 * 		username: 名字
	 * 		avater:   用户的图片
	 * 		jnScore   技能分
	 *      xyScore   信用分
	 * 		content:  说说的内容
	 * 		arriveTime:    到岗时间
	 * 		createdTime:   创建时间
	 * 		work      工种
	 * 		likeList: 赞列表
	 * 		commentList : 评论列表 
	 * */
	public List getEmployeeList( int index, int userId , int startPage, int length ){
		
		//1. 获取自己的user_id,获取自己的好友user_id
				//2. 根据user_id 去查询说说记录
				//3. 获取到说说记录之后，再根据说说记录，获取点赞列表，评论列表，图片列表	
				List emplyeeList =  employeeCircleDao.findByPagenation(startPage, length);
				if( emplyeeList != null ){
					for( int i=0; i<emplyeeList.size(); i++ ){
						
						ZmEmployeeCircle employee = (ZmEmployeeCircle)emplyeeList.get(i);
						fillWithOtherDataInShuoshuo(employee, index );
					}
				}
				return emplyeeList;
	}
	
	
	public ZmEmployeeCircle  getEmployeeId( int  employeeId, int index ){
		
		ZmEmployeeCircle employee = employeeCircleDao.findById( employeeId );
		return fillWithOtherDataInShuoshuo(employee, index );
	}
	
	
	public ZmEmployeeCircle  fillWithOtherDataInShuoshuo(  ZmEmployeeCircle  employee, int index ){
		
		if( employee == null ){ return null; }
		employee.setIndex( index ); 
		//根据userId获取userInfo
		UserInfo userInfo = userInfoDao.findById(employee.getUserId());
		employee.setUserInfo(userInfo);
		//根据说说id 获取 图片列表，评论列表，点赞列表
		List picList = friendCircleAlbumDao.findByIndexAndIndexId( index ,employee.getId() );
		List likeList = friendCircleLikeDao.findByIndexAndIndexId( index, employee.getId());
		
		if( likeList != null ){
			
			for( int j=0; j<likeList.size(); j++ ){
				ZmFriendCircleLike like = (ZmFriendCircleLike) likeList.get(j);
				UserInfo likeUserInfo =userInfoDao.findById( like.getUserId() );
				like.setUserInfo(likeUserInfo);
			}
		}
		
		List commentList = friendCircleCommentDao.findByIndexAndIndexId( index, employee.getId());
		if( commentList != null ){
			for( int j=0; j<commentList.size(); j++ ){
				ZmFriendCircleComment comment = (ZmFriendCircleComment) commentList.get(j);
				UserInfo fromUserInfo = userInfoDao.findById( comment.getFromUserId() );
				UserInfo toUserInfo   = userInfoDao.findById( comment.getToUserId() );
				comment.setFromUserInfo(fromUserInfo);
				comment.setToUserInfo(toUserInfo);
			}
		}
		employee.setLikeList(likeList);
		employee.setCommentList(commentList);
		return employee;
	}
	
	
	
	/**
	 * 3.发送找工信息 
	 *    userId  包含了名字，用户头像
	 *    content 说说的内容
	 *    arrivetime 到岗时间
	 *    workId      工种id
	 *    createDate  创建时间
	 * */
	public String addEmployee( int index,  int userId, String content, Date arriveTime, String work ){
		
		String resultMsg = null;
		try {
			/*
			 * 1. 先存储数据到说说表中:  type, userId, content, initTime
			 * 2. 再存储图片到图片表中:  shuoshuo_id, pic_url
			 * */
			//1.存储说说到 说说表中
			ZmEmployeeCircle employeeCircle = new ZmEmployeeCircle();
			employeeCircle.setUserId(userId);
			employeeCircle.setContent(content);
			employeeCircle.setArriveTime( new Timestamp( arriveTime.getTime() ) );
			employeeCircle.setWork( work );
			employeeCircle.setCreateTime( new Timestamp( new Date().getTime() ) );
			employeeCircle.setIndex( index );
			employeeCircleDao.save( employeeCircle );
			
			//1.1 获取主键id
			int keyId = employeeCircle.getId();
			
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
	 * 3.1 增加点赞 / 删除点赞
	 * 		shuoshuoId 被赞的说说ID
	 * 		userId  赞的人的id
	 * */
	public String addLike( int index ,int indexId ,int userId ){
		
		//检查点赞记录是否存在，如果不存在，则进行增加； 如果存在，那么进行删除
		//检查说说是否存在
		String resultStr = null;
		List likeList = friendCircleLikeDao.findByIndexAndIndexId( index  , indexId );
		if( likeList == null || likeList.size() == 0 ){ //赞不存在，那么增加赞
			
			
			ZmFriendCircleLike like = new ZmFriendCircleLike();
			like.setIndexId(indexId);
			like.setUserId(userId);
			like.setIndex(index);
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
	public String addComment(int index, int indexId, int fromUserId, int toUserId, String content  ){
		
		String resultStr = null;
		try {
			
			ZmFriendCircleComment comment = new ZmFriendCircleComment();
			comment.setFromUserId( fromUserId );
			comment.setToUserId( toUserId );
			comment.setIndex( FriendCircleService.INT_EMPLOYEE_ID );
			comment.setIndexId(indexId);
			comment.setComment(content);
			comment.setIndex(FriendCircleService.INT_EMPLOYEE_ID);
			friendCircleCommentDao.save(comment);
			
		} catch( Exception e ){
			
			resultStr = "增加评论失败";
		}
		return resultStr;
	}
	
	
	
	/**
	 * 4.2 删除评论
	 * 		int shuoshuoId,  说说的id
	 * 		int fromUserId,  评论的id
	 * 		int toUserId,    被评论的id，如果为0，那么表示直接评论的说说；
	 * */
	public String deleteComment( int commentId, int fromUserId, int toUserId ){
		
		String resultStr = null;
		try {
			
			List list = friendCircleCommentDao.findByIndexAndIndexId(FriendCircleService.INT_EMPLOYEE_ID, commentId);
			if( list != null ){
				for( int i=0; i<list.size(); i++ ){
					
					ZmFriendCircleComment comment =  (ZmFriendCircleComment) list.get(i);
					friendCircleCommentDao.delete(comment);
				}
			}
		}catch( Exception e ){
			
			resultStr = "删除评论失败";
		}
		return resultStr;
	}
	
	
	
	
	/**
	 *  删除找工作
	 * 		int index        值为2；
	 * 		int indexId      招工信息的id
	 * 		int userId       用户的id
	 * */
	public String deleteEmployeeShare( int index,  int indexId, int userId ){
		
		String resultMsg = null;
		try {
			
			//1.先查询是否该说说是否存在
			ZmEmployeeCircle  employee = employeeCircleDao.findById(indexId);
			//2.如果存在，那么删除该说说
			if( employee != null ){ //该说说记录存在
				
				//2.1 删除该说说    
				employeeCircleDao.delete(employee);
				
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
