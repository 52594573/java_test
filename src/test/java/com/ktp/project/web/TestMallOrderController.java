package com.ktp.project.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring/spring-*.xml"})
public class TestMallOrderController {

    @Autowired
    MallOrderController orderController;
    private MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void testCreateOrder() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/mall/order/create")
                .param("buyerId", "2053")
                .param("outTradeNo", "")
                .param("goods", "1#0.01#1,2#0.01#1,3#0.01#1")
                .param("customerName", "杜先生")
                .param("customerMobile", "13690805806")
                .param("customerAddress", "麓湖御景A栋802")
                .param("amount", "0.01")
                .param("sendPrice", "1"));
        MvcResult mvcResult = resultActions.andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("客户端获得反馈数据:" + result);
    }

    @Test
    public void testUpdateOrder() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/mall/order/create")
                .param("buyerId", "2053")
                .param("outTradeNo", "201805231138580692053257906")
                .param("goods", "1#0.01#4,2#0.01#5,3#0.01#6")
                .param("customerName", "杜先生1")
                .param("customerMobile", "13690805806")
                .param("customerAddress", "麓湖御景A栋804")
                .param("amount", "0.01")
                .param("sendPrice", "1"));
        MvcResult mvcResult = resultActions.andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("客户端获得反馈数据:" + result);
    }

    @Test
    public void testPay() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/mall/order/pay")
                .param("payType", "2")
                .param("amount", "0.01")
                .param("outTradeNo", "201805191613470282053187318"));
        MvcResult mvcResult = resultActions.andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("客户端获得反馈数据:" + result);
    }

    @Test
    public void testRefund() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/mall/order/refund")
                .param("outTradeNo", "18070919205975189550"));
        MvcResult mvcResult = resultActions.andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("客户端获得反馈数据:" + result);
    }

    @Test
    public void testTask() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/mall/order/testTask"));
        MvcResult mvcResult = resultActions.andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("客户端获得反馈数据:" + result);
    }

    @Test
    public void cacelTask() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/mall/order/cacelTask"));
        MvcResult mvcResult = resultActions.andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("客户端获得反馈数据:" + result);
    }
}
