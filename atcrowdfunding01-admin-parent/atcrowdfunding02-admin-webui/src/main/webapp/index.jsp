<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 2020/7/24
  Time: 10:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <script type="text/javascript" src="jquery/jquery.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#btn1").click(function () {

                $.ajax({
                    traditional: true,
                    "url": "send/array.html", //请求目标地址
                    "type": "post",           //请求方式
                    "data": {
                        "array": [2, 3, 2]
                    },                      //要发送的请求参数
                    "dataType": "text",       //如何对待服务器段返回的数据
                    "success": function (response) {
                        alert(response)
                    },
                    "error": function (response) {
                        alert(response)
                    }
                });
            })
            $("#btn2").click(function () {
                var StudentInfo = {
                    "name": "hello",
                    "age": "18"
                };
                var requestBody = JSON.stringify(StudentInfo);
                $.ajax({
                    traditional: true,
                    "url": "send/student.html", //请求目标地址
                    "type": "post",           //请求方式
                    "data": requestBody,                      //要发送的请求参数
                    "dataType": "text",       //如何对待服务器段返回的数据
                    "contentType": "application/json;charset=UTF-8",
                    "success": function (response) {
                        console.log(response)
                    },
                    "error": function (response) {
                        console.log(response)
                    }
                });

            })
            $("#btn3").click(function () {
                var StudentInfo = {
                    "name": "hello",
                    "age": "18"
                };
                var requestBody = JSON.stringify(StudentInfo);
                $.ajax({
                    traditional: true,
                    "url": "send/ResultEntity.json", //请求目标地址
                    "type": "post",           //请求方式
                    "data": requestBody,                      //要发送的请求参数
                    "dataType": "json",       //如何对待服务器段返回的数据
                    "contentType": "application/json;charset=UTF-8",
                    "success": function (response) {
                        console.log(response)
                    },
                    "error": function (response) {
                        console.log(response)
                    }
                });

            })
            $("#btn4").click(function () {
                layer.msg("这是layer的弹窗");
            })
    })
    </script>
</head>
<body>
    <brn/>
    <a href="test/ssm.html"> 测试ssm整合环境</a>
    <br/>
    <button id="btn1">Send [1,2,3] One </button>
    <br/>
    <button id="btn2">测试封装对象studentInfo</button>
    <br/>
    <button id="btn3">测试封装对象ResultEntity</button>
    <br/>
    <button id="btn4">点击按钮弹出提示框</button>

</body>
</html>
