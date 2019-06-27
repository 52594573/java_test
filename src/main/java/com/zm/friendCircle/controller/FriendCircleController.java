package com.zm.friendCircle.controller;

import com.google.gson.Gson;
import com.zm.friendCircle.Service.EmployeeService;
import com.zm.friendCircle.Service.EmployerService;
import com.zm.friendCircle.Service.FriendCircleService;
import com.zm.friendCircle.Service.NetworkArticleService;
import com.zm.friendCircle.entity.ZmFriendCircle;
import com.zm.friendCircle.entity.ZmFriendCircleComment;
import com.zm.utils.ResponseInfoMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class FriendCircleController {

	@Autowired
	private FriendCircleService friendCircleService;
	@Autowired
	private EmployeeService  employeeService;
	@Autowired
	private EmployerService  employerService;
	@Autowired
	private NetworkArticleService networkArticleService;
	
	
	/**
	 * 1.获取说说
	 * 		userId  用户的id
	 * 		index  : 上传的类型    index=0全部  index=1说说  index=2招工  index=3找工  4
	 * 		startPage: 起始页位置
	 * 		length : 数量
	 * */
	/**@RequestMapping (value = " / getShare ", produces = { " application / json ; charset = UTF - 8 " })
	 @ResponseBody public String getShuoshuo( int index, int userId , int startPage, int length) {

	 if( startPage <= 0 ){
	 startPage = 1; //默认从1开始
	 }
	 int begin = (startPage-1) * length;
	 List<Object> list = new ArrayList<>();
	 List temp = friendCircleService.getAll(index, userId, begin, length);
	 if (temp != null) {
	 for (int i = 0; i < temp.size(); i++) {
	 Object obj = null;
	 ZmFriendCircle friendCircle = (ZmFriendCircle) temp.get(i);
	 if (friendCircle.getIndex() == 1) {
	 obj = employerService.getEmployerById(friendCircle.getIndexId(), FriendCircleService.INT_EMPLOYER_ID);
	 } else if (friendCircle.getIndex() == 2) {
	 obj = employeeService.getEmployeeId(friendCircle.getIndexId(), FriendCircleService.INT_EMPLOYEE_ID);
	 } else if (friendCircle.getIndex() == 3) {
	 obj = friendCircleService.getShuoshuoById(userId, friendCircle.getIndexId());
	 } else if (friendCircle.getIndex() == 4) {
	 obj = networkArticleService.getNetworkArticleById(friendCircle.getIndexId(), FriendCircleService.INT_NETWORK_ARTICLE_ID);
	 }
	 System.out.println("--> " + i + " --> " + obj);
	 list.add(obj);
	 }
	 }
//		if( index==0 ){
//
//			list = new ArrayList();
//			List temp = friendCircleService.getAll(index, userId, begin, length);
//			if( temp != null ){
//				for( int i=0; i<temp.size(); i++ ){
//					Object obj = null;
//					ZmFriendCircle friendCircle = (ZmFriendCircle) temp.get(i);
//					if( friendCircle.getIndex() == 1 ){
//						obj = employerService.getEmployerById(friendCircle.getIndexId(), FriendCircleService.INT_EMPLOYER_ID);
//					}else if( friendCircle.getIndex() == 2 ){
//						obj = employeeService.getEmployeeId(friendCircle.getIndexId(), FriendCircleService.INT_EMPLOYEE_ID);
//					}else if( friendCircle.getIndex() == 3 ){
//						obj = friendCircleService.getShuoshuoById(userId, friendCircle.getIndexId());
//					}else if( friendCircle.getIndex() == 4 ){
//						obj = networkArticleService.getNetworkArticleById(friendCircle.getIndexId(), FriendCircleService.INT_NETWORK_ARTICLE_ID);
//					}
//					System.out.println( "--> "+i+" --> "+obj );
//					list.add(obj);
//				}
//			}
//
//		}else if( index==1 ){ //招工
//			list = employerService.getEmployerList(index, userId, begin, length);
//		}else if( index==2 ){ //找工
//			list =  employeeService.getEmployeeList(index, userId, begin, length);
//		}else if( index==3 ){ //说说
//			list = friendCircleService.getShuoshuo(userId, index, begin, length);
//		}else if( index==4 ){ //网络文章
//			list = networkArticleService.getNetworArticleList(index, userId, begin, length);
//		}
	 Gson gson = new Gson();
	 return  gson.toJson(ResponseInfoMap.getResponseMap( ResponseInfoMap.SUCCESS_CODE , "success", list));
	 }*/

	/**
	 * 1.获取说说
	 * userId  用户的id
	 * index  : 上传的类型    index=0全部  index=1说说  index=2招工  index=3找工  4
	 * startPage: 起始页位置
	 * length : 数量
	 */
	@RequestMapping(value = "/getShare", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getShuoshuo(int index, int userId, int startPage, int length) {

		if (startPage <= 0) {
			startPage = 1; //默认从1开始
		}
		int begin = (startPage - 1) * length;
		List list = null;
		if (index == 0) {

			list = new ArrayList();
			List temp = friendCircleService.getAll(userId, begin, length);
			if (temp != null) {
				for (int i = 0; i < temp.size(); i++) {
					Object obj = null;
					ZmFriendCircle friendCircle = (ZmFriendCircle) temp.get(i);
					if (friendCircle.getIndex() == 1) {
						obj = employerService.getEmployerById(friendCircle.getIndexId(), FriendCircleService.INT_EMPLOYER_ID);
					} else if (friendCircle.getIndex() == 2) {
						obj = employeeService.getEmployeeId(friendCircle.getIndexId(), FriendCircleService.INT_EMPLOYEE_ID);
					} else if (friendCircle.getIndex() == 3) {
						obj = friendCircleService.getShuoshuoById(userId, friendCircle.getIndexId());
					} else if (friendCircle.getIndex() == 4) {
						obj = networkArticleService.getNetworkArticleById(friendCircle.getIndexId(), FriendCircleService.INT_NETWORK_ARTICLE_ID);
					}
					System.out.println("--> " + i + " --> " + obj);
					list.add(obj);
				}
			}

		} else if (index == 1) { //招工
			list = employerService.getEmployerList(index, userId, begin, length);
		} else if (index == 2) { //找工
			list = employeeService.getEmployeeList(index, userId, begin, length);
		} else if (index == 3) { //说说
			list = friendCircleService.getShuoshuo(userId, index, begin, length);
		} else if (index == 4) { //网络文章
			list = networkArticleService.getNetworArticleList(index, userId, begin, length);
		}
		Gson gson = new Gson();
		return gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, "success", list));
	}
	
	
	/**
	 * 1.2 获取自己的说说
	 * 	 * 	userId  用户的id
	 * 		startPage: 起始页位置
	 * 		length : 数量
	 * */
	@RequestMapping (value="/getAlbumShare",produces={"application/json;charset=UTF-8"})
	@ResponseBody 
	public String getMyShuoshuo(  int userId , int startPage, int length ) {
		
		if( startPage <= 0 ){
			startPage = 1; //默认从1开始
		}
		
		int begin = (startPage-1) * length;
		List list = null;
		list = new ArrayList(); 
		List temp = friendCircleService.getMy(userId, begin, length);
		if( temp != null ){
			for( int i=0; i<temp.size(); i++ ){
				
				Object obj = null;
				ZmFriendCircle friendCircle = (ZmFriendCircle) temp.get(i);
				if( friendCircle.getIndex() == 1 ){
					
					obj = employerService.getEmployerById(friendCircle.getIndexId(), FriendCircleService.INT_EMPLOYER_ID);
					
				}else if( friendCircle.getIndex() == 2 ){
					
					obj = employeeService.getEmployeeId(friendCircle.getIndexId(), FriendCircleService.INT_EMPLOYEE_ID);
					
				}else if( friendCircle.getIndex() == 3 ){
					
					obj = friendCircleService.getShuoshuoById(friendCircle.getIndexId());
					
				}else if( friendCircle.getIndex() == 4 ){
					
					obj = networkArticleService.getNetworkArticleById(friendCircle.getIndexId(), FriendCircleService.INT_NETWORK_ARTICLE_ID);
				}
				if (obj != null) {
					list.add(obj);
				}
			}
		}
		Gson gson = new Gson();
		return  gson.toJson(ResponseInfoMap.getResponseMap( ResponseInfoMap.SUCCESS_CODE , "success", list));
	}
	

	
	
	
	/**
	 * 2.上传说说
	 * 	 	userId 用户的id
	 * 		index  上传的类型    index=1说说   index=2招工   index=3找工
	 * 		content 说说的内容
	 * 		picList 上传的图片集合
	 * */
	@RequestMapping (value="/postShare",produces={"application/json;charset=UTF-8"})
	@ResponseBody 
	public String addNewShuoShuo( int index, int userId, String content, String picJson ){
		
		Gson gson = new Gson();
		String[] picList = null;
		if ( picJson != null && picJson.length() >0  ){
			picList  = picJson.split(",");
		}
		
		String resultMsg = friendCircleService.addNewShuoShuo( index, userId, content, picList);
		
		if( resultMsg == null ){
			
			return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, ResponseInfoMap.SUCCESS_MSG, "" ));
			
		}else {
			
			return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.FAILURE_CODE,  resultMsg ,  "" ));
		}
	}
	
	
	
	/**
	 * 2.1 上传找工作
 * 	 		userId 用户的id
	 * 		index  上传的类型    index=1说说   index=2招工   index=3找工
	 * 		content 说说的内容
	 * 		picList 上传的图片集合
	 * */
	@RequestMapping (value="/postFindWork",produces={"application/json;charset=UTF-8"})
	@ResponseBody 
	public String addEmployee( int index,  int userId, String content, String arriveTime, String work ){
		
		Gson gson = new Gson();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date arrivetimeDate=null;
		try {
			arrivetimeDate = dateFormat.parse(arriveTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String resultMsg = employeeService.addEmployee(index, userId, content, arrivetimeDate, work);
		
		if( resultMsg == null ){
			
			return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, ResponseInfoMap.SUCCESS_MSG, "" ));
			
		}else {
			
			return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.FAILURE_CODE,  resultMsg ,  "" ));
		}
	}
	
	
	
	/**
	 * 2.2 上传招工
	 *    userId  包含了名字，用户头像
	 *    content 说说的内容
	 *    workIds    工种列表
	 *    amount  数量
	 *    address : 地址	
	 *    createDate  创建时间
	 * */
	@RequestMapping (value="/postFindWorker",produces={"application/json;charset=UTF-8"})
	@ResponseBody 
	public String postEmployer( int index, int userId, String content, String workType, String works, int amount, String address ){
		
		Gson gson = new Gson();

		String resultMsg = employerService.postEmployer(index, userId, content, workType, works, amount, address);
		if( resultMsg == null ){
			
			return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, ResponseInfoMap.SUCCESS_MSG, "" ));
			
		}else {
			
			return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.FAILURE_CODE,  resultMsg ,  "" ));
		}
	}
	
	
	/**
	 * 2.3 上传网络文章
	 * */
	@RequestMapping (value="/postNetworkArticle",produces={"application/json;charset=UTF-8"})
	@ResponseBody 
	public String postNetworkArticle( int index,  int userId, String content, String url, String picUrl, String summary ){
		
		Gson gson = new Gson();
		String resultMsg = networkArticleService.addNetworkArticle(index, userId, content, url, picUrl, summary);
		if( resultMsg == null ){
			
			return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, ResponseInfoMap.SUCCESS_MSG, "" ));
			
		}else {
			
			return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.FAILURE_CODE,  resultMsg ,  "" ));
		}
	}
	
	
	/**
	 * 3.删除说说
	 * */
	@RequestMapping (value="/deleteShare",produces={"application/json;charset=UTF-8"})
	@ResponseBody 
	public String deleteShuoShuo( int index, int indexId, int userId ){
		
		Gson gson = new Gson();
		String resultMsg = null;
		
		if( index == 1 ){
			resultMsg = employerService.deleteEmployerShare(index, indexId, userId);
		}else if( index ==2 ){
			resultMsg = employeeService.deleteEmployeeShare(index, indexId, userId);
		}else if( index == 3 ){
			resultMsg = friendCircleService.deleteShuoShuo( index, indexId, userId );
		}else if( index ==4 ){
			resultMsg = networkArticleService.deleteNetworkAriticsShare(index, indexId, userId);
		}else {
			resultMsg = "index指向错误";
		}
		 
		if( resultMsg == null ){
			
			return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, ResponseInfoMap.SUCCESS_MSG, "删除成功" ));
			
		}else {
			
			return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.FAILURE_CODE,  resultMsg ,  "" ));
		}
	}
	
	
	
	/**
	 * 4.增加点赞/删除点赞
	 * */
	@RequestMapping (value="/addLike",produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String addLike( int index, int indexId, int userId ){ 
		
		String resultMsg = friendCircleService.addLike( index,indexId , userId);
		Gson gson = new Gson();
		return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, resultMsg , "" ));
	}
	
	
	
	/**
	 * 5.增加评论
	 * */
	@RequestMapping (value="/addComment",produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String addComment( int index ,int indexId, int fromUserId, int toUserId, String content  ){
		
		ZmFriendCircleComment comment = friendCircleService.addComment( index,indexId , fromUserId, toUserId, content);
	    Gson gson = new Gson();
	    if( comment == null ){
			
			return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, ResponseInfoMap.SUCCESS_MSG, "评论操作失败，请重试" ));
			
		}else {
			
			return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE,  ResponseInfoMap.SUCCESS_MSG ,  comment ));
		}
	}
	

	
	/**
	 * 6.删除评论
	 * */
	@RequestMapping (value="/deleteComment",produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String deleteComment( int index, int indexId , int commentId, int fromUserId ){
		
		 String resultStr = friendCircleService.deleteComment( index,indexId, commentId, fromUserId);
		 Gson gson = new Gson();
	    if( resultStr == null ){
			
			return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, ResponseInfoMap.SUCCESS_MSG, "删除评论成功" ));
			
		}else {
			
			return  gson.toJson(ResponseInfoMap.getResponseMap(ResponseInfoMap.FAILURE_CODE,  resultStr ,  "" ));
		}
	}
	
	
	/**
	 *  4.1 获取相册图片集合
	 * */
	@RequestMapping (value="/getAlbum",produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String getAlbumList(  int  index, int userId ){
		
		List list = friendCircleService.getAlbumList(index, userId);
		Map resultMap = ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, ResponseInfoMap.SUCCESS_MSG, list);
		Gson gson = new Gson();
		return gson.toJson(resultMap);
	}

	
	/**
	 * 4.2 是否屏蔽了某个人
	 * */
	@RequestMapping (value="/isRefusedLookPerson",produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String isRefusedLookAboutOnePerson( int fromUserId, int toUserId ){
		
		boolean result = friendCircleService.isRefusedLookAboutOnePerson(fromUserId, toUserId);
		Gson gson = new Gson();
		Map resultMap = ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, ResponseInfoMap.SUCCESS_MSG, result);
		return gson.toJson(resultMap);
	}
	
	
	/**
	 * 4.3 屏蔽/取消屏蔽某人的朋友圈
	 * */
	@RequestMapping (value="/refusedLookPerson",produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String refusedLookAboutOnePerson( int fromUserId, int toUserId  ){
		
		String resultStr = friendCircleService.refusedLookAboutOnePerson(fromUserId, toUserId);
		Gson gson = new Gson();
		if( resultStr == null ){
			
			Map resultMap = ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, ResponseInfoMap.SUCCESS_MSG, "");
			return gson.toJson(resultMap);
			
		}else {
			
			Map resultMap = ResponseInfoMap.getResponseMap(ResponseInfoMap.FAILURE_CODE, resultStr, "");
			return gson.toJson(resultMap);
		}
	}
	
	
	
	/**
	 * 4.4 屏蔽/取消屏蔽 某一条说说
	 * */
	@RequestMapping (value="/refusedLookShuoshuo",produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String refusedLookAboutOneShuoShuo( int fromUserId, int index, int indexId ){
		
		String resultStr = friendCircleService.refusedLookAboutOneShuoShuo(fromUserId, index, indexId);
		Gson gson = new Gson();
		if( resultStr == null ){
			
			Map resultMap = ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, ResponseInfoMap.SUCCESS_MSG, "");
			return gson.toJson(resultMap);
			
		}else {
			
			Map resultMap = ResponseInfoMap.getResponseMap(ResponseInfoMap.FAILURE_CODE, resultStr, "");
			return gson.toJson(resultMap);
		}
	}
	
	
	

	/**
	 *  7.获取建筑工人的职业类型
	 *      木工，铁工，混凝土，外加，粗装修，其他
	 * */
	@RequestMapping (value="/getWorkCategory",produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String getWorkClassify( ){
		
		List list = employeeService.getWorkClassify();
		Gson gson = new Gson();
	    Map resultMap = ResponseInfoMap.getResponseMap(ResponseInfoMap.SUCCESS_CODE, ResponseInfoMap.SUCCESS_MSG, list);
	    return gson.toJson(resultMap);
	    
	}
}
