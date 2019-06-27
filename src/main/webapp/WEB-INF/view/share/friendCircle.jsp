<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@include file="/commons/taglibs.jsp" %>

<html>
<head>
    <title>建信开太平说说</title>
</head>
<body>

<style>
    ul li {
        list-style-type: none;
    }

    .ktp-header {
        margin-top: 14px;
        vertical-align: middle;
        height: 120px;
        width: 120px;
        border-radius: 50%;
        margin-bottom:15px;
    }

    .ktp-name {
        vertical-align: middle;
        font-size: 45px;
        color: #3b97d7;
        margin-left:10px;
    }

    .ktp-main-h{
        display: flex;
        align-items: baseline;
        justify-content: space-between;
    <%--width: 36%;--%>
        margin-top: 10px;
    }

    .ktp-name1 {
        vertical-align: middle;
        font-size: 40px;
        color: #3b97d7;
    }

    .ktp-img {
        height: 300px;
        width: 300px;
    }

    .ktp-txt{
        margin-top:10px;
        margin-bottom: 10px;
    }

    .ktp-article-img {
        vertical-align: middle;
        margin: 10px 10px 10px 10px;
        height: 150px;
        width: 150px;
    }

    .ktp-article-content {
        font-size: 35px;
        vertical-align: middle;
    }

    .ktp-time {
        font-size: 30px;
        color: #626466;
        font-family: times;

    }

    .ktp-time1 {
    <%--padding: 46px;--%>
        font-size: 30px;
        color: #626466;
        font-family: times;
    <%--float: right;--%>
    }

    .ktp-words{
        border-top: 3px solid #DDDEDF;
        margin-left: 65px;
    }

    .ktp-table-h1{
        font-size: 30px;
        border-bottom: 3px solid #DDDEDF;
        text-align: left;
    }

    .ktp-table-h {
        font-size: 30px;
    <%--border-top: 3px solid #DDDEDF;--%>
        text-align: left;
    }

    tr:first-child .ktp-words {
        border-top: none;
    }

    .ket-table-m{
        border-bottom: 3px solid #DDDEDF;
    }

    .ktp-foot {
        color:white;
        background-color: #389CFB;
        text-align: center;
        vertical-align: middle;
        height: 100px;
        width: 910px;
        border-radius: 10px;
        font-size: 45px;
        margin-top: 100px;
    }

    .ktp-foot1 {
        border-top:1px solid lightslategrey;
        position: fixed;
        background-color: white;
        vertical-align: middle;
        height: 100px;
        width: 910px;
        bottom: 0px;
        font-size: 45px;
        margin-top: 550px;
    }
    .default-margin-test{
        font-size:40px;
    <%--width: 36%;--%>
        word-wrap: break-word;
        margin-top:10px;
    }

    .default-margin {
        margin-top: 10px;
        margin-bottom: 10px;
    }

    .ktp-ioc-g {
        margin-right: 5px;
        border-radius: 5px;
        font-size: 30px;
        color: white;
        background-color: yellowgreen;
    }

    .ktp-ioc-b {
        margin-right: 5px;
        border-radius: 5px;
        font-size: 30px;
        color: white;
        background-color: #3b97d7;
    }

    .ktp-ioc-b1 {
        margin-right: 5px;
        border-radius: 5px;
        font-size: 30px;
        color: white;
        background-color: cornflowerblue;
    }

    .ktp-ioc-f {
        margin-right: 5px;
        border-radius: 5px;
        font-size: 30px;
        color: white;
        background-color:lightcoral;
    }

    .ktp-ioc-h {
        margin-right: 5px;
        border-radius: 5px;
        font-size: 30px;
        color: white;
        background-color:lightslategrey;
    }

    .ktp-ioc-hg {
        margin-right: 5px;
        border-radius: 5px;
        font-size: 30px;
        color: white;
        background-color:chocolate;
    }

    .ktp-link-foot {
        text-align:center;
        width: 160px;
        height: 90px;
        font-size: 35px;
        color: white;
        background-color: #035C7A;
        border-radius: 30px;
    }

    .ktp-link-logo {
        width: 100px;
        height:100px;
        border-radius:10px;
    }

    .ktp-status {
        background-color: #F3F3F5;
        width: 910px;
        height:100px;
        margin-top: 20px;
    }
</style>


