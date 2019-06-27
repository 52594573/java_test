package com.ktp.project.dao;

import com.ktp.project.exception.BusinessException;
import com.taobao.api.domain.Userinfos;
import com.zm.entity.ProOrganPer;
import com.zm.entity.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2018-11-7 下午 17:33
 */
@Repository("userInfo")
public class UserInfoDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<UserInfo> getAllUser() {
        String sql = "SELECT DISTINCT u.* FROM user_info u JOIN pro_organ_per pro ON pro.user_id=u.id " +
                "JOIN pro_organ o ON pro.po_id=o.id " +
                "JOIN project p ON p.id=o.pro_id " +
                "WHERE u.last_device IS NOT NULL ";
        SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
        sqlQuery.addEntity("user_info",UserInfo.class);
        List<UserInfo> userInfoList = sqlQuery.list();
        return userInfoList;
    }

    /**
      * 更新最后登录的设备
      *
      * @return void
      * @Author: wuyeming
      * @params: [userId, device]
      * @Date: 2018-11-12 下午 15:31
      */
    public void updateDevice(Integer userId, String device) {
        String sql = "UPDATE user_info SET last_device = '" + device + "' WHERE id = " + userId + " ";
        try {
            this.getCurrentSession().createSQLQuery(sql).executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }



    /**
      *
      * 根据id查询用户
      * @return com.zm.entity.UserInfo
      * @params: [id]
      * @Author: wuyeming
      * @Date: 2018-11-27 上午 10:13
      */
    public UserInfo getUserInfoById(Integer id) {
        UserInfo userInfo = (UserInfo) this.getCurrentSession().get(UserInfo.class, id);
        return userInfo;
    }


    /**
      * 保存
      *
      * @return void
      * @params: [userInfo]
      * @Author: wuyeming
      * @Date: 2018-11-27 上午 10:17
      */
    public void saveOrUpdate(UserInfo userInfo){
        this.getCurrentSession().saveOrUpdate(userInfo);
    }


    /**
      * 根据用户类型查询用户
      *
      * @return java.util.List<com.zm.entity.UserInfo>
      * @params: [type]
      * @Author: wuyeming
      * @Date: 2018-11-29 下午 18:47
      */
    public List<UserInfo> getUserListByType(Integer type) {
        String hql = "from UserInfo where u_type = '" + type + "'";
        Query query = this.getCurrentSession().createQuery(hql);
        List<UserInfo> userInfoList = query.list();
        return userInfoList;
    }

    /**
     * 根据用户类型查询用户
     *
     * @return java.util.List<com.zm.entity.UserInfo>
     * @params: [type]
     * @Author: lsh
     * @Date: 2018-11-29 下午 18:47
     */
    public List<UserInfo> gitthankUser() {
        String hql = "from UserInfo where 0 = datediff(now(),u_intime)%365";
        Query query = this.getCurrentSession().createQuery(hql);
        List<UserInfo> userInfoList = query.list();
        return userInfoList;

    }

    /**
     * 通过手机号查询用户
     */
   public UserInfo getUserInfoByPhone(String phone){
       String hql = "from UserInfo where u_name = ?";
       UserInfo userInfo = null;
       try {
           Query query  = getCurrentSession().createQuery(hql);
           query.setParameter(0, phone);
           query.setFirstResult(0);
           query.setMaxResults(1);
           userInfo = (UserInfo) query.uniqueResult();
       } catch (Exception e) {
           e.printStackTrace();
           throw new BusinessException(String.format("通过手机号:%s,查询用户失败", phone));
       }
       return userInfo;
   }

    /**
     * 通过手机号查询用户
     */
    public List<UserInfo> listUserInfoPhoneIn(String mobiles) {
        String hql = "from UserInfo where u_name in ( " + mobiles + " )";
        List list = null;
        try {
            Query query = getCurrentSession().createQuery(hql);
//            query.setParameter(0, mobiles);
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过手机号:%s,查询用户失败", mobiles));
        }
        return list;
    }

    public UserInfo getUserInfoByCardId(String identityCode) {
        String hql = "from UserInfo where u_sfz like ? ";
        UserInfo userInfo = null;
        try {
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, "%" + identityCode + "%" );
            query.setFirstResult(0);query.setMaxResults(1);
            userInfo = (UserInfo) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过身份证号码:%s,查询用户失败", identityCode));
        }
        return userInfo;
    }

    public List<UserInfo> getUserInfoByUserIdIn(String userIds) {
        String hql = "from UserInfo where id in ( " + userIds + " )";
        List<UserInfo> userInfos = null;
        try {
            Query query = getCurrentSession().createQuery(hql);
            userInfos = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过用户ID串:%s,查询用户失败", userIds));
        }
        return userInfos;
    }

}
