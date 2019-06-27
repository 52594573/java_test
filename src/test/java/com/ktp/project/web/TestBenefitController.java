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
public class TestBenefitController {

    @Autowired
    BenefitController benefitController;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(benefitController).build();
    }

    @Test
    public void testDonatePub() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/benefit/donateSubmit")
                .param("donActId", "1")
                .param("donPrince", "13")
                .param("donUserId", "3077")
                .param("donSum", "123")
                .param("donUnit", "件")
                .param("donPercent", "5")
                .param("donAddress", "广东省 广州市 天河区")
                .param("donWay", "当面领取,148,2")
                .param("donPostage", "")
                .param("donDescribe", "纯棉T恤，团建后剩下的，9成新，有需要的可以领掉")
                .param("donPicture", "https://images.ktpis.com/images/pic/20180914114207619448659724.jpg")
                .param("donGoodsSort", "服饰"));
        MvcResult mvcResult = resultActions.andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("客户端获得反馈数据:" + result);
    }

    /**
     * 审核捐赠
     */
    @Test
    public void testAuditDonate() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/benefit/auditDonate")
                .param("donId", "109"));
        MvcResult mvcResult = resultActions.andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("客户端获得反馈数据:" + result);
    }
}
