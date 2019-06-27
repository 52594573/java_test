package com.ktp.project.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: wuyeming
 * @Date: 2018-12-25 下午 17:48
 * 用户积分
 */
@Entity
@Table(name = "user_jf")
public class UserJf {
   private Integer id;
   private Integer userId;
   private Integer jfType;
   private String jfShu;
   private String jfYue;
   private Integer jfState;
   private Date inTime;

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   @Column(name = "user_id")
   public Integer getUserId() {
      return userId;
   }

   public void setUserId(Integer userId) {
      this.userId = userId;
   }

   @Column(name = "jf_type")
   public Integer getJfType() {
      return jfType;
   }

   public void setJfType(Integer jfType) {
      this.jfType = jfType;
   }

   @Column(name = "jf_shu")
   public String getJfShu() {
      return jfShu;
   }

   public void setJfShu(String jfShu) {
      this.jfShu = jfShu;
   }

   @Column(name = "jf_yue")
   public String getJfYue() {
      return jfYue;
   }

   public void setJfYue(String jfYue) {
      this.jfYue = jfYue;
   }

   @Column(name = "jf_state")
   public Integer getJfState() {
      return jfState;
   }

   public void setJfState(Integer jfState) {
      this.jfState = jfState;
   }

   @Column(name = "in_time")
   public Date getInTime() {
      return inTime;
   }

   public void setInTime(Date inTime) {
      this.inTime = inTime;
   }
}