<div style="margin-left: -12px">
    <ul>
        <li>
            <c:choose>
                <c:when test="${fn:contains(result.userInfo.u_pic, 'https')}">
                    <img src="${result.userInfo.u_pic}" class="ktp-header"
                         onerror="defaultImg(this,${result.userInfo.u_sex})">
                </c:when>
                <c:otherwise>
                    <img src="https://t.ktpis.com${result.userInfo.u_pic}" class="ktp-header"
                         onerror="defaultImg(this,${result.userInfo.u_sex})">
                </c:otherwise>
            </c:choose>
            <span class="ktp-name">&nbsp;${(result.userInfo.u_nicheng eq null || result.userInfo.u_nicheng eq "") ? '无昵称' : result.userInfo.u_nicheng}</span>
        </li>

        <c:choose>
            <c:when test="${result.circleDel ne 1}">
                <li>
                    <table class="ktp-status">
                        <tr>
                            <td>
                                <span style="font-size:40px;margin-bottom: 10px;">&nbsp;该内容已删除</span>
                            </td>
                        </tr>
                    </table>
                </li>
            </c:when>
            <c:when test="${result.status ne 1}">
                <li>
                    <table class="ktp-status">
                        <tr>
                            <td>
                                <span style="font-size:40px;margin-bottom: 10px;">&nbsp;该内容审核中</span>
                            </td>
                        </tr>
                    </table>
                </li>
            </c:when>
            <c:otherwise>
                <li class="ktp-txt">
                    <span style="font-size:40px;">&nbsp;${result.content}</span>
                </li>

                <%--招工--%>
                <c:if test="${shareType eq 1}">
                    <li>
                        <c:choose>
                            <c:when test="${result.workType eq '班组'}">
                                <span class="ktp-ioc-b1">&nbsp;${result.workType}&nbsp;</span>
                            </c:when>
                            <c:otherwise>
                                <span class="ktp-ioc-g">&nbsp;${result.workType}&nbsp;</span>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${result.works eq '木工'}">
                                <span class="ktp-ioc-g">&nbsp;木工&nbsp;</span>
                            </c:when>
                            <c:when test="${result.works eq '钢筋工'}">
                                <span class="ktp-ioc-h">&nbsp;${result.works}&nbsp;</span>
                            </c:when>
                            <c:when test="${result.works eq '架子工'}">
                                <span class="ktp-ioc-b">&nbsp;${result.works}&nbsp;</span>
                            </c:when>
                            <c:when test="${result.works eq '混凝土工'}">
                                <span class="ktp-ioc-hg">&nbsp;${result.works}&nbsp;</span>
                            </c:when>
                            <c:when test="${result.works eq '装饰装修工'}">
                                <span class="ktp-ioc-f">&nbsp;${result.works}&nbsp;</span>
                            </c:when>
                            <c:otherwise>
                                <span class="ktp-ioc-g">&nbsp;其他&nbsp;</span>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:if>

                <%--找工--%>
                <c:if test="${shareType eq 2}">
                    <li>
                        <c:choose>
                            <c:when test="${result.works eq '木工'}">
                                <span class="ktp-ioc-g">&nbsp;木工&nbsp;</span>
                            </c:when>
                            <c:when test="${result.works eq '钢筋工'}">
                                <span class="ktp-ioc-h">&nbsp;${result.works}&nbsp;</span>
                            </c:when>
                            <c:when test="${result.works eq '架子工'}">
                                <span class="ktp-ioc-b">&nbsp;${result.works}&nbsp;</span>
                            </c:when>
                            <c:when test="${result.works eq '混凝土工'}">
                                <span class="ktp-ioc-hg">&nbsp;${result.works}&nbsp;</span>
                            </c:when>
                            <c:when test="${result.works eq '装饰装修工'}">
                                <span class="ktp-ioc-f">&nbsp;${result.works}&nbsp;</span>
                            </c:when>
                            <c:otherwise>
                                <span class="ktp-ioc-g">&nbsp;其他&nbsp;</span>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:if>

                <%--说说--%>
                <c:if test="${shareType eq 3}">
                    <c:if test="${result.picList.size() > 0}">
                        <c:forEach items="${result.picList}" var="item" varStatus="status">
                            <c:choose>
                                <%--图片--%>
                                <c:when test="${item.type eq 0}">
                                    <c:choose>
                                        <c:when test="${status.index eq 0}">
                                            <li>
                                            <img src="${item.picUrl}" class="ktp-img">
                                        </c:when>
                                        <c:when test="${status.index%3 eq 2}">
                                            <img src="${item.picUrl}" class="ktp-img">
                                            </li>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${item.picUrl}" class="ktp-img">
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <%--视频--%>
                                <c:otherwise>
                                    <li>
                                    <video src="${item.videoUrl}" controls="controls"
                                           style="width: 900px;height: auto"/>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${result.picList.size()%3 ne 0}">
                            </li>
                        </c:if>
                    </c:if>
                </c:if>

                <%--网络文章--%>
                <c:if test="${shareType eq 4}">
                    <li onclick="article(this.title)" title="${result.url}">
                        <table style="background-color:#E9EEF1;width:910px">
                            <tr>
                                <td>
                                    <img src="${result.picUrl}" class="ktp-article-img">
                                </td>
                                <td>
                                    <span class="ktp-article-content"> ${result.summary}</span>
                                </td>
                            </tr>
                        </table>
                    </li>
                </c:if>

                <li class="default-margin">
                    <span class="ktp-time">${result.createDate}</span>
                </li>

                <li>
                    <table style="background-color:#F3F3F5;width:910px">
                        <c:if test="${result.likeList.size() > 0}">
                            <thead>
                            <tr>
                                <td class="ktp-table-h1">
                                    <b style="vertical-align: middle;font-size: 45px;">&nbsp;♡</b>
                                    <c:forEach items="${result.likeList}" var="item" varStatus="status">
                                        <span class="ktp-name1">&nbsp;${item.userInfo.u_nicheng}</span>
                                        <c:if test="${result.likeList.size() gt status.index + 1}">
                                            <span style="font-size: 45px;">,</span>
                                        </c:if>
                                    </c:forEach>
                                </td>
                            </tr>
                            </thead>
                        </c:if>

                        <c:if test="${result.commentList.size() > 0}">
                            <tbody>
                            <c:forEach items="${result.commentList}" var="item" varStatus="status">
                                <tr>
                                    <td class="ktp-table-h">
                                        <div class="ktp-words">
                                            <div style="display: flex;">
                                                <c:choose>
                                                    <c:when test="${fn:contains(item.fromUserInfo.u_pic, 'https')}">
                                                        <img src="${item.fromUserInfo.u_pic}" class="ktp-header"
                                                             onerror="defaultImg(this,${item.fromUserInfo.u_sex})">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="https://t.ktpis.com${item.fromUserInfo.u_pic}"
                                                             class="ktp-header"
                                                             onerror="defaultImg(this,${item.fromUserInfo.u_sex})">
                                                    </c:otherwise>
                                                </c:choose>
                                                <div style="width: 100%; margin-left:15px;">
                                                    <div class="ktp-main-h">
                                                        <span class="ktp-name1">${item.fromUserInfo.u_nicheng}</span>
                                                        <span class="ktp-time1">&nbsp;<fmt:formatDate value="${item.createTime}" pattern="YYYY-MM-dd HH:mm:ss"/></span>
                                                    </div>
                                                    <div class="default-margin-test">
                                                            ${item.comment}
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </c:if>
                    </table>
                </li>

                <c:choose>
                    <c:when test="${result.commentList.size() > 0}">
                        <li>
                            <input value="打开建信开太平客户端，查看更多评论" class="ktp-foot" readonly onclick="link()"/>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li>
                            <input value="打开建信开太平客户端，查看原文" class="ktp-foot" readonly onclick="link()"/>
                        </li>
                    </c:otherwise>
                </c:choose>

            </c:otherwise>
        </c:choose>

        <li style="height: 300px;"></li>

        <li>
            <table class="ktp-foot1" style="width:910px">
                <tr>
                    <td>
                        <img src="/images/logo.png" class="ktp-link-logo">
                    </td>
                    <td>
                        <span style="font-size: 48px;">建信开太平</span><br>
                        <span style="font-size: 30px;color:silver">为6千万建筑工人提供劳务服务</span>
                    </td>
                    <td>
                        <input readonly class="ktp-link-foot" value="打开" onclick="link()">
                    </td>
                </tr>
            </table>
        </li>
    </ul>

</div>


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

    function defaultImg(o, sex) {
        if (sex == 1) {
            o.src = "/images/male.png";
        } else {
            o.src = "/images/female.png";
        }
    }


    function article(url) {
        window.location.href = url;
    }

</script>

</html>
