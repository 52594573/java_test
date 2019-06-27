package com.ktp.project.web;

import com.ktp.project.util.Md5Util;
import com.zm.controller.UserOrGroupRegisterToServerController;
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
public class TestUserOrGroupRegisterToServerController {

    @Autowired
    UserOrGroupRegisterToServerController serverController;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(serverController).build();
    }

    @Test
    public void testSaveAddress() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/updateUserInfoInOpenIm")
                .param("userId", "43107")
                .param("userName", "杨晓雪")
                .param("userPic", "/images/pic/2018718121124465196007.jpg")
                .param("mobile", "18575701120")
                .param("key", Md5Util.MD5Encode("43107FAFJJeremf@#$&245")));
        MvcResult mvcResult = resultActions.andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("客户端获得反馈数据:" + result);
    }
}
