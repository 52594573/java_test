package com.zm.service;

import com.ktp.project.entity.ChatFriend;
import com.zm.entity.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;


@Service
@Transactional
public class ChatFriendService {


	private static int INT_ADD_NEW_FRIEND_APPLY =  1; //新添加好友申请
	private static int INT_ALREADY_IS_FRIEND = 2; //已经是好友关系
	private static int INT_DELETE_FRIEND = 3; //删除好友
	private static int INT_REFUSED = 4;  //拒绝添加好友
	private static int INT_REFUSED_IN_THREE_DAY = 5; //三天之内拒绝添加好友
	private static int INT_REFUSED_IN_FUTURE = 6; //永久拒绝添加好友
	private static int INT_ADD_NEW_FRIEND_NOREAD_APPLY = 99; //新添加好友申请，且对方没有收到这条好友申请信息
	
	static double DEF_PI = 3.14159265359; // PI
    static double DEF_2PI= 6.28318530712; // 2*PI
    static double DEF_PI180= 0.01745329252; // PI/180.0
    static double DEF_R =6370693.5; // radius of earth
	
	
	@Autowired
	private ChatFriendDAO chatFriendDao;
	@Autowired
	private UserInfoDAO   userInfoDao;
	@Autowired
	private KeyContentDAO  keyContentDao;
	@Autowired
	private ProOrganPerDAO proOrganPerDao;
	@Autowired
	private ProOrganDAO  proOrganDao;
	/**
	 *   1.搜索好友
	 *   2.增加好友
	 *   3.删除好友
	 * */
	
	@Test
	public void test(){
		
		//测试搜索好友
//		ApplicationContext context = new FileSystemXmlApplicationContext("/src/main/resources/spring/spring-dao.xml" );
//		chatFriendDao = (ChatFriendDAO) context.getBean("chatFriendDao");
//		userInfoDao = (UserInfoDAO) context.getBean("userInfoDao");
//1.测试搜索好友通过		
//		List list = searchUserByPhone("157"); 
//		System.out.println( list );
		//测试增加好友
		

//2.用户1793查看谁新添加他为好友
//		List list = searchNewAddFriendByUserId(1790);
//		System.out.println( list );
		
		
//3.用户1793查看好友添加的处理状态
//		List list = searchFriendApplyListByUserId(1793);
//		System.out.println( list );
		
		
//4.用户1973添加好友		
//		String str = addFriend( 1793 , 1791, INT_ADD_NEW_FRIEND_NOREAD_APPLY); 
//		if( str == null ){
//			System.out.println("添加好友消息发送成功!");
//		}else {
//			System.out.println(str);
//		}
		
		
//5.用户1973同意添加好友
//		String str = agreeAddFriend( 1790 , 1793);
//		if( str == null ){
//			System.out.println("好友添加成功!");
//		}else {
//			System.out.println( str );
//		}
	
		
//6.用户1973拒绝添加好友  在被拒绝的用户作为fromUserId 的记录中添加被拒绝标识		
//		String str = refusedToAddFriend( 1791 , 1793,  INT_REFUSED_IN_THREE_DAY );
//			System.out.println("操作成功!");
//		}else {
//			System.out.println( str );
//		}
		
		
//7.用户1973删除好友
//		String str =removeFriend( 1793, 1792 );
//		if( str == null ){
//			System.out.println("操作成功!");
//		}else {
//			System.out.println( str );
//		}
		
	}
	
	
	@Transactional
	public List chatFriendListFillWithFriendUserInfoByUserId( List chatFriendList, boolean isLeftQuery, boolean isNeedKeyContent ){
		
		if( chatFriendList == null ){
			return chatFriendList;
		}
		
		List<ChatFriend>  list = new ArrayList<ChatFriend>();
		for( int i=0; i<chatFriendList.size(); i++ ){
			
			ChatFriend chatFriend = (ChatFriend) chatFriendList.get(i);
			int userId = chatFriend.getLeftUid() ;
			if( isLeftQuery ){ userId = chatFriend.getRightUid(); }
			UserInfo userInfo = userInfoDao.findById( userId );
			if( isNeedKeyContent ){//需要user_id的详细信息
				
				if( userInfo != null ){
					KeyContent keyContent = keyContentDao.findById( userInfo.getU_type() );
					userInfo.setU_typename(keyContent.getKeyName());
					
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id", userInfo.getId());
					map.put("u_pic", userInfo.getU_pic());
					map.put("user_id", userInfo.getUser_id());
					map.put("u_cert", userInfo.getU_cert());
					map.put("u_realname", userInfo.getU_realname());
					map.put("u_sex", userInfo.getU_sex());
					map.put("u_xyf", userInfo.getU_xyf());
					map.put("u_jnf", userInfo.getU_jnf());
					chatFriend.setRightUserInfo(map);
					list.add(chatFriend);
				}
			}else {
				
				list.add(chatFriend);
			}
		}
		return list;
	}
	

