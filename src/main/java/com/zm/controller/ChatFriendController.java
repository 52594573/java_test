package com.zm.controller;

import com.google.gson.Gson;
import com.ktp.project.service.UserService;
import com.zm.service.ChatFriendService;
import com.zm.utils.ResponseInfoMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *   好友请求操作controller
 * */
@Controller
public class ChatFriendController {

	@Autowired
	private ChatFriendService chatFriendService;
	@Autowired
	private UserService userService;
	private int  INT_ERR_INFO_CODE = -1;
	private int  INT_RIGHT_INFO_CODE = 100;
	/**
	 *  API接口:
	 *  1.根据手机号查询好友
	 *  2.获取别人添加自己为好友的消息
	 *  3.获取自己处理好友添加消息的列表
	 *  4.添加好友请求
	 *  5.拒绝好友添加请求
	 *  6.删除好友
	 *  7.判断某个人是不是自己的好友
	 *  8.获取某人的好友列表
	 * */
	
	/** 1. 根据手机号查询好友 */
	@RequestMapping (value="/searchUserByPhone",produces={"application/json;charset=UTF-8"})
	@ResponseBody 
	public String searchUserByPhone( String phone ){
		
		Map map = null;
		if( phone == null || phone.length() == 0 ){
				
			map = ResponseInfoMap.getResponseMap(INT_ERR_INFO_CODE, "参数错误",  null );
			
		}else {
			
			List  list  =  chatFriendService.searchUserByPhone( phone );
			if( list == null  ){
				
				map = ResponseInfoMap.getResponseMap( INT_ERR_INFO_CODE, "未搜索好用户",  null );
				
			}else {
				
				map = ResponseInfoMap.getResponseMap( INT_RIGHT_INFO_CODE,  "success",  list );
			}
		}	
		Gson gson = new Gson();
		return gson.toJson( map );
	}
	
	/**
	 * 1.2 更新用户坐标
	 * */
	@RequestMapping (value="/updateUserCoordinate",produces={"application/json;charset=UTF-8"})
	@ResponseBody 
	public String updateUserCoordinate( int userId , double latitude , double longitude ){

		userService.saveOrUpdateUserActive(userId, latitude, longitude);
		
		Map map = null;
		String resultStr  =  chatFriendService.updateUserCoordinate(userId, latitude, longitude);
		if( resultStr == null  ){
			
			map = ResponseInfoMap.getResponseMap( INT_RIGHT_INFO_CODE,  "success",  "用户坐标更新成功" );
			
		}else {
			
			map = ResponseInfoMap.getResponseMap( INT_ERR_INFO_CODE,  resultStr,  "" );
		}
		Gson gson = new Gson();
		return gson.toJson( map );
	}
	
	
	
