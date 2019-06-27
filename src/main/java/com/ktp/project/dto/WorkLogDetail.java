package com.ktp.project.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class WorkLogDetail {


    /**
     * id : 1645
     * pro_id : 23
     * send_uid : 2055      //发送人
     * wl_type : 1        //工作日志
     * wl_content : ????????????  //工作日志
     * wl_star : 4    //工作日志
     * wl_yzcd : 0  //工作日志
     * wl_safe_type  //工作日志
     * in_time : 2018/10/18 15:10:16  //工作日志时间
     * wl_state : 1  //工作日志
     * wl_lbs_x : 0     //工作日志
     * wl_lbs_y : 0     //工作日志
     * wl_lbs_name :    //工作日志
     * <p>====================wl_pw_info:{} 节点信息
     * pw_id : 1012         //节点
     * pw_content :        //节点描述
     * pw_name : 地下车库   //节点
     * pw_pid : 1007        //节点
     * p_pw_name : 2#楼      //节点
     * <p>====================send_info:{}  发送人信息
     * u_name : 18819376030 //发送人
     * u_realname : 付万年         //发送人
     * u_pic : /images/pic/2018912105054518617027.jpg       //发送人
     * u_cert : 2       //发送人
     * u_cert_type : 2      //发送人
     * p_type : 0           //发送人角色id
     * zhiwu :          //发送人职务  角色
     * po_id : 312          //发送人部门
     * po_name : 管理层        //发送人部门
     * <p>====================works_list[{},{}]  被评价人列表信息
     * gr_list : [{
     * "gr_user_id":"44943",       //用户id
     * "gr_u_name":"18888188188",  //用户un
     * "gr_u_pic":"",              //用户图片
     * "gr_u_realname":"张三",       //用户真名
     * "gr_po_id":"1004",          //班组id
     * "gr_po_name":"123123",      //班组名称
     * "gr_pro_id":"23",           //用户项目id
     * "gr_p_name":"测试项目1"        //项目名称
     * gr_gongzhong 工种
     * gr_banzuzhang 班组长
     * }]
     * <p>====================pic_list  ["",""]  工作日志图片列表信息
     * pic_list : []    //工作日志 图片列表
     */



    private Integer id;
    private Integer pro_id;
    private String pro_name;
    private Integer send_uid;
    private String u_name;
    private String u_realname;
    private String u_pic;
    private Integer u_cert;
    private Integer u_cert_type;
    private Integer p_type;
    private Integer po_id;
    private String po_name;
    private Integer pw_id;
    private String pw_name;
    private Integer pw_pid;
    private String p_pw_name;
    private String pw_content;
    private Integer wl_type;
    private String zhiwu;
    private String wl_content;
    private Integer wl_star;
    private Integer wl_state;
    private Integer wl_yzcd;
    private BigDecimal wl_lbs_x;
    private BigDecimal wl_lbs_y;
    private String wl_lbs_name;
    private Integer wl_safe_type;
    private Date in_time;
    private List<GrListBean> gr_list;
    private List<String> pic_list;


    public Integer getPo_id() {
        return po_id;
    }

    public void setPo_id(Integer po_id) {
        this.po_id = po_id;
    }

    public Integer getPw_id() {
        return pw_id;
    }

    public void setPw_id(Integer pw_id) {
        this.pw_id = pw_id;
    }

    public Integer getPw_pid() {
        return pw_pid;
    }

    public void setPw_pid(Integer pw_pid) {
        this.pw_pid = pw_pid;
    }

    public Integer getWl_safe_type() {
        return wl_safe_type;
    }

    public void setWl_safe_type(Integer wl_safe_type) {
        this.wl_safe_type = wl_safe_type;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPro_id() {
        return pro_id;
    }

    public void setPro_id(Integer pro_id) {
        this.pro_id = pro_id;
    }

    public Integer getSend_uid() {
        return send_uid;
    }

    public void setSend_uid(Integer send_uid) {
        this.send_uid = send_uid;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_realname() {
        return u_realname;
    }

    public void setU_realname(String u_realname) {
        this.u_realname = u_realname;
    }

    public String getU_pic() {
        return u_pic;
    }

    public void setU_pic(String u_pic) {
        this.u_pic = u_pic;
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

    public Integer getP_type() {
        return p_type;
    }

    public void setP_type(Integer p_type) {
        this.p_type = p_type;
    }


    public String getPo_name() {
        return po_name;
    }

    public void setPo_name(String po_name) {
        this.po_name = po_name;
    }


    public String getPw_name() {
        return pw_name;
    }

    public void setPw_name(String pw_name) {
        this.pw_name = pw_name;
    }


    public String getP_pw_name() {
        return p_pw_name;
    }

    public void setP_pw_name(String p_pw_name) {
        this.p_pw_name = p_pw_name;
    }

    public String getPw_content() {
        return pw_content;
    }

    public void setPw_content(String pw_content) {
        this.pw_content = pw_content;
    }

    public Integer getWl_type() {
        return wl_type;
    }

    public void setWl_type(Integer wl_type) {
        this.wl_type = wl_type;
    }

    public String getZhiwu() {
        return zhiwu;
    }

    public void setZhiwu(String zhiwu) {
        this.zhiwu = zhiwu;
    }

    public String getWl_content() {
        return wl_content;
    }

    public void setWl_content(String wl_content) {
        this.wl_content = wl_content;
    }

    public Integer getWl_star() {
        return wl_star;
    }

    public void setWl_star(Integer wl_star) {
        this.wl_star = wl_star;
    }

    public Integer getWl_state() {
        return wl_state;
    }

    public void setWl_state(Integer wl_state) {
        this.wl_state = wl_state;
    }

    public Integer getWl_yzcd() {
        return wl_yzcd;
    }

    public void setWl_yzcd(Integer wl_yzcd) {
        this.wl_yzcd = wl_yzcd;
    }

    public BigDecimal getWl_lbs_x() {
        return wl_lbs_x;
    }

    public void setWl_lbs_x(BigDecimal wl_lbs_x) {
        this.wl_lbs_x = wl_lbs_x;
    }

    public BigDecimal getWl_lbs_y() {
        return wl_lbs_y;
    }

    public void setWl_lbs_y(BigDecimal wl_lbs_y) {
        this.wl_lbs_y = wl_lbs_y;
    }

    public String getWl_lbs_name() {
        return wl_lbs_name;
    }

    public void setWl_lbs_name(String wl_lbs_name) {
        this.wl_lbs_name = wl_lbs_name;
    }

    public Date getIn_time() {
        return in_time;
    }

    public void setIn_time(Date in_time) {
        this.in_time = in_time;
    }

    public List<GrListBean> getGr_list() {
        return gr_list;
    }

    public void setGr_list(List<GrListBean> gr_list) {
        this.gr_list = gr_list;
    }

    public List<String> getPic_list() {
        return pic_list;
    }

    public void setPic_list(List<String> pic_list) {
        this.pic_list = pic_list;
    }

    public static class GrListBean {
        /**
         * gr_user_id : 44943
         * gr_u_name : 18888188188
         * gr_u_pic :
         * gr_u_realname : 张三
         * gr_po_id : 1004
         * gr_po_name : 123123
         * gr_pro_id : 23
         * gr_p_name : 测试项目1
         * gr_gongzhong 工种
         * gr_banzuzhang 班组长
         */

        private String gr_user_id;
        private String gr_u_name;
        private String gr_u_pic;
        private String gr_u_realname;
        private String gr_po_id;
        private String gr_po_name;
        private String gr_pro_id;
        private String gr_p_name;
        private String gr_gongzhong;
        private String gr_banzuzhang;

        public String getGr_banzuzhang() {
            return gr_banzuzhang;
        }

        public void setGr_banzuzhang(String gr_banzuzhang) {
            this.gr_banzuzhang = gr_banzuzhang;
        }

        public String getGr_gongzhong() {
            return gr_gongzhong;
        }

        public void setGr_gongzhong(String gr_gongzhong) {
            this.gr_gongzhong = gr_gongzhong;
        }

        public String getGr_user_id() {
            return gr_user_id;
        }

        public void setGr_user_id(String gr_user_id) {
            this.gr_user_id = gr_user_id;
        }

        public String getGr_u_name() {
            return gr_u_name;
        }

        public void setGr_u_name(String gr_u_name) {
            this.gr_u_name = gr_u_name;
        }

        public String getGr_u_pic() {
            return gr_u_pic;
        }

        public void setGr_u_pic(String gr_u_pic) {
            this.gr_u_pic = gr_u_pic;
        }

        public String getGr_u_realname() {
            return gr_u_realname;
        }

        public void setGr_u_realname(String gr_u_realname) {
            this.gr_u_realname = gr_u_realname;
        }

        public String getGr_po_id() {
            return gr_po_id;
        }

        public void setGr_po_id(String gr_po_id) {
            this.gr_po_id = gr_po_id;
        }

        public String getGr_po_name() {
            return gr_po_name;
        }

        public void setGr_po_name(String gr_po_name) {
            this.gr_po_name = gr_po_name;
        }

        public String getGr_pro_id() {
            return gr_pro_id;
        }

        public void setGr_pro_id(String gr_pro_id) {
            this.gr_pro_id = gr_pro_id;
        }

        public String getGr_p_name() {
            return gr_p_name;
        }

        public void setGr_p_name(String gr_p_name) {
            this.gr_p_name = gr_p_name;
        }
    }

}
