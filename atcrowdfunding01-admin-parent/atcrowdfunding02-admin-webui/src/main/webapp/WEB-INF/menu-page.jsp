<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/doc.min.css">
    <link rel="stylesheet" href="ztree/zTreeStyle.css">
    <script src="jquery/jquery-2.1.1.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <script src="layer/layer.js"></script>
    <script src="script/docs.min.js"></script>
    <script src="ztree/jquery.ztree.all-3.5.min.js"></script>
    <script type="text/javascript" src="crowd/my-menu.js"></script>
    <script type="text/javascript">
        $(function () {


            // 1.准备生成树形结构的JSON 数据的来源是发送Ajax请求从数据库中得到
            generateTree();

            $(function () {
                $(".list-group-item").click(function(){
                    if ( $(this).find("ul") ) {
                        $(this).toggleClass("tree-closed");
                        if ( $(this).hasClass("tree-closed") ) {
                            $("ul", this).hide("fast");
                        } else {
                            $("ul", this).show("fast");
                        }
                    }
                });
            });

            $("#treeDemo").on("click", ".addBtn", function () {

                // 找到当前节点的id属性
                window.pid = this.id;
                // 打开模态框
                $("#menuAddModal").modal("show");
                return false;
            });

            // 给保存单击按钮绑定单机响应函数
            $("#menuSaveBtn").click(function () {

                // 收集表单的数据
                var name = $.trim($("#menuAddModal [name = name]").val());
                var url = $.trim($("#menuAddModal [name = url]").val());

                // 定位到被选中的图标
                var icon = $.trim($("#menuAddModal [name = icon]:checked").val());

                // 发送ajax请求
                $.ajax({
                    "url":"menu/save.json",
                    "type":"post",
                    "data":{
                       "pid":window.pid,
                        "name":name,
                        "url":url,
                        "icon":icon
                    },
                    "dataType":"json",
                    "success":function (response) {
                        var result = response.result;

                        if (result == "SUCCESS") {
                            layer.msg("操作成功");

                            // 重新加载数据库 ：确认服务器端已经保存的数据
                            generateTree();
                        }
                        if (result == "FAILED") {
                            layer.msg("操作失败"+" "+response.message);
                        }
                    },
                    "error":function (response) {
                        layer.msg(response.status + " " + response.statusText);
                    }
                });

                // 关闭模态框
                $("#menuAddModal").modal("hide");

                // 重新加载树形结构

                // 系统再提交数据后点一下重置按钮重置一下数据，不传任何参数详相当于用户点击一下

                $("#menuResetBtn").click()
            });

            // 给编辑按钮绑定单击响应函数
            $("#treeDemo").on("click", ".editBtn", function () {

                // 找到当前节点的id属性
                window.id = this.id;
                // 打开模态框
                $("#menuEditModal").modal("show");

                // 获取zTreeObj对象
                var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");

                // 根据Id查询节点对象

                // 用来搜索节点的属性名
                var key = "id";

                // 用来搜索节点的属性值
                var value = this.id;

                var currentNode = zTreeObj.getNodeByParam(key, value);

                // 回显数据
                $("#menuEditModal [name=name]").val(currentNode.name);
                $("#menuEditModal [name=url]").val(currentNode.url);

                // radio回显得本质是吧value属性和currentNode.icon一致的radio选中
                $("#menuEditModal [name=icon]").val([currentNode.icon]);


                return false;
            });

            // 给更新模态框中的更新按钮绑定单击响应函数
            $("#menuEditBtn").click(function () {
                // 取出表单数据
                var name = $("#menuEditModal [name=name]").val();
                var url = $("#menuEditModal [name=url]").val();
                var icon = $("#menuEditModal [name=icon]:checked").val();

                $.ajax({
                    "url":"menu/edit.json",
                    "type":"post",
                    "data":{
                        "id":window.id,
                        "name":name,
                        "url":url,
                        "icon":icon
                    },
                    "dataType":"json",
                    "success":function (response) {
                        var result = response.result;

                        if (result == "SUCCESS") {
                            layer.msg("操作成功");

                            // 重新加载数据库 ：确认服务器端已经保存的数据
                            generateTree();
                        }
                        if (result == "FAILED") {
                            layer.msg("操作失败"+" "+response.message);
                        }
                    },
                    "error":function (response) {
                        layer.msg(response.status + " " + response.statusText);
                    }
                });

                $("#menuEditModal").modal("hide");
            });

            // 给删除按钮绑定单击响应函数
            $("#treeDemo").on("click", ".removeBtn", function () {

                // 找到当前节点的id属性
                window.id = this.id;
                // 打开模态框
                $("#menuConfirmModal").modal("show");

                // 获取zTreeObj对象
                var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");

                // 根据Id查询节点对象

                // 用来搜索节点的属性名
                var key = "id";

                // 用来搜索节点的属性值
                var value = this.id;

                var currentNode = zTreeObj.getNodeByParam(key, value);

                // 回显数据
                $("#removeNodeSpan").html(" <i class='" + currentNode.icon + "'></i>" + currentNode.name+" ");

                return false;
            });
            $("#confirmBtn").click(function () {
                $.ajax({
                    "url":"menu/remove.json",
                    "type":"post",
                    "data":{
                        "id":window.id,
                    },
                    "dataType":"json",
                    "success":function (response) {
                        var result = response.result;

                        if (result == "SUCCESS") {
                            layer.msg("操作成功");

                            // 重新加载数据库 ：确认服务器端已经保存的数据
                            generateTree();
                        }
                        if (result == "FAILED") {
                            layer.msg("操作失败"+" "+response.message);
                        }
                    },
                    "error":function (response) {
                        layer.msg(response.status + " " + response.statusText);
                    }
                });

                $("#menuConfirmModal").modal("hide");
            })

        });
    </script>

    <style type="text/css">
        .tree li {
            list-style-type: none;
            cursor: pointer;
        }
        .tree-closed {
            height : 40px;
        }
        .tree-expanded {
            height : auto;
        }
    </style>
