package com.zm.service;

import com.google.gson.Gson;
import com.taobao.api.ApiException;
import com.taobao.api.request.OpenimTribeCreateRequest;
import com.taobao.api.request.OpenimTribeDismissRequest;
import com.taobao.api.request.OpenimTribeGetalltribesRequest;
import com.taobao.api.request.OpenimTribeJoinRequest;
import com.taobao.api.response.OpenimTribeCreateResponse;
import com.taobao.api.response.OpenimTribeDismissResponse;
import com.taobao.api.response.OpenimTribeGetalltribesResponse;
import com.taobao.api.response.OpenimTribeJoinResponse;
import com.zm.entity.*;
import com.zm.utils.TaoBaoClientUtil;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

/**
 *   群
 * */

@Service
@Transactional
public class ChatGroupService {

	@Autowired
	private ProOrganDAO proOrganDao;  //班组bean的操作对象
	@Autowired
	private ProjectDAO projectDao;   //项目bean的操作对象
	@Autowired
	private ChatGroupDAO chatGroupDao;
	@Autowired
	private ProOrganPerDAO proOrganPerDao; //工组与工人
	
	private int count=0;
	
	{
		//测试搜索好友
//		ApplicationContext context = new FileSystemXmlApplicationContext("/src/main/resources/spring/spring-dao.xml" );
//		proOrganDao  = (ProOrganDAO) context.getBean("proOrganDao");
//		projectDao   = (ProjectDAO)  context.getBean("projectDao");
//		chatGroupDao = (ChatGroupDAO)context.getBean("chatGroupDao");
//		proOrganPerDao  = (ProOrganPerDAO) context.getBean("proOrganPerDao");
	}
	
	/**
	 *  一 创建群
	 *  1.获取project信息， 
	 *  	p_name项目名 --> tribeName群名;
	 *  	p_content项目简介 -->  notice群公告；
	 *  	tribe_type=0表示为普通群； tribe_type=1表示为讨论组；  如果tribe_type=1时，members参数有效且必填，否则无效。
	 *  
	 *  1.1 创建群，指定群主
	 *  1.2 群主要求群成员
	 *  1.3 被邀请的群成员同意群主邀请
	 *  1.4 获取用户群列表
	 *  
	 * */
	/**
	 *   获取所有的班组的名称
	 * */

	public void bzToChatGroup(){
		
		//1.先获取所有的班组
		List<ProOrgan> proOrganList = proOrganDao.findAll();
		for( int i=0; i<proOrganList.size(); i++ ){
			ProOrgan proOrgan = (ProOrgan)proOrganList.get( i );
			if( proOrgan.getPoFzr() == 0 ){
				continue;
			}
			oneProjectToChatGroup(proOrgan);
		}
	}
	
	/**
	 *  通过班组获取一条班组记录
	 * */
	public String oneBzByProOrganId( int proOrganId ){
		
		ProOrgan proOrgan = proOrganDao.findById( proOrganId );
		if( proOrgan != null ){
			return oneProjectToChatGroup( proOrgan );
		}
		return "班组记录不存在";
	}
	
	
	
	/**
	 *  处理一个班组的数据  
	 * */
	public String oneProjectToChatGroup( ProOrgan proOrgan  ){
		
		Project  project  = getProjectInfoByProjectId(proOrgan.getProId());
		//获取班主信息
		OpenImUser openImUser = getImUserInfo( proOrgan.getPoFzr() );

		String notice =  project.getPContent();
		if( notice == null || notice.length() == 0 ){
			notice = project.getPName()+"项目简介:暂无";
		}
		
		if( notice.length() > 500 ){
			notice = notice.substring(0, 500);
		}
		
		OpenimTribeCreateRequest request = projectInfo_tranform_groupReq( project.getPName()+"-"+proOrgan.getPoName(), notice, openImUser );
		return createChatGroup( request, proOrgan.getId(),proOrgan.getPoFzr() );
	}
	
	
	
