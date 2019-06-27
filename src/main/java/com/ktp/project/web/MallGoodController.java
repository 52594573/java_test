package com.ktp.project.web;

import com.ktp.project.entity.*;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.MallGoodService;
import com.ktp.project.util.ResponseUtil;
import com.ktp.project.util.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 商城商品
 *
 * @author djcken
 * @date 2018/5/28
 */
@Controller
@RequestMapping(value = "api/mall/good", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class MallGoodController {

    @Autowired
    private MallGoodService mallGoodService;

    @RequestMapping(value = "getSortList", method = RequestMethod.GET)
    @ResponseBody
    public String getSortList(HttpServletResponse response, HttpServletRequest request) {
//        if (u_id <= 0) {
//            throw new BusinessException("缺少必要参数");
//        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        int userId = StringUtil.parseToInt(request.getParameter("u_id"));
        List<MallSort> mallSortList = mallGoodService.queryMallSortList(1);
        if (mallSortList == null) {
            mallSortList = new ArrayList<>();
        }
        long count = userId > 0 ? mallGoodService.queryMallCarCount(userId) : 0;
        MallSortInfo mallSortInfo = new MallSortInfo();
        mallSortInfo.setList(mallSortList);
        mallSortInfo.setCount(count);
        return ResponseUtil.createNormalJson(mallSortInfo);
    }

    @RequestMapping(value = "getMallGoodList", method = RequestMethod.GET)
    @ResponseBody
    public String getMallGoodList(HttpServletResponse response, HttpServletRequest request) {
//        if (u_id <= 0) {
//            throw new BusinessException("缺少必要参数");
//        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sortIdStr = request.getParameter("sortId");
        List<GoodOrderQuery> goodOrderQueryList = mallGoodService.queryMallGoodList(StringUtil.parseToInt(sortIdStr));
        if (goodOrderQueryList == null) {
            goodOrderQueryList = new ArrayList<>();
        } else {
            for (GoodOrderQuery orderQuery : goodOrderQueryList) {
                int goodId = orderQuery.getGoodId();
                List<MallGoodSpec> list = mallGoodService.queryMallGoodSpecList(goodId);
                if (list != null && !list.isEmpty()) {
                    double minPrice = list.get(0).getPrice();
                    double minOriginPrice = list.get(0).getOriginPrice();
                    for (int i = 0; i < list.size(); i++) {
                        if (i > 0) {
                            minPrice = Math.min(minPrice, list.get(i).getPrice());
                            minOriginPrice = Math.min(minOriginPrice, list.get(i).getOriginPrice());
                        }
                    }
                    orderQuery.setGoodPrice(minPrice);
                    orderQuery.setGoodOriginPrice(minOriginPrice);
                }
                long buyCount = mallGoodService.queryBuyCount(goodId);
                orderQuery.setBuyCount(buyCount);
                orderQuery.setGoodSpecList(list);
            }
        }
        return ResponseUtil.createNormalJson(goodOrderQueryList);
    }

    @RequestMapping(value = "getMallGoodDetail", method = RequestMethod.GET)
    @ResponseBody
    public String getMallGoodDetail(@Param(value = "goodId") int goodId) {
//        if (u_id <= 0) {
//            throw new BusinessException("缺少必要参数");
//        }
        GoodOrderQuery goodOrderQuery = mallGoodService.queryGoodOrderDetail(goodId);
        if (goodOrderQuery == null) {
            goodOrderQuery = new GoodOrderQuery();
        } else {
            long buyCount = mallGoodService.queryBuyCount(goodId);
            goodOrderQuery.setBuyCount(buyCount);
            List<MallGoodSpec> list = mallGoodService.queryMallGoodSpecList(goodId);
            goodOrderQuery.setGoodSpecList(list);
        }
        return ResponseUtil.createNormalJson(goodOrderQuery);
    }

    @RequestMapping(value = "saveMallCarGood", method = RequestMethod.POST)
    @ResponseBody
    public String saveMallCarGood(@Param(value = "u_id") int u_id, @Param(value = "goodId") int goodId, @Param(value = "specId") int specId, @Param(value = "count") int count) {
        if (u_id <= 0 || goodId == 0 || specId == 0 || count == 0) {
            throw new BusinessException("缺少必要参数");
        }
        MallCar mallCar = mallGoodService.queryMalCarGood(u_id, goodId, specId);
        if (mallCar == null) {
            mallCar = new MallCar();
        }
        mallCar.setUserId(u_id);
        mallCar.setGoodId(goodId);
        mallCar.setGoodSpecId(specId);
        mallCar.setCount(count);
        boolean success = mallGoodService.saveOrUpdateMallCar(mallCar);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    @RequestMapping(value = "deleteMallCarGood", method = RequestMethod.POST)
    @ResponseBody
    public String deleteMallCarGood(@Param(value = "u_id") int u_id, @Param(value = "goodId") int goodId, @Param(value = "specId") int specId) {
        if (u_id <= 0 || goodId == 0 || specId == 0) {
            throw new BusinessException("缺少必要参数");
        }
        boolean success = mallGoodService.deleteMallCarGood(u_id, goodId, specId) == 1;
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    @RequestMapping(value = "getMallCarList", method = RequestMethod.GET)
    @ResponseBody
    public String getMallCarList(@Param(value = "u_id") int u_id) {
        if (u_id <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        List<ShopingCar> shopingCarList = mallGoodService.queryMallCarByUserId(u_id);
        return ResponseUtil.createNormalJson(shopingCarList);
    }
}
