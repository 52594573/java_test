<%--
  Created by IntelliJ IDEA.
  User: 83896
  Date: 2018/9/3
  Time: 14:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<%
    String path = request.getContextPath();//
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<head>
    <meta charset="UTF-8">
    <meta name="referrer" content="no-referrer"/>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0"/>
    <title>建信开太平简介</title>
    <link type="text/css" rel="stylesheet" href="../css/main.css"/>
    <script type="text/javascript" src="../js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="../js/server.js"></script>
    <script type="text/javascript">
        //初始化弹窗
        function initRegInfo() {
            var buyBtn = $(".ProductDetail .btnGroup .buy");
            var cartBtn = $(".ProductDetail .btnGroup .cart");
            $(".BottomBtn a").on("click touchend", function () {
                popTelBox();
                return false;
            })
            $(".EventObjList > .more").on("click touchend", function () {
                popTelBox();
                return false;
            })
            $(".Popup .contentWrap .close,.Popup").on("click touchend", function () {
                $(".Popup").stop().animate({opacity: 0}, 200, function () {
                    $(".Popup").css("display", "none");
                });
                return false
            })
            $(".Popup .contentWrap").on("click touchend", function (e) {
                e.stopPropagation();
            })
        }

        //分享微信

        function fenxWeiXin() {
            alert("分享微信")
        }

        //分享QQ
        function fenxQQ() {
            alert("分享QQ")
        }

        //分享朋友圈
        function fenxPyQuqn() {
            alert("分享朋友圈")
        }

        function link() {
            window.location.href = "https://a.app.qq.com/o/simple.jsp?pkgname=com.ktp.project";
        }

        <%--$(function () {--%>
        <%--//请求视频路径--%>
        <%--var url = "https://sys.ktpis.com/api/linkVideo/h5/videoAddress";--%>
        <%--//var url = "<%=basePath%>"+"api/linkVideo/h5/videoAddress";--%>
        <%--//var url = "http://localhost:8081/api/linkVideo/h5/videoAddress";--%>
        <%--// var url = "https://syscs.ktpis.com/api/linkVideo/h5/videoAddress";--%>

        <%--$.ajax({--%>
        <%--url: url,--%>
        <%--type: "GET",--%>
        <%--success: function (data) {--%>
        <%--var returnData = JSON.parse(data);--%>
        <%--if (returnData.status.code == 10 && returnData.busstatus.code == 100) {--%>
        <%--$("#videoId").attr("src", returnData.content);--%>
        <%--$("#videoId").attr("poster", returnData.content);--%>
        <%--}--%>
        <%--//window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.ktp.project";--%>
        <%--//设置视频地址--%>

        <%--},--%>
        <%--error: function (data) {--%>
        <%--}--%>
        <%--})--%>
        <%--})--%>
    </script>
    <style>
        .ktp-link-foot {
            width: 70px;
            height: 40px;
            font-size: 20px;
            color: white;
            background-color: #035C7A;
            border-radius: 30px;
        }

        .ktp-foot1 {
            border-top: 1px solid lightslategrey;
            position: fixed;
            background-color: white;
            vertical-align: middle;
            height: 30px;
            width: 100%;
            bottom: 0px;
            font-size: 45px;
            margin-top: 550px;
        }

        .blank {

            background-color: white;
            vertical-align: middle;
            height: 180px;
            width: 100%;
            bottom: 0px;
            font-size: 40px;
            margin-top: 100px;
        }

        .ktp-link-logo_video {
            width: 60px;
            height: 60px;
            border-radius: 10px;
        }


    </style>

</head>
<body style="background: #eee;">
<!--ProductDetail-->
<%--<div>
    <div style="justify-content:center;align-items:center;display:flex;width:100%;height:240px;"  >
        <!--  autoplay='autoplay' 如果出现该属性，则视频在就绪后马上播放,controls='controls', 如果出现该属性，则向用户显示控件，比如播放按钮。muted='muted'
         规定视频的音频输出应该被静音
         -->
        <video id="videoId" src="#" style="width:100%;height:100%;justify-content:center;align-items:center;display:flex" controls="controls">
            您的浏览器不支持 video 标签。
        </video>
    </div>