	/** 1.3  获取附近的人 */
	@RequestMapping (value="/getNearUsers",produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String getNearUsers( int userId , double latitude , double longitude, int startPage, int length ){

		Map map = null;

		List  list  =  chatFriendService.getNearUsers(userId, latitude, longitude, startPage, length);
		if( list == null  ){

			map = ResponseInfoMap.getResponseMap(INT_ERR_INFO_CODE, "附近无工友", "附近无工友");

		}else {
				
				map = ResponseInfoMap.getResponseMap( INT_RIGHT_INFO_CODE,  "success",  list );
		}
			
		
		Gson gson = new Gson();
		return gson.toJson( map );
	}


	/** 2.获取别人添加自己为好友的消息 */
	@RequestMapping (value="/newAddFriendMsg",produces={"application/json;charset=UTF-8"})
	@ResponseBody 
	public String searchNewAddFriendByUserId( int fromUserId ){
		
		Map map = null;
		List  list  =  chatFriendService.searchNewAddFriendByUserId( fromUserId );
		if( list == null  ){
			
			map = ResponseInfoMap.getResponseMap( INT_ERR_INFO_CODE, "没有被请求的好友信息", null );
			
		}else {
			
			map = ResponseInfoMap.getResponseMap( INT_RIGHT_INFO_CODE , "success",  list );
		}
		Gson gson = new Gson();
		return  gson.toJson(map);
	}
	
	

	/** 3.获取好友添加的状态 */
	@RequestMapping (value="/getAddFriendList",produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String searchFriendApplyListByUserId( int fromUserId ){
		
		Map map = null;
		List  list  =  chatFriendService.searchFriendApplyListByUserId( fromUserId );
		if( list == null  ){
			
			map = ResponseInfoMap.getResponseMap( INT_ERR_INFO_CODE, "没有被请求的好友信息", null );
			
		}else {
			
			map = ResponseInfoMap.getResponseMap( INT_RIGHT_INFO_CODE,"success", list );
		}
		
		Gson gson = new Gson();
		return  gson.toJson(map);
	}

	

	/** 4.添加好友 */
	@RequestMapping (value="/addFriend",produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String addFriend( int fromUserId, int toUserId, String attachMsg ){ //relationType为99
		
		Map map = null;
		String result  =  chatFriendService.addFriend(fromUserId, toUserId, attachMsg );
		if( result == null ){
			
			map = ResponseInfoMap.getResponseMap( INT_RIGHT_INFO_CODE ,"添加好友申请发送成功", null );
			
		}else {
			
			map = ResponseInfoMap.getResponseMap( -1 ,"success", result );
		}
		
		Gson gson = new Gson();
		return gson.toJson( map );
	}
	
	
	
	/** 5.同意添加好友 */
	@RequestMapping (value="/agreeAddFriend" ,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String agreeAddFriend( int fromUserId, int toUserId ){ //relationType为2
		
		Map map = null;
		String result  =  chatFriendService.agreeAddFriend(fromUserId, toUserId);
		if( result == null ){
			
			map = ResponseInfoMap.getResponseMap( INT_RIGHT_INFO_CODE ,"好友添加成功",null );
			
		}else {
			
			map = ResponseInfoMap.getResponseMap( -1,result,null );
		}
		
		Gson gson = new Gson();
		return gson.toJson( map );
	}
	
	
	
	
	/** 6.拒绝好友添加请求 */
	@RequestMapping (value ="/refuseAddFriend" ,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String addFriend( int fromUserId, int toUserId, int addType ){ //addType只能为4，5，6
		
		Map map = null;
		Gson gson = new Gson();
		String result  =  chatFriendService.refusedToAddFriend(fromUserId, toUserId, addType);
		if( result == null ){
			
			map = ResponseInfoMap.getResponseMap( INT_RIGHT_INFO_CODE ,"拒绝添加好友成功",null );
			
		}else {
			
			map = ResponseInfoMap.getResponseMap( -1,result,null );
		}
		return gson.toJson( map );
	}
	
	
	
//	 *  6.删除好友
//	 *  7.判断某个人是不是自己的好友
//	 *  8.获取某人的好友列表
	/** 7.删除好友 */
	@RequestMapping (value="/deleteFriend" ,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String deleteFriend(  int fromUserId, int toUserId ){ //被删除的好友，relationType被设置为3
		
		Map map = null;
		String result  =  chatFriendService.removeFriend(fromUserId, toUserId);
		if( result == null ){
			
			map = ResponseInfoMap.getResponseMap( INT_RIGHT_INFO_CODE ,"删除好友成功",null );
			
		}else {
			
			map = ResponseInfoMap.getResponseMap( INT_RIGHT_INFO_CODE ,result,null );
		}
		Gson gson = new Gson();
		return gson.toJson( map );
	}
	
	
	
	
	/** 8. 判断某人是不是你的好友  */
	@RequestMapping (value ="/isFriend",produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String isFriend( int fromUserId, int toUserId ){
		
		Map map = null;
		String result  =  chatFriendService.isFriend(fromUserId, toUserId);
		int code = INT_RIGHT_INFO_CODE;
		if( result == null ){
			
			result = "isFriend";
		}
		map = ResponseInfoMap.getResponseMap( ResponseInfoMap.SUCCESS_CODE , ResponseInfoMap.SUCCESS_MSG , result );
		
		Gson gson = new Gson();
		return gson.toJson( map );
	}
	
	
	/**
	 * 8.1 判断是不是工友
	 * */
	@RequestMapping (value ="/isWorkFriend",produces={"application/json;charset=UTF-8"})
	@ResponseBody
     public String isWorkFriend( int fromUserId, int toUserId ){
		
		Map map  =  chatFriendService.isWorkFriend(fromUserId, toUserId);
		map = ResponseInfoMap.getResponseMap( ResponseInfoMap.SUCCESS_CODE , ResponseInfoMap.SUCCESS_MSG ,map );
		Gson gson = new Gson();
		return gson.toJson( map );
	}
	
	
	
	
	/** 9. 获取某个人的好友列表 */
	@RequestMapping (value="/getFriendList",produces={"application/json;charset=UTF-8"} )
	@ResponseBody
	public String getFriendList( int fromUserId ){
		
		Map map = null;
		List list  =  chatFriendService.getFriendList(fromUserId);
		if( list == null ){

			list = new ArrayList();		
		}
		map = ResponseInfoMap.getResponseMap( INT_RIGHT_INFO_CODE , "请求成功" ,list);
		
		Gson gson = new Gson();
		return gson.toJson( map );
	}		
}
