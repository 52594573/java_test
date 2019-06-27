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
        var rec_Id;
        var don_Id;

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
            var url = "h5/donateShare?actId=" + _id + "&donId=" + don_Id + "&recId=" + rec_Id;
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
                        mainImg.html('<img  src="' + returnData.content.activity.actPicture + '" />');
                        mainTitle.html(returnData.content.activity.actTop);
                        mainCompany.html(returnData.content.activity.actSponsor);
                        mainDetail.html(returnData.content.activity.actDetail);
                        mainPeopleCount.html(returnData.content.activity.actRecipientSum);
                        mainGetCount.html(returnData.content.activity.actInventorySum);
                        mainlineCount.html(returnData.content.activity.deadline);


                        //捐赠详细详情
                        var EventObj = $(".DonateObject");

                        //头像
                        var mainHead = EventObj.find(".DonateHead  .img .img");
                        var mainName = EventObj.find(".DonateHeadcon .intro");
                        var mainStatus = EventObj.find(".DonateHeadcon .namemane");
                        var mainIntroduce = EventObj.find(".dondetail");
                        var maindonway = EventObj.find(".BottomFaceRec");
                        var maindocattr = EventObj.find(".BottomDocAttr .docattra");
                        var maindocattr1 = EventObj.find(".BottomDocAttr .docattr");

                        console.log(mainHead)
                        mainHead.html('<img style="border-radius: 90px;width:60px;height:60px;display:block" src="' + returnData.content.donateDetailDto.donHead + '" />');
                        mainName.html(returnData.content.donateDetailDto.donName);
                        mainStatus.html("参加活动" + returnData.content.donateDetailDto.joinSum + "次,成功捐赠" + returnData.content.donateDetailDto.donFinishSum + "次");
                        mainIntroduce.html("&nbsp;&nbsp;&nbsp;&nbsp;" + returnData.content.donateDetailDto.donDescribe);
                        maindonway.html(returnData.content.donateDetailDto.donWay.toString().split(",")[0]);
                        if (returnData.content.donateDetailDto.donPercent == 10) {
                            maindocattr.html("全新");
                        } else {
                            maindocattr.html(returnData.content.donateDetailDto.donPercent + "成新");
                        }

                        maindocattr1.html("原价" + returnData.content.donateDetailDto.donPrince + "元");

                        var html = "";
                        var ObjData = returnData.content.donateDetailDto.donPicture.toString().split(",");
                        for (var i = 0; i < ObjData.length && i < 5; i++) {
                            html += '<img style="width: 100%;display:block" src="' + ObjData[i] + '"/>';
                        }
                        $(".DonateDetailObjList .list").html(html)


                        var RecipientDetailsList = $(".RecipientDetailsList .list");
                        var RecipientDetail = RecipientDetailsList.find(".RecipientDetail").clone();
                        RecipientDetailsList.find(".RecipientDetail").remove();
                        var reObjData = returnData.content.recipientDetails;
                        console.log(reObjData);
                        for (var i = 0; i < reObjData.length; i++) {
                            var ObjR = RecipientDetail.clone();
                            ObjR.find(".recHead .img .img").html('<img style="border-radius: 90px;width:60px;height:60px;display:block"  src="' + reObjData[i].recHead + '"/>');
                            ObjR.find(".recname").html(reObjData[i].recName);
                            if (reObjData[i].recCert.toString() == "2") {
                                ObjR.find(".recCert").html("已认证");
                            } else {
                                ObjR.find(".recCert").html("未认证");
                            }

                            ObjR.find(".time").html(reObjData[i].recTime);
                            ObjR.find(".info .txt").html(reObjData[i].recReason);
                            ObjR.find(".RecipientDetailrecSum").html("申请" + reObjData[i].recSum + reObjData[i].donUnit);
                            ObjR.find(".RecipientDetailrecWay").html(reObjData[i].recWay);
                            var recStatusstr;
                            switch (reObjData[i].recStatus) {
                                case 0:
                                    recStatusstr = "等待同意";
                                    break;

                                case 1:
                                    recStatusstr = "已同意";
                                    break;
                                case 2:
                                    recStatusstr = "已发货";
                                    break;
                                case 3:
                                    recStatusstr = "已确认收货";
                                    break;
                                case 4:
                                    recStatusstr = "已评论";
                                    break;
                            }
                            // recStar
                            // 0:等待同意 1:已同意 2:已发货 3:已确认收货 4:已评论'
                            ObjR.find(".RecipientDetailrecStatus").html(recStatusstr);
                            RecipientDetailsList.append(ObjR);

                            var recrstarr;
                            if (reObjData[i].recStar == 0) {
                                recrstarr = "0%";
                            } else if (reObjData[i].recStar > 0 && reObjData[i].recStar < 1) {
                                recrstarr = "20%";
                            } else if (reObjData[i].recStar > 1 && reObjData[i].recStar < 2) {
                                recrstarr = "20%";
                            } else if (reObjData[i].recStar > 2 && reObjData[i].recStar < 3) {
                                recrstarr = "40%";
                            } else if (reObjData[i].recStar > 3 && reObjData[i].recStar < 4) {
                                recrstarr = "60%";
                            } else if (reObjData[i].recStar > 4 && reObjData[i].recStar < 5) {
                                recrstarr = "80%";
                            } else if (reObjData[i].recStar == 5) {
                                recrstarr = "100%";
                            }
                            ObjR.find(".starbg").html('<div  style="width:  ' + recrstarr + '"/>');
                            RecipientDetailsList.append(ObjR);
                        }
                        if (reObjData.length == 0) {
                            RecipientDetailsList.remove();
                        }


                    } else {
                        console.log(data);
                    }
                },
                error: function (data) {
                    console.log(data);
                }
            });
        }

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
            var charityID = getUrlParam("actId");
            console.log(charityID)
            console.log("actid=" + charityID);
            if (charityID == null || isNaN(parseInt(charityID))) {
                charityID = "1";
            }
            uid = getUrlParam("uid");
            if (uid == null || isNaN(parseInt(uid))) {
                uid = "1";
            }
            don_Id = getUrlParam("donId");
            if (don_Id == null || isNaN(parseInt(don_Id))) {
                don_Id = "2";
            }
            rec_Id = getUrlParam("recId");
            if (rec_Id == null || isNaN(parseInt(rec_Id))) {
                rec_Id = "4";
            }
            console.log("uid=" + uid);
            getCharityData(charityID);
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

            <div class="count">
                <div class="num">6</div>
                <div class="title">剩余天数</div>
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

    <div class="DonateObject"
         style="min-height:300px;padding-top: 15px;border-radius: 4px;background: #fff;margin-bottom: 0px;">

        <div class="DonateHead" style="justify-content:center;align-items:center;display:flex">
            <div class="img" style="display:block;margin-left: 15px;margin-right: 10px;width: 60px;height: 60px">
                <div class="img"
                     style="border-radius: 90px; border: 1px solid #fff;background: #eee;display:block;width: 100%;height: 100%">
                </div>
            </div>
            <div class="DonateHeadcon" style="flex:1">
                <div class="intro" style="flex:1;font-size: 16px;color: #333333;font-weight:bold;">title</div>
                <div class="namemane" style="flex:1;font-size: 12px;color: #999999;margin-top: 10px">content</div>
            </div>
            <div class="BottomFaceRec" style="text-align: center; background: #EBA024;border-radius: 2px;
            color: #FFFFFF;font-size: 12px;margin-right: 20px; width: 54px;height: 16px;padding-top: 2px;padding-bottom: 2px">
                当面
            </div>
        </div>

        <div style="margin:10px;height:1px;display:flex;background: #EEEEEE;"></div>

        <div class="dondetail" style="margin:15px; line-height: 25px;font-size: 15px;display:flex;color: #333333;">
            白色卫衣，只穿过1次，码数不对，想送捐赠给有需要的人，白色卫衣，只穿过1次，码数不对
        </div>

        <div class="BottomDocAttr">
            <a class="docattra">全新~</a>
            <a class="docattr">原价25元</a>
        </div>

        <div class="DonateDetailObjList">
            <div class="list">
                <div class="img" style="background:#FFFFFF;width: 100%;display:block">
                </div>
            </div>
        </div>

    </div>

    <div style="display:flex;flex: 1;justify-content:center;align-items:center;">
        <a style="display:flex;width: 100px;font-size:18px;font-weight:bold;color: #333333;margin: 10px;text-align: center">-
            领取列表 -</a>

    </div>


    <div class="RecipientDetailsList"
         style="padding-top: 20px;padding-left:15px;padding-right:15px;padding-bottom:30px;border-radius: 4px;background: #fff;margin-bottom: 20px;">
        <div class="list">
            <!--EventObj-->
            <div class="RecipientDetail">
                <div style="justify-content:center;align-items:center;display:flex">
                    <a class="RecipientDetailrecSum" style="font-size: 16px;color: #393939;font-weight:bold;">申请2件</a>
                    <div style="margin-left:10px;margin-right:10px;width:1px;height:20px;background: #EEEEEE"></div>
                    <a class="RecipientDetailrecWay" style="flex:1;font-size: 12px;color: #999999;">受助者承担运费</a>
                    <a class="RecipientDetailrecStatus"
                       style="font-size: 16px;color: #FF6E00;font-weight:bold;">成功领取</a>
                </div>


                <div style="margin-top:15px;margin-bottom:15px;height:1px;display:flex;background: #EEEEEE;"></div>

                <div class="recHead" style="justify-content:center;align-items:center;display:flex;margin-bottom: 10px">
                    <div class="img"
                         style="display:block;margin-left: 1px;margin-right: 10px;width: 60px;height: 60px">
                        <div class="img"
                             style="border-radius: 90px; border: 1px solid #fff;background: #eee;display:block;width: 100%;height: 100%">
                        </div>
                    </div>
                    <div class="recHeadcon" style="flex:1">
                        <div style="align-items:center;display:flex;margin-bottom: 10px">
                            <div class="recname"
                                 style="font-size: 16px;color: #333333;">
                                李思思
                            </div>
                            <div class="recCert"
                                 style="padding-left:8px;padding-right:8px;
                                 padding-top:0px;padding-bottom:0px;font-size: 12px;
                                 color: #FF8122;border-radius:2px;border:1px solid #FF8122">
                                已认证
                            </div>
                        </div>

                        <div class="starbg">
                            <div style="width: 50%;"></div>
                        </div>

                    </div>
                    <div class="time"
                         style="text-align: center;color: #999999;font-size: 12px;margin-right: 0px; ">
                        2018.09.21 10:30
                    </div>
                </div>
                <div class="info">
                    <span class="txt" style="line-height: 25px;font-size: 15px;display:flex;color: #999999;">截至2018年6月，我国IPv6地址数量为23555块/32半年增长自2017年11月《推进互联网协议第六版规模部署行动计发布以来，我国运营商已基本具备在网络层面支持IPv6的能力</span>
                </div>
            </div>
            <!--EventObjEnd-->

        </div>
    </div>

</div>
<!--ProductDetailEnd-->

<div class="BottomBtn">
    <%-- <a href="#">我要领取</a>
     <span class="space"></span>--%>
    <a href="#">点我领取</a>
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