	/** 1.搜索好友 */
	public List searchUserByPhone( String phone ){
		
		return  userInfoDao.findByPrefUName( phone );
	}	
	
	
	
	public List getNearUsers(  int userId, double latitude, double longitude,  int startPage, int length ){
		
		float distance = 10000;
		List resultList = new ArrayList();
		Map map = returnLLSquarePoint( latitude, longitude ,distance);
		List list = userInfoDao.findAllWithDistance( userId, (double)map.get("left") ,
										(double)map.get("right"),
										(double)map.get("top"),
										(double)map.get("bottom"));
		
		for( int i=0; i<list.size(); i++ ){
			
			UserInfo userInfo = (UserInfo) list.get(i);
			String city =  userInfoDao.findUserSfzInfo( userInfo.getId() );
			if( city == null || city.length()==0 ){
				city = "未知";
			}
			
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("user_id", userInfo.getId());
			temp.put("u_realname", userInfo.getU_realname());
			temp.put("u_cert", userInfo.getU_cert());
			temp.put("u_city", city);
			temp.put("u_lbs_x", userInfo.getU_lbs_x());
			temp.put("u_lbs_y", userInfo.getU_lbs_y());
			temp.put("u_pic", userInfo.getU_pic());
			resultList.add(temp);
		}
		
		return resultList;
	}
	
	
    
    /**
     *  1. 更新用户的坐标
     * */
    public String updateUserCoordinate( int userId, double lati, double lon ){
   	 
   	 String resultStr = null;
   	 UserInfo userInfo = userInfoDao.findById(userId);
   	 if( lati < 1.000 && lon<1.00 ){
   		 resultStr = "用户坐标错误";
   		 return resultStr;
   	 }
   		 
		 if( userInfo != null ){
   		 
   		 try {
   			 
   			 userInfo.setU_lbs_x( lati );
   			 userInfo.setU_lbs_y( lon );
   			 userInfoDao.update(userInfo);
   			 
   		 }catch( Exception e ){
   			 
   			 resultStr = "数据库存储失败";
   			 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
   		 }
   		 
   	 }else {
   		 
   		 resultStr = "用户不存在";
   		 
   	 } 
   	 return resultStr;
   	 
    }
    
    
	
	/**
	 * 1.2 获取最新的 “被” 请求添加为好友的信息, app登录之后发送一个get，进行添加好友的消息获取
	 * 		主要是针对被添加好友不在线时收到的好友请求信息
	 * */
	public List searchNewAddFriendByUserId( int toUserId ){
		
		List list =  chatFriendDao.findByUserIdAndRelationType( toUserId,  INT_ADD_NEW_FRIEND_NOREAD_APPLY  );
		return chatFriendListFillWithFriendUserInfoByUserId(list, false, false );
		
	}
	
	
	
