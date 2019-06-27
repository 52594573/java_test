<%--
  Created by IntelliJ IDEA.
  User: 83896
  Date: 2018/8/14
  Time: 14:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="referrer" content="no-referrer"/>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0"/>
    <title>建信开太平</title>
    <link type="text/css" rel="stylesheet" href="css/main.css"/>
    <link type="text/css" rel="stylesheet" href="js/layui/css/layui.mobile.css"/>
    <link type="text/css" rel="stylesheet" href="js/layer/theme/default/layer.css"/>
    <link type="text/css" rel="stylesheet" href="css/hjl_star_style.css"/>
    <script type="text/javascript" src="js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="js/layui/layui.js"></script>
    <script type="text/javascript" src="js/layer/layer.js"></script>
    <script type="text/javascript">
        var uid;
        var proId;
        var organid;

        function getUrlParam(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
            var r = window.location.search.substr(1).match(reg);  //匹配目标参数
            var r2 = window.location.search.substr(1);
            if (r != null) {
                return unescape(r[2]);
            } else if (r2 != null) {
                return unescape(r2);
            } else {
                return null; //返回参数值
            }
        }

        var actionType = "";

        function countOpen() {
            var url = "h5/share/click?userId=" + uid + "&shareType=3";//shareType： 1 商城；2 APP 3为分享邀请
            console.log(url);
            $.ajax({
                url: url,
                type: "GET",
                contentType: "application/json;charset=UTF-8",
                dataType: "text",
                success: function (data) {
                    console.log(data);
                },
                error: function (data) {
                    console.log(data);
                    var returnData = JSON.parse(data);
                    console.log(returnData.status);
                }
            })
        }

        function getName() {
            var url = "benefit/h5/use_and_pro_content?userId=" + uid + "&proId=" + proId;
            $.ajax({
                url: url,
                type: "GET",
                contentType: "application/json;charset=UTF-8",
                dataType: "text",
                success: function (data) {
                    console.log(data);
                    var returnData = JSON.parse(data);
                    console.log(returnData.status.code, returnData.busstatus.code, returnData.content.shareUserName)
                    if (returnData.status.code == "10" && returnData.busstatus.code == "100") {
                        var name = returnData.content.shareUserName;
                        var murl = returnData.content.shareUserPic;
                        var projectName = returnData.content.shareProjectName
                        $(".SharePage .shareContent .contentWrap .txt .headicon").html('<img style="float:left;border-radius: 90px;width:30px;height:30px;"  src="' + murl + '" />')
                        $(".SharePage .shareContent .contentWrap .txt .highlight").html(name);
                        $(".SharePage .shareContent .contentWrap .projectClass .projectName").html(projectName);
                        countOpen();
                    } else {
                        console.log(returnData.status);
                    }
                },
                error: function (data) {
                    console.log(data);
                    var returnData = JSON.parse(data);
                    console.log(returnData.status);
                }
            })
        }

        // function regTel(_uid, _tel) {
        //     var url = "h5/share/commit?userId=" + _uid + "&shareType=3&commitType=1&commitTel=" + _tel;
        //     $.ajax({
        //         url: url,
        //         type: "GET",
        //         contentType: "application/json;charset=UTF-8",
        //         success: function (data) {
        //             console.log(data);
        //             join(_tel);
        //         },
        //         error: function (data) {
        //             console.log("error=" + data);
        //             join(_tel);
        //         }
        //     })
        // }

        function join(_tel) {
            var url = "api/shareAndInvite/joinKtpFamilyByPhone";
            $.ajax({
                    url: url,
                    type: "POST",
                    data: {mobile: _tel, projectId: proId, organId: organid, inviteUserId: uid},
                    contenttype: "application/x-www-form-urlencoded;charset=utf-8",
                    success: function (data) {
                        console.log(data);
                        var returnData = JSON.parse(data);
                        if (returnData.status.code == 10 && returnData.busstatus.code == 100) {
                            var str1 = returnData.busstatus.msg;
                            var str2 = "添加成功";

                            if (str1 == str2) {
                                layer.msg("成功加入");
                            } else {
                                layer.msg(returnData.busstatus.msg);
                            }
                            setTimeout(function () {
                                window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.ktp.project";
                            }, 2000);

                        } else {
                            var str3 = returnData.busstatus.msg;
                            var str4 = "未注册";
                            if (str3 == str4) {
                                layer.msg("成功加入");
                                setTimeout(function () {
                                    createShareCommit(_tel);
                                }, 2000);
                            } else {
                                layer.msg(returnData.busstatus.msg);
                                setTimeout(function () {
                                    window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.ktp.project";
                                }, 2000);
                            }
                        }
                    },
                    error: function (data) {
                        console.log(data);
                    }
                }
            )
        }

        function createShareCommit(_tel) {
            layer.load(1, {
                shade: [0.1, '#fff'] //0.1透明度的白色背景
            });
            var url = "api/shareAndInvite/createShare";
            $.ajax({
                url: url,
                type: "POST",
                data: {userId: uid, commitTel: _tel, srChannel: "3", proId: proId, organid: organid},
                contenttype: "application/x-www-form-urlencoded;charset=utf-8",
                success: function (data) {
                    console.log(data);
                    var returnData = JSON.parse(data);
                    window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.ktp.project";
                },
                error: function (data) {
                    console.log(data);
                    window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.ktp.project";
                }
            })
        }


        function isPhoneNo(phone) {
            var pattern = /^1[3456789]\d{9}$/;
            return pattern.test(phone);
        }

        $(function () {
            uid = getUrlParam("userId");
            proId = getUrlParam("proId");
            organid = getUrlParam("organId");
            getName();
            $(".SharePage .btn a").on("click touchend", function () {
                var _tel = $(".SharePage .input input").val();
                console.log(_tel);
                if (_tel != null && isPhoneNo(_tel)) {
                    //加载中的动画
                    layer.load(1, {
                        shade: [0.1, '#fff'] //0.1透明度的白色背景
                    });
                    join(_tel);
                } else {
                    layer.msg("手机号码格式有误");
                }
                return false;
            })
        })

        $(function () {
            var setBGSize = function () {
                var wrap = $(".SharePage .shareBG");
                var img = $(".SharePage .shareBG img");
                if (img.height() > 0 && img.width() > 0) {
                    if (img.height() < wrap.height()) {
                        img.css("height", "100%");
                        img.css("width", "auto");
                    } else {
                        img.css("width", "100%");
                        img.css("height", "auto");
                    }
                    var ml = (wrap.width() - img.width()) * 0.5 + "px";
                    var mt = (wrap.height() - img.height()) * 0.5 + "px";
                    img.css("margin", mt + " 0 0 " + ml);
                } else {
                    var tempImg = new Image();
                    tempImg.src = img.attr("src");
                    tempImg.onload = function () {
                        setBGSize();
                    }
                }

                var share = $(".SharePage");
                var content = $(".SharePage .shareContent");
                var sl = (share.width() - content.width()) * 0.5 + "px";
                var st = (share.height() - content.height()) * 0.5 + "px";
                content.css("left", sl);
                content.css("top", st);
            }
            setBGSize();
            $(window).on("resize", function () {
                setBGSize();
            })
        });


    </script>
</head>
<body>

<!--ShareDetail-->
<div class="ContentContainer SharePage">

    <div class="shareBG"><img src="images/share_bg.jpg"/></div>

    <div class="shareContent">
        <!--bg-->
        <div class="contentBG"><img src="images/share_content_bg.png"/></div>
        <!--bg End-->

        <div class="contentWrap">
            <div class="slogan"><img src="images/share_slogan.png"/></div>
            <div class="txt">
                <div class="head" style="justify-content:center;align-items:center;display:flex">
                    <div style="margin-left: 10px ;font-size: 15px">你的好友</div>
                    <div class="headicon"></div>
                    <p><span style="float:left;font-size: 16px" class="highlight"></span></p>
                    <div style="margin-left: 10px ;font-size: 15px">邀请你加入</div>
                </div>
                <br/>
                <div class="projectClass">
                    <p><span style="font-size: 20px" class="projectName"></span></p>
                </div>
            </div>


            <div class="input">
                <input type="text"
                       placeholder="请输入手机号码">
            </div>
            <div class="arrow"></div>
            <div class="btn">
                <a href="#">确认加入</a>
            </div>
        </div>

    </div>
</div>


</body>
</html>
