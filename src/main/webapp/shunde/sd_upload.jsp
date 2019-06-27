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


    </script>
    <style>

    </style>

</head>
<body>
<html>
<body>
<form method="POST" enctype="multipart/form-data"
      action="http://183.239.33.103:8888/services/rest/document/upload">
    Select folder: <input type="text" name="folderId" value="4753305"/><br/>
    Select filename: <input type="text" name="filename" /><br/>
    Select file: <input type="file" name="filedata" size="45"/><br/>
    <input type="submit" value="Upload" />
</form>

<form method="GET" enctype="multipart/form-data"
      action="http://183.239.33.103:8888/services/rest/folder/createPath">
    Select folder: <input type="text" name="folderId" value="4753305"/><br/>
    Select path: <input type="text" name="path" /><br/>
    <%--Select file: <input type="file" name="filedata" size="45"/><br/>--%>
    <input type="submit" value="Upload" />
</form>
<%--<br/>--%>
<%--<form method="post" action="http://39.108.131.216:8091/API/People/UpdateEmployeePhotoID?Token=">--%>
    <%--<input type="text" name="Token" value="">--%>
    <%--<input type="text" name="imgBase64" value='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'>--%>
    <%--<input type="text" name="IDCardNum" value="">--%>
    <%--<input type="text" name="PhotoID" value="">--%>
    <%--<input type="submit" value="submit" />--%>
<%--</form>--%>
</body>
</html>

</body>
</html>