</head>

<body>
<%@include file="include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <div class="tree">
                <ul style="padding-left:0px;" class="list-group">
                    <li class="list-group-item tree-closed" >
                        <a href="main.html"><i class="glyphicon glyphicon-dashboard"></i> 控制面板</a>
                    </li>
                    <li class="list-group-item tree-closed">
                        <span><i class="glyphicon glyphicon glyphicon-tasks"></i> 权限管理 <span class="badge" style="float:right">3</span></span>
                        <ul style="margin-top:10px;display:none;">
                            <li style="height:30px;">
                                <a href="admin/get/page.html"><i class="glyphicon glyphicon-user"></i> 用户维护</a>
                            </li>
                            <li style="height:30px;">
                                <a href="role/to/page.html"><i class="glyphicon glyphicon-king"></i> 角色维护</a>
                            </li>
                            <li style="height:30px;">
                                <a href="menu/to/page.html"><i class="glyphicon glyphicon-lock"></i> 菜单维护</a>
                            </li>
                        </ul>
                    </li>
                    <li class="list-group-item tree-closed">
                        <span><i class="glyphicon glyphicon-ok"></i> 业务审核 <span class="badge" style="float:right">3</span></span>
                        <ul style="margin-top:10px;display:none;">
                            <li style="height:30px;">
                                <a href="auth_cert.html"><i class="glyphicon glyphicon-check"></i> 实名认证审核</a>
                            </li>
                            <li style="height:30px;">
                                <a href="auth_adv.html"><i class="glyphicon glyphicon-check"></i> 广告审核</a>
                            </li>
                            <li style="height:30px;">
                                <a href="auth_project.html"><i class="glyphicon glyphicon-check"></i> 项目审核</a>
                            </li>
                        </ul>
                    </li>
                    <li class="list-group-item tree-closed">
                        <span><i class="glyphicon glyphicon-th-large"></i> 业务管理 <span class="badge" style="float:right">7</span></span>
                        <ul style="margin-top:10px;display:none;">
                            <li style="height:30px;">
                                <a href="cert.html"><i class="glyphicon glyphicon-picture"></i> 资质维护</a>
                            </li>
                            <li style="height:30px;">
                                <a href="type.html"><i class="glyphicon glyphicon-equalizer"></i> 分类管理</a>
                            </li>
                            <li style="height:30px;">
                                <a href="process.html"><i class="glyphicon glyphicon-random"></i> 流程管理</a>
                            </li>
                            <li style="height:30px;">
                                <a href="advertisement.html"><i class="glyphicon glyphicon-hdd"></i> 广告管理</a>
                            </li>
                            <li style="height:30px;">
                                <a href="message.html"><i class="glyphicon glyphicon-comment"></i> 消息模板</a>
                            </li>
                            <li style="height:30px;">
                                <a href="project_type.html"><i class="glyphicon glyphicon-list"></i> 项目分类</a>
                            </li>
                            <li style="height:30px;">
                                <a href="tag.html"><i class="glyphicon glyphicon-tags"></i> 项目标签</a>
                            </li>
                        </ul>
                    </li>
                    <li class="list-group-item tree-closed" >
                        <a href="param.html"><i class="glyphicon glyphicon-list-alt"></i> 参数管理</a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <div class="panel panel-default">
                <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <div class="panel-body">
                    <ul id="treeDemo" class="ztree"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/modal-menu-add.jsp"%>
<%@include file="/WEB-INF/modal-menu-edit.jsp"%>
<%@include file="/WEB-INF/modal-menu-confirm.jsp"%>
</body>
</html>

