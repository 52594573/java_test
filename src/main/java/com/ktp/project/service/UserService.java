package com.ktp.project.service;

import com.google.common.base.Strings;
import com.ktp.project.dao.*;
import com.ktp.project.dto.CertDTO;
import com.ktp.project.entity.*;
import com.ktp.project.entity.UserSfz;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.util.CardUtil;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.StringUtil;
import com.zm.entity.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author djcken
 * @date 2018/5/24
 */
@Service
@Transactional
public class UserService {

    private static double DEF_R = 6370693.5; // radius of earth
    @Autowired
    private DataBaseDao dataBaseDao;
    @Autowired
    private ProOrganPerDAO proOrganPerDAO;
    @Autowired
    private UserInfoDAO userInfoDAO;
    @Autowired
    private UserActiveDao userActiveDao;
    @Autowired
    private UserInfoDao userInfo;
    @Autowired
    private QueryChannelDao queryChannelDao;
    @Autowired
    private ModifyChannelDao modifyChannelDao;
    @Autowired
    private ShareService shareService;


    /**
     * 实名认证
     *
     * @param certDTO
     */
    public void newCert(CertDTO certDTO) {
        String userName = certDTO.getUserName();
        String idCardNo = certDTO.getIdCardNo();
        Integer userId = getUserId(certDTO);
        if (userId == null || TextUtils.isEmpty(userName) || TextUtils.isEmpty(idCardNo)) {
            throw new RuntimeException("参数错误");
        }

        List<UserInfo> user = queryUserInfoBy(idCardNo, userId);
        if (user.size() != 0 && !Strings.isNullOrEmpty(user.get(0).getU_name())) {
            String enMobile = user.get(0).getU_name().replaceAll("^(\\d{3})\\d*(\\d{4})$", "$1****$2");
            throw new BusinessException("上传认证数据失败，身份证已被" + enMobile + "注册");
        }

        UserInfo userInfo = builderUserInfo(certDTO, userId);
        builderUserSfz(certDTO, userId);
        updateShareService(userInfo.getU_name());
    }

    public Integer getUserId(CertDTO certDTO) {
        if (certDTO.getU_id() != null) {
            return certDTO.getU_id();
        }
        if (certDTO.getUserId() != null) {
            return certDTO.getUserId();
        }
        return null;
    }

    public UserInfo builderUserInfo(CertDTO certDTO, Integer userId) {
        String userName = certDTO.getUserName();
        String idCardNo = certDTO.getIdCardNo();

        UserInfo userInfo = queryChannelDao.queryOne(UserInfo.class, userId);
        if (userInfo == null) {
            throw new RuntimeException("该用户不存在");
        }
        if (!idCardNo.equals(userInfo.getU_sfz())) {
            throw new RuntimeException("身份证号码不匹配");
        }

        Map<String, Object> map = CardUtil.getCarInfo1(idCardNo);
        if (map != null) {
            userInfo.setU_sex((int) map.get("sex"));
            userInfo.setU_birthday(DateUtil.getFormatDate(map.get("birthday").toString(), DateUtil.FORMAT_DATE));  // 设置出生日期
        }

        userInfo.setU_realname(userName);  // 设置真实姓名
        userInfo.setU_nicheng(userName);
        userInfo.setU_sfz(idCardNo);
        userInfo.setU_cert_type(1);  // 身份证认证
        userInfo.setU_cert(2);  // 标记为已认证
        userInfo.setU_authentication(new Date());
        userInfo.setU_sfzpic(certDTO.getSfzPic());
        userInfo.setU_cert_pic(certDTO.getCertPic());
        userInfo.setU_similarity(Double.parseDouble(certDTO.getSimilarity()));
        modifyChannelDao.update(userInfo);
        return userInfo;
    }

