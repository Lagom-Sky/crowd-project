// 生成的树形结构的函数
function generateTree() {
    $.ajax({
        "url":"menu/get/whole/tree.json",
        "type":"post",
        "dataType":"json",
        "success":function (response) {
            var result = response.result;
            if(result == "SUCCESS"){

                // 1.创建JSON对象用于存储对zTree所做的设置
                var setting = {
                    "view":{
                        "addDiyDom":myAddDiyDom,
                        "addHoverDom":myAddHoverDom,
                        "removeHoverDom":myRemoveHoverDom
                    },
                    "data":{
                        "key":{
                            "url":"you sha b"
                        }
                    }
                };

                // 2.响应体中获取用来生成树形结构的JSON数据
                var zNodes = response.data;

                // 3.初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }
            if(result =="FAILED"){
                layer.msg(response.message);
            }
        }
    })
}

// 在鼠标移入节点范围是去添加按钮组
function myAddHoverDom(treeId,treeNode) {


    // 按钮组的标签结构 ：<span><a><i>
    // 按钮组的出现的位置某节点中tree_Demo_n_a超链接的后面

    // 为了在移除按钮组的时候能够找到他，需要给他添加id
    var btnGroupId = treeNode.tId + "_btnGrp";


    // 判断一下以前是否已经是否添加了按钮组
    if ($("#" + btnGroupId).length > 0) {
        return ;
    }

    // 准备三个按钮的HTML标签
    var addBtn = "<a id='"+treeNode.id+"' class='btn btn-info dropdown-toggle btn-xs addBtn' onclick='showAddModal(this)' style='margin-left:10px;padding-top:0px;' href='#' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var removeBtn = "<a id='"+treeNode.id+"' class='btn btn-info dropdown-toggle btn-xs removeBtn' style='margin-left:10px;padding-top:0px;' href='#' title='删除节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    var editBtn = "<a id='" + treeNode.id + "' class='btn btn-info dropdown-toggle btn-xs editBtn' style='margin-left:10px;padding-top:0px;' href='#' title='修改权限信息'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";


    // 声明变量去存储拼接好的按钮的代码
    var btnHTML = "";
    // 获得当前节点的级别数据
    var level = treeNode.level;

    // 等于零说明是根节点

    if(level == 0){
        btnHTML = addBtn;
    }
    if (level == 1) {
        btnHTML = addBtn +" "+ editBtn;

        // 获得当前节点的子节点的数量
        var length = treeNode.children.length;

        if (length == 0) {
            btnHTML = btnHTML +" " + removeBtn;
        }

    }
    if (level == 2) {
        btnHTML =  editBtn +" "+ removeBtn;
    }

    var anchorId = treeNode.tId + "_a";

    // 执行在超链接后面附加span元素
    $("#"+anchorId).after("<span id ='"+btnGroupId+"'>"+btnHTML+"</span>");

}

// 在鼠标移出节点是删除按钮组
function myRemoveHoverDom(treeId,treeNode) {


    // 找到id
    var btnGroupId = treeNode.tId + "_btnGrp";

    // 执行移除
    $("#" + btnGroupId).remove();
}

function myAddDiyDom(treeId,treeNode) {

    // 根据控制图标的span标签的id找到这个span标签

    // zTree生成id的规则
    // 例子：treeDemo_7_ico
    // 解析：ul标签的id_当前节点的序号_功能
    // 提示：ul标签的id_当前节点的序号，部分可以通过访问TreeNode的tId属性得到
    var spanId = treeNode.tId + "_ico";

    // 根据控制图标的span标签的id找到这个span标签
    $("#"+spanId).removeClass().addClass(treeNode.icon);

    // 删除旧的CLASS

    // 添加新的class

    //
}