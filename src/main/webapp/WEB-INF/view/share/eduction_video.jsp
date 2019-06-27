<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@include file="/commons/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${dto.vName}</title>
</head>
<style>
    body{
        margin:0;
        padding:0;
    }
    .ktp-foot {
        width: 100%;
        text-align: center;
        font-size: 46px;
        color: #fff;
        background: #389CFB;
        padding: 24px 0;
    }
    .tableClass {
        border-radius: 15px;
        font-size: 36px;
        background: #0B7ED8;
        color: #fff;
        padding: 18px 30px;
        display: inline-block;
        margin-left: 25px;
    }
    .learnClass {
        font-size: 38px;
        color:#767676;
        margin-top:21px;
        margin-left: 25px;
    }
    ol, ul {
        list-style: none;
    }
    ul, menu, dir {
        display: block;
        list-style-type: disc;
        -webkit-margin-before: 1em;
        -webkit-margin-after: 1em;
        -webkit-margin-start: 0px;
        -webkit-margin-end: 0px;
        -webkit-padding-start: 40px;
    }
    li {
        display: list-item;
        text-align: -webkit-match-parent;
    }
    .round_icon{
        width: 100px;
        height: 100px;
        border-radius: 50%;
        margin-right:15px;
        margin-left: 25px;
    }

    .cmt-name{
        font-size: 33px;
        color: #065B7A;
    }

    .cmt-time{
        color: #767676;
        font-size: 30px;
    }

    .cmt-txt{
    <%--float:right;--%>
        font-size: 40px;
        color: #070707;
        margin-top: 15px;
        margin-bottom: 17px;
        word-wrap: break-word;
        width: 80%;
        margin-right: 54px;
    }
    .allfloat{
        width: 100%;
        overflow: hidden;
        flex-wrap: nowrap;
        display: inline-flex;
        justify-content: space-between;
        align-items: center;
        margin:27px 0 20px 0;
    }
    .allfloat2{
        width: 100%;
        overflow: hidden;
        flex-wrap: nowrap;
        display:flex;
        margin-top: 30px;
    }
    .allfloat .left-txt{
        font-size:50px;
        font-weight:bold;
        margin-left: 25px;
    }

    .allfloat .right-txt{
        vertical-align: middle;
        font-size:38px;
        margin-right:25px;
        color:#767676;
    }
    .content-txt{
        font-size: 40px;
        color:#767676;
        margin-left:25px;
        margin-right:54px;
        margin-bottom:32px;
    }
    .ktp-link-foot {
        width: 140px;
        height: 90px;
        font-size: 45px;
        color: white;
        background-color: #035C7A;
        border-radius: 30px;
    }
    .ktp-foot1 {
        border-top:1px solid lightslategrey;
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
</style>
<body>
<video  id='my-video'   src='${dto.vUrl}'   webkit-playsinline='true' poster="${dto.pUrl}"
            playsinline='true' x-webkit-airplay='true' x5-video-player-type='h5' x5-video-player-fullscreen='true'  
        controls="controls"  x5-video-ignore-metadata='true'   width='100%' height='auto'>
    <p>当前视频不支持</p>
</video>
<div class="ktp-foot" onclick="link()">打开建信开太平客户端，查看更多视频</div>
<div class="allfloat" >
    <span class="left-txt" >${dto.vName}</span>
    <span class="right-txt" >&nbsp;♡&nbsp;${dto.vLikeNum}&nbsp;</span>
</div>
<c:forEach items="${dto.label}" var="item" varStatus="status">
    <div class="tableClass">${item.name}</div>
</c:forEach>
<div class="learnClass">累计 ${dto.vLearnNum} 学过</div>
<div class="learnClass">[提供者]:${dto.vSupplyName}</div>
<div class="allfloat" >
    <span class="left-txt" >内容介绍</span>
</div>
<div class="content-txt">${dto.vContent}</div>
<hr style="margin: 0 25px;">
<div class="allfloat" >
    <span class="left-txt" >热门评论</span>
    <span class="right-txt" >${dto.vCommentNum} 个人评论</span>
</div>
<c:forEach items="${dto.comments}" var="item" varStatus="status">
    <div class="allfloat2">
        <img src="${item.uUrl}" class="round_icon"  onerror="defaultImg(this,${1})">
        <div style="width:100%">
            <div class="cmt-name">${item.uName}</div>
            <div class="cmt-txt">${item.uComment}</div>
            <div class="cmt-time">${item.uTime}</div>
        </div>
    </div>

    <%--<hr class="cmt-txt">--%>
</c:forEach>
<%--<br>
<div class="dakaiktp" >下载或者打开开太平APP看更多评论</div>--%>
<div class="blank"></div>
<table class="ktp-foot1" >
    <tr>
        <td>
            <img src="/images/logo.png" class="ktp-link-logo">
        </td>
        <td>
            <span style="font-size: 34px;font-weight:bold;">建信开太平</span><br>
            <span style="font-size: 30px;">为6千万建筑工人提供劳务服务</span>
        </td>
        <td>
            <input style="text-align: center;font-size: 28px;" readonly class="ktp-link-foot" value="打开" onclick="link()">
        </td>
    </tr>
</table>
</body>
<script type="text/javascript">
    function link() {
        window.location.href = "https://a.app.qq.com/o/simple.jsp?pkgname=com.ktp.project";
        /*        var u = navigator.userAgent;
                var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
                if (isiOS){
                    alert('ios');
        /!*            window.location.href = "../../..";//打开某手机上的某个app应用
                    setTimeout(function () {
                        window.location.href = "../../..";//如果超时就跳转到app下载页
                    }, 500);*!/
                } else{
                    alert('Android');

                }*/
    }

    function article(url) {
        window.location.href = url;
    }

    function defaultImg(o,sex) {
        if (sex == 1){
            o.src="/images/male.png";
        }else{
            o.src="/images/female.png";
        }
    }

</script>
</html>