    public void builderUserSfz(CertDTO certDTO, Integer userId) {
        String userName = certDTO.getUserName();
        String idCardNo = certDTO.getIdCardNo();

        UserSfz userSfz = queryUserSfzByUserId(userId);
        if (userSfz == null) {
            userSfz = new UserSfz();
        }
        userSfz.setUserId(userId);
        userSfz.setName(userName);
        userSfz.setPic(certDTO.getSfzPic());
        userSfz.setNation(certDTO.getNation());
        userSfz.setAddress(certDTO.getAddress());
        userSfz.setOrg(certDTO.getOrg());
        userSfz.setStartTime(certDTO.getStartTime());
        userSfz.setExpireTime(certDTO.getExpireTime());

        Map<String, Object> map = CardUtil.getCarInfo1(idCardNo);
        if (map == null) {
            userSfz.setSex((int) map.get("sex"));
            userSfz.setAge((int) map.get("age"));
            userSfz.setBirthYear(map.get("year").toString());
            userSfz.setBirthMonth(map.get("month").toString());
            userSfz.setBirthDay(map.get("day").toString());
        }

        Map<String, String> addressMap = CardUtil.addressResolution(certDTO.getAddress());
        if (addressMap != null) {
            userSfz.setProvince(addressMap.get("province"));
            userSfz.setCity(addressMap.get("city"));
            userSfz.setArea(addressMap.get("county"));
        }
        modifyChannelDao.saveOrUpdate(userSfz);
    }

    public void updateShareService(String phone) {
        if (Strings.isNullOrEmpty(phone)) return;
        List<ShareCommit> shareCommits = shareService.queryShareCommitbyphone(phone);
        if (CollectionUtils.isEmpty(shareCommits)) return;
        ShareCommit shareCommit = shareCommits.get(0);
        if (shareCommit.getCommitRegister() == 1) {
            shareCommit.setCommitCert(1);
            shareService.saveOrUpdateShareCommit(shareCommit);
        }
    }


    /**
     * 修改手机号码
     *
     * @param newMobile
     * @param idCardNo
     */
    public void modifyMobile(String newMobile, String idCardNo) {
        if (newMobile.length() != 11) {
            throw new RuntimeException("请输入正确的手机号码");
        }
        List<UserInfo> users = queryChannelDao.queryMany("from UserInfo u where u.u_name = ?", Arrays.asList(newMobile), UserInfo.class);
        if (users.size() != 0) {
            throw new RuntimeException("该手机号码已注册");
        }
        users = queryChannelDao.queryMany("from UserInfo u where u.u_sfz = ?", Arrays.asList(idCardNo), UserInfo.class);
        if (users.size() == 0) {
            throw new RuntimeException("非法操作");
        }
        if (users.size() != 1) {
            throw new RuntimeException("存在多行记录，请联系客服处理");
        }
        UserInfo userInfo = users.get(0);
        userInfo.setU_name(newMobile);
        modifyChannelDao.update(userInfo);
    }


    /*public UserToken queryUserTokenByImei(String myImei, String userId) {
        return dataBaseDao.queryUserTokenByImei(myImei, userId);
    }*/
    public List<UserToken> queryUserTokenByImei(String myImei, String userId) {
        return dataBaseDao.queryUserTokenByImei(myImei, userId);
    }

    public boolean checkexist(int userId, int popType) {
        return proOrganPerDAO.checkexist(userId, popType);
    }

    public UserInfo queryUserInfo(int userId) {
        return userInfoDAO.findById(userId);
    }


    public List<UserInfo> queryUserInfobyphone(String phone) {
        return userInfoDAO.queryUserInfobyphone(phone);
    }

    public boolean updateUserInfoType(int userId, int changeType) {
        return userInfoDAO.updateUserInfoType(userId, changeType) == 1;
    }

    public boolean updateUserInfo(UserInfo userInfo) {
        return userInfoDAO.updateUserInfo(userInfo);
    }

    public UserSfz queryUserSfzByUserId(int userId) {
        return dataBaseDao.queryUserSfzByUserId(userId);
    }


