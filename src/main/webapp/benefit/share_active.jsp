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
        var u_id;

        //获取前端传递过来的参数 例如actId    donId   uid  recId
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

        function getCharityData(_id) {
            var url = "h5/activityShare?actId=" + _id;
            // var url = "https://sys.ktpis.com/benefit/h5/activityShare?actId=" + _id;
            //http://192.168.1.183:8080/benefit/h5/activityShare?actId=1
            //https://sys.ktpis.com/benefit/h5/activityShare?actId=1
            $.ajax({
                url: url,
                type: "GET",
                dataType: "text",
                success: function (data) {
                    //console.log(data);
                    var returnData = JSON.parse(data);
                    if (returnData.status.code == 10 && returnData.busstatus.code == 100) {
                        //set charity detail
                        //console.log(data);

                        /*EventMain*/
                        // 获取相应的控件
                        var returnData = JSON.parse(data);
                        var EventMain = $(".EventMain");
                        var mainImg = EventMain.find(".eventImg .img");//活动的图片
                        var mainTitle = EventMain.find(".eventImg .content .title");//活动的标题
                        var mainCompany = EventMain.find(".eventImg .content .sub");//活动的主办方
                        var mainDetail = EventMain.find(".eventIntro .txtWrap");//活动的详情内容
                        var mainPeopleCount = EventMain.find(".eventCount .count:eq(0) .num");//受赠人
                        var mainGetCount = EventMain.find(".eventCount .count:eq(1) .num");//待令数
                        var mainDateCount = EventMain.find(".eventCount .count:eq(2) .num");//剩余天数（该页面隐藏）

                        //console.log(returnData.content)
                        //数据展示
                        mainImg.html('<img src="' + returnData.content.activity.actPicture + '" />');
                        mainTitle.html(returnData.content.activity.actTop);
                        mainCompany.html(returnData.content.activity.actSponsor);
                        mainDetail.html(returnData.content.activity.actDetail);
                        mainPeopleCount.html(returnData.content.activity.actRecipientSum);
                        mainGetCount.html(returnData.content.activity.actInventorySum);

                        //时间的计数
                        var endDate = new Date(Date.parse(returnData.content.activity.actETime.replace(/-/g, "/")));
                        var startDate = new Date(Date.parse(returnData.content.activity.actSTime.replace(/-/g, "/")));
                        var nowDate = new Date();
                        var leftDay = daysBetween(nowDate, endDate);
                        mainDateCount.html(leftDay);
                        if (nowDate < startDate) {
                            leftDay = daysBetween(nowDate, startDate);
                            mainDateCount.html(leftDay);
                            EventMain.find(".eventCount .count:eq(2) .title").html("开始倒计时(天)");
                        }
                        /*EventMainEnd*/

                        /*ResultList*/
                        //动态获取控件并展示数据
                        var EventResultList = $(".EventResultList");
                        var resultList = EventResultList.find(".result").clone();
                        EventResultList.find(".result").remove();
                        var resultData = returnData.content.recipients;
                        for (var i = 0; i < resultData.length && i < 5; i++) {
                            var result = resultList.clone();
                            result.css("left", 8 + i * 328 + "px");
                            var Intro = result.find(".ResultIntro");
                            Intro.find(".avatar .img:eq(0)").html('<img src="' + resultData[i].donHead + '" />');
                            Intro.find(".avatar .img:eq(1)").html('<img src="' + resultData[i].recHead + '" />');
                            var recDate = new Date(resultData[i].recDealTime);
                            Intro.find(".intro").html(resultData[i].donName + " 成功捐赠了 " + resultData[i].recActualSum + " 件物品 " + recDate.format("yyyy-MM-dd"));
                            /*<div class="ResultCommentList">
                                <div class="txt"></div>
                                <div class="list">
                                </div>
                            </div>*/
                            var Comment = result.find(".ResultCommentList");
                            var commentTxt = Comment.find(".txt");
                            var commentImgList = Comment.find(".list");
                            commentTxt.html(resultData[i].evaDescribe);
                            if (typeof (resultData[i].evaPicture) != "undefined") {
                                var imgSrcList = resultData[i].evaPicture.split(",");
                                var html = "";
                                for (var l = 0; l < imgSrcList.length; l++) {
                                    html += '<div class="img"><img src="' + imgSrcList[l] + '" /></div>';
                                }
                                commentImgList.html(html);
                            }
                            EventResultList.append(result);
                        }
                        if (resultData.length == 0) {
                            EventResultList.remove();
                        }
                        /*ResultListEnd*/

                        /*<div class="Session EventObj">
                            <div class="img">
                            </div>
                            <div class="detail">
                                <div class="title">我为工人添衣服我为工人添衣服我为工人添衣服我为工人添衣服</div>
                                <div class="info">
                                    <div class="fr">
                                        <span class="txt">免邮</span>
                                        <span class="txt">全新</span>
                                    </div>
                                    <div class="count">剩余 7 件</div>
                                </div>
                            </div>
                        </div>*/

                        var EventObjList = $(".EventObjList .list");
                        var EventObj = EventObjList.find(".EventObj").clone();
                        EventObjList.find(".EventObj").remove();
                        var ObjData = returnData.content.donates;
                        for (var i = 0; i < ObjData.length && i < 5; i++) {
                            var Obj = EventObj.clone();
                            Obj.find(".img").html('<img src="' + ObjData[i].donPicture.split(",")[0] + '"/>');
                            Obj.find(".detail .title").html(ObjData[i].donDescribe);
                            if (typeof (ObjData[i].donPostage) != "undefined") {
                                Obj.find(".detail .info .txt:eq(0)").html(ObjData[i].donPostage.split(",")[0]);
                            }
                            if (ObjData[i].donPercent == 10) {
                                Obj.find(".detail .info .txt:eq(1)").html("全新");
                            }
                            Obj.find(".detail .info .count").html("剩余 " + ObjData[i].donSum + " " + ObjData[i].donUnit);
                            EventObjList.append(Obj);
                        }
                        if (ObjData.length == 0) {
                            EventObjList.remove();
                        }
                        //set charity detail end
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
            var url = "h5/share/click?userId=" + u_id + "&shareType=3";
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
            var charityID = getUrlParam("actId");
            console.log(charityID)
            if (charityID == null || isNaN(parseInt(charityID))) {
                charityID = "1";
            }
            u_id = getUrlParam("uid");
            if (u_id == null || isNaN(parseInt(u_id))) {
                u_id = "1";
            }
            getCharityData(charityID);
            initRegInfo();
            $(".Popup .btnGroup .reg").on("click touchend", function () {
                var _tel = $(".Popup .inputWrap input").val();
                console.log(_tel);
                if (_tel != null && isPhoneNo(_tel)) {
                    regTel(u_id, _tel);
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
<!--ProductDetail-->
<div class="ContentContainer ListPage Chartity">
    <!--EventMain-->
    <div class="Session EventMain">
        <div class="eventImg">
            <div class="img"></div>
            <div class="content">
                <div class="title">爱心物品银行</div>
                <div class="sub">主办单位：广东开太平信息科技有限责任公司</div>
            </div>
        </div>
        <!--eventCount-->
        <div class="eventCount">

            <div class="count">
                <div class="num">66</div>
                <div class="title">受赠人数</div>
            </div>

            <div class="count">
                <div class="num">6</div>
                <div class="title">待领取数量</div>
            </div>

            <!--<div class="count">
                <div class="num">3</div>
                <div class="title">剩余天数</div>
            </div>-->

        </div>
        <!--eventCountEnd-->

        <!--eventIntro-->
        <div class="eventIntro close">
            <div class="txtWrap"></div>
            <a href="#" class="btn"></a>
        </div>
        <!--eventIntorEnd-->

    </div>
    <!--EventMain-->

    <!--EventResultList-->
    <div class="EventResultList">

        <!--result-->
        <div class="Session result">
            <!--ResultIntro-->
            <div class="ResultIntro">
                <div class="avatar">
                    <div class="img"></div>
                    <div class="img"></div>
                </div>
                <!--成功捐赠列表标题-->
                <div class="intro"></div>
                <div class="status">受助成功</div>
            </div>
            <!--ResultIntroEnd-->
            <!--ResultCommentList-->
            <div class="ResultCommentList">
                <div class="txt"></div>
                <div class="list">
                </div>
            </div>
            <!--ResultCommentListEnd-->
        </div>
        <!--resultEnd-->

    </div>
    <!--EventResultList-->


    <!--EventObjList-->
    <div class="EventObjList">

        <div class="list">

            <!--EventObj-->
            <div class="Session EventObj">
                <div class="img">
                </div>
                <div class="detail">
                    <div class="title"></div>
                    <div class="info">
                        <div class="fr">
                            <span class="txt"></span>
                            <span class="txt"></span>
                        </div>
                        <div class="count"></div>
                    </div>
                </div>
            </div>
            <!--EventObjEnd-->

        </div>

        <a href="#" class="more">查看更多</a>

    </div>
    <!--EventObjListEnd-->

</div>
<!--ProductDetailEnd-->

<div class="BottomBtn">
    <a href="#">我要领取</a>
    <span class="space"></span>
    <a href="#">我要捐赠</a>
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
