package com.ktp.project.web;

import com.ktp.project.entity.GoodOrderQuery;
import com.ktp.project.entity.MallAttr;
import com.ktp.project.entity.MallGoodSpec;
import com.ktp.project.entity.MallSort;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.MallGoodService;
import com.ktp.project.util.ResponseUtil;
import com.ktp.project.util.StringUtil;
import org.apache.http.util.TextUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "h5/mall/good", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class MallGoodH5Controller {

    private Logger logger = LoggerFactory.getLogger("MallGoodH5Controller");

    @Autowired
    private MallGoodService mallGoodService;

    @RequestMapping(value = "getMallGoodDetail", method = RequestMethod.GET)
    @ResponseBody
    public String getMallGoodDetail(@Param(value = "goodId") int goodId, HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        HttpSession session = request.getSession();
        logger.debug("test xxxxxxxx session is " + session.getId());
        String checkcode = (String) session.getAttribute("checkcode");
        if (checkcode == null) {
            checkcode = StringUtil.randomUUID();
            session.setAttribute("checkcode", checkcode);
        }
        GoodOrderQuery goodOrderQuery = mallGoodService.queryGoodOrderDetail(goodId);
        if (goodOrderQuery == null) {
            goodOrderQuery = new GoodOrderQuery();
        } else {
            List<MallGoodSpec> list = mallGoodService.queryMallGoodSpecList(goodId);
            goodOrderQuery.setGoodSpecList(list);
        }
        return ResponseUtil.createNormalJson(goodOrderQuery, checkcode);
    }

    @RequestMapping(value = "addSort", method = RequestMethod.POST)
    @ResponseBody
    public String addSort(@Param(value = "sort") String sort, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (TextUtils.isEmpty(sort)) {
            throw new BusinessException("缺少必要参数");
        }
        boolean checkexist = mallGoodService.checkSortExist(sort);
        if (checkexist) {
            throw new BusinessException("该商品分类已存在");
        }
        MallSort mallSort = new MallSort();
        mallSort.setSortName(sort);
        mallSort.setSortState(1);
        boolean success = mallGoodService.saveMallSort(mallSort);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    @RequestMapping(value = "addSpec", method = RequestMethod.POST)
    @ResponseBody
    public String addSpec(@Param(value = "spec") String spec, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (TextUtils.isEmpty(spec)) {
            throw new BusinessException("缺少必要参数");
        }
        boolean exist = mallGoodService.checkSpecExist(spec);
        if (exist) {
            throw new BusinessException("该商品规格已存在");
        }
        MallAttr mallAttr = new MallAttr();
        mallAttr.setName(spec);
        boolean success = mallGoodService.saveMallAttr(mallAttr);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

}