	/**
	 *  1.3 查看自己的好友申请列表
	 * */
	public List searchFriendApplyListByUserId( int toUserId ){
		
		List list =   chatFriendDao.findByRightUid( toUserId ); //relationType=99
		for( int i=0; i<list.size(); i++ ){
			
			ChatFriend chatFriend = (ChatFriend) list.get(i);
			if( chatFriend.getRelationType() == INT_ADD_NEW_FRIEND_NOREAD_APPLY ){
				
				chatFriend.setRelationType( INT_ADD_NEW_FRIEND_APPLY );
				chatFriendDao.update(chatFriend);
			}
		}
		/**
		 *  第二个参数表示 true表示是用leftUserId去查询用户信息，false则用rightUserId去查询用户信息
		 *  第三个参数表示 是否需要获取用户详细信息
		 * */
		return chatFriendListFillWithFriendUserInfoByUserId(list, false, true );
	}
	
	
	/** 2.增加好友 
	 *     增加好友  addType的值为1
	 *     
	 * */
	public String addFriend( int fromUserId, int toUserId, String attachMsg ){
		
		String errMsg = null;
		ChatFriend chatFriend = null;
		//1.先查询是否存在请求方，与被请求放
		//2.如果存在，那么查询是否可以被添加好友
		//3.如果可以，则添加好友，且赋值为99，表示这条消息还没有被查看
		
		if( fromUserId == toUserId ){
			errMsg = "请求用户ID异常";
			return errMsg;
		}
		
		UserInfo fromUserInfo = userInfoDao.findById( fromUserId );
		UserInfo toUserInfo  = userInfoDao.findById( toUserId );
		if( fromUserInfo == null ){
			
			errMsg = "请求用户ID异常";
		}
		
		if( toUserInfo == null ){
			
			errMsg = "被请求用户ID异常";
		}
		
		
		//保证两个ID都存在
		if( errMsg == null ){
			
			//再次判断是否存在请求被拒绝情形
			chatFriend = chatFriendDao.findByLeftUserIdAndRightUserId( fromUserId, toUserId );
			if( chatFriend != null ){
				int relationType = chatFriend.getRelationType();
				if( relationType == INT_REFUSED_IN_FUTURE   ){
					
					errMsg = "永久拒绝被添加为好友";
					
				}else if( relationType == INT_REFUSED_IN_THREE_DAY ){ 
					
					if( isInThreeDay( new Date(chatFriend.getLastTime().getTime()))){
						errMsg = "三天内拒绝添加为好友";
					}
					
				}else if( relationType == INT_ALREADY_IS_FRIEND ){
					
					errMsg = "已经是好友了";
				}				
			}
		}
		
		//排除了所有不能添加好友的情形，剩下的就是添加好友了
		if( errMsg == null ){
			
			//添加好友
			if( chatFriend == null ){
				
				try {
					
					chatFriend = new ChatFriend();
					chatFriend.setLeftUid( fromUserId );
					chatFriend.setRightUid( toUserId );
					chatFriend.setLastTime( new Timestamp( new Date().getTime() ) );
					chatFriend.setRelationType( INT_ADD_NEW_FRIEND_NOREAD_APPLY );
					chatFriend.setApplyMsg(attachMsg);
					chatFriendDao.save( chatFriend );
					
				}catch( Exception e ){
					
					errMsg = "操作失败，请重新操作";
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
				
			
			}else {
				
				try {
					
					chatFriend.setRelationType( INT_ADD_NEW_FRIEND_NOREAD_APPLY );
					chatFriend.setApplyMsg(attachMsg);
					chatFriendDao.update(chatFriend);
					
				}catch( Exception e ){
					
					errMsg = "操作失败，请重新操作";
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
				
			}
		}
		
		return errMsg;
	}
	
	
	
	
	/**
	 *  2.2 同意添加好友
	 * */
	@Transactional
	public String agreeAddFriend( int fromUserId, int toUserId ){
		
		String errMsg = null;
		if( fromUserId == toUserId ){
			errMsg = "请求用户ID异常";
			return errMsg;
		}
		
		//1. 判断对方是否发送了好友请求的邀请 -> 间接判断了用户是否存在
		//2. 同意对方为好友
		//3. 修改双方的好友关系 relationType都为2
		ChatFriend reverseFriend = chatFriendDao.findByLeftUserIdAndRightUserId( toUserId, fromUserId );
		
		
		if( reverseFriend ==null ){
			
			errMsg = "异常信息,对方并没有添加你为好友";
			
		}else if( !( reverseFriend.getRelationType() == INT_ADD_NEW_FRIEND_APPLY || reverseFriend.getRelationType()== INT_ADD_NEW_FRIEND_NOREAD_APPLY ) ){
			
			errMsg = "异常信息,对方并没有添加你为好友";
			
		}else {
			
			try {
				
				ChatFriend chatFriend = chatFriendDao.findByLeftUserIdAndRightUserId( fromUserId , toUserId );
				if( chatFriend == null ){ //以前不存在好友关系记录
						
					chatFriend = new ChatFriend();
					chatFriend.setRelationType( INT_ALREADY_IS_FRIEND );
					chatFriend.setLeftUid(fromUserId);
					chatFriend.setRightUid(toUserId);
					chatFriend.setLastTime( new Timestamp( new Date().getTime() ) );
					chatFriendDao.save( chatFriend );
					
				}else { //以前存在关系记录
					
					chatFriend.setRelationType( INT_ALREADY_IS_FRIEND );
					chatFriend.setLastTime( new Timestamp( new Date().getTime() ) );
					chatFriendDao.update( chatFriend );
				}
				reverseFriend.setRelationType(INT_ALREADY_IS_FRIEND );
				reverseFriend.setLastTime( new Timestamp( new Date().getTime() ) );
				chatFriendDao.update( reverseFriend );
				
				
			}catch( Exception e ){
				
				errMsg = "操作失败，请重新操作";
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			
			
		}
		
		return errMsg;
	}
	
	
	
//  <comment>关系类型: 值为 1,2,3,4  
//	1: A添加B， B还没有回复添加 
//	2: A,B互相为好友  
//	3: B删除了A 
//	4. 已经拒绝,B永久不添加A 
//	5. 已经拒绝,B三天之内不添加A 
//	6. 已经拒绝
//	99. A添加B，B还没有回复，且没有被B用户读取到这条新加好友消息
	/**  2.2     addtype 4 拒绝一次  5 三天之内不受到好友添加  6 永远不收到好友添加 */
	public String refusedToAddFriend( int fromUserId, int toUserId, int addType ){
		
		String errMsg = null;
		if( fromUserId == toUserId ){
			errMsg = "请求用户ID异常";
			return errMsg;
		}
		
		//0.先查询别人是否添加自己为好友
		//1.在查询自己和他是不是已经是好友关系
		//2.更改拒绝标识
		ChatFriend reverseFriend = chatFriendDao.findByLeftUserIdAndRightUserId(  toUserId, fromUserId );
		if( reverseFriend == null ){
				
			errMsg = "对方未申请加你为好友";
			
		}else {
			
			if( reverseFriend.getRelationType() == INT_ALREADY_IS_FRIEND ){
				
				errMsg = "已经是好友关系";
				
			}else {
				
				try {
					
					reverseFriend.setRelationType( addType );
					reverseFriend.setLastTime( new Timestamp( new Date().getTime() ) );
					chatFriendDao.update( reverseFriend );
					
				}catch( Exception e ){
					
					e.printStackTrace();
					errMsg = "操作失败，请重试";
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			}
		}
		return errMsg;
	}
	

	/** 3.删除好友 */
	public String removeFriend( int fromUserId, int toUserId ){
		
		String errMsg = null;
		if( fromUserId == toUserId ){
			errMsg = "请求用户ID异常";
			return errMsg;
		}
		
		//0.先查找是否存在该记录
		//1.删除自己对于对方的记录
		//2.设置对方对于自己的记录
		ChatFriend chatFriend = chatFriendDao.findByLeftUserIdAndRightUserId( fromUserId,  toUserId );
		ChatFriend reverseFriend = chatFriendDao.findByLeftUserIdAndRightUserId( toUserId , fromUserId );
		if( chatFriend == null || reverseFriend == null  ){
				
			errMsg = "好友关系不存在";
			
		}else {
			
			if( chatFriend.getRelationType() != INT_ALREADY_IS_FRIEND && reverseFriend.getRelationType() != INT_ALREADY_IS_FRIEND ){
				
					errMsg = "好友关系不存在";

			}else {
				
				try {
					
					chatFriendDao.delete( chatFriend );
					reverseFriend.setLastTime( new Timestamp( new Date().getTime() ) );
					reverseFriend.setRelationType( INT_DELETE_FRIEND  );
					chatFriendDao.update( reverseFriend );
					
				}catch( Exception e ){
					
					errMsg = "操作失败,请重试";
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			}
		}
		return errMsg;
	}
	
	
	
	/**
	 *  4.判断是不是自己的好友
	 * 
	 * */
	public String isFriend( int fromUserId, int toUserId ){
		
		String errMsg = null;
		
		//判断是不是好友
		ChatFriend chatFriend = chatFriendDao.findByLeftUserIdAndRightUserId( fromUserId,  toUserId );
		if(  chatFriend==null || chatFriend.getRelationType() != INT_ALREADY_IS_FRIEND  ){
				return "isNotFriend";
		}
		
		return errMsg;
	}
	
	
	/**
	 * 4.1 判断是不是项目工友
	 * */
	public Map<String,Object> isWorkFriend( int fromUserId, int toUserId ){

        Map<String, Object> map = new HashMap<>();
		String isFriendResult =  isFriend(fromUserId, toUserId);
		map.put("isFriend", isFriendResult == null );
        long isWrokFriend = proOrganPerDao.isWorkFriend(fromUserId, toUserId);
        map.put("isWorkerFriend", isWrokFriend > 0);

//		List fromUserPoList = proOrganPerDao.findByUserIdAndPoType(fromUserId); //查询其所在班组
//		List toUserPoList   = proOrganPerDao.findByUserIdAndPoType(toUserId);
//
//		if( fromUserPoList.size() ==0 || toUserPoList.size() == 0 ){
//
//			map.put("isWorkerFriend", false );
//			return map;
//		}
//
//		for( int i=0; i<fromUserPoList.size(); i++ ){
//			for( int j=0; j<toUserPoList.size(); j++ ){
//
//				int fromUserPo = (int) fromUserPoList.get(i);
//				int toUserPo   = (int) toUserPoList.get(j);
//				if( fromUserPo == toUserPo ){
//
//					map.put("isWorkerFriend", true );
//					return map;
//				}
//			}
//		}
//
//		//通过班组去拿项目的id
//		String fromUserSql = "";
//		for( int i=0; i<fromUserPoList.size(); i++ ){
//			fromUserSql +=  " id = "+fromUserPoList.get(i) + " AND";
//		}
//		fromUserSql = fromUserSql.substring( 0 , fromUserSql.length()-3);
//		fromUserSql = "select proId from ProOrgan where " + fromUserSql;
//
//		String toUserSql = "";
//		for( int i=0; i<toUserPoList.size(); i++ ){
//			toUserSql +=  " id = "+toUserPoList.get(i) + " AND";
//		}
//		toUserSql = toUserSql.substring( 0 , toUserSql.length()-3);
//		toUserSql = "select proId from ProOrgan where " + toUserSql;
//
//
//
//		List fromUserProList = proOrganDao.findBySql( fromUserSql );
//		List toUserProList   = proOrganDao.findBySql( toUserSql );
//		for( int i=0; i<fromUserProList.size(); i++ ){
//			for( int j=0; j<toUserProList.size(); j++ ){
//
//				int fromUserProId =  (int) fromUserProList.get(i);
//				int toUserProId =  (int) toUserProList.get(j);
//				if( fromUserProId == toUserProId ){
//
//					map.put("isWorkerFriend", true );
//					return map;
//				}
//			}
//		}
		return map;
	}
	
	
	
	
	
	/**
	 * 5.获取自己的好友用户
	 * 
	 * */
	public List getFriendList( int fromUserId ){

		List list =  chatFriendDao.findFriendList(fromUserId, INT_ALREADY_IS_FRIEND );
		return chatFriendListFillWithFriendUserInfoByUserId(list, true, true);
	}
	

	

	//判断是否在三天之内
	public boolean isInThreeDay( Date date ){
		
		Date now = new Date();
		long lNow = now.getTime()/1000/(60*60*24); //剩下日期
		long lDate = date.getTime()/1000/(60*60*24);
		
		return lNow >= lDate && lNow-3<= lDate; //表示3天内
	}
	
	
	
	
	/**  经纬度计算 */
	//适用于近距离
    public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
    {
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
        dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
        dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }
	    
    
    
    /* 计算经纬度点对应正方形4个点的坐标 
    * 
    * @param longitude 
    * @param latitude 
    * @param distance 
    * @return 
    */  
   public static Map<String, Double> returnLLSquarePoint(double latitude, double longitude,  
            double distance ) {  
   	
       Map<String, Double> squareMap = new HashMap<String, Double>();  
       // 计算经度弧度,从弧度转换为角度  
       double dLongitude = 2 * (Math.asin(Math.sin(distance  
               / (2 * DEF_R))  
               / Math.cos(Math.toRadians(latitude))));  
       dLongitude = Math.toDegrees(dLongitude);  
       // 计算纬度角度  
       double dLatitude = distance / DEF_R;  
       dLatitude = Math.toDegrees(dLatitude);  
       // 正方形   
       squareMap.put("left", Math.min( latitude + dLatitude, latitude - dLatitude));  
       squareMap.put("right", Math.max( latitude + dLatitude,latitude - dLatitude));  
       squareMap.put("top", Math.min( longitude + dLongitude,longitude - dLongitude));  
       squareMap.put("bottom", Math.max( longitude + dLongitude,longitude - dLongitude));  
       return squareMap;  
   }  

	
}
