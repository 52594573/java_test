<%--
  Created by IntelliJ IDEA.
  User: 83896
  Date: 2018/6/7
  Time: 16:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="referrer" content="no-referrer"/>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0"/>
    <title>开太平网上商城</title>
    <link type="text/css" rel="stylesheet" href="css/main.css"/>
    <script type="text/javascript" src="js/jquery-1.9.1.js"></script>
    <script type="text/javascript">
        var uid;

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

        function getProductData(_id) {
            var url = "h5/mall/good/getMallGoodDetail?goodId=" + _id;
            var _data = '{"status":{"code":10,"msg":"成功"},"busstatus":{"code":100,"msg":"成功"},"content":{"goodId":2,"sortId":0,"goodName":"儿童篮球架室内可升降投篮框筐宝宝家用落地式小男孩玩具户外球类","goodSpecId":0,"goodPic":"https://images.ktpis.com/mall_good_toy1.jpg,https://images.ktpis.com/mall_good_toy2.jpg,https://images.ktpis.com/mall_good_toy3.jpg,https://images.ktpis.com/mall_good_toy4.jpg,https://images.ktpis.com/mall_good_toy5.jpg","goodAdPic":"https://images.ktpis.com/mall_good_toy_title.jpg","goodPrePic":"https://images.ktpis.com/mall_good_toy_preview.jpg","goodOriginPrice":0.0,"goodPrice":0.0,"goodSpecList":[{"goodId":2,"originPrice":53.9,"price":99.0,"specId":7,"spec":"蓝色"},{"goodId":2,"originPrice":53.9,"price":49.0,"specId":8,"spec":"粉色"}],"count":0,"buyCount":0}}';
            $.ajax({
                url: url,
                type: "GET",
                dataType: "text",
                success: function (data) {
                    //console.log(data);
                    var returnData = JSON.parse(data);
                    if (returnData.status.code == 10 && returnData.busstatus.code == 100) {
                        product = returnData.content;
                        //set product detail
                        $(".ProductDetail .productTitle").html(product.goodName);
                        parmList = product.goodSpecList;
                        var lPrice = 0;
                        var tPrice = 0;
                        for (var i = 0; i < parmList.length; i++) {
                            if (lPrice == 0) {
                                lPrice = parmList[i].price;
                            } else {
                                if (lPrice > parmList[i].price) {
                                    lPrice = parmList[i].price;
                                }
                            }
                            if (tPrice == 0) {
                                tPrice = parmList[i].price;
                            } else {
                                if (tPrice < parmList[i].price) {
                                    tPrice = parmList[i].price;
                                }
                            }
                            parmHtml = '<a href="#" class="parm" dPrice="' + changeTwoDecimal_f(parmList[i].originPrice) + '" price="' + changeTwoDecimal_f(parmList[i].price) + '">' + parmList[i].spec + '</a>';
                            $(".ProductDetail .parmSelecter").append($(parmHtml));
                        }
                        var defaultPriceStr = changeTwoDecimal_f(lPrice) + " - " + changeTwoDecimal_f(tPrice);
                        if (lPrice == tPrice) {
                            defaultPriceStr = changeTwoDecimal_f(lPrice);
                            $(".ProductDetail .price .default").html($(".ProductDetail .parmSelecter .parm:eq(0)").attr("dPrice"));
                        }
                        $(".ProductDetail .price .now").html(defaultPriceStr);
                        $(".ProductDetail .parmSelecter").append($('<div class="CB"><div>'));
                        $(".ProductDetail .parmSelecter .parm").each(function (index, parm) {
                            $(parm).on("click touchend", function () {
                                $(".ProductDetail .price .now").html($(parm).attr("price"));
                                $(".ProductDetail .price .default").html($(parm).attr("dPrice"));
                                $(".ProductDetail .parmSelecter .parm").removeClass("hover");
                                $(parm).addClass("hover");
                                return false;
                            })
                        });
                        $(".ProductDetail .price .sold").html("已售" + product.buyCount + "件");

                        var imgList = product.goodPic.split(",");
                        for (var i = 0; i < imgList.length; i++) {
                            var img = $('<p><img src="' + imgList[i] + '" /></p>');
                            $(".ProductDetail .detailWarp .detailWarpContent").append(img);
                        }
                        //set product detail end
                        var ctrlShowed = true;
                        $(".ProductDetail .detailWarp").scroll(function () {
                            var ProductDetail = $(".ProductDetail");
                            var DetailWrap = $(".ProductDetail .detailWarp");
                            var Ctrl = $(".ProductDetail .productCtrl");
                            var CtrlWrap = $(".ProductDetail .productCtrl .contentWarp");
                            var CtrlHideWrap = $(".ProductDetail .productCtrl .contentWarp .detailContent");
                            if (DetailWrap.scrollTop() > 0 && ctrlShowed) {
                                ctrlShowed = false;
                                var height = CtrlWrap.height();
                                var decHeight = CtrlHideWrap.outerHeight();
                                console.log(height + " / " + decHeight);
                                CtrlWrap.height(height + "px");
                                //console.log(height - decHeight);
                                CtrlWrap.stop().animate({height: height - decHeight + 8 + "px"}, 200);
                                CtrlHideWrap.css("display", "none");
                                var wHeight = CtrlWrap.outerHeight(true) - decHeight + 8 + "px";
                                Ctrl.stop().animate({height: wHeight}, 200);
                            } else if (DetailWrap.scrollTop() <= 0 && !ctrlShowed) {
                                ctrlShowed = true;
                                CtrlHideWrap.css("display", "block");
                                var height = CtrlWrap.height();
                                var decHeight = CtrlHideWrap.outerHeight();
                                console.log(height + " / " + decHeight);
                                CtrlWrap.stop().animate({height: height + decHeight - 8 + "px"}, 200, function () {
                                    CtrlWrap.css("height", "auto");
                                });
                                var wHeight = CtrlWrap.outerHeight(true) + decHeight - 8 + "px"
                                Ctrl.stop().animate({height: wHeight}, 200, function () {
                                    Ctrl.css("height", "auto");
                                });
                            }
                        })
                        $(".ProductDetail").css("display", "block");
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

        var actionType = "";

        function popTelBox(_actionType) {
            actionType = _actionType;
            var popup = $(".Popup");
            var popupContent = $(".Popup .contentWrap");
            popup.css("display", "block");
            var mTop = ($(window).height() - popupContent.height()) * 0.4;
            popupContent.css("margin-top", mTop + "px");
            popup.stop().animate({opacity: 1}, 200);
        }

        function initNumCtrl(_ctrl) {
            var ctrl = _ctrl
            var addBtn = ctrl.find(".add");
            var decBtn = ctrl.find(".dec");
            var input = ctrl.find("input");
            addBtn.on("click touchend", function () {
                var ctrlNum = parseInt(input.val());
                ctrlNum++;
                if (ctrlNum < 1) {
                    ctrlNum = 1;
                    decBtn.addClass("disable");
                } else {
                    decBtn.removeClass("disable");
                }
                input.val(ctrlNum);
                return false;
            })
            decBtn.on("click touchend", function () {
                var ctrlNum = parseInt(input.val());
                ctrlNum--;
                if (ctrlNum <= 1) {
                    ctrlNum = 1;
                    decBtn.addClass("disable");
                }
                input.val(ctrlNum);
                return false;
            })
            input.change(function () {
                var ctrlNum = parseInt(input.val());
                ctrlNum++;
                if (ctrlNum < 1 || isNaN(ctrlNum)) {
                    ctrlNum = 1;
                    decBtn.addClass("disable");
                }
                input.val(ctrlNum);
            })
        }

        function countOpen() {
            var url = "h5/share/click?userId=" + uid + "&shareType=1";
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

        function regTel(_uid, _tel, _type) {
            var channel = 4;
            var ua = navigator.userAgent.toLowerCase();//获取判断用的对象
            if (ua.match(/MicroMessenger/i) == "micromessenger") {
                //在微信中打开
                channel = 1;
            }
            if (ua.match(/WeiBo/i) == "weibo") {
                //在新浪微博客户端打开
                channel = 3;
            }
            if (ua.match(/QQ/i) == "qq") {
                //在QQ空间打开
                channel = 2;
            }
            var url = "h5/share/commit?userId=" + _uid + "&shareType=1&commitType=" + _type + "&commitTel=" + _tel + "&channel=" + channel;
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

        function initRegInfo() {
            var buyBtn = $(".ProductDetail .btnGroup .buy");
            var cartBtn = $(".ProductDetail .btnGroup .cart");
            buyBtn.on("click touchend", function () {
                popTelBox(2);
                return false;
            })
            cartBtn.on("click touchend", function () {
                popTelBox(1);
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

        function isPhoneNo(phone) {
            var pattern = /^1[34578]\d{9}$/;
            return pattern.test(phone);
        }

        $(function () {
            var productID = getUrlParam("id");
            if (productID == null) {
                productID = "1";
            }
            uid = getUrlParam("uid");
            getProductData(productID);
            initNumCtrl($(".ProductDetail .numCtrl"));
            initRegInfo();
            $(".Popup .btnGroup .reg").on("click touchend", function () {
                var _tel = $(".Popup .inputWrap input").val();
                console.log(_tel);
                if (_tel != null && isPhoneNo(_tel)) {
                    regTel(uid, _tel, actionType);
                } else {
                    alert("请输入手机号");
                }
                return false;
            })
            var sy = -1;
            var ny = -1;
            $(window).on("touchmove", function (e) {
                if ($(".ProductDetail .detailWarp").scrollTop() >= $(".ProductDetail .detailWarp .detailWarpContent").height() - $(".ProductDetail .detailWarp").height()) {
                    if (sy == -1) {
                        sy = e.originalEvent.targetTouches[0].pageY;
                    }
                    ny = e.originalEvent.targetTouches[0].pageY;
                    var bottom = (ny - sy) / 300 * $(".productCtrl").height();
                    console.log(sy, ny, bottom);
                    $(".productCtrl").css("bottom", bottom + "px");
                } else {
                    sy = -1;
                    ny = -1;
                    $(".productCtrl").css("bottom", 0);
                }
            })
            $(window).on("touchend", function () {
                sy = -1;
                ny = -1;
                if (Number($(".productCtrl").css("bottom")) != 0) {
                    $(".productCtrl").animate({"bottom": 0}, 200);
                }
            })
        })

    </script>
</head>

<body>
<!--ProductDetail-->
<div class="ContentContainer ProductDetail" style="display:none">
    <div class="detailWarp">
        <div class="detailWarpContent"></div>
    </div>
    <div class="productCtrl">
        <div class="bgShadow"></div>
        <div class="contentWarp">
            <div class="price">
                <span class="now"></span>
                <span class="default"></span>
                <span class="sold"></span>
            </div>
            <div class="detailContent">
                <div class="productTitle"></div>
                <div class="parmGroup">
                    <div class="title">选择规格</div>
                    <div class="parmSelecter">
                    </div>
                </div>
                <div class="numCtrl">
                    <div class="title">购买数量</div>
                    <div class="numSelecter">
                        <a href="#" class="btn dec disable"></a>
                        <input type="input" placeholder="" value="1"/>
                        <a href="#" class="btn add"></a>
                    </div>
                    <div class="CB"></div>
                </div>
            </div>
            <div class="btnGroup">
                <div class="wrap">
                    <a href="#" class="btn cart">加入购物车</a>
                    <a href="#" class="btn buy highLight">立即购买</a>
                </div>
            </div>
        </div>
    </div>
</div>
<!--ProductDetailEnd-->

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