    public boolean saveOrUpdateUserSfz(UserSfz transientInstance) {
        return dataBaseDao.saveOrUpdateUserSfz(transientInstance);
    }

    public List<UserBankModel> queryUserBank(int userId) {
        return dataBaseDao.queryUserBank(userId);
    }

    public UserBank queryUserBank(int userId, String bankId) {
        return dataBaseDao.queryUserBank(userId, bankId);
    }

    public UserBankModel queryUserBank(int userId, int popType, int poId) {
        return dataBaseDao.queryUserBank(userId, popType, poId);
    }

    public UserBankModel queryUserBankModel(int userId, String bankNo) {
        return dataBaseDao.queryUserBankModel(userId, bankNo);
    }

    public boolean saveOrUpdateUserBank(UserBank transientInstance) {
        return dataBaseDao.saveOrUpdateUserBank(transientInstance);
    }

    public boolean deleteUserBank(int userId, String bankId) {
        return dataBaseDao.deleteUserBank(userId, bankId) == 1;
    }

    public String queryBankId(int userId, int popType, int poId) {
        return proOrganPerDAO.queryBankId(userId, popType, poId);
    }

    public boolean updateBankId(int userId, int popType, int poId, String bankId) {
        return proOrganPerDAO.updateBankId(userId, popType, poId, bankId) == 1;
    }

    public List<SysBank> querySysBank() {
        return dataBaseDao.querySysBank();
    }

    /**
     * 保存或更新UserActive表数据
     *
     * @return
     */
    public boolean saveOrUpdateUserActive(int userId, double lat, double lon) {
        return userActiveDao.saveOrUpdate(userId, lat, lon);
    }

