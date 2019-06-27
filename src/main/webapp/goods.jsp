<%--
  Created by IntelliJ IDEA.
  User: 83896
  Date: 2018/6/9
  Time: 12:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="referrer" content="no-referrer"/>
    <title>商品列表</title>
    <script type="text/javascript" src="js/jquery-1.9.1.js"></script>
    <script type="text/javascript">
        $(function () {
            var tbody = window.document.getElementById("tbody-result");

            $.ajax({
                type: "GET",
                dataType: "text",
                url: "api/mall/good/getMallGoodList",
                success: function (data) {
                    console.log(data);
                    var returnData = JSON.parse(data);
                    if (returnData.status.code == 10 && returnData.busstatus.code == 100) {
                        productList = returnData.content;
                        var str = "";
                        for (var i = 0; i < productList.length; i++) {
                            str += "<tr>" +
                                "<td><img src=" + productList[i].goodPrePic + " width='50' height='50'></td>" +
                                "<td>" + productList[i].goodName + "</td>" +
                                "<td>" + productList[i].goodOriginPrice + "</td>" +
                                "<td>" + productList[i].goodPrice + "</td>" +
                                "</tr>";
                        }
                        tbody.innerHTML = str;
                    }
                },
                error: function () {
                    alert("查询失败")
                }
            });
        });
    </script>
</head>

<body>
<div>
    <div>
        <button id="button-add">新增商品</button>
    </div>
    <div>
        <table id="table-result">
            <thead>
            <tr>
                <th>商品</th>
                <th>商品名称</th>
                <th>商品原价</th>
                <th>商品现价</th>
            </tr>
            </thead>
            <tbody id="tbody-result">
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
