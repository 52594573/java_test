package com.zm.entity;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.Date;

/**
 * UserInfo entity. @author MyEclipse Persistence Tools
 */

/**
 *   获取用户信息
 * */
@Entity
public class UserInfo implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer user_id;
	private String u_name;
	private String u_pic;
	private String u_sfz;
	private String u_realname;
	private String u_introduce;
	private Integer u_type;
	private String  u_typename;
	private Integer u_state;
	private Date u_birthday;
	private Integer u_cert;
	private Integer u_cert_type;
	private String u_cert_pic;
	private Integer u_sex;
	private Integer u_proid;
	private Integer u_up_proid;
	private Double u_xyf;
	private Double u_jnf;
	private String u_sfzpic;
	private String u_yhkpic;
	private Double u_lbs_x;
	private Double u_lbs_y;
	private String u_lbs;
	private Double u_star;
	private Integer u_isfujin;
	private String u_nicheng;
	private Double u_similarity;
//	2018-11-12 wuyeming
	private String last_device;//最后登录的设备 A:Android I:IOS
	private Timestamp u_intime;//初始化时间
	private Timestamp u_lastime;//最后登录时间
	private Timestamp logout_time;//退出登录时间

	private String unique_id;//用户唯一标识

	public Date getU_authentication() {
		return u_authentication;
	}

	public void setU_authentication(Date u_authentication) {
		this.u_authentication = u_authentication;
	}

	private Date u_authentication;//实名认证时间

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
		this.user_id = id;
	}
	public String getU_name() {
		return u_name;
	}
	public void setU_name(String u_name) {
		this.u_name = u_name;
	}
	public String getU_pic() {
		return u_pic;
	}
	public void setU_pic(String u_pic) {
		this.u_pic = u_pic;
	}

	public String getU_sfz() {
		return u_sfz;
	}

	public void setU_sfz(String u_sfz) {
		this.u_sfz = u_sfz;
	}
	public String getU_realname() {
		return u_realname;
	}
	public void setU_realname(String u_realname) {
		this.u_realname = u_realname;
	}

	public String getU_introduce() {
		return u_introduce;
	}

	public void setU_introduce(String u_introduce) {
		this.u_introduce = u_introduce;
	}

	public Integer getU_type() {
		return u_type;
	}
	public void setU_type(Integer u_type) {
		this.u_type = u_type;
	}
	public Integer getU_state() {
		return u_state;
	}
	public void setU_state(Integer u_state) {
		this.u_state = u_state;
	}
	public Date getU_birthday() {
		return u_birthday;
	}
	public void setU_birthday(Date u_birthday) {
		this.u_birthday = u_birthday;
	}
	public Integer getU_cert() {
		return u_cert;
	}
	public void setU_cert(Integer u_cert) {
		this.u_cert = u_cert;
	}

	public Integer getU_cert_type() {
		return u_cert_type;
	}

	public void setU_cert_type(Integer u_cert_type) {
		this.u_cert_type = u_cert_type;
	}

	public String getU_cert_pic() {
		return u_cert_pic;
	}

	public void setU_cert_pic(String u_cert_pic) {
		this.u_cert_pic = u_cert_pic;
	}

	public Integer getU_sex() {
		return u_sex;
	}
	public void setU_sex(Integer u_sex) {
		this.u_sex = u_sex;
	}
	public Integer getU_proid() {
		return u_proid;
	}
	public void setU_proid(Integer u_proid) {
		this.u_proid = u_proid;
	}
	public Integer getU_up_proid() {
		return u_up_proid;
	}
	public void setU_up_proid(Integer u_up_proid) {
		this.u_up_proid = u_up_proid;
	}
	public Double getU_xyf() {
		return u_xyf;
	}
	public void setU_xyf(Double u_xyf) {
		this.u_xyf = u_xyf;
	}
	public Double getU_jnf() {
		return u_jnf;
	}
	public void setU_jnf(Double u_jnf) {
		this.u_jnf = u_jnf;
	}
	public String getU_sfzpic() {
		return u_sfzpic;
	}
	public void setU_sfzpic(String u_sfzpic) {
		this.u_sfzpic = u_sfzpic;
	}
	public String getU_yhkpic() {
		return u_yhkpic;
	}
	public void setU_yhkpic(String u_yhkpic) {
		this.u_yhkpic = u_yhkpic;
	}
	public Double getU_lbs_x() {
		return u_lbs_x;
	}
	public void setU_lbs_x(Double u_lbs_x) {
		this.u_lbs_x = u_lbs_x;
	}
	public Double getU_lbs_y() {
		return u_lbs_y;
	}
	public void setU_lbs_y(Double u_lbs_y) {
		this.u_lbs_y = u_lbs_y;
	}
	public String getU_lbs() {
		return u_lbs;
	}
	public void setU_lbs(String u_lbs) {
		this.u_lbs = u_lbs;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getU_typename() {
		return u_typename;
	}
	public void setU_typename(String u_typename) {
		this.u_typename = u_typename;
	}

	public Double getU_star() {
		return u_star;
	}

	public void setU_star(Double u_star) {
		this.u_star = u_star;
	}

	public Integer getU_isfujin() {
		return u_isfujin;
	}

	public void setU_isfujin(Integer u_isfujin) {
		this.u_isfujin = u_isfujin;
	}

	public String getU_nicheng() {
		return u_nicheng;
	}

	public void setU_nicheng(String u_nicheng) {
		this.u_nicheng = u_nicheng;
	}

	public Double getU_similarity() {
		return u_similarity;
	}

	public void setU_similarity(Double u_similarity) {
		this.u_similarity = u_similarity;
	}

	public String getLast_device() {
		return last_device;
	}

	public void setLast_device(String last_device) {
		this.last_device = last_device;
	}

	public Timestamp getU_intime() {
		return u_intime;
	}

	public void setU_intime(Timestamp u_intime) {
		this.u_intime = u_intime;
	}

	public Timestamp getU_lastime() {
		return u_lastime;
	}

	public void setU_lastime(Timestamp u_lastime) {
		this.u_lastime = u_lastime;
	}

	public Timestamp getLogout_time() {
		return logout_time;
	}

	public void setLogout_time(Timestamp logout_time) {
		this.logout_time = logout_time;
	}

	public String getUnique_id() {
		return unique_id;
	}

	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}

	// Constructors



	
}