package com.zm.controller;

import com.google.gson.Gson;
import com.ktp.project.util.Md5Util;
import com.zm.service.ChatGroupService;
import com.zm.service.UserInfoUpLoadToAlibabaService;
import com.zm.utils.ResponseInfoMap;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 *   个人用户，或者群注册
 * */
@Controller
public class UserOrGroupRegisterToServerController {

	private static final String PASSWORD = "FAFJJeremf@#$&245";

	@Autowired
	private UserInfoUpLoadToAlibabaService uploadService;
	@Autowired
	private ChatGroupService chatGroupService;
	
	
	/** 1. 将用户注册到ImServer */
	@RequestMapping (value="/registerUser",produces={"application/json;charset=UTF-8"})
	@ResponseBody 
	public String registerUserToServer( int userId ){
		
		//用户id，然后通过id，获取用户信息
		//将用户注册到ImServer
		String str = uploadService.uploadUserWithUserId( userId );
		Map resultMap = null;
		if( str == null ){
			resultMap = ResponseInfoMap.getResponseMap( ResponseInfoMap.SUCCESS_CODE , ResponseInfoMap.SUCCESS_MSG,  null );
		}else {
			
			if( str.contains("exist") ){
				resultMap = ResponseInfoMap.getResponseMap( ResponseInfoMap.SUCCESS_CODE, ResponseInfoMap.SUCCESS_MSG,  null );
			}else {
				resultMap = ResponseInfoMap.getResponseMap( ResponseInfoMap.FAILURE_CODE , str,  null );
			}
			
		}
		Gson gson = new Gson();
		return gson.toJson(resultMap);
	}
	
	
	/** 1. 将群主注册到ImServer */
	@RequestMapping (value="/registerGroup",produces={"application/json;charset=UTF-8"})
	@ResponseBody 
	public String registerGroupToServer( int bzId ){
		
		//班组id，然后通过id，获取班组信息
		//注册群组
		//将班组中的成员导入到群组中
		String resultStr = chatGroupService.oneBzByProOrganId(bzId);
		if( resultStr.contains("zemeng-")){
			
			String sGroupId  = resultStr.substring(7);
			long  groupId    = Long.valueOf(sGroupId);
			resultStr = chatGroupService.oneBzUserListJoinGroupByGroupId(groupId);
		}
		
		Map resultMap = null;
		if( resultStr == null ){
			resultMap = ResponseInfoMap.getResponseMap( ResponseInfoMap.SUCCESS_CODE , "success",  null );
		}else {
			resultMap = ResponseInfoMap.getResponseMap( -1 , resultStr,  null );
		}
		Gson gson = new Gson();
		return gson.toJson(resultMap);
	}
	
	
	/**
	 * 1.用户更新资料
	 * */
	/** 1. 将群主注册到ImServer */
	@RequestMapping (value="/updateUserInIm",produces={"application/json;charset=UTF-8"})
	@ResponseBody 
	public String updateUserInIm( int userId ){
		
		//班组id，然后通过id，获取班组信息
		//注册群组
		//将班组中的成员导入到群组中
		String resultStr = uploadService.updateOneUserToAlibabaServer(userId);
		Map resultMap = null;
		if( resultStr == null ){
			resultMap = ResponseInfoMap.getResponseMap( ResponseInfoMap.SUCCESS_CODE , ResponseInfoMap.SUCCESS_MSG,  null );
		}else {
			resultMap = ResponseInfoMap.getResponseMap( -1 , resultStr,  null );
		}
		Gson gson = new Gson();
		return gson.toJson(resultMap);
	}

	/**
	 * @param userId
	 * @param key     md5(userId+PASSWORD)
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateUserInfoInOpenIm", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String updateUserInfoInOpenIm(int userId, String key, HttpServletRequest request) {
		String userName = request.getParameter("userName");
		String userPic = request.getParameter("userPic");
		String mobile = request.getParameter("mobile");
		Gson gson = new Gson();
		if (userId <= 0) {
			return gson.toJson(ResponseInfoMap.getResponseMap(-1, "缺少userId参数", null));
		}
		String md5Key = Md5Util.MD5Encode(userId + PASSWORD);
		if (md5Key != null && !md5Key.equals(key)) {
			return gson.toJson(ResponseInfoMap.getResponseMap(-1, "校验失败", null));
		}
		if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(userPic) && TextUtils.isEmpty(mobile)) {
			return gson.toJson(ResponseInfoMap.getResponseMap(-1, "缺少必要参数", null));
		}

		//班组id，然后通过id，获取班组信息
		//注册群组
		//将班组中的成员导入到群组中
		String resultStr = uploadService.updateOneUserToAlibabaServer(userId, userName, userPic, mobile);
		Map resultMap = null;
		if (resultStr == null) {
			resultMap = ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, ResponseInfoMap.SUCCESS_MSG, null);
		} else {
			resultMap = ResponseInfoMap.getResponseMap(-1, resultStr, null);
		}
		return gson.toJson(resultMap);
	}
	
	
	/**
	 * 2.项目自动建群
	 *     为项目经理，所有的项目班组长建立一个群； 群组为项目经理
	 * */
	
	
	/**
	 * 3.班组添加人，则自动拉入聊天群
	 * */
	
	
//	uploadUserWithUserId
	
}
