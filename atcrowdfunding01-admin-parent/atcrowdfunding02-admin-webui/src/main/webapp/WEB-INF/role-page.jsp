<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 2020/7/28
  Time: 16:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="GB18030">
<head>
    <meta charset="GB18030">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="ztree/zTreeStyle.css">
    <script src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="jquery/jquery.pagination.js"></script>
    <link rel="stylesheet" href="css/pagination.css">
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <script src="script/docs.min.js"></script>
    <script src="crowd/my-role.js"></script>
    <script src="layer/layer.js"></script>
    <script src="ztree/jquery.ztree.all-3.5.min.js"></script>
    <style>
        .tree li {
            list-style-type: none;
            cursor: pointer;
        }

        table tbody tr:nth-child(odd) {
            background: #F4F4F4;
        }

        table tbody td:nth-child(even) {
            color: #C00;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $(".list-group-item").click(function () {
                if ($(this).find("ul")) {
                    $(this).toggleClass("tree-closed");
                    if ($(this).hasClass("tree-closed")) {
                        $("ul", this).hide("fast");
                    } else {
                        $("ul", this).show("fast");
                    }
                }
            });
        });

        $("tbody .btn-success").click(function () {
            window.location.href = "assignPermission.html";
        });
    </script>
    <script>
        $(function () {

            // 为分页准备数据
            window.pageNum = 1;
            window.pageSize = 5;
            window.keyword = "";
            generatePage();

            // 给查询按钮绑定函数
            $("#searchBtn").click(function () {
                // 获取关键字
                window.keyword = $("#keywordInput").val();

                // 调用分页函数刷新页面
                generatePage();
            });

            // 4.点击新增按钮打开模态框
            $("#showAddModalBtn").click(function(){
                $("#addModal").modal("show");
            });

            // 5.给新增模态框中的保存按钮绑定单击响应函数
            $("#saveRoleBtn").click(function(){
                // ①获取用户在文本框中输入的角色名称
                // #addModal 表示找到整个模态框
                // 空格表示在后代元素中继续查找
                // [name=roleName]表示匹配 name 属性等于 roleName 的元素
                var roleName = $.trim($("#addModal [name=roleName]").val());
                // ②发送 Ajax 请求
                $.ajax({
                    "url": "role/save.json",
                    "type":"post",
                    "data": { "name": roleName
                        },
                    "dataType": "json",
                    "success":function(response){
                        var result = response.result;
                        if(result == "SUCCESS") {
                            layer.msg("操作成功！");

                            // 将页码定位到最后一页
                            window.pageNum = 99999999;

                            // 重新加载分页数据
                            generatePage();
                        }
                        if(result == "FAILED") {
                            layer.msg("操作失败！"+response.message);
                        }
                    },"error":function(response){
                        layer.msg(response.status+" "+response.statusText);
                    }
                });
                // 关闭模态框
                $("#addModal").modal("hide");

                // 清理模态框
                $("#addModal [name=roleName]").val("");
            });
            // 6.给页面上的”铅笔“按钮绑定单击响应函数，目的是打开模态框,这种绑定方式只对第一页有效
            // 点击下一页无法生效
            // $(".pencilBtn").click(function () {
            //
            // })

            // 使用jquery的on函数可以解决这个问题
            // 首先找到所有的动态生成的元素附着的”静态的元素“
            // **第一个参数是事件类型 。2：是要绑定的元素 3： 是要做出回应的方法
            $("#rolePageBody").on("click", ".pencilBtn", function () {
                $("#editModal").modal("show");

                // 获取表格当前行的角色名称
                var roleName = $(this).parent().prev().text();

                // 获取一下当前点击元素要使用的的Id
                window.roleId = this.id;

                // 使用roleName的只设置模态框的文本框
                $("#editModal [name=roleName]").val(roleName);
            });

            // 7.给更新模态框的按钮绑定单击响应函数
            $("#updateRoleBtn").click(function () {

                // 1.从文本框中获取名称
                var roleName = $("#editModal [name=roleName]").val();

                // 发送Ajax请求执行更新
                $.ajax({
                    "url" : "role/update.json",
                    "type" : "post",
                    "data":{
                        "id":window.roleId,
                        "name":roleName,
                    },
                    "dataType":"json",
                    "success":function (response) {
                        var result = response.result;
                        if(result == "SUCCESS") {
                            layer.msg("操作成功！");

                            // 重新加载分页数据
                            generatePage();
                        }
                        if(result == "FAILED") {
                            layer.msg("操作失败！"+response.message);
                        }
                    },
                    "error":function () {
                        layer.msg(response.status+" "+response.statusText);
                    }
                })
                // 关闭模块框
                $("#editModal").modal("hide");
            });

            // 8.点击确认模态框中的确认删除按钮执行删除
            $("#removeRoleBtn").click(function () {

                // 重全局范围去获取到角色id的数组，转化为JSON的在字符串
                var requestBody = JSON.stringify(window.roleIdArray);

                $.ajax({
                    "url": "role/remove/by/role/id/array.json",
                    "type": "POST",
                    "data": requestBody,
                    "contentType": "application/json;charset=UTF-8",
                    "dataType":"json",
                    "success":function (response) {
                        var result = response.result;
                        if(result == "SUCCESS") {
                            layer.msg("操作成功！");

                            // 重新加载分页数据

                            generatePage();
                        }
                        if(result == "FAILED") {
                            layer.msg("操作失败！"+response.message);
                        }
                    },
                    "error":function () {
                        layer.msg(response.status+" "+response.statusText);
                    }

                });

                // 关闭模态框
                $("#confirmModal").modal("hide");
            });

            $("#rolePageBody").on("click", ".removeButton", function () {

                // 从当前按钮出发去获取角色的名称
                var roleName = $(this).parent().prev().text();
                // 创建role对象
                var roleArray =[{
                    roleId:this.id,
                    roleName:roleName
                }]

                // 打开模态框
                showConfirmModel(roleArray)

                // 获取表格当前行的角色名称
                var roleName = $(this).parent().prev().text();

                // 获取一下当前点击元素要使用的的Id
                window.roleId = this.id;

                // 使用roleName的只设置模态框的文本框
                $("#editModal [name=roleName]").val(roleName);
            });

            // 给总的chekBox绑定单机响应函数
            $("#summaryBox").click(function () {

                // 获取当前多选框自己的状态
                var currentStatus = this.checked;

                // 用当前多选框的状态去定位其他单选框
                $(".itemBox").prop("checked", currentStatus);
            })

            // 11.全选与全不选的反向操作
            $("#rolePageBody").on("click", ".itemBox",function () {

                // 获取一下当前已经选中的.itemBox的数量
                var checkedBosCount = $(".itemBox:checked").length;


                // 获取全部.ItemBox的数量
                var totalBoxCount = $(".itemBox").length

                // 使用二者的比较结果来黄色至总的checkedBox
                $("#summaryBox").prop("checked", checkedBosCount == totalBoxCount);
            })

            // 12给批量删除按钮绑定单击响应函数
            $("#batchRemoveBtn").click(function () {

                // 创建一个数组对象用来存放选中的数据
                var roleArray = [] ;

                $(".itemBox:checked").each(function () {

                    // 使用this引用当前的遍历得到的单选框
                    var roleId = this.id;

                    // 通过DOM获得角色的名称
                    var roleName = $(this).parent().next().text();

                    roleArray.push({
                        "roleId" : roleId,
                        "roleName" : roleName
                    });

                    // showConfirmModel()
                })
                // 检查roleArray 的长度
                if(roleArray.length == 0){
                    layer.msg("请至少选择一个元素！来进行执行删除")
                    return ;
                }

                showConfirmModel(roleArray);

            });
            $("#rolePageBody").on("click", ".checkButton", function () {

                // 把当前角色的id存入全局变量
                window.roleId = this.id;

                $("#assignModal").modal("show");

                // 在模态框中装载树Auth的形状结构
                fillAuthTree();
            });

            $("#assignBtn").click(function () {

                // 收集一下树形结构的各个被勾选节点
                // 声明一个数组去存储勾选的id
                var authIdArray = [];
                // 获得ztreeObj对象
                var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");

                // 获取全被勾选的节点
                var checkedNodes = zTreeObj.getCheckedNodes(true);

                for (var i = 0; i < checkedNodes.length; i++) {

                    var checkedNode = checkedNodes[i];

                    var authId = checkedNode.id;

                    authIdArray.push(authId);
                }

                var responsaeBody = {
                    "authIdArray":authIdArray,
                    "roleId":[window.roleId]
                }
                responsaeBody = JSON.stringify(responsaeBody);
                $.ajax({
                    "url":"assign/do/role/assign/auth.json",
                    "type":"post",
                    "data":responsaeBody,
                    "contentType":"application/json;charset=UTF-8",
                    "dataType":"json",
                    "success":function (response) {
                        var result = response.result;
                        if(result == "SUCCESS") {
                            layer.msg("操作成功！");
                        }
                        if(result == "FAILED") {
                            layer.msg("操作失败！"+response.message);
                        }
                    },
                    "error":function (response) {
                        layer.msg(response.status+" ___"+response.statusText);
                    }
                });
                $("#assignModal").modal("hide");

            });
        });
    </script>
</head>

<body>
<%@include file="include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id ="searchBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button id ="batchRemoveBtn" type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button id="showAddModalBtn" type="button" class="btn btn-primary" style="float:right;"><i class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id ="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody"></tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/modal-role-add.jsp" %>
<%@include file="/WEB-INF/modal-role-edit.jsp" %>
<%@include file="/WEB-INF/modal-role-confirm.jsp"%>
<%@include file="/WEB-INF/modal-role-assign-auth.jsp"%>
</body>
</html>

