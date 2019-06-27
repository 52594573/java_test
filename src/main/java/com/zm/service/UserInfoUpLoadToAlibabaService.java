package com.zm.service;

import com.google.gson.Gson;
import com.taobao.api.ApiException;
import com.taobao.api.request.OpenimUsersAddRequest;
import com.taobao.api.request.OpenimUsersGetRequest;
import com.taobao.api.request.OpenimUsersUpdateRequest;
import com.taobao.api.response.OpenimUsersAddResponse;
import com.taobao.api.response.OpenimUsersGetResponse;
import com.taobao.api.response.OpenimUsersUpdateResponse;
import com.zm.entity.UserInfo;
import com.zm.entity.UserInfoDAO;
import com.zm.utils.TaoBaoClientUtil;
import org.apache.http.util.TextUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class UserInfoUpLoadToAlibabaService {
	
	@Autowired
	private UserInfoDAO userInfoDao;
	
	private int    page   = 0;
	private int    pageCount = 20;
	private List<Userinfos>  userInfosList = new ArrayList<Userinfos>();
	private static String ktpPicUrl = "https://t.ktpis.com";
	
	
	@Test
	public void test(){
		
		ApplicationContext context = new FileSystemXmlApplicationContext("/src/main/resources/spring/spring-dao.xml" );
		userInfoDao = (UserInfoDAO)context.getBean("userInfoDao");

		//updateUserListToAlibabaServer();

		uploadAllUserToAlibabaServer();
	}
	
	/**  
	 *  获取所有的用户列表信息
	 **/
	public void getUserListFromAlibabaServer( String userIds ){
		
		OpenimUsersGetRequest req = new OpenimUsersGetRequest();
		req.setUserids( userIds );
		OpenimUsersGetResponse rsp;
		try {
			rsp = TaoBaoClientUtil.getClient().execute(req);
			System.out.println(rsp.getBody());
			
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 *  更新用户信息 
	 * */
	public  void updateUserListToAlibabaServer(){
		
		//设置每次从数据库中读取数据的分页值
		int page = 0;
		int pageNumber = 100;

		while( true ){
			
			List<UserInfo> userInfoList = userInfoDao.findByPage(page, pageNumber);	
			if( userInfoList.size() <= 0 ){
				break;
			}
			updateUsersToAlibabaServer(userInfoList);
			page++; //页数就行循环
		}
	}
	
	
	/**
	 * 更新IM中某一个人的信息
	 * */
	public String updateOneUserToAlibabaServer( int userId ){
		
		UserInfo userInfo = userInfoDao.findById(userId);
		if( userInfo == null ){
			return "用户不存在";
		}
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		userInfoList.add(userInfo);
		return updateUsersToAlibabaServer(userInfoList);
	}

	public String updateOneUserToAlibabaServer(int userId, String userName, String userPic, String mobile) {

		UserInfo userInfo = userInfoDao.findById(userId);
		if (userInfo == null) {
			return "用户不存在";
		}
		if (!TextUtils.isEmpty(userName)) {
			userInfo.setU_realname(userName);
		}
		if (!TextUtils.isEmpty(mobile)) {
			userInfo.setU_name(mobile);
		}
		if (!TextUtils.isEmpty(userPic)) {
			userInfo.setU_pic(userPic);
		}
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		userInfoList.add(userInfo);
		return updateUsersToAlibabaServer(userInfoList);
	}
	
	
	public String updateUsersToAlibabaServer( List<UserInfo> userInfoList ){
		
		OpenimUsersUpdateRequest req = new OpenimUsersUpdateRequest();
		List<Userinfos> alibabaInfoList = fromKtpUserInfoList_To_alibabaUserInfoList(userInfoList);
		Gson gson = new Gson();
		return updateUserToAlibabaServer(req, gson.toJson(alibabaInfoList));
	}
	
	
	/**   
	 *  更新指定用户组的信息
	 * */
	public String updateUserToAlibabaServer( OpenimUsersUpdateRequest req,String userInfos ){

		String resultStr = null;
		
		req.setUserinfos( userInfos );
		OpenimUsersUpdateResponse rsp;
		try {
			
			rsp = TaoBaoClientUtil.getClient().execute(req);
			System.out.println(rsp.getBody());
			
			
		} catch (ApiException e) {
		
			e.printStackTrace();
			resultStr = "IM用户信息更新失败";
			
		}
		return resultStr;
	}
	

	
/***************************************************************************************************/
	/** 
	 * 上传所有用户信息到IM 
	 * 
	 * */
	public void  uploadAllUserToAlibabaServer(){
		
		int page = 0;
		int pageNumber = 100;
		
		while( true ){
			
			List<UserInfo> userInfoList = userInfoDao.findByPage(page, pageNumber);	
			Gson gson = new Gson();
			if( userInfoList.size() <= 0 ){
				break;
			}
			List<Userinfos> alibabaInfoList = fromKtpUserInfoList_To_alibabaUserInfoList(userInfoList);
			//获取所有的用户
			uploadUserToAlibabaServer( gson.toJson(alibabaInfoList));
			page++; //页数就行循环
		}
	}

	
	
	/**
	 *  上传一个用户组到ImServer 
	 *    注意:如果只有一个用户，也需要用List的形式进行存储, 且转为json。
	 * */
	public String uploadUserToAlibabaServer( String userInfos ){
		
		String resultString = null;
		Gson gson = new Gson();
		
		OpenimUsersAddRequest req = new OpenimUsersAddRequest();
		req.setUserinfos(  userInfos   );
		OpenimUsersAddResponse rsp;
		try {
			
			rsp = TaoBaoClientUtil.getClient().execute(req);
			Map responseData =  gson.fromJson( rsp.getBody(), Map.class );
			if( responseData != null ){
				Map openim_users_add_response = (Map) responseData.get("openim_users_add_response");
				if( openim_users_add_response != null ){
					Map uid_succ =   (Map) openim_users_add_response.get("uid_succ");
					if( uid_succ == null ){
						Map uid_msg = (Map) openim_users_add_response.get("fail_msg");
						if( uid_msg != null ){
							resultString = uid_msg.toString();
						}
					}
				}
			}
			System.out.println("-------------- zemeng: "+rsp.getBody()+"--------------------");
			
		} catch (ApiException e) {
	
			e.printStackTrace();
		}
		return resultString;
	}
	
	
	/**
	 *  补充:
	 *  传入用户ID，则去获取用户数据，并且自动上传;
	 * */
	public String uploadUserWithUserId( int userId ){
		
		UserInfo userInfo = userInfoDao.findById( userId );
		if( userInfo == null ){ return "userId不存在"; }
		Gson gson = new Gson();
		Userinfos imUserInfo = fromKtpUserInfo_To_alibabaUserInfo(userInfo);
		
		List<Userinfos> list = new ArrayList<Userinfos>();
		list.add(imUserInfo);
		//获取所有的用户
		return uploadUserToAlibabaServer( gson.toJson(list));
	}
	
	
	
	
	
//	1.获取用户数据   分页获取，一次10条
	public List getUserList(  int page , int pageNumber ){
		
		List list = userInfoDao.findByPage( page, pageNumber );

		return list;
	}
	
	
	
	public List<Userinfos> fromKtpUserInfoList_To_alibabaUserInfoList( List<UserInfo> userInfoList ){
		
		List<Userinfos> alibabaUserList = new ArrayList<Userinfos>();
		for( UserInfo temp : userInfoList  ){
			
			setUserInfos(alibabaUserList , temp);
		}
		return alibabaUserList;
	}

	/**
	 *  将 ‘开太平用户信息’ 转化为 ‘阿里粑粑用户信息’，并且添加到数组中
	 * */
	public void setUserInfos(  List<Userinfos> list, UserInfo userInfo ){
		
		Userinfos obj3 = fromKtpUserInfo_To_alibabaUserInfo(userInfo);
		list.add(obj3);
	}
	
	
	/**
	 *   将用户信息结构转换为 阿里云聊天的用户结构
	 * */
	public Userinfos fromKtpUserInfo_To_alibabaUserInfo( UserInfo userInfo ){
		
		/*
		 * 处理用户头像问题
		 * 1. 拼接 
		 * 
		*/
		String picUrl = ktpPicUrl + userInfo.getU_pic();
		
		/**
		 *  处理性别问题
		 * 
		 * */
		int sex = userInfo.getU_sex();
		String gender = "M";
		if( sex == 2 ){
			gender = "F";
		}
		
		
		/**
		 * 	处理年龄问题
		 * */
		int age = 0;
		Date birthDate = userInfo.getU_birthday();
		if( birthDate != null ){
			
			DateFormat format=new SimpleDateFormat("yyyy");  
			Date nowDate   = new Date();
			format.format(birthDate);
			
			try {
				int birthYear = Integer.valueOf(format.format(birthDate));
				int nowYear   = Integer.valueOf(format.format(nowDate));
				age = nowYear - birthYear;
			}catch( Exception e ){
				age = 0;
			}	
		}

		Userinfos alibabaUserInfo = new Userinfos();
		alibabaUserInfo.setUserid( ""+userInfo.getId() ); //用户ID
		alibabaUserInfo.setNick( userInfo.getU_realname() ); //昵称
		alibabaUserInfo.setIcon_url(  picUrl ); //用户头像
		//alibabaUserInfo.setEmail("uid@taobao.com"); //邮箱
		alibabaUserInfo.setMobile( userInfo.getU_name() ); //手机号码
		alibabaUserInfo.setPassword( "123456" );//密码
//		alibabaUserInfo.setRemark("demo"); 
		alibabaUserInfo.setExtra("{}");
//		alibabaUserInfo.setCareer("demo");
		alibabaUserInfo.setVip("{}"); //是否是vip
//		alibabaUserInfo.setAddress("demo");
		alibabaUserInfo.setName( userInfo.getU_realname() ); //姓名
		
		alibabaUserInfo.setAge(  new Long(age) ); //年龄
		alibabaUserInfo.setGender( gender ); //性别
		
		return alibabaUserInfo;
	}
	
	
}



class Userinfos {
	
	private String nick;
	private String icon_url;
	private String email;
	private String mobile;
	private String taobaoid;
	private String userid;
	private String password;
	private String remark;
	private String extra;
	private String career;
	private String vip;
	private String address;
	private String name;
	private Long age;
	private String gender;
	private String wechat;
	private String qq;
	private String weibo;
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIcon_url() {
		return icon_url;
	}
	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTaobaoid() {
		return taobaoid;
	}
	public void setTaobaoid(String taobaoid) {
		this.taobaoid = taobaoid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public String getCareer() {
		return career;
	}
	public void setCareer(String career) {
		this.career = career;
	}
	public String getVip() {
		return vip;
	}
	public void setVip(String vip) {
		this.vip = vip;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getAge() {
		return age;
	}
	public void setAge(Long age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWeibo() {
		return weibo;
	}
	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}
	
}
