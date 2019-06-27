package com.ktp.project.web;

import com.alipay.api.response.AlipayTradeRefundResponse;
import com.ktp.project.common.Common;
import com.ktp.project.entity.*;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.logic.schedule.OrderTask;
import com.ktp.project.pay.ali.*;
import com.ktp.project.pay.weixin.Weixinpay;
import com.ktp.project.pay.weixin.WeixinpayCfg;
import com.ktp.project.pay.weixin.response.WeixinpayAyncNotify;
import com.ktp.project.pay.weixin.response.WeixinpayRefundAyncNotify;
import com.ktp.project.pay.weixin.value.WeixinpayStatus;
import com.ktp.project.service.AddressService;
import com.ktp.project.service.MallOrderService;
import com.ktp.project.util.*;
import org.apache.http.util.TextUtils;
import org.apache.ibatis.annotations.Param;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商城订单controller
 *
 * @author djcken
 * @date 2018/5/19
 */
@Controller
@RequestMapping(value = "api/mall/order", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class MallOrderController {

    private Logger logger = LoggerFactory.getLogger("MallOrderController");

    @Autowired
    private MallOrderService mallOrderService;
    @Autowired
    private AddressService addressService;

    /**
     * 创建订单
     *
     * @param u_id         购买用户id
     * @param outTradeNo      订单号-如果有要传
     * @param goods           商品信息 1#0.01#2,2#0.01#3 商品id#商品原价格#购买商品数量，多件不同商品逗号分隔
     * @param customerName    客户姓名
     * @param customerMobile  客户电话
     * @param customerAddress 客户地址
     * @param mark            客户备注
     * @param amount          购买总价
     * @param sendPrice       物流价格
     * @return
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Param(value = "u_id") int u_id, @Param(value = "outTradeNo") String outTradeNo, @Param(value = "goods") String goods,
                         @Param(value = "customerName") String customerName, @Param(value = "customerMobile") String customerMobile,
                         @Param(value = "customerAddress") String customerAddress, @Param(value = "mark") String mark,
                         @Param(value = "amount") double amount, @Param(value = "sendPrice") double sendPrice, HttpServletRequest request) {
        return ResponseUtil.createBussniessErrorJson(0, "商品正在上架中，请稍后再来");
        /*if (u_id <= 0 || TextUtils.isEmpty(goods) || TextUtils.isEmpty(customerName) || TextUtils.isEmpty(customerMobile) || TextUtils.isEmpty(customerAddress)) {
            throw new BusinessException("缺少必要参数");
        }
        if (amount == 0) {
            throw new BusinessException("订单价格不能为0");
        }
        //处理订单逻辑
        String addressIdStr = request.getParameter("addressId");
        int addressId = -1;
        if (!TextUtils.isEmpty(addressIdStr)) {
            addressId = StringUtil.parseToInt(addressIdStr);
        }
        return saveOrUpdateOderInfo(outTradeNo, amount, sendPrice, mark, addressId, customerName, customerMobile, customerAddress, goods, u_id);*/
    }

    @RequestMapping(value = "pay", method = RequestMethod.POST)
    @ResponseBody
    public String pay(@Param(value = "u_id") int u_id, @Param(value = "payType") int payType, @Param(value = "outTradeNo") String outTradeNo, HttpServletRequest request) {
        return ResponseUtil.createBussniessErrorJson(0, "商品正在上架中，请稍后再来");
        /*if (u_id <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        if (payType != 1 && payType != 2) {
            throw new BusinessException("支付类型有误");
        }
        if (TextUtils.isEmpty(outTradeNo)) {
            throw new BusinessException("订单号不能为空");
        }
        MallOrder order = mallOrderService.queryMallOrderByOutTradeNo(outTradeNo);
        order.setPayType(Common.PAY_TYPE_ONLINE);
        order.setPayOnlineType(payType);
        String subject = StringUtil.replaceBlank(order.getSubject());
        String body = StringUtil.replaceBlank(order.getBody());
        if (payType == 1) {
            // 支付宝
            // 构建支付信息，然后返回给client，client以此进行支付(Android和IOS类似)  https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.PsmSu9&treeId=193&articleId=105300&docType=1
            //注意subject和body不能为空，否则无法创建订单
            String orderStr = Alipay.getOrderInfo(order.getOutTradeNo(), order.getTotalAmount(), subject, body);
            if (orderStr != null) {
                PayCreate payCreate = new PayCreate();
                payCreate.setOrderStr(orderStr);
                mallOrderService.updateMallOrder(order);
                return ResponseUtil.createNormalJson(payCreate, "创建支付宝订单信息成功");
            } else {
                return ResponseUtil.createBussniessErrorJson(0, "创建支付宝订单信息失败");
            }
        } else {
            //微信支付
            //创建预支付订单
            //在正式开发的时需要检查当前订单是否已经预创建过，已经预创建过的订单不能再次使用相同的商户订单号创建订单，要么更新商户订单号，要么使用旧的prepayid
            //注意body和detail不能为空，否则无法创建订单
            String userIp = IpUtil.get(request);
            Map<String, String> response = Weixinpay.createNew(outTradeNo, order.getTotalAmount(), subject, body, userIp, null);
            if (response != null) {
                //预创建支付订单成功
                String orderStr = GsonFactory.newGson().toJsonTree(response).toString();
                PayCreate payCreate = new PayCreate();
                payCreate.setOrderStr(orderStr);
                logger.debug("wechat pay string is " + orderStr);
                mallOrderService.updateMallOrder(order);
                return ResponseUtil.createNormalJson(payCreate, "创建微信订单信息成功");
            } else {
                //创建失败
                return ResponseUtil.createBussniessErrorJson(0, "创建微信订单信息失败");
            }
        }*/
    }

    @RequestMapping(value = "refund", method = RequestMethod.POST)
    @ResponseBody
    public String refund(@Param(value = "outTradeNo") String outTradeNo, HttpServletRequest request) {
        if (TextUtils.isEmpty(outTradeNo)) {
            throw new BusinessException("订单号不能为空");
        }
        MallOrder order = mallOrderService.queryMallOrderByOutTradeNo(outTradeNo);
        if (order == null) {
            return ResponseUtil.createBussniessErrorJson(0, "订单不存在");
        }
        int payType = order.getPayOnlineType();//获取在线支付类型
        int buyerId = order.getBuyerId();
        double totalAmount = order.getTotalAmount();
        if (payType == 1 || payType == 2) {
            //首先通过该订单查找相关退款数据-如果没有则创建一条退款数据
            MallOrderRefund mallOrderRefund = mallOrderService.queryMallOrderRefundByOutTradeNo(outTradeNo);
            String ktpRefundNo;//开太平退款单号
            if (mallOrderRefund == null) {
                mallOrderRefund = new MallOrderRefund();
                //生成开太平退款订单号
                ktpRefundNo = StringUtil.getKtpTradeNo(buyerId);//DateUtil.format(new Date(), DateUtil.FORMAT_DATE_TIME_NORMAL) + buyerId + RandomUtil.random(100000, 999999);
                mallOrderRefund.setOutRefundNo(ktpRefundNo);
            } else {
                ktpRefundNo = mallOrderRefund.getOutRefundNo();
            }
            mallOrderRefund.setOutTradeNo(outTradeNo);
            mallOrderRefund.setPayOnlineState(payType);

            if (payType == 1) {
                // 支付宝退款
                totalAmount = AlipayCfg.debug ? 0.01 : totalAmount;
                mallOrderRefund.setRefundAmount(totalAmount);
                AlipayTradeRefundResponse refundResponse = Alipay.refund(outTradeNo, ktpRefundNo, totalAmount);
                if (Alipay.refundSuccess(refundResponse)) {
                    mallOrderRefund.setRefundState(Common.ORDER_REFUND_STATE_APPLY_SUCCESS);
                    mallOrderService.saveOrUpdateMallOrderRefund(mallOrderRefund);
                    return ResponseUtil.createBussniessJson("申请支付宝退款成功");
                } else {
                    mallOrderRefund.setRefundState(Common.ORDER_REFUND_STATE_APPLY_FAIL);
                    mallOrderService.saveOrUpdateMallOrderRefund(mallOrderRefund);
                    return ResponseUtil.createBussniessErrorJson(0, "申请支付宝退款失败");
                }
            } else {
                //微信退款
                totalAmount = WeixinpayCfg.debug ? 1 : totalAmount;
                mallOrderRefund.setRefundAmount(totalAmount);
                Map<String, String> response = Weixinpay.refund(outTradeNo, ktpRefundNo, totalAmount, totalAmount);
                if (Weixinpay.refundSuccess(response)) {
                    mallOrderRefund.setRefundNo(response.get("refund_id"));//创建成功后填入微信退款单号
                    mallOrderRefund.setRefundState(Common.ORDER_REFUND_STATE_APPLY_SUCCESS);
                    mallOrderService.saveOrUpdateMallOrderRefund(mallOrderRefund);
                    return ResponseUtil.createBussniessJson("申请微信退款成功");
                } else {
                    mallOrderRefund.setRefundState(Common.ORDER_REFUND_STATE_APPLY_FAIL);
                    if (response != null) {
                        mallOrderRefund.setRefundMessage(response.get("err_code_des"));
                    }
                    mallOrderService.saveOrUpdateMallOrderRefund(mallOrderRefund);
                    return ResponseUtil.createBussniessErrorJson(0, "申请微信退款失败");
                }
            }
        } else {
            //参数错误
            return ResponseUtil.createBussniessErrorJson(0, "支付类型有误");
        }
    }

    /**
     * 支付宝服务器异步通知页面
     *
     * @param ayncNotify
     * @param request
     * @return
     */
    @RequestMapping(value = "aync_notify", method = RequestMethod.POST)
    @ResponseBody
    public String ayncnotify(AlipayAyncNotify ayncNotify, HttpServletRequest request) {
        logger.info("Alipay aync notify: {}" + ayncNotify);
        // 验证签名
        if (AlipayNotify.verifyRequest(request.getParameterMap())) {
            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

            String outTradeNo = ayncNotify.getOut_trade_no();
            MallOrder order = mallOrderService.queryMallOrderByOutTradeNo(outTradeNo);
            if (order != null) {
                //验证订单的四个重要信息是否匹配
                boolean checkResult = ayncNotify.getOut_trade_no().equals(order.getOutTradeNo()) && ayncNotify.getTotal_amount() == (AlipayCfg.debug ? 0.01 : order.getTotalAmount()) && ayncNotify.getSeller_id().equals("" + AlipayCfg.seller_id) && ayncNotify.getApp_id().equals("" + AlipayCfg.app_id);
                if (checkResult) {
                    if (AlipayStatus.TRADE_FINISHED.equals(ayncNotify.getTrade_status())) {
                        //判断该笔订单是否在商户网站中已经做过处理
                        //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                        //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                        //如果有做过处理，不执行商户的业务程序

                        //注意：
                        //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                        order.setStatus(Common.ORDER_STATE_ALI_TRADE_CLOSE);
                        order.setFinishTime(new Date());
                        mallOrderService.updateMallOrder(order);//更新订单状态
                    } else if (AlipayStatus.TRADE_SUCCESS.equals(ayncNotify.getTrade_status())) {
                        //判断该笔订单是否在商户网站中已经做过处理
                        //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                        //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                        //如果有做过处理，不执行商户的业务程序


                        //注意：
                        //付款完成后，支付宝系统发送该交易状态通知
                        if (ayncNotify.getRefund_fee() != null && ayncNotify.getRefund_fee() > 0) {//如果退款金额大于0则为退款消息，非全额退款情况：trade_status= TRADE_SUCCESS，而refund_status=REFUND_SUCCESS
                            fixAlipayRefund(outTradeNo, ayncNotify);
                        } else {
                            if (Common.ORDER_STATE_CREATE == order.getStatus()) {
                                order.setTradeNo(ayncNotify.getTrade_no());
                                order.setStatus(Common.ORDER_STATE_PAY);
                                order.setFinishTime(new Date());
                                //关闭定时任务
                                OrderTask.cancelTask(outTradeNo);
                                mallOrderService.updateMallOrder(order);//更新订单状态
                            }
                        }
                    } else if (AlipayStatus.TRADE_CLOSED.equals(ayncNotify.getTrade_status())) {
                        if (ayncNotify.getRefund_fee() != null && ayncNotify.getRefund_fee() > 0) {//如果退款金额大于0则为退款消息，全额退款情况：trade_status= TRADE_CLOSED，而refund_status=REFUND_SUCCESS
                            fixAlipayRefund(outTradeNo, ayncNotify);
                        } else {
                            order.setStatus(Common.ORDER_STATE_CLOSE);
                            order.setCloseTime(new Date());
                            mallOrderService.updateMallOrder(order);//更新订单状态
                        }
                    }
                    logger.info("Verify aync notify success!");
                    return "success";
                }
                logger.error("该订单信息与开太平订单信息不匹配");
                return "fail";
            } else {
                logger.error("该订单在开太平不存在");
                return "fail";
            }
        } else {
            logger.error("Verify aync notify fail!");
            return "fail";
        }
    }

    /**
     * 微信异步通知页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "weixin_aync_notify", method = RequestMethod.POST)
    @ResponseBody
    public String weixinCallback(HttpServletRequest request) {
        ServletInputStream in = null;
        try {
            in = request.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        WeixinpayAyncNotify notify = XmlUtil.convertoObj(in, WeixinpayAyncNotify.class);
        if (WeixinpayStatus.success.equals(notify.getReturn_code())) {
            if (!SignUtil.sign(notify, WeixinpayCfg.merchant_key).equals(notify.getSign())) {
                //签名错误
                logger.info("签名校验失败");
                return Weixinpay.responseForAnsyNotify("FAIL", "签名校验失败");
            }

            String outTradeNo = notify.getOut_trade_no();
            MallOrder order = mallOrderService.queryMallOrderByOutTradeNo(outTradeNo);
            if (order == null) {
                logger.info("订单不存在");
                return Weixinpay.responseForAnsyNotify("FAIL", "订单不存在");
            }
            if (Common.ORDER_STATE_PAY == order.getStatus() || Common.ORDER_STATE_REFUND == order.getStatus()) {
                logger.info("订单已处理");
                return Weixinpay.responseForAnsyNotify("SUCCESS", "订单已处理");
            }
            double totalAmount = order.getTotalAmount();
            int totalFee = WeixinpayCfg.debug ? 1 : (int) (totalAmount * 100);
            if (notify.getAppid().equals(WeixinpayCfg.app_id) && notify.getMch_id().equals("" + WeixinpayCfg.merchant_id) && notify.getOut_trade_no().equals(outTradeNo) && notify.getTotal_fee() == totalFee) {
                //校验完成开始执行业务逻辑
                String resultCode = notify.getResult_code();
                if (WeixinpayStatus.TRADE_STATE_SUCCESS.equals(resultCode)) {//支付成功
                    order.setStatus(Common.ORDER_STATE_PAY);
                    order.setTradeNo(notify.getTransaction_id());
                    mallOrderService.updateMallOrder(order);
                    //关闭定时任务
                    OrderTask.cancelTask(outTradeNo);
                } else if (WeixinpayStatus.TRADE_STATE_FAIL.equals(resultCode)) {//支付失败
                    //暂不做处理-等于是订单创建成功状态
                }
            }
            logger.info("Verify aync notify success!");
            return Weixinpay.responseForAnsyNotify("SUCCESS", "OK");
        } else {
            return Weixinpay.responseForAnsyNotify(notify.getReturn_code(), notify.getReturn_msg());
        }
    }

    /**
     * 微信退款结果异步通知
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "weixin_refund_aync_notify", method = RequestMethod.POST)
    @ResponseBody
    public String weixinRefundCallback(HttpServletRequest request) {
        ServletInputStream in = null;
        try {
            in = request.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> response = XmlUtil.writeToMap(in);
        String return_code = response.get("return_code");
        if (WeixinpayStatus.success.equals(return_code)) {
            String appid = response.get("appid");
            String req_info = response.get("req_info");
            String mch_id = response.get("mch_id");
            Security.addProvider(new BouncyCastleProvider());
            String xmlStr = AESUtil.decryptData(req_info);
            WeixinpayRefundAyncNotify notify = (WeixinpayRefundAyncNotify) XmlUtil.xmlStrToBean(xmlStr, WeixinpayRefundAyncNotify.class);

            if (notify == null) {
                return Weixinpay.responseForAnsyNotify("FAIL", "退款信息解密失败");
            }

            String outTradeNo = notify.getOut_trade_no();
            MallOrderRefund orderRefund = mallOrderService.queryMallOrderRefundByOutTradeNo(outTradeNo);
            if (orderRefund == null) {
                logger.info("退款订单不存在");
                return Weixinpay.responseForAnsyNotify("FAIL", "退款订单不存在");
            }
            if (Common.ORDER_REFUND_STATE_SUCCESS == orderRefund.getRefundState()) {
                logger.info("退款订单已处理");
                return Weixinpay.responseForAnsyNotify("SUCCESS", "退款订单已处理");
            }
            double refundAmount = orderRefund.getRefundAmount();
            int totalFee = WeixinpayCfg.debug ? 1 : (int) (refundAmount * 100);
            if (WeixinpayCfg.app_id.equals(appid)
                    && ("" + WeixinpayCfg.merchant_id).equals(mch_id)
                    && notify.getOut_trade_no().equals(outTradeNo) //订单号相同
                    && notify.getOut_refund_no().equals(orderRefund.getOutRefundNo())//退款单号相同
                    && notify.getRefund_fee() == totalFee) //退款金额相同
            {
                //校验完成开始执行业务逻辑
                String resultCode = notify.getRefund_status();
                if (WeixinpayStatus.success.equals(resultCode)) {
                    orderRefund.setRefundState(Common.ORDER_REFUND_STATE_SUCCESS);
                    orderRefund.setRefundNo(notify.getRefund_id());
                    mallOrderService.updateMallOrderByOutTradeId(outTradeNo, Common.ORDER_STATE_REFUND);
                } else {
                    orderRefund.setRefundState(Common.ORDER_REFUND_STATE_FAIL);
                }
                mallOrderService.saveOrUpdateMallOrderRefund(orderRefund);
                logger.info("Verify aync notify success!");
                return Weixinpay.responseForAnsyNotify("SUCCESS", "OK");
            }
            logger.info("退款订单信息不匹配");
            return Weixinpay.responseForAnsyNotify("FAIL", "退款订单信息不匹配");
        } else {
            return Weixinpay.responseForAnsyNotify("FAIL", "Verify aync notify fail!");
        }
    }

    /**
     * 保存或更新订单
     */
    private String saveOrUpdateOderInfo(String outTradeNo, double amount, double sendPrice, String mark, int addressId, String customerName, String customerMobile, String customerAddress, String goods, int buyerId) {
        boolean save = TextUtils.isEmpty(outTradeNo);
        MallOrder mallOrder;
        if (save) {//新增
            //生成开太平订单号 规则 日期格式化到毫秒+userId+6位随机数
            String ktpTradeNo = StringUtil.getKtpTradeNo(buyerId);//DateUtil.format(new Date(), DateUtil.FORMAT_DATE_TIME_NORMAL) + buyerId + RandomUtil.random(100000, 999999);
            mallOrder = new MallOrder();
            mallOrder.setOutTradeNo(ktpTradeNo);
        } else {
            mallOrder = mallOrderService.queryMallOrderByOutTradeNo(outTradeNo);
        }
        Date nowDate = new Date();
        long expireTime = nowDate.getTime() + (30 * 60 * 1000);
        mallOrder.setBuyerId(buyerId);
        mallOrder.setCreateTime(nowDate);
        mallOrder.setExpireTime(new Date(expireTime));
        mallOrder.setTotalAmount(amount);
        mallOrder.setStatus(Common.ORDER_STATE_CREATE);
        mallOrder.setSendPrice(sendPrice);
        mallOrder.setRemark(mark);
        mallOrder.setCustomerName(customerName);
        mallOrder.setCustomerMobile(customerMobile);
        mallOrder.setCustomerAddress(customerAddress);

        int queryTotalFee = 0;
        if (!TextUtils.isEmpty(goods)) {
            List<GoodOrderQuery> goodList = GsonUtil.jsonToList(goods, GoodOrderQuery.class);
            List<MallGoodOrder> goodOrderList = new ArrayList<>();
            StringBuilder subjectBuilder = new StringBuilder();
            StringBuilder bodyBuilder = new StringBuilder();
            for (int i = 0; i < goodList.size(); i++) {
                GoodOrderQuery mallGood = goodList.get(i);
                if (mallGood != null) {
                    int goodId = mallGood.getGoodId();
                    double goodPrice = mallGood.getGoodPrice();
                    int goodNum = mallGood.getCount();
                    int specId = mallGood.getGoodSpecId();
                    if (goodId > 0 && goodPrice > 0 && specId > 0 && goodNum > 0) {
                        GoodOrderQuery goodOrderQuery = mallOrderService.queryGoodOrderJoinList(goodId, specId);
                        if (goodOrderQuery != null) {
                            String goodName = goodOrderQuery.getGoodName();
                            if (TextUtils.isEmpty(goodName)) {
                                return ResponseUtil.createBussniessErrorJson(0, "商品名称为空");
                            }
                            if (i == 0) {
                                subjectBuilder.append(goodName);
                                String goodSpecName = goodOrderQuery.getGoodSpecName();//商品属性名称
                                bodyBuilder.append(!TextUtils.isEmpty(goodSpecName) ? goodSpecName : "默认");
                            }
                            double goodQueryPrice = goodOrderQuery.getGoodPrice();
                            int goodQueryFee = ((int) (goodQueryPrice * 100)) * goodNum;
                            queryTotalFee += goodQueryFee;
                            MallGoodOrder goodOrder;
                            if (save) {//新增
                                goodOrder = new MallGoodOrder();
                            } else {
                                goodOrder = mallOrderService.queryGoodOrder(mallOrder.getOutTradeNo(), goodId);
                            }
                            if (goodOrder != null && goodPrice == goodQueryPrice) {
                                goodOrder.setCreateTime(new Date());
                                goodOrder.fixGoodOrder(goodOrderQuery);
                                goodOrder.setGoodNum(goodNum);
                                if (save) {//如果是新增则保存订单号
                                    goodOrder.setOutTradeNo(mallOrder.getOutTradeNo());
                                }
                                goodOrderList.add(goodOrder);
                            } else {
                                return ResponseUtil.createBussniessErrorJson(0, "商品价格不匹配");
                            }
                        } else {
                            return ResponseUtil.createBussniessErrorJson(0, "商品不存在");
                        }
                    }
                } else {
                    return ResponseUtil.createBussniessErrorJson(0, "商品信息不全");
                }
            }
            if (goodList.size() > 1) {
                subjectBuilder.append("等");
                bodyBuilder.append("等");
            }
            int amountFee = (int) (amount * 100);
            int caculateAmountFee = queryTotalFee + (int) (sendPrice * 100);
            if (caculateAmountFee != amountFee) {
                return ResponseUtil.createBussniessErrorJson(0, "商品实时总价格不匹配");
            }
            mallOrder.setSubject(subjectBuilder.toString());
            mallOrder.setBody(bodyBuilder.toString());
            if (save) {
                boolean saveOrder = mallOrderService.saveMallOrder(mallOrder);
                boolean saveGoodOrder = mallOrderService.saveGoodOrder(goodOrderList);
                saveAddress(buyerId, addressId, customerName, customerMobile, customerAddress);//修改或保存地址
                deleteMallCarGood(buyerId, goodList);
                if (saveGoodOrder && saveOrder) {
                    CreateOrder createOrder = new CreateOrder();
                    createOrder.setOutTradeNo(mallOrder.getOutTradeNo());
                    //启动定时任务，30分钟后关闭订单
                    new OrderTask(new OrderTask.Data(buyerId, mallOrder.getOutTradeNo())).start();
                    return ResponseUtil.createNormalJson(createOrder, "创建订单成功");
                } else {
                    return ResponseUtil.createBussniessErrorJson(0, "创建订单失败");
                }
            } else {
                boolean updateOrder = mallOrderService.updateMallOrder(mallOrder);
                boolean updateGoodOrder = mallOrderService.updateMallGoodOrder(goodOrderList);
                saveAddress(buyerId, addressId, customerName, customerMobile, customerAddress);//修改或保存地址
                deleteMallCarGood(buyerId, goodList);
                if (updateGoodOrder && updateOrder) {
                    CreateOrder createOrder = new CreateOrder();
                    createOrder.setOutTradeNo(mallOrder.getOutTradeNo());
                    return ResponseUtil.createNormalJson(createOrder, "更新订单成功");
                } else {
                    return ResponseUtil.createBussniessErrorJson(0, "更新订单失败");
                }
            }
        } else {
            return ResponseUtil.createBussniessErrorJson(0, "商品信息不能为空");
        }
    }

    /**
     * 更新退款信息
     *
     * @param outTradeNo
     * @param ayncNotify
     */
    private void fixAlipayRefund(String outTradeNo, AlipayAyncNotify ayncNotify) {
        mallOrderService.updateMallOrderByOutTradeId(outTradeNo, Common.ORDER_STATE_REFUND);
        MallOrderRefund orderRefund = mallOrderService.queryMallOrderRefundByOutTradeNo(outTradeNo);
        if (orderRefund != null) {
            orderRefund.setRefundState(Common.ORDER_REFUND_STATE_SUCCESS);
            orderRefund.setRefundNo(ayncNotify.getOut_biz_no());
            //更新退款表
            mallOrderService.saveOrUpdateMallOrderRefund(orderRefund);
        }
    }

    private boolean saveAddress(int buyerId, int addressId, String customerName, String customerMobile, String customerAddress) {
        MallGet mallGet = null;
        if (addressId > 0) {
            mallGet = addressService.getMallGetById(addressId);
        }
        if (mallGet == null) {
            mallGet = new MallGet();
        }
        mallGet.setUserId(buyerId);
        mallGet.setName(customerName);
        mallGet.setMobile(customerMobile);
        mallGet.setAddress(customerAddress);
        return addressService.saveOrUpdateMallGet(mallGet);
    }

    /**
     * 删除购物车商品
     *
     * @param userId   用户id
     * @param goodList 商品列表
     */
    private void deleteMallCarGood(int userId, List<GoodOrderQuery> goodList) {
        if (goodList != null && !goodList.isEmpty()) {
            for (GoodOrderQuery goodOrderQuery : goodList) {
                mallOrderService.deleteMallCarGood(userId, goodOrderQuery.getGoodId(), goodOrderQuery.getGoodSpecId());
            }
        }
    }

    @RequestMapping(value = "getMallOrderList", method = RequestMethod.GET)
    @ResponseBody
    public String getMallOrderList(@Param(value = "u_id") int u_id, @Param(value = "state") int state) {
        if (u_id <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        List<MallOrder> mallOrderList = mallOrderService.getOrderList(u_id, state);
        List<OrderDetail> orderList = new ArrayList<>();
        if (mallOrderList != null) {
            for (MallOrder mallOrder : mallOrderList) {
                long expireTime = mallOrder.getExpireTime() != null ? mallOrder.getExpireTime().getTime() : 0;
                long nowTime = new Date().getTime();
                //过期时间大于当前时间则相减否则剩余时间为0
                long leftTime = expireTime > nowTime ? expireTime - nowTime : 0;
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setCreateTime(DateUtil.format(mallOrder.getCreateTime(), DateUtil.FORMAT_DATE_TIME));
                orderDetail.setLeftTime(leftTime);
                orderDetail.setOutTradeNo(mallOrder.getOutTradeNo());
                orderDetail.setCustomerName(mallOrder.getCustomerName());
                orderDetail.setCustomerMobile(mallOrder.getCustomerMobile());
                orderDetail.setCustomerAddress(mallOrder.getCustomerAddress());
                orderDetail.setTotalAmount(mallOrder.getTotalAmount());
                orderDetail.setSendPrice(mallOrder.getSendPrice());
                orderDetail.setStatus(mallOrder.getStatus());
                orderDetail.setRemark(mallOrder.getRemark());
                List<GoodOrderQuery> queryList = mallOrderService.queryMallGoodOrderList(u_id, mallOrder.getOutTradeNo());
                orderDetail.setGoodList(queryList);
                orderList.add(orderDetail);
            }
        }
        return ResponseUtil.createNormalJson(orderList);
    }

    @RequestMapping(value = "getAddressList", method = RequestMethod.GET)
    @ResponseBody
    public String getAddressList(@Param(value = "u_id") int u_id) {
        if (u_id <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        List<MallGet> mallGetList = mallOrderService.queryMallGetList(u_id);
        if (mallGetList == null) {
            mallGetList = new ArrayList<>();
        }
        return ResponseUtil.createNormalJson(mallGetList);
    }

    /**
     * 计算商品运费并返回地址信息
     *
     * @param u_id
     * @param goods
     * @return
     */
    @RequestMapping(value = "calculate", method = RequestMethod.POST)
    @ResponseBody
    public String calculate(@Param(value = "u_id") int u_id, @Param(value = "goods") String goods, @Param(value = "address") String address) {
        if (u_id <= 0 || TextUtils.isEmpty(goods) || TextUtils.isEmpty(goods) || TextUtils.isEmpty(address)) {
            throw new BusinessException("缺少必要参数");
        }
        if (!TextUtils.isEmpty(goods)) {
            List<GoodOrderQuery> goodList = GsonUtil.jsonToList(goods, GoodOrderQuery.class);
            logger.debug("commit goodlist is " + goodList);
        }
        MallCalculate mallCalculate = new MallCalculate();
        mallCalculate.setSendPrice(0);
        return ResponseUtil.createNormalJson(mallCalculate);
    }

    /**
     * 获取订单详情
     *
     * @param u_id
     * @return
     */
    @RequestMapping(value = "getOrderDetail", method = RequestMethod.GET)
    @ResponseBody
    public String getOrderDetail(@Param(value = "u_id") int u_id, @Param(value = "outTradeNo") String outTradeNo) {
        if (u_id <= 0 || TextUtils.isEmpty(outTradeNo)) {
            throw new BusinessException("缺少必要参数");
        }
        MallOrder mallOrder = mallOrderService.queryMallOrderByOutTradeNo(outTradeNo);
        if (mallOrder == null) {
            return ResponseUtil.createBussniessErrorJson(0, "该订单不存在");
        }
        long expireTime = mallOrder.getExpireTime() != null ? mallOrder.getExpireTime().getTime() : 0;
        long nowTime = new Date().getTime();
        //过期时间大于当前时间则相减否则剩余时间为0
        long leftTime = expireTime > nowTime ? expireTime - nowTime : 0;
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCreateTime(DateUtil.format(mallOrder.getCreateTime(), DateUtil.FORMAT_DATE_TIME));
        orderDetail.setLeftTime(leftTime);
        orderDetail.setOutTradeNo(mallOrder.getOutTradeNo());
        orderDetail.setCustomerName(mallOrder.getCustomerName());
        orderDetail.setCustomerMobile(mallOrder.getCustomerMobile());
        orderDetail.setCustomerAddress(mallOrder.getCustomerAddress());
        orderDetail.setTotalAmount(mallOrder.getTotalAmount());
        orderDetail.setSendPrice(mallOrder.getSendPrice());
        orderDetail.setStatus(mallOrder.getStatus());
        orderDetail.setRemark(mallOrder.getRemark());
        List<GoodOrderQuery> queryList = mallOrderService.queryMallGoodOrderList(u_id, outTradeNo);
        orderDetail.setGoodList(queryList);
        return ResponseUtil.createNormalJson(orderDetail);
    }

    @RequestMapping(value = "deleteMallOrder", method = RequestMethod.POST)
    @ResponseBody
    public String deleteMallOrder(@Param(value = "u_id") int u_id, @Param(value = "outTradeNo") String outTradeNo) {
        if (u_id <= 0 || TextUtils.isEmpty(outTradeNo)) {
            throw new BusinessException("缺少必要参数");
        }
        boolean success = mallOrderService.deleteMallOrder(u_id, outTradeNo) == 1;
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    @RequestMapping(value = "cancelMallOrder", method = RequestMethod.POST)
    @ResponseBody
    public String cancelMallOrder(@Param(value = "u_id") int u_id, @Param(value = "outTradeNo") String outTradeNo) {
        if (u_id <= 0 || TextUtils.isEmpty(outTradeNo)) {
            throw new BusinessException("缺少必要参数");
        }
        boolean success = mallOrderService.cancelMallOrder(u_id, outTradeNo) == 1;
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

}
