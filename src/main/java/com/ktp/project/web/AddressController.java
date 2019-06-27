package com.ktp.project.web;

import com.ktp.project.entity.MallGet;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.AddressService;
import com.ktp.project.util.ResponseUtil;
import com.ktp.project.util.StringUtil;
import org.apache.http.util.TextUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 地址管理
 *
 * @author djcken
 * @date 2018/6/11
 */
@Controller
@RequestMapping(value = "api/mall/address", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 地址列表
     *
     * @param u_id 用户id
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public String getAddressList(@Param(value = "u_id") int u_id) {
        if (u_id <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        List<MallGet> mallGetList = addressService.queryMallGetList(u_id);
        if (mallGetList == null) {
            mallGetList = new ArrayList<>();
        }
        return ResponseUtil.createNormalJson(mallGetList);
    }

    /**
     * 删除接口
     *
     * @param u_id      用户id
     * @param addressId 地址id
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteAddress(@Param(value = "u_id") int u_id, @Param(value = "addressId") int addressId) {
        if (u_id <= 0 || addressId <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        boolean success = addressService.deleteMallGet(u_id, addressId);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    /**
     * 保存或修改
     *
     * @param u_id            用户id
     * @param customerName    收货名称
     * @param customerMobile  收货电话
     * @param customerAddress 收获地址
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public String saveAddress(@Param(value = "u_id") int u_id, @Param(value = "customerName") String customerName,
                              @Param(value = "customerMobile") String customerMobile, @Param(value = "customerAddress") String customerAddress, HttpServletRequest request) {
        if (u_id <= 0 || TextUtils.isEmpty(customerName) || TextUtils.isEmpty(customerMobile) || TextUtils.isEmpty(customerAddress)) {
            throw new BusinessException("缺少必要参数");
        }
        String addressIdStr = request.getParameter("addressId");
        int addressId = -1;
        if (!TextUtils.isEmpty(addressIdStr)) {
            addressId = StringUtil.parseToInt(addressIdStr);
        }
        String defaultStr = request.getParameter("isDefault");
        int isDefault = -1;
        if (!TextUtils.isEmpty(defaultStr)) {
            isDefault = StringUtil.parseToInt(defaultStr);
        }
        MallGet mallGet = null;
        if (addressId > 0) {
            mallGet = addressService.getMallGetById(addressId);
        }
        if (mallGet == null) {
            mallGet = new MallGet();
        }
        mallGet.setUserId(u_id);
        mallGet.setName(customerName);
        mallGet.setMobile(customerMobile);
        mallGet.setAddress(customerAddress);
        if (isDefault == 1) {
            mallGet.setIsDefault(1);
            addressService.updateMallGetDefaultByUserId(u_id);
        }
        boolean success = addressService.saveOrUpdateMallGet(mallGet);
        if (mallGet.getId() <= 0) {
            int getAddressId = addressService.queryMaxMallGetId(u_id);
            mallGet.setId(getAddressId);
        }
        return success ? ResponseUtil.createNormalJson(mallGet) : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    /**
     * 默认地址
     *
     * @param u_id      用户id
     * @param addressId 地址id
     * @return
     */
    @RequestMapping(value = "default", method = RequestMethod.POST)
    @ResponseBody
    public String defaultAddress(@Param(value = "u_id") int u_id, @Param(value = "addressId") int addressId) {
        if (u_id <= 0 || addressId <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        MallGet mallGet = addressService.getMallGetById(addressId);
        if (mallGet == null) {
            throw new BusinessException("该地址不存在");
        }
        mallGet.setIsDefault(1);
        addressService.updateMallGetDefaultByUserId(u_id);
        boolean success = addressService.saveOrUpdateMallGet(mallGet);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }
}
