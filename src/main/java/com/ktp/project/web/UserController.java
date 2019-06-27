package com.ktp.project.web;

import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dao.UserJfDao;
import com.ktp.project.dto.CertDTO;
import com.ktp.project.dto.UserInfoDTO;
import com.ktp.project.entity.*;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.ChatIgnoreService;
import com.ktp.project.service.FriendService;
import com.ktp.project.service.ShareService;
import com.ktp.project.service.UserService;
import com.ktp.project.util.*;
import com.zm.entity.CcbPush;
import com.zm.entity.KeyContentDAO;
import com.zm.entity.UserInfo;
import org.apache.http.util.TextUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author djcken
 * @date 2018/6/7
 */
@Controller
@RequestMapping(value = "api/user", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private ChatIgnoreService chatIgnoreService;

    @Autowired
    private ShareService shareService;

    @Value("${ktp.team.id}")
    private String ktpTeamId;
    @Value("${ktp.team.name}")
    private String ktpTeamName;

    @Autowired
    private UserJfDao userJfDao;

    @Autowired
    private KeyContentDAO keyContentDAO;


    @RequestMapping(value = "cert", method = RequestMethod.POST)
    @ResponseBody
    /*public String cert(@Param(value = "u_id") int u_id, @Param(value = "userName") String userName, @Param(value = "idCardNo") String idCardNo, @Param(value = "address") String address,
                       @Param(value = "nation") String nation, @Param(value = "org") String org, @Param(value = "startTime") String startTime,
                       @Param(value = "expireTime") String expireTime, HttpServletRequest request)*/
    public String cert(CertDTO certDTO, HttpServletRequest request) {
        String userName = certDTO.getUserName();
        String idCardNo = certDTO.getIdCardNo();
        int u_id = 0;
        if (certDTO.getU_id() != null) {
            u_id = certDTO.getU_id();
        }
        if (certDTO.getUserId() != null) {
            u_id = certDTO.getUserId();
        }
        if (u_id <= 0 || TextUtils.isEmpty(userName) || TextUtils.isEmpty(idCardNo)) {
            throw new BusinessException("参数错误");
        }
        UserInfo userInfo = userService.queryUserInfo(u_id);
        List<UserInfo> user = userService.queryUserInfoBy(idCardNo, u_id);
        if (userInfo == null) {
            throw new BusinessException("该用户不存在");
        }
        if (!TextUtils.isEmpty(userInfo.getU_sfz()) && !userInfo.getU_sfz().equals(idCardNo)) {//原有身份证号码不为空的情况需判断是否相同
            throw new BusinessException("身份证号码不匹配");
        }
        if (user.size() != 0) {
            throw new BusinessException("身份证已被注册");
        }
        Map<String, Object> map = CardUtil.getCarInfo1(idCardNo);
        int sex = 0;
        int age = 0;
        String birthYear = "";
        String birthMonth = "";
        String birthDay = "";
        if (map != null) {
            sex = (int) map.get("sex");
            age = (int) map.get("age");
            String birthday = (String) map.get("birthday");
            birthYear = (String) map.get("year");
            birthMonth = (String) map.get("month");
            birthDay = (String) map.get("day");
            userInfo.setU_sex(sex);//设置性别
            userInfo.setU_birthday(DateUtil.getFormatDate(birthday, DateUtil.FORMAT_DATE));//设置出生日期
        }
        Map<String, String> addressMap = CardUtil.addressResolution(certDTO.getAddress());
        String province = "";
        String city = "";
        String area = "";
        if (addressMap != null) {
            province = addressMap.get("province");
            city = addressMap.get("city");
            area = addressMap.get("county");
        }
        userInfo.setU_realname(userName);//设置真实姓名
        userInfo.setU_nicheng(userName);
        userInfo.setU_sfz(idCardNo);//设置身份证
        userInfo.setU_cert_type(1);//身份证认证
        userInfo.setU_cert(2);//标记为已认证
        userInfo.setU_authentication(new Date());
        String sfzPic = request.getParameter("sfzPic");
        if (!TextUtils.isEmpty(sfzPic)) {
            userInfo.setU_sfzpic(sfzPic);
        }
        String certPic = request.getParameter("certPic");
        if (!TextUtils.isEmpty(certPic)) {
            userInfo.setU_cert_pic(certPic);
        }
        String similarity = request.getParameter("similarity");
        if (!TextUtils.isEmpty(similarity)) {
            userInfo.setU_similarity(Double.parseDouble(similarity));
        }
        UserSfz userSfz = userService.queryUserSfzByUserId(u_id);
        if (userSfz == null) {
            userSfz = new UserSfz();
        }
        userSfz.setUserId(u_id);
        userSfz.setName(userName);
        userSfz.setSex(sex);
        userSfz.setAge(age);
        userSfz.setPic(userInfo.getU_sfzpic());
        userSfz.setNation(certDTO.getNation());
        userSfz.setAddress(certDTO.getAddress());
        userSfz.setProvince(province);
        userSfz.setCity(city);
        userSfz.setArea(area);
        userSfz.setOrg(certDTO.getOrg());
        userSfz.setStartTime(certDTO.getStartTime());
        userSfz.setExpireTime(certDTO.getExpireTime());
        userSfz.setBirthYear(birthYear);
        userSfz.setBirthMonth(birthMonth);
        userSfz.setBirthDay(birthDay);
        boolean success = userService.updateUserInfo(userInfo);
        boolean saveSfz = userService.saveOrUpdateUserSfz(userSfz);

        if (!StringUtil.isEmpty(userInfo.getU_name())) {
            updateShareService(userInfo.getU_name());
        }


        return (success && saveSfz) ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    @Async
    public void updateShareService(String phone) {
        List<ShareCommit> shareCommits = shareService.queryShareCommitbyphone(phone);
        if (shareCommits != null && shareCommits.size() > 0) {
            ShareCommit shareCommit = shareCommits.get(0);
            if (!StringUtil.isEmpty(shareCommit.getCommitRegister() + "") && shareCommit.getCommitRegister() == 1) {
                shareCommit.setCommitCert(1);
                shareService.saveOrUpdateShareCommit(shareCommit);
            }
        }
    }


    /**
     * 新的实名认证
     *
     * @param certDTO
     * @return
     */
    @RequestMapping(value = "newCert", method = RequestMethod.POST)
    @ResponseBody
    public String newCert(CertDTO certDTO) {
        try {
            userService.newCert(certDTO);
            return ResponseUtil.createNormalJson("成功");
        } catch (Exception e) {
            log.error("实名认证异常", e);
            if (e instanceof BusinessException) {
                return ResponseUtil.createApiErrorJson(409, e.getMessage());
            }
            return ResponseUtil.createApiErrorJson(500, e.getMessage());
        }
    }


    /**
     * 修改手机号
     *
     * @param newMobile
     * @param idCardNo
     * @return
     */
    @RequestMapping(value = "modifyMobile", method = RequestMethod.POST)
    @ResponseBody
    public String modifyMobile(@RequestParam("newMobile") String newMobile, @RequestParam("idCardNo") String idCardNo) {
        try {
            userService.modifyMobile(newMobile, idCardNo);
            return ResponseUtil.createNormalJson("成功");
        } catch (Exception e) {
            log.error("修改手机号", e);
            return ResponseUtil.createApiErrorJson(500, e.getMessage());
        }
    }


    /**
     * 人脸采集认证
     *
     * @return
     */
    @RequestMapping(value = "facecert", method = RequestMethod.POST)
    @ResponseBody
    public String sfzpic(@Param(value = "u_id") int u_id, @Param(value = "sfzPic") String sfzPic, HttpServletRequest request) {
        if (u_id <= 0 || TextUtils.isEmpty(sfzPic)) {
            throw new BusinessException("缺少必要参数");
        }
        UserInfo userInfo = userService.queryUserInfo(u_id);
        if (userInfo == null) {
            throw new BusinessException("该用户不存在");
        }
        if (TextUtils.isEmpty(userInfo.getU_sfzpic())) {//如果本身为空就保存
            userInfo.setU_sfzpic(sfzPic);
        }
        userInfo.setU_cert_pic(sfzPic);
        userInfo.setU_cert_type(2);//人脸采集认证
        userInfo.setU_cert(2);//标记为已认证
        userInfo.setU_authentication(new Date());
        boolean success = userService.updateUserInfo(userInfo);

        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    /**
     * 切换身份类型
     *
     * @param u_id 用户id
     * @return
     */
    @RequestMapping(value = "introduce", method = RequestMethod.POST)
    @ResponseBody
    public String saveIntroduce(@Param(value = "u_id") int u_id, @Param(value = "introduce") String introduce) {
        if (u_id <= 0) {
            throw new BusinessException("参数错误");
        }
        UserInfo userInfo = userService.queryUserInfo(u_id);
        if (userInfo == null) {
            throw new BusinessException("该用户不存在");
        }
        userInfo.setU_introduce(introduce);
        boolean success = userService.updateUserInfo(userInfo);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    /**
     * 切换身份类型
     *
     * @param u_id 用户id
     * @param type 切换身份 1表示切换为工人，2表示切换为班组长
     * @return
     */
    @RequestMapping(value = "changeType", method = RequestMethod.POST)
    @ResponseBody
    public String changeUserType(@Param(value = "u_id") int u_id, @Param(value = "type") int type) {
        if (u_id <= 0 || (type != 1 && type != 2)) {
            throw new BusinessException("参数错误");
        }
        int changeType = type == 1 ? 4 : 8;
        if (!userService.checkexist(u_id, changeType)) {
            throw new BusinessException("切换失败，该身份不存在");
        }
        UserInfo userInfo = userService.queryUserInfo(u_id);
        if (userInfo == null) {
            throw new BusinessException("该用户不存在");
        }
        int uType = userInfo.getU_type();
        if (changeType == uType) {
            throw new BusinessException("当前身份无需切换");
        }
        boolean success = userService.updateUserInfoType(u_id, changeType);
        return success ? ResponseUtil.createBussniessJson("切换成功") : ResponseUtil.createBussniessErrorJson(0, "切换失败");
    }

    @RequestMapping(value = "bank/list", method = RequestMethod.GET)
    @ResponseBody
    public String getUserBank(@Param(value = "u_id") int u_id, HttpServletRequest request) {
        if (u_id <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        List<UserBankModel> userBankList = userService.queryUserBank(u_id);
        return ResponseUtil.createNormalJson(userBankList);
    }

    @RequestMapping(value = "bank/save", method = RequestMethod.POST)
    @ResponseBody
    public String saveBank(@Param(value = "u_id") int u_id, @Param(value = "sysBankId") int sysBankId, @Param(value = "bankName") String bankName, @Param(value = "bankNo") String bankNo, @Param(value = "bankPic") String bankPic, HttpServletRequest request) {
        if (u_id <= 0 || sysBankId <= 0 || TextUtils.isEmpty(bankName) || TextUtils.isEmpty(bankNo)) {
            throw new BusinessException("缺少必要参数");
        }
        UserBank userBank = userService.queryUserBank(u_id, bankNo);
        if (userBank == null) {
            userBank = new UserBank();
        }
        userBank.setUserId(u_id);
        userBank.setSysBankId(sysBankId);
        userBank.setBankName(bankName);
        userBank.setBankId(bankNo);
        userBank.setIsDel(0);
        userBank.setInTime(new Date());
        userBank.setBankPic(bankPic);
        String popType = request.getParameter("popType");
        String poId = request.getParameter("poId");
        if (!TextUtils.isEmpty(popType) && !TextUtils.isEmpty(poId)) {//更新为默认银行卡
            userService.updateBankId(u_id, Integer.parseInt(popType), Integer.parseInt(poId), bankNo);
        }
        boolean success = userService.saveOrUpdateUserBank(userBank);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    @RequestMapping(value = "bank/delete", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String deleteBank(@Param(value = "u_id") int u_id, @Param(value = "bankNo") String bankNo, HttpServletRequest request) {
        if (u_id <= 0 || TextUtils.isEmpty(bankNo)) {
            throw new BusinessException("缺少必要参数");
        }
        boolean success = userService.deleteUserBank(u_id, bankNo);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    @RequestMapping(value = "bank/default", method = RequestMethod.GET)
    @ResponseBody
    public String getUserBankPro(@Param(value = "u_id") int u_id, @Param(value = "popType") int popType, @Param(value = "poId") int poId, HttpServletRequest request) {
        if (u_id <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        UserBankModel userBank = userService.queryUserBank(u_id, popType, poId);
        return ResponseUtil.createNormalJson(userBank);
    }

    @RequestMapping(value = "bank/prodefault", method = RequestMethod.POST)
    @ResponseBody
    public String deleteBank(@Param(value = "u_id") int u_id, @Param(value = "bankNo") String bankNo,
                             @Param(value = "popType") int popType, @Param(value = "poId") int poId, HttpServletRequest request) {
        if (u_id <= 0 || TextUtils.isEmpty(bankNo)) {
            throw new BusinessException("缺少必要参数");
        }
        boolean success = userService.updateBankId(u_id, popType, poId, bankNo);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    @RequestMapping(value = "sysbank/list", method = RequestMethod.GET)
    @ResponseBody
    public String getSysBank(HttpServletRequest request) {
        List<SysBank> sysBankList = userService.querySysBank();
        return ResponseUtil.createNormalJson(sysBankList);
    }

    @RequestMapping(value = "bank/detail", method = RequestMethod.GET)
    @ResponseBody
    public String getBankCardDetail(@Param(value = "u_id") int u_id, @Param(value = "bankNo") String bankNo, HttpServletRequest request) {
        if (u_id <= 0 || TextUtils.isEmpty(bankNo)) {
            throw new BusinessException("缺少必要参数");
        }
        UserBankModel userBank = userService.queryUserBankModel(u_id, bankNo);
        return ResponseUtil.createNormalJson(userBank);
    }

    @RequestMapping(value = "active", method = RequestMethod.POST)
    @ResponseBody
    public String active(@Param(value = "userId") int userId, @Param(value = "latitude") double latitude, @Param(value = "longitude") double longitude, HttpServletRequest request) {
        if (userId <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        boolean success = userService.saveOrUpdateUserActive(userId, latitude, longitude);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    /**
     * 1.3  获取附近的人
     */
    @RequestMapping(value = "getNearUserList", method = RequestMethod.GET)
    @ResponseBody
    public String getNearUserList(Integer userId, double latitude, double longitude, int startPage, int length, HttpServletRequest request) {
        userId = userId == null ? 0 : userId;
        String proId = request.getParameter("proId");//项目id--暂未用到
        String ageStart = request.getParameter("ageStart");
        String ageEnd = request.getParameter("ageEnd");
        String sexStr = request.getParameter("sex");//1男 2女 其他为全部
        String areas = request.getParameter("areas");//地区
        String searchKey = request.getParameter("searchKey");//搜索
        String startYear = "";
        String endYear = "";
        int sex = !TextUtils.isEmpty(sexStr) ? StringUtil.parseToInt(sexStr) : -1;
        if (!TextUtils.isEmpty(ageStart) && !TextUtils.isEmpty(ageEnd)) {//计算年龄范围条件
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int start = year - StringUtil.parseToInt(ageEnd);
            int end = year - StringUtil.parseToInt(ageStart);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(Calendar.YEAR, start);
            startCalendar.set(Calendar.MONTH, 0);
            startCalendar.set(Calendar.DAY_OF_MONTH, 1);
            startYear = DateUtil.format(startCalendar.getTime(), "yyyy-MM-dd");
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.set(Calendar.YEAR, end);
            endCalendar.set(Calendar.MONTH, 11);
            endCalendar.set(Calendar.DAY_OF_MONTH, 1);
            endYear = DateUtil.format(endCalendar.getTime(), "yyyy-MM-dd");
        }

        List<UserNear> list = userService.getNearUserList(userId, latitude, longitude, startPage, length, startYear, endYear, sex, areas, searchKey);
        UserNearModel model = new UserNearModel();
        model.setData(list);
        return ResponseUtil.createNormalJson(model);
    }

    @RequestMapping(value = "relation", method = RequestMethod.GET)
    @ResponseBody
    public String relation(@Param(value = "fromUserId") int fromUserId, @Param(value = "toUserId") int toUserId, HttpServletRequest request) {
        if (fromUserId <= 0 || toUserId <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        boolean isFriend = friendService.isFriend(fromUserId, toUserId);
        boolean isWorkFriend = userService.isWorkFriend(fromUserId, toUserId);
        boolean isIgnoreMsg = chatIgnoreService.isIgnoreMsg(toUserId, fromUserId);
        boolean isIgnoreFriendMsg = chatIgnoreService.isIgnoreMsg(fromUserId, toUserId);
        Relation relation = userService.queryRelation(toUserId);
        relation.setFriend(isFriend);
        relation.setWorkerFriend(isWorkFriend);
        relation.setIgnoreMsg(isIgnoreMsg);
        relation.setIgnoreFriendMsg(isIgnoreFriendMsg);
        Content content = new Content();
        content.setData(relation);
        return ResponseUtil.createNormalJson(content);
    }

    /**
     * 修改用户昵称
     *
     * @param u_id 用户id
     * @return
     */
    @RequestMapping(value = "editNickname", method = RequestMethod.POST)
    @ResponseBody
    public String editNickname(@Param(value = "u_id") int u_id, @Param(value = "nickname") String nickname) {
        if (u_id <= 0 || TextUtils.isEmpty(nickname)) {
            throw new BusinessException("缺少必要参数");
        }
        UserInfo userInfo = userService.queryUserInfo(u_id);
        if (userInfo == null) {
            throw new BusinessException("该用户不存在");
        }
        userInfo.setU_nicheng(nickname);
        boolean success = userService.updateUserInfo(userInfo);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    /**
     * 修改用户资料
     *
     * @param u_id 用户id
     * @return
     */
    @RequestMapping(value = "editUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public String editUserInfo(@Param(value = "u_id") int u_id, HttpServletRequest request) {
        if (u_id <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        UserInfo userInfo = userService.queryUserInfo(u_id);
        if (userInfo == null) {
            throw new BusinessException("该用户不存在");
        }
        if (userInfo.getU_cert() == 2) {
            throw new BusinessException("该用户已认证不能修改个人信息");
        }
        String name = request.getParameter("name");
        String sex = request.getParameter("sex");
        String birthday = request.getParameter("birthday");
        String area = request.getParameter("area");
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(sex) && TextUtils.isEmpty(birthday) && TextUtils.isEmpty(area)) {
            throw new BusinessException("没有修改任何信息,请确认修改项");
        }
        boolean editUserInfo = false;
        if (!TextUtils.isEmpty(name)) {
            userInfo.setU_realname(name);
            editUserInfo = true;
        }
        if (!TextUtils.isEmpty(sex)) {
            userInfo.setU_sex(Integer.parseInt(sex));
            editUserInfo = true;
        }
        if (!TextUtils.isEmpty(birthday)) {
            userInfo.setU_birthday(DateUtil.getFormatDate(birthday, DateUtil.FORMAT_DATE));
            editUserInfo = true;
        }
        boolean editSfz = false;
        boolean saveSfzSuccess = false;
        if (!TextUtils.isEmpty(area)) {
            if (area.contains("-")) {
                editSfz = true;
                String[] addresss = area.split("-");
                String province = addresss[0];
                String city = addresss[1];
                UserSfz userSfz = userService.queryUserSfzByUserId(u_id);
                if (userSfz == null) {
                    userSfz = new UserSfz();
                }
                userSfz.setUserId(u_id);
                userSfz.setProvince(province);
                userSfz.setCity(city);
                saveSfzSuccess = userService.saveOrUpdateUserSfz(userSfz);
            } else {
                throw new BusinessException("修改籍贯数据格式错误！");
            }
        }
        if (editUserInfo && !editSfz) {//单修改个人信息
            boolean success = userService.updateUserInfo(userInfo);
            return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
        } else if (!editUserInfo && editSfz) {//单修改籍贯信息
            return saveSfzSuccess ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
        } else {//同时修改籍贯和个人信息
            boolean success = userService.updateUserInfo(userInfo);
            return (success && saveSfzSuccess) ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
        }

    }

    /**
     * 更新最后登录的设备
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [userId, device]
     * @Date: 2018-11-12 下午 16:12
     */
    @RequestMapping(value = "updateDevice", method = RequestMethod.POST)
    @ResponseBody
    public String updateDevice(Integer userId, String device) {
        try {
            UserInfo userInfo = this.userService.queryUserInfo(userId);
            if (userInfo != null) {
                userService.updateDevice(userId, device);
                return ResponseUtil.createNormalJson(null);
            } else {
                throw new RuntimeException("用户不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 退出登录
     *
     * @return java.lang.String
     * @params: [userId]
     * @Author: wuyeming
     * @Date: 2018-11-27 上午 10:17
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public String logout(Integer userId) {
        try {
            UserInfo userInfo = this.userService.queryUserInfo(userId);
            if (userInfo != null) {
                userInfo.setLogout_time(new Timestamp(System.currentTimeMillis()));
                userService.saveOrUpdate(userInfo);
                return ResponseUtil.createNormalJson(null);
            } else {
                throw new RuntimeException("用户不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 推送消息
     *
     * @return java.lang.String
     * @params: [userId, status]
     * @Author: wuyeming
     * @Date: 2018-11-27 上午 10:28
     */
    @RequestMapping(value = "sendMessage", method = RequestMethod.POST)
    @ResponseBody
    public String sendMessage(int userId, int status) {
        try {
            UserInfo toUserInfo = userService.queryUserInfo(userId);
            if (toUserInfo != null) {
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> extMap = new HashMap<>();//附加信息
                extMap.put("myUserName", ktpTeamName);
                List<Object> toUserIdList = new ArrayList<>();//要推送的用户列表
                toUserIdList.add(toUserInfo.getId());
                Date now = new Date();//当前时间
                Date logoutDate = toUserInfo.getLogout_time();//上次退出登录时间
                if (status == 1) {//登录
                    if ((logoutDate != null) && ((now.getTime() - logoutDate.getTime()) / (1000 * 3600 * 24)) >= 14) {//判断当前登录时间与上次退出登录时间间隔是否超过两周
//                    if ((logoutDate != null) && ((now.getTime() - logoutDate.getTime()) / (1000 * 60)) >= 5) {//5分钟，用于测试
                        map.put("status", 1);
                        HuanXinRequestUtils.sendMessage(ktpTeamId, toUserIdList, extMap, FreemarkerFactory.parseTemplate("user.ftl", map));
                    }
                } else {//注册
                    map.put("status", 0); //1:登录 0:注册
                    HuanXinRequestUtils.sendMessage(ktpTeamId, toUserIdList, extMap, FreemarkerFactory.parseTemplate("user.ftl", map));
                }
                return ResponseUtil.createNormalJson(extMap);
            } else {
                throw new RuntimeException("用户不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 设置密码和推荐码
     *
     * @params: [userId, old_psw, new_psw，code]
     */
    @RequestMapping(value = "/setCodeAndPWSNew", method = RequestMethod.POST)
    @ResponseBody
    public String setCodeAndPWSNew(UserInfoDTO userInfoDTO) {
        try {
            Map<String, Object> map = new HashMap<>();
            if (userInfoDTO.getNew_psw() == null) {
                userInfoDTO.setNew_psw(" ");
            }
            String new_pws = userInfoDTO.getNew_psw().replace(" ", "");
            map.put("user_id", userInfoDTO.getUser_id());
            map.put("u_psw_type", 0);
            if (new_pws != null && !new_pws.equals("")) {
                userService.updateByUserId(userInfoDTO.getUser_id(), new_pws);
                map.put("u_psw_type", 1);
            }
            CcbPush ccb = userService.getCcbPush(userInfoDTO.getUser_id());
            if (ccb == null) {
                map.put("u_code", 0);
                if (userInfoDTO.getCode() == null) {
                    userInfoDTO.setCode(" ");
                }
                String code = userInfoDTO.getCode().replace(" ", "");
                CcbPush ccbPush = new CcbPush();
                ccbPush.setUid(userInfoDTO.getUser_id());
                ccbPush.setInitime(new Date());
                ccbPush.setUid(userInfoDTO.getUser_id());
                if (code != null && !code.equals("")) {
                    ccbPush.setCcbid(code);
                    map.put("u_code", 1);
                }
                userService.insertCcbPush(ccbPush);
            }
            return ResponseUtil.createNormalJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 设置密码和推荐码
     *
     * @params: [userId, old_psw, new_psw，code]
     */
    @RequestMapping(value = "/setCodeAndPWS", method = RequestMethod.POST)
    @ResponseBody
    public String setCodeAndPWS(UserInfoDTO userInfoDTO) {
        try {
            Map<String, Object> map = new HashMap<>();
            if (userInfoDTO.getNew_psw() == null) {
                userInfoDTO.setNew_psw(" ");
            }
            String new_pws = userInfoDTO.getNew_psw().replace(" ", "");
            map.put("user_id", userInfoDTO.getUser_id());
            map.put("u_psw_type", 0);
            if (new_pws != null && !new_pws.equals("")) {
                String s = Md5Util.encryption(new_pws + "FAFJJeremf@#$&245");
                userService.updateByUserId(userInfoDTO.getUser_id(), s);
                map.put("u_psw_type", 1);
            }
            CcbPush ccb = userService.getCcbPush(userInfoDTO.getUser_id());
            if (ccb == null) {
                map.put("u_code", 0);
                if (userInfoDTO.getCode() == null) {
                    userInfoDTO.setCode(" ");
                }
                String code = userInfoDTO.getCode().replace(" ", "");
                CcbPush ccbPush = new CcbPush();
                ccbPush.setUid(userInfoDTO.getUser_id());
                ccbPush.setInitime(new Date());
                ccbPush.setUid(userInfoDTO.getUser_id());
                if (code != null && !code.equals("")) {
                    ccbPush.setCcbid(code);
                    map.put("u_code", 1);
                }
                userService.insertCcbPush(ccbPush);
            }
            return ResponseUtil.createNormalJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /***
     * 判断当前用户是否在2019-01-01之后实名认证的
     * @param user_id
     * @return
     */
    @RequestMapping(value = "/isRegistered", method = RequestMethod.POST)
    @ResponseBody
    public String isRegistered(Integer user_id) {
        try {
            Map<String, Object> map = new HashMap<>();
            UserInfo userInfo = userService.findById(user_id);
            Date u_authentication = userInfo.getU_authentication();
            if (u_authentication == null) {
                map.put("exist", 0);
                return ResponseUtil.createNormalJson(map);
            }
            Date date = u_authentication;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = sdf.parse("2019-01-01");
            map.put("exist", 0);
            if (!date.before(parse)) {
                map.put("exist", 1);
                String ccid = userService.getCcid(user_id);
                map.put("code", "");
                if (ccid != null && !ccid.equals("")) {
                    map.put("code", ccid);
                }
                return ResponseUtil.createNormalJson(map);
            }
            return ResponseUtil.createNormalJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "/insertCcbPush", method = RequestMethod.POST)
    @ResponseBody
    public String insertCcbPush(Integer user_id, String code) {
        try {
            code = code.replace(" ", "");
            int i = userService.updateCcbPush(user_id, code);
            if (i == 0) {
                CcbPush ccbPush = new CcbPush();
                ccbPush.setUid(user_id);
                ccbPush.setInitime(new Date());
                ccbPush.setCcbid(code);
                userService.insertCcbPush(ccbPush);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("str", "添加推荐码成功");
            return ResponseUtil.createNormalJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 获取用户唯一标识
     *
     * @return java.lang.String
     * @params: [userId]
     * @Author: wuyeming
     * @Date: 2019/3/25 15:50
     */
    @RequestMapping(value = "/getUniqueId", method = RequestMethod.GET)
    @ResponseBody
    public String getUniqueId(Integer userId) {
        try {
            String uniqueId = userService.getUniqueId(userId);
            Map<String, Object> map = new HashMap<>();
            map.put("uniqueId", uniqueId);
            return ResponseUtil.createNormalJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

}