	/**
	 *   创建聊天群
	 * */
	public String createChatGroup( OpenimTribeCreateRequest req, int groupId, int masterId ){
			
//		OpenImUser obj1 = new OpenImUser();
		String resultString = null;
		OpenimTribeCreateResponse rsp;
		try {

 			rsp = TaoBaoClientUtil.getClient().execute(req);
			Gson gson = new Gson();
			Map map = gson.fromJson(rsp.getBody(), Map.class);
			if( map != null ){
				Map temp = (Map) map.get("openim_tribe_create_response");
				if( temp != null ){
					
					Map info = (Map)temp.get("tribe_info");
					Object check_mode = info.get("check_mode");
					String name    =    (String)info.get("name");   //群名字
					String notice  =    (String)info.get("notice"); //群简介
					double recv_flag = 	(double)info.get("recv_flag");
					double tribe_id  = 	(double)info.get("tribe_id");
					double tribe_type = (double)info.get("tribe_type");
					
					int iRecv_flag = (int)recv_flag;
					long iRribe_id  = toTribeId(tribe_id);
					int iTribe_type = (int)tribe_type;
					
					ChatGroup chatGroup = new ChatGroup();
					chatGroup.setName(name);
					chatGroup.setNotice(notice);
					chatGroup.setRecvFlag(iRecv_flag);
					chatGroup.setTribeType(iTribe_type);
					chatGroup.setId(iRribe_id);
					chatGroup.setProOrganId(groupId);
					chatGroup.setMasterId(""+masterId);
					
					chatGroupDao.save(chatGroup);
					return "zemeng-"+iRribe_id;
				}
			}else {
				
				resultString = rsp.getMsg();
			}
			
		} catch (ApiException e) {
				
			resultString = "异常,createChatGroup抛出异常退出";
			e.printStackTrace();
		}
		
		return resultString;
	}
	
	
	

	
	
	
	/******************************************************************************************************************/
	/**
	 * 2. 为所有的群 邀请班组中的工人
	 * */
	public void groupMasterInviteAllImUser(){
		
		//1.获取所有的群id，以及群主id，以及班组id
		List groupList = chatGroupDao.findAll();
		
		for( int i=0; i < groupList.size(); i++ ){
			
			ChatGroup chatGroup = (ChatGroup) groupList.get(i);
			oneBzUserListJoinGroup(chatGroup);
		}
		//2.根据班组id，获取班组中的所有用户
		//3.邀请所有的用户
	}
	
	
	/**
	 * 2.1 通过群主id找到群记录，创建chatGroup
	 * */
	public String oneBzUserListJoinGroupByGroupId( long groupId ){
		
		ChatGroup chatGroup = chatGroupDao.findById( groupId );
		return oneBzUserListJoinGroup(chatGroup);
	}
	
	
	/**
	 *  2.2 一个班组的所有用户加入群
	 * */
	public String oneBzUserListJoinGroup( ChatGroup chatGroup ){
		
		StringBuffer sb = new StringBuffer();
		String  groupMasterId = chatGroup.getMasterId(); //群主id
		
		long groupId = chatGroup.getId();     //群id
		int  proOrganId  = chatGroup.getProOrganId();  //班组id
		List proOrganPerList = proOrganPerDao.findByPoId(proOrganId);//通过班组id，拿到所有的
		for( int j=0; j<proOrganPerList.size(); j++ ){
			
			ProOrganPer perInfo = (ProOrganPer) proOrganPerList.get(j);
			String resultStr = imUserJoinGroup( groupId , getImUserInfo(perInfo.getUserId()));
			if( resultStr != null ){
				sb.append(resultStr);
				sb.append( " & " );
			}
		}
		return sb.toString();
	}
	
	
	/**
	 *  2.2 用户主动加群
	 * */
	public String imUserJoinGroup( Long tribeId, OpenImUser imUser){ 
		
		String resultStr = null;
		
		Gson gson = new Gson();
		OpenimTribeJoinRequest req = new OpenimTribeJoinRequest();
		req.setUser( gson.toJson(imUser)  );
		req.setTribeId(tribeId);
		OpenimTribeJoinResponse rsp;
		try {
			
			rsp = TaoBaoClientUtil.getClient().execute(req);
			Map map = gson.fromJson( rsp.getBody(), Map.class);
			Map responseData = (Map) map.get("openim_tribe_join_response");
			if( responseData ==null  ){
				
				Map errResponse = (Map) map.get("error_response");
				if( errResponse != null ){
					
					resultStr  = (String) errResponse.get("sub_code");
					
				}else {
					
					resultStr = tribeId +"-"+imUser.getUid()+"加入群组失败.";
				}
			}

			
		} catch (ApiException e) {
			
			resultStr = tribeId +"-"+imUser.getUid()+"加入群组失败. imUserJoinGroup函数抛出异常";
			e.printStackTrace();
		}
		return resultStr;
	}
	
	
	
	/****************************获取某个用户的群信息 以及解散 **************************************************************************************/
	/**
	 *  3.先获取所有群主，然后获取群主的id
	 * */
	
