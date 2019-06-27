package com.ktp.project.web;

import com.ktp.project.service.realName.AuthRealNameApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 测高明项目接口
 *
 * @author djcken
 * @date 2018/6/11
 */
@Controller
@RequestMapping(value = "api/testGm",produces = {"text/html;charset=UTF-8;", "application/json;"})
public class TestGmController {

    @Resource(name="gmAuthRealNameService")
    private AuthRealNameApi gmAuthRealNameService;
    @Autowired
    private com.ktp.project.service.realName.GmAuthRealNameApi gmAuthRealNameApi;

    /**
     * 地址列表
     * @return
     */
    @RequestMapping(value = "test", method = RequestMethod.POST)
    @ResponseBody
    public void getAddressList() {
        try {

            // gmAuthRealNameApi.getBaseDataDictionary(1);
            //gmAuthRealNameService.synCompanyInfo( "9144078319428766XQ",  "开平住宅建筑工程集团有限公司",  "5894","2017-08-17");
            // gmAuthRealNameService.synWorkinfo(63);
            //gmAuthRealNameService.uploadTeamInfo(63, 702,  "杂工",  "POSAVE");
             //gmAuthRealNameApi.uploadTeamInfo(63, 997,  "创高门窗幕墙",  "POSAVE");
            //gmAuthRealNameService.uploadTeamInfo(63, 702,  "杂工",  "POUPDATE");
             //gmAuthRealNameApi.uploadRosterInfo(63,702,7349);
            //gmAuthRealNameApi.synBuildPoUserJT( 63,  7349,  "",  702);
            gmAuthRealNameService.synWorkerAttendance( 63,  29587);
        }catch (Exception E){
            E.printStackTrace();
        }
    }

}
