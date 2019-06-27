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
    <script type="text/javascript" src="js/jquery-1.9.1.js"></script>
    <link type="text/css" rel="stylesheet" href="js/layui/css/layui.mobile.css"/>
    <link type="text/css" rel="stylesheet" href="js/layer/theme/default/layer.css"/>
    <script type="text/javascript" src="js/layui/layui.js"></script>
    <script type="text/javascript" src="js/layer/layer.js"></script>
    <script type="text/javascript">
        var bank_id;

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


        // function countOpen() {
        //     var url = "h5/share/click?userId=" + 0 + "&shareType=4";//shareType： 1 商城；2 APP 3邀请好友 4记录支行
        //     console.log(url);
        //     $.ajax({
        //         url: url,
        //         type: "GET",
        //         dataType: "text",
        //         success: function (data) {
        //             console.log(data);
        //         },
        //         error: function (data) {
        //             console.log(data);
        //             var returnData = JSON.parse(data);
        //             console.log(returnData.status);
        //         }
        //     })
        // }


        function regTel(_tel) {
            var url = "h5/share/commitbybankid?bank_id=" + bank_id + "&shareType=4&commitType=-1&commitTel=" + _tel;
            $.ajax({
                url: url,
                type: "GET",
                success: function (data) {
                    window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.ktp.project";
                },
                error: function (data) {
                }
            })
        }

        function isPhoneNo(phone) {
            var pattern = /^1[3456789]\d{9}$/;
            return pattern.test(phone);
        }

        $(function () {
            bank_id = getUrlParam("bank_id");
            $(".SharePage .btn a").on("click touchend", function () {
                var _tel = $(".SharePage .input input").val();
                console.log(_tel);
                if (_tel != null && isPhoneNo(_tel)) {
                    //加载中的动画
                    layer.load(1, {
                        shade: [0.1, '#fff'] //0.1透明度的白色背景
                    });
                    regTel(_tel);
                } else {
                    layer.msg("手机号码格式有误");
                    // alert("");
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
                <%--<p>您的好友<span class="highlight"></span></p>--%>
                <p>建信开太平建筑劳务管理服务平台</p>
                <p>一起来脱离贫苦、享受城市文明的成果吧～</p>
            </div>

            <div class="input">
                <input type="text" placeholder="请输入手机号码">
            </div>
            <div class="arrow"></div>
            <div class="btn">
                <a href="#">立即加入建信开太平</a>
            </div>
        </div>

    </div>
</div>
<!--ShareDetailEnd-->
</body>
</html>