    /**
     * 附近的人
     *
     * @return
     */
    public List<UserNear> getNearUserList(int userId, double latitude, double longitude, int startPage, int length, String startYear, String endYear, int sex, String areas, String searchKey) {
        float distance = 10000;
        Map map = returnLLSquarePoint(latitude, longitude, distance);
        List<UserNear> list = userInfoDAO.getNearUserList(userId, (double) map.get("left"), (double) map.get("right"),
                (double) map.get("top"), (double) map.get("bottom"), startYear, endYear, sex, areas);

        if (list != null && !list.isEmpty()) {
            Iterator<UserNear> iterator = list.iterator();
            while (iterator.hasNext()) {
                UserNear userNear = iterator.next();
                String uNicheng = userNear.getU_nicheng();
                String zhiwu = "";
                try {
                    zhiwu = proOrganPerDAO.findzhiwu(userNear.getUser_id(), userNear.getU_proid());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                userNear.setZhiwu(zhiwu);
                Date lasttime = userNear.getLastime();
                Date nowTime = new Date();
                userNear.setInterval(nowTime.getTime() - lasttime.getTime());
                if (!TextUtils.isEmpty(searchKey) && !StringUtil.getNotNullString(zhiwu).contains(searchKey) && !StringUtil.getNotNullString(uNicheng).contains(searchKey)) {
                    iterator.remove();
                }
            }
        }
        return list;
    }

    /* 计算经纬度点对应正方形4个点的坐标
     *
     * @param longitude
     * @param latitude
     * @param distance
     * @return
     */
    public static Map<String, Double> returnLLSquarePoint(double latitude, double longitude,
                                                          double distance) {

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
        squareMap.put("left", Math.min(latitude + dLatitude, latitude - dLatitude));
        squareMap.put("right", Math.max(latitude + dLatitude, latitude - dLatitude));
        squareMap.put("top", Math.min(longitude + dLongitude, longitude - dLongitude));
        squareMap.put("bottom", Math.max(longitude + dLongitude, longitude - dLongitude));
        return squareMap;
    }


    /**
     * 判断是不是项目工友
     */
    public boolean isWorkFriend(int fromUserId, int toUserId) {
        return proOrganPerDAO.isWorkFriend(fromUserId, toUserId) > 0;
    }

    /**
     * 查新用户关系信息
     *
     * @param userId
     * @return
     */
    public Relation queryRelation(int userId) {
        return userInfoDAO.queryRelation(userId);
    }


    /**
     * 查询所有用户
     *
     * @return java.util.List<com.zm.entity.UserInfo>
     * @Author: wuyeming
     * @params: []
     * @Date: 2018-11-12 下午 16:10
     */
    @Transactional
    public List<UserInfo> getAllUser() {
        List<UserInfo> list = userInfo.getAllUser();
        return list;
    }

    /**
     * 更新最后登录的设备
     *
     * @return void
     * @Author: wuyeming
     * @params: [userId, device]
     * @Date: 2018-11-12 下午 15:33
     */
    @Transactional
    public void updateDevice(Integer userId, String device) {
        userInfo.updateDevice(userId, device);
    }


    /**
     * 保存
     *
     * @return void
     * @params: [user]
     * @Author: wuyeming
     * @Date: 2018-11-27 上午 10:19
     */
    @Transactional
    public void saveOrUpdate(UserInfo user) {
        userInfo.saveOrUpdate(user);
    }


    @Transactional
    public List<UserInfo> getUserListByType(Integer type) {
        return userInfo.getUserListByType(type);
    }


    public List<Object> getUserList(Integer projectId) {
        return proOrganPerDAO.getUserList(projectId);
    }

    /*public List<Object> getUserListAndBan(Integer proId) {
        return proOrganPerDAO.getUserListAndBan(proId);
    }*/
    @Transactional
    public List<UserInfo> gitthankUser() {
        //获取注册时间距离365周年
        return userInfo.gitthankUser();
    }

    public List<Map<String, Object>> getAllUserList(Integer proId) {
        return proOrganPerDAO.getAllUserList(proId);
    }

    public int getUser(Integer proId, Integer userId) {
        return proOrganPerDAO.getUser(proId, userId);
    }

    public void updateByInitTime() {
        userInfoDAO.updateByInitTime();
    }

    public void updateByUserId(Integer user_id, String new_pws) {
        userInfoDAO.updateByUserId(user_id, new_pws);
    }

    public void insertCcbPush(CcbPush ccbPush) {
        userInfoDAO.insertCcbPush(ccbPush);
    }

    public UserInfo findById(Integer user_id) {
        return userInfoDAO.findById(user_id);
    }

    public String getCcid(Integer user_id) {
        return userInfoDAO.getCcid(user_id);
    }

    public int updateCcbPush(Integer user_id, String code) {
        return userInfoDAO.updateCcbPush(user_id, code);
    }

    public CcbPush getCcbPush(Integer user_id) {
        return userInfoDAO.getCcbPush(user_id);
    }

    public List<UserInfo> queryUserInfoBy(String idCardNo, Integer u_id) {
        return userInfoDAO.queryUserInfoBy(idCardNo, u_id);
    }


    /**
      * 获取用户唯一标识
      *
      * @return java.lang.String
      * @params: [userId]
      * @Author: wuyeming
      * @Date: 2019/3/25 15:50
      */
    public String getUniqueId(Integer userId) {
        UserInfo user = userInfo.getUserInfoById(userId);
        Assert.isTrue(user != null, "用户不存在");
        String uniqueId = user.getUnique_id();
        String idCard = user.getU_sfz();
        if (StringUtils.isNotBlank(uniqueId)) {
            return uniqueId;
        } else {
            if (StringUtils.isBlank(idCard)) {
                idCard = random(8);
            } else {
                idCard = idCard.substring(idCard.length() - 8);
            }
        }
        String returnStr = "KTP" + idCard + random(7);
        user.setUnique_id(returnStr);
        userInfo.saveOrUpdate(user);
        return returnStr;
    }

    /**
     * 获取随机数
     * @param size
     * @return
     */
    private static String random(int size) {
        String num = "";
        for (int i = 0; i < size; i++) {
            num += new Random().nextInt(9);
        }
        return num;
    }

}
