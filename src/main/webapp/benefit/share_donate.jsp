<%--
  Created by IntelliJ IDEA.
  User: 83896
  Date: 2018/9/3
  Time: 14:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="referrer" content="no-referrer"/>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0"/>
    <title>公益帮扶</title>
    <link type="text/css" rel="stylesheet" href="../css/main.css"/>
    <link type="text/css" rel="stylesheet" href="../css/hjl_star_style.css"/>
    <script type="text/javascript" src="../js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="../js/server.js"></script>
    <link type="text/css" rel="stylesheet" href="../js/layui/css/layui.mobile.css"/>
    <link type="text/css" rel="stylesheet" href="../js/layer/theme/default/layer.css"/>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <script type="text/javascript" src="../js/layer/layer.js"></script>
    <script type="text/javascript">
        var uid;
        var don_Id;
        var act_Id;

        function getUrlParam(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
            var r = window.location.search.substr(1).match(reg);  //匹配目标参数
            if (r != null) return unescape(r[2]);
            return null; //返回参数值
        }

        function changeTwoDecimal_f(x) {
            var f_x = parseFloat(x);
            if (isNaN(f_x)) {
                return 0;
            }
            var f_x = Math.round(x * 100) / 100;
            var s_x = f_x.toString();
            var pos_decimal = s_x.indexOf('.');
            if (pos_decimal < 0) {
                pos_decimal = s_x.length;
                s_x += '.';
            }
            while (s_x.length <= pos_decimal + 2) {
                s_x += '0';
            }
            return s_x;
        }

        function getCharityData() {
            var url = "h5/recipientShare?actId=" + act_Id + "&donId=" + don_Id;
            $.ajax({
                url: url,
                type: "GET",
                dataType: "text",
                success: function (data) {
                    var returnData = JSON.parse(data);
                    if (returnData.status.code == 10 && returnData.busstatus.code == 100) {
                        //set charity detail
                        //console.log(data);

                        /*EventMain*/
                        var returnData = JSON.parse(data);
                        var EventMain = $(".EventMain");
                        var mainImg = EventMain.find(".eventImg .img");
                        var mainTitle = EventMain.find(".eventImg .content .title");
                        var mainCompany = EventMain.find(".eventImg .content .sub");
                        var mainDetail = EventMain.find(".eventIntro .txtWrap");
                        var mainPeopleCount = EventMain.find(".eventCount .count:eq(0) .num");
                        var mainGetCount = EventMain.find(".eventCount .count:eq(1) .num");
                        var mainlineCount = EventMain.find(".eventCount .count:eq(2) .num");

                        //console.log(returnData.content)
                        if (returnData.content != null && returnData.content.activity != null) {
                            mainImg.html('<img src="' + returnData.content.activity.actPicture + '" />');
                        }
                        mainTitle.html(returnData.content.activity.actTop);
                        mainCompany.html(returnData.content.activity.actSponsor);
                        mainDetail.html(returnData.content.activity.actDetail);
                        mainPeopleCount.html(returnData.content.activity.actRecipientSum);
                        mainGetCount.html(returnData.content.activity.actInventorySum);
                        mainlineCount.html(returnData.content.activity.deadline);


                        //捐赠详细详情
                        var RecipientDetailObj = $(".RecipientDetailObj");

                        //头像
                        var mainHead = RecipientDetailObj.find(".RecipientDetailHead  .img .img");
                        var maintitle = RecipientDetailObj.find(".RecipientDetailHead .RecipientDetailHeadcon .title");
                        var maincontents = RecipientDetailObj.find(".RecipientDetailHead .RecipientDetailHeadcon .contents");
                        var mainrecsucss = RecipientDetailObj.find(".RecipientDetailHead .RecipientDetailHeadcon .recsucss");

                        if (returnData.content.recipientDetail.donPicture != null && returnData.content.recipientDetail.donPicture != "") {
                            mainHead.html('<img style="width: 80px;height: 80px;" src="' + returnData.content.recipientDetail.donPicture.toString().split(",")[0] + '"/>');
                        }
                        maintitle.html(returnData.content.recipientDetail.donName);
                        maincontents.html(returnData.content.recipientDetail.donDescribe);
                        mainrecsucss.html("成功领取" + returnData.content.recipientDetail.recSum + returnData.content.recipientDetail.donUnit);

                        RecipientDetailObj.find(".RecipientDetailCen .imgfor .img").html('<img style="border-radius: 90px;width: 20px;height: 20px;" src="' + returnData.content.recipientDetail.donHead + '"/>');
                        RecipientDetailObj.find(".RecipientDetailCen .imgfor .imgtwo").html('<img style="border-radius: 90px;width: 20px;height: 20px;" src="' + returnData.content.recipientDetail.recHead + '"/>');
                        RecipientDetailObj.find(".RecipientDetailCen .namemane").html(returnData.content.recipientDetail.recName + "确认捐赠" + returnData.content.recipientDetail.recSum
                            + returnData.content.recipientDetail.donUnit + "物品&nbsp;&nbsp;" + returnData.content.recipientDetail.recGetTime);
                        RecipientDetailObj.find(".RecipientDetailaddress .title").html(returnData.content.recipientDetail.recAddress);

                        RecipientDetailObj.find(".RecipientDetailaddress .title").html(returnData.content.recipientDetail.recAddress);

                        RecipientDetailObj.find(".RecipientDetailEvaluateList .evacontent").html(returnData.content.recipientDetail.evaDescribe);


                        var html = "";
                        var ObjData = returnData.content.recipientDetail.evaPicture.toString().split(",");
                        for (var i = 0; i < ObjData.length; i++) {
                            console.log(ObjData[i]);
                            html += '<img style="margin:3px;float:left;margin-left:15px; background: #EEEEEE;width: 73px;height:73px;display:block" src="' + ObjData[i] + '"/>';
                        }
                        $(".RecipientDetailEvaluateList .evaImgList").html(html)

                        countOpen();
                    } else {
                        console.log(data);
                    }
                },
                error: function (data) {
                    console.log(data);
                }
            });
        }

        function daysBetween(sDate1, sDate2) {
            //Date.parse() 解析一个日期时间字符串，并返回1970/1/1 午夜距离该日期时间的毫秒数
            var time1 = Date.parse(sDate1);
            var time2 = Date.parse(sDate2);
            var nDays = Math.abs(parseInt((time2 - time1) / 1000 / 3600 / 24));
            return nDays;
        };
        Date.prototype.format = function (format) {
            /*
            * 使用例子:format="yyyy-MM-dd hh:mm:ss";
            */
            var o = {
                "M+": this.getMonth() + 1, // month
                "d+": this.getDate(), // day
                "h+": this.getHours(), // hour
                "m+": this.getMinutes(), // minute
                "s+": this.getSeconds(), // second
                "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
                "S": this.getMilliseconds()
                // millisecond
            }

            if (/(y+)/.test(format)) {
                format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
                    - RegExp.$1.length));
            }

            for (var k in o) {
                if (new RegExp("(" + k + ")").test(format)) {
                    format = format.replace(RegExp.$1, RegExp.$1.length == 1
                        ? o[k]
                        : ("00" + o[k]).substr(("" + o[k]).length));
                }
            }
            return format;
        }
        var actionType = "";

        function popTelBox() {
            var popup = $(".Popup");
            var popupContent = $(".Popup .contentWrap");
            popup.css("display", "block");
            var mTop = ($(window).height() - popupContent.height()) * 0.5;
            popupContent.css("margin-top", mTop + "px");
            popup.stop().animate({opacity: 1}, 200);
        }

        //记录分享人的分享次数增加
        function countOpen() {
            var url = "h5/share/click?userId=" + uid + "&shareType=3";
            console.log(url);
            $.ajax({
                url: url,
                type: "GET",
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

        //统计提交
        function regTel(_uid, _tel) {
            //加载中的动画
            layer.load(1, {
                shade: [0.1, '#fff'] //0.1透明度的白色背景
            });
            var url = "h5/share/commit?userId=" + _uid + "&shareType=3&commitType=1&commitTel=" + _tel;
            $.ajax({
                url: url,
                type: "GET",
                success: function (data) {
                    window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.ktp.project";
                },
                error: function (data) {
                    window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.ktp.project";
                }
            })
        }

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

        //检测是否为真正的手机号
        function isPhoneNo(phone) {
            var pattern = /^1[34578]\d{9}$/;
            return pattern.test(phone);
        }

        $(function () {

            uid = getUrlParam("uid");
            if (uid == null || isNaN(parseInt(uid))) {
                uid = "1";
            }
            act_Id = getUrlParam("actId");
            if (act_Id == null || isNaN(parseInt(act_Id))) {
                act_Id = "1";
            }
            don_Id = getUrlParam("donId");
            if (don_Id == null || isNaN(parseInt(don_Id))) {
                don_Id = "2";
            }
            getCharityData();
            initRegInfo();
            $(".Popup .btnGroup .reg").on("click touchend", function () {
                var _tel = $(".Popup .inputWrap input").val();
                console.log(_tel);
                if (_tel != null && isPhoneNo(_tel)) {
                    regTel(uid, _tel);
                } else {
                    layer.msg("手机号码格式有误");
                }
                return false;
            })
            $(".EventMain .eventIntro").on("click touchend", function () {
                if ($(this).hasClass("close")) {
                    $(this).removeClass("close");
                    $(this).addClass("open");
                } else {
                    $(this).removeClass("open");
                    $(this).addClass("close");
                }
                return false;
            })
        })

    </script>
</head>
<body style="background: #eee;">
<div class="ContentContainer ListPage Chartity">
    <div class="Session EventMain">
        <div class="eventImg">
            <div class="img"></div>
            <div class="content">
                <div class="title">爱心物品银行</div>
                <div class="sub">主办单位：广东开太平信息科技有限责任公司</div>
            </div>
        </div>
        <div class="eventCount">

            <div class="count">
                <div class="num">66</div>
                <div class="title">受赠人数</div>
            </div>

            <div class="count">
                <div class="num">6</div>
                <div class="title">待领取数量</div>
            </div>
        </div>
        <div class="eventIntro close">
            <div class="txtWrap"></div>
            <a href="#" class="btn"></a>
        </div>
    </div>

    <div class="RecipientDetailObj"
         style="padding-top: 15px;padding-bottom:15px;border-radius: 4px;background: #fff;margin-bottom: 20px;">

        <div class="RecipientDetailHead" style="justify-content:center;align-items:center;display:flex">
            <div class="img"
                 style="display:block;margin-left: 15px;margin-right: 10px;width: 80px;height: 80px">
                <div class="img"
                     style="border-radius: 90px; border: 1px solid #fff;background: #eee;display:block;width: 100%;height: 100%">
                </div>
            </div>
            <div class="RecipientDetailHeadcon" style="flex:1">
                <div class="title" style="flex:1;font-size: 16px;color: #333333;font-weight:bold;">我为工人添衣服</div>
                <div class="contents" style="margin-right:15px;flex:1;font-size: 14px;color: #999999;margin-top: 0px">
                    公益是公共利益事业的简称这是为人民服务的一种通俗讲法指有关社会公众的福祉和
                </div>
                <div class="recsucss" style="flex:1;font-size: 12px;color: #999999;margin-top: 10px">成功领取 3 件</div>
            </div>
        </div>

        <div style="margin:10px;height:1px;display:flex;background: #EEEEEE;"></div>

        <div class="RecipientDetailCen" style="justify-content:center;align-items:center;display:flex">
            <div class="imgfor"
                 style="display:block;margin-left: 15px;margin-right: 10px;justify-content:center;align-items:center;display:flex">
                <div class="img"
                     style="border-radius: 90px; border: 1px solid #fff;background: #eee;display:block;width: 20px;height: 20px">
                </div>
                <div class="imgtwo"
                     style="border-radius: 90px; border: 1px solid #fff;background: #eee;display:block;width: 20px;height: 20px;display: block;">
                </div>
            </div>
            <div class="namemane" style="flex:1;font-size: 12px;color: #CCCCCC;margin-top: 0px">张三确认捐赠 2 件物品
                2018-07-30
            </div>
            <div class="BottomFaceRec" style="text-align: center;
            color: #CCCCCC;font-size: 14px;margin-right: 20px;  padding-top: 2px;padding-bottom: 2px">
                受助成功
            </div>
        </div>

        <div style="margin:10px;height:1px;display:flex;background: #EEEEEE;"></div>

        <div class="RecipientDetailaddress" style="justify-content:center;align-items:center;display:flex">
            <div class="img"
                 style="margin-left: 15px;margin-right: 10px;border-radius: 90px; border: 1px solid #fff;background: #eee;display:block;width: 15px;height: 15px"
            >
                <img style=" display: block; width: 100%;" src="images/icon_ditu.png">
            </div>
            <div class="title" style="flex:1;font-size: 12px;color: #999999;margin-top: 0px">广东省 广州市 越秀区</div>
        </div>

        <div style="margin:10px;height:1px;display:flex;background: #EEEEEE;"></div>

        <div class="RecipientDetailEvaluateList" style="flex: 1">
            <div style="margin-left:15px; line-height: 25px;font-size: 14px;display:flex;color: #333333;">
                评价
            </div>
            <div class="evacontent"
                 style="margin-left:15px;margin-right:15px;margin-top: 7px;margin-bottom: 15px; line-height: 25px;font-size: 14px;display:flex;color: #999999;">
                很感谢您，孩子很高兴，谢谢！
            </div>
            <div class="evaImgList" style="display:inline-block">
                <div class="img" style="width: 100%;display:block;display:inline-block">
                </div>
            </div>
        </div>


    </div>

</div>

<div class="BottomBtn">
    <a href="#">我要领取</a>
</div>

<!--popup-->
<div class="Popup">
    <div class="contentWrap">
        <div class="title">
            <a href="#" class="close">关闭</a>
            <span class="txt">加入开太平</span>
        </div>
        <div class="content">
            <div class="inputWrap">
                <input type="text" placeholder="请输入手机号"/>
            </div>
            <div class="btnGroup">
                <a href="#" class="btn reg">马上注册</a>
            </div>
        </div>
    </div>
</div>
<!--popupEnd-->

</body>
</html>