</div>--%>
<div>
    <video id='my-video'   src='https://file.ktpis.com/video_9_20181217182622781_4798.mp4'   webkit-playsinline='true'
           poster="https://file.ktpis.com/video_9_20181217182622781_4798.mp4"
                   playsinline='true' x-webkit-airplay='true' x5-video-player-type='h5'
           x5-video-player-fullscreen='true'  
           controls="controls"
                   x5-video-ignore-metadata='true'   width='100%' height='400px'>
                    <p>当前视频不支持</p>
            
    </video>
    <div style="font-size:20px;font-weight:bold;margin-top: 10px;">内容介绍</div>
    <br/>
    <div style="font-size: 15px; height: 30%;overflow:auto;"><span
            style="height:100%;margin-left: 10px;margin-right: 10px;padding-bottom: 150px;">&nbsp&nbsp广东开太平信息科技有限责任公司（以下简称建信开太平）是一家互联网科技平台公司，创立于2015年，总部位于改革开放的前沿深圳前海。
    <br/>
    &nbsp&nbsp&nbsp&nbsp建信开太平自主开发了全天候人脸识别系统/GPS定位系统/电子围栏/闸机系统等硬件设施，依托移动互联网云技术，建立强大的后台数据中心，结合建筑行业的管理特点，研发出建筑行业考勤管理体系/实名制管理体系/工资管理体系/工人评级体系。该系统和体系的建立集工人端/考勤端APP和企业/政府PC端为一体，解决了常年困扰建筑行业的实名制管理问题和考勤工资问题。
    <br/>
    &nbsp&nbsp&nbsp&nbsp农民工问题是目前中国最重要的社会问题之一，政府出台的一系列政策和措施正在改变农民工的生活和生存状态。企业如何通过商业的手段和政府政策保持一致有效解决问题是全新的课题。建信开太平的运营模式以解决社会问题为导向，以获得少许利润维系和支持企业可持续发展为辅助方式，旨在创建一家新时代中国特色的社会企业。
    <br/>
    &nbsp&nbsp&nbsp&nbsp为了实现以上目标，建信开太平发起并成立企业基金会和社会服务机构，希望通过商业和公益融合的方式有效解决农民工问题。公益模式包括农民工教育培训/农民工互助机制/一对一捐赠/农民工城市驿站的方法全方位帮扶农民工；商业模式包括金融创新和孵化劳务公司，金融创新使用普惠金融解决农民工欠薪问题，孵化劳务公司解决农民工保障问题；同时辅以农民工评级体系的建立来实现建筑工人的标准化体系建立。
    <br/>
    &nbsp&nbsp&nbsp&nbsp愿  景：成为中国最大的农民工互联网帮扶平台
    <br/>
    &nbsp&nbsp&nbsp&nbsp使  命：构建中国建筑农民工美好生态，实现农民工向产业工人转型
    <br/>
    &nbsp&nbsp&nbsp&nbsp价值观：梦想，坚持，担当，利他
    <br/>
    &nbsp&nbsp&nbsp&nbsp口  号：为生民立命，为万世开太平！</span>
    </div>
    <div class="blank"></div>
    <div>
        <table class="ktp-foot1">
            <tr>
                <td>
                    <img src="/images/logo.png" class="ktp-link-logo_video">
                </td>
                <td>
                    <span style="font-size: 20px;font-weight:bold;">建信开太平</span><br>
                    <span style="font-size: 15px;">为6千万建筑工人提供劳务服务</span>
                </td>
                <td>
                    <input style="text-align: center;font-size: 15px;" readonly class="ktp-link-foot" value="打开"
                           onclick="link()">
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>