	@Test
	public  void getAllMasterList(){
		
		//1.获取所有的群id，以及群主id，以及班组id
		List groupList = chatGroupDao.findAll();
		for( int i=0; i<groupList.size(); i++ ){
			
			ChatGroup chatGroup = (ChatGroup) groupList.get(i);
			getGroupListByUserId( chatGroup.getMasterId() );
		}
	}
	
	
	/**
	 * 3.1 获取某个人所在的群组
	 * */
	public void getGroupListByUserId( String userId ){
		
		Gson gson = new Gson();

		OpenimTribeGetalltribesRequest req = new OpenimTribeGetalltribesRequest();
		OpenImUser imUer = getImUserInfo(userId);
		req.setUser( gson.toJson(imUer) );
		req.setTribeTypes("0"); //设置群类型
		OpenimTribeGetalltribesResponse rsp;
		
		List tribeInfoList = null;
		try {
			
			rsp = TaoBaoClientUtil.getClient().execute(req);
			Map responseData = gson.fromJson(rsp.getBody(), Map.class );
			if( responseData != null ){
				Map openim_tribe_getalltribes_response = (Map) responseData.get("openim_tribe_getalltribes_response");
				if( openim_tribe_getalltribes_response != null ){
					Map tribe_info_list = (Map) openim_tribe_getalltribes_response.get("tribe_info_list");
					if( tribe_info_list != null ){
						tribeInfoList = (List) tribe_info_list.get("tribe_info");
					}
				}
			}
			
			if( tribeInfoList != null ){
				for( int i=0; i<tribeInfoList.size(); i++ ){
					
					Map tribe_info = (Map) tribeInfoList.get(i);
					removeGroupByGroupId( (double)(tribe_info.get("tribe_id")), userId );
				}
			}
			System.out.println(rsp.getBody());
			
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 3.2.解散群
	 * */
	public void removeGroupByGroupId( double dGroupId , String masterId ){
		
		/**
		 *  判断这个群是否在chat_group表中，如果在，那就不删除。
		 * 	
		 * */
		long groudId =  toTribeId(dGroupId);
		ChatGroup chatGroup =  chatGroupDao.findById(groudId);
		if( chatGroup != null ){
			return;
		}
		
		Gson gson = new Gson();
		OpenimTribeDismissRequest req = new OpenimTribeDismissRequest();
		OpenImUser master = getImUserInfo(masterId);

		req.setUser(gson.toJson(master));
		req.setTribeId( groudId );
		OpenimTribeDismissResponse rsp;
		try {
			
			rsp = TaoBaoClientUtil.getClient().execute(req);
			System.out.println(rsp.getBody());
			
			
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	/****************************获取某个用户的群信息**************************************************************************************/
	
	/**
	 *  1.2  获取所有的项目名称
	 * */
	
//	public void projectToChatGroup1(){
//		
//		//1.先获取所有的班组
//		List<ProOrgan> proOrganList = proOrganDao.findAll();
//		for( int i=0; i<proOrganList.size(); i++ ){
//			
//			//2.获取班组的projectId
//			ProOrgan proOrgan = (ProOrgan)proOrganList.get( i );
////			Project  project  = getProjectInfoByProjectId(proOrgan.getProId());
//			//获取班主信息
//			OpenImUser openImUser = getImUserInfo( proOrgan.getPoFzr() );
//			getChatGroupInfo(  openImUser, proOrgan.getId() );
//		}
//	}
//	
//	
//	
//	/**
//	 *  2.获取群信息   根据群主信息以及群id获取群的信息
//	 * */
//	public void getChatGroupInfo( OpenImUser obj,  int cricleId  ){
//		
//		OpenimTribeGettribeinfoRequest req = new OpenimTribeGettribeinfoRequest();
//		Gson gson = new Gson();
//		req.setUser( gson.toJson(obj) );
//		OpenimTribeGettribeinfoResponse rsp = null;
//		try {
//			
//			rsp = TaoBaoClientUtil.getClient().execute(req);
//			
//		} catch (ApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
	
	

	/**
	 *  将班组信息转化为群信息
	 * */
	public OpenimTribeCreateRequest projectInfo_tranform_groupReq(  String bzName, String content, OpenImUser groupHeader  ){
		
		OpenimTribeCreateRequest req = new OpenimTribeCreateRequest();
		req.setTribeName( bzName ); //项目名字，群名
		req.setNotice( content ); //群简介，群公告
		req.setTribeType( 0L );  //为0，表示是普通； 为1，表示是讨论组		
		Gson gson = new Gson();
		System.out.println(gson.toJson( groupHeader ));
		req.setUser( gson.toJson( groupHeader ) );
		
		return req;
	}
	
	
	/**
	 *  根据班组ID，获取项目的名字，项目的简介
	 */
	public Project getProjectInfoByProjectId( int proId ){
		
		Project project = projectDao.findById( proId ); //获取项目内容
		return project;
	}
	
	
	
	/**
	 *  拼组成OpenImUser
	 * */
	public OpenImUser getImUserInfo( int userInfoId ){
		return getImUserInfo(""+userInfoId);
	}
	
	public OpenImUser getImUserInfo( String userInfoId ){
		
		OpenImUser user = new OpenImUser();
		user.setUid(userInfoId);
		user.setTaobao_account(false);
		
		return user;
	}
	
	
	
	
	/**
	 * 处理Gson中的 Int被转化为 double的问题
	 * */
	public Long toTribeId( double dTribeId ){
		
		String tempValue = ""+ dTribeId;
		long value = 0;
		for( int i=0; i<tempValue.length(); i++ ){
			char ch = tempValue.charAt(i);
			
			if( Character.isDigit(ch) ){
				value = value*10+ch-48;
			}else if( ch == '.' ) {
				continue;
			}else {
				
				while( value < 1000000000 ){
					value *= 10;
				}
				break;
			}
		}
		return value;
	}
	
}



/**
 * 群用户信息对象
 * */
class OpenImUser {
	
	private String uid; //用户id
	private boolean taobao_account;//是否为淘宝账号
	private String  app_key = TaoBaoClientUtil.getAppKey();//appkey
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getApp_key() {
		return app_key;
	}
	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}
	public boolean isTaobao_account() {
		return taobao_account;
	}
	public void setTaobao_account(boolean taobao_account) {
		this.taobao_account = taobao_account;
	}

}

 