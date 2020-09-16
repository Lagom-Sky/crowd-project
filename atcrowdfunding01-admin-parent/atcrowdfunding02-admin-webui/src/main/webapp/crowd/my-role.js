// 声明专门的函数来在模态框中显示权限分配的的树形结构
function fillAuthTree () {
    // 发送ajax请求i获得auth的数据
    var ajaxReturn = $.ajax({
        "url":"assign/get/all/auth.json",
        "type":"post",
        "dataType":"json",
        "async":false
    })
    // 判断请求是否已经正确响应
    if (ajaxReturn.status != 200) {
        layer.msg("处理请求失败！响应状态码："+ajaxReturn.status);
        return ;
    }

    // 拿到返回数据
    var authList = ajaxReturn.responseJSON.data;

    // 准备对Ztree设置的json对象
    var setting = {

        // 显示checkBox
        "check":{
            "enable":true,
        },

        "data":{
            "simpleData":{
                // 开启自动拼接菜单节点
                "enable":true,

                // 使用categoryId属性关联到父节点，不用默认pid
                "pIdKey":"categoryId"

            },
            "key":{
                // 使用title属性,不用默认的节点名
                "name":"title",
            }
        }
    };

    // 生成树形结构
    $.fn.zTree.init($("#authTreeDemo"), setting, authList);

    // 调用zTreeobj的对象的方法，让节点默认展开
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");

    zTreeObj.expandAll(true);

    // 查询已分配的Auth的id组成的数据List
     ajaxReturn = $.ajax({
        "url":"assign/get/assigned/id.json",
        "type":"post",
        "data":{
            "roleId":window.roleId,
        },
        "dataType":"json",
        "async":false
    });

    // 判断请求是否已经正确响应
    if (ajaxReturn.status != 200) {
        layer.msg("处理请求失败！响应状态码："+ajaxReturn.status);
        return ;
    }
    // 从响应结果中取出数据
    var authIdArray = ajaxReturn.responseJSON.data;


    // 那树形结构中相应的节点打上钩
    for(var i = 0; i < authIdArray.length; i++) {
        var authId = authIdArray[i];

        // ②根据 id 查询树形结构中对应的节点
        var treeNode = zTreeObj.getNodeByParam("id", authId);

        // ③将 treeNode 设置为被勾选
        // checked 设置为 true 表示节点勾选
        var checked = true;

        // checkTypeFlag 设置为 false，表示不“联动”，不联动是为了避免把不该勾选的勾选上
        var checkTypeFlag = false;

        // 执行
        zTreeObj.checkNode(treeNode, checked, checkTypeFlag);
    }
}
// 声明专门的函数显示确认模态框
function showConfirmModel(roleArray) {

    // 代开模态框
    $("#confirmModal").modal("show");

    // 清除旧数据
    $("#roleNameDiv").empty();


    // 全局变量范围去创建一个数组用来存放角色的id
    window.roleIdArray = [];

    // 遍历一下RoleArray数组
    for (var i = 0; i < roleArray.length; i++) {
        var role = roleArray[i];
        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName+"<br/>");
        var roleId = role.roleId;
        // 调用数组对象的push方法存入新元素
        window.roleIdArray.push(roleId);

    }

}

// 执行分页，生成页面效果，任何时候调用这个函数都会重新加载页面
function generatePage() {
    // 1.获取分页数据
    var pageInfo = getPageInfoRemote();

    // 2.填充表格
    fillTableBody(pageInfo);
    // 3.
}

//远成访问服务器程序获取pageInfo数据
function getPageInfoRemote() {
    var  pageInfo = null;
    var ajaxResult = $.ajax({
        "url":"role/get/page/info.json",
        "type":"post",
        "data":{
            "pageNum" : window.pageNum,
            "pageSize" : window.pageSize,
            "keyword" : window.keyword
        },
        "async":false,
        "dataType" : "json",
        "success" : function (response) {
            pageInfo = response.data;
        },
        "error" : function (response) {

        }
    });
   // fillTableBody(pageInfo);
    var statusCode = ajaxResult.status;


    // 如果当前请求失败
    if(statusCode != 200) {
        layer.msg("失败，响应状态码为=" + statusCode + "说明信息=" + ajaxResult.statusText);
        return null;
    }

    // 如果响应状态码为200,说明处理成功，获取pageInfo
    var resultEntity = ajaxResult.responseJSON;

    // 从resultEntity中获取result属性
    var result = resultEntity.result;

    // 判断result是否成功
    if(result == "FAILED"){
        layer.msg(resultEntity.message);
        return null;
    }

    // 确认result为成功后获取pageInfo
    var pageInfo = resultEntity.data;

    // 返回pageInfo
    return  pageInfo;
}


// 填充表格
function fillTableBody(pageInfo) {

    //  清除tbody中旧的数据
    $("#rolePageBody").empty();
    $("#Pagination").empty();
    // 判断pageInfo是否有效
    if (pageInfo == null || pageInfo == undefined || pageInfo.list == null || pageInfo.list.length == 0) {
        $("#rolePageBody").append("<tr><td colspan='4'>抱歉！没有查询到您要的数据。</td></tr>");
        return;
    }

    // 使用pageInfo的list属性去填充tbody
    for (var i = 0; i < pageInfo.list.length; i++) {

        var role = pageInfo.list[i];

        var roleId = role.id;

        var roleName = role.name;

        var numberTd = "<td>" + (i + 1) + "</td>";

        var checkboxTd = "<td><input id='"+roleId+"' class ='itemBox' type='checkbox'></td>";

        var roleNameTd = "<td>" + roleName + "</td>";

        var checkButton = "<button id = '"+roleId+"' type='button' class='btn btn-success btn-xs checkButton'><i class=' glyphicon glyphicon-check'></i></button>";

        var pencilButton = "<button id = '"+roleId+"' type='button'  class='btn btn-primary btn-xs pencilBtn'><i class=' glyphicon glyphicon-pencil'></i></button>";

        var removeButton = "<button id = '"+roleId+"' type='button' class='btn btn-danger btn-xs removeButton'><i class=' glyphicon glyphicon-remove'></i></button>";

        var buttonTd = "<td>" + checkButton + " " + pencilButton + " " + removeButton + "</td>";

        var tr = "<tr>" + numberTd + checkboxTd + roleNameTd + buttonTd + "</tr>";

        $("#rolePageBody").append(tr);
    }
    $("#summaryBox").prop("checked", false);
    generateNavigator(pageInfo);
}
function generateNavigator(pageInfo) {
    // 获得总记录数
    var totalRecord = pageInfo.total;

    // 说声明相关属性
    //申明一个Json对象存储Pagination 要设置的属性
    var properties = {
        num_edge_entries: 1, //边缘页数
        num_display_entries: 4, //主体页数
        callback: paginationCallBack,
        items_per_page:pageInfo.pageSize, //每页显示多少项
        current_page:pageInfo.pageNum -1, //插件内部使用页码下标重0 开始
        prev_text:"上一页",
        next_text:"下一页"
    };

    // 调用pagination（)函数
    $("#Pagination").pagination(totalRecord, properties);

}

// 翻页时的回调函数
function paginationCallBack(page_index, jq) {

    // 修改widow对象的pageNum属性
    window.pageNum  = page_index + 1;

    // 调用分页函数
    generatePage();

    // 取消页码的默认行为
    return false;
}
