package com.ktp.project.logic;

import com.ktp.project.entity.UserToken;
import com.ktp.project.exception.DefaultException;
import com.ktp.project.exception.TokenException;
import com.ktp.project.exception.UnAuthoriedException;
import com.ktp.project.service.UserService;
import com.ktp.project.util.Md5Util;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 默认拦截器
 *
 * @author djcken
 * @date 2018/5/23
 */
public class DefaultInterceptor extends HandlerInterceptorAdapter {

    private static final String PASSWORD = "FAFJJeremf@#$&245";

    @Autowired
    private UserService userService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求处理完成之后
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 处理器执行完毕之后
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 处理器实际执行之前
        String myImei = request.getParameter("my_IMEI");
        String verKey = request.getParameter("ver_key");
        String token = request.getParameter("token");
        if (TextUtils.isEmpty(myImei) || TextUtils.isEmpty(verKey) || TextUtils.isEmpty(token)) {
            throw new DefaultException("缺少必要参数");
        }
        String userId = request.getParameter("u_id");
        if (!TextUtils.isEmpty(userId)) {
//            UserToken userToken = userService.queryUserTokenByImei(myImei, userId);
//            if (userToken == null || !userId.equals(userToken.getUserId())) {
//                throw new TokenException("token和id不匹配");
//            }
            List<UserToken> list = userService.queryUserTokenByImei(myImei, userId);
            if (list.size() == 0) {
                throw new TokenException("token和id不匹配");
            }
        }
        String myVerKey = Md5Util.MD5Encode(token + PASSWORD);
        if (!myVerKey.equals(verKey)) {
            throw new UnAuthoriedException("用户鉴权失败");
        }
        return super.preHandle(request, response, handler);
    }


}
