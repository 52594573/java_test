<%@ page import="com.ktp.project.util.DateUtil" %>
<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: 83896
  Date: 2018/5/8
  Time: 14:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
  <title>登录</title>
  <!-- Bootstrap -->
  <link href="./bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      background: url(./images/bnner.jpg) no-repeat;
      background-size: 1920px 1080px;
      font-size: 16px;
    }
  </style>
</head>
<body>
今天的日期是：<%=DateUtil.format(new Date(), "yyyy年MM月dd日 HH:mm:ss")%>


<script type="text/javascript">checkCookie()</script>
<!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
<script src="./bootstrap/js/jquery-3.3.1.min.js"></script>
<!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
<script src="./bootstrap/js/bootstrap.min.js"></script>
</body>
</html>
