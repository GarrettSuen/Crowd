// 修改默认节点图标
function addMyDiyDom(treeId , treeNode) {
    // 1.获取图标span标签的id
    var spanId = "#" + treeNode.tId + "_ico";
    // 2. 链式操作移除旧span标签的class属性，替换为数据中的icon属性
    $(spanId).removeClass().addClass(treeNode.icon);
}

// 鼠标悬浮节点显示相关功能按钮
function addMyHoverDom(treeId , treeNode) {
    // 为生成的Span按钮组设置有规律的id
    var btnGroupId = treeNode.tId + "_btnGrp";

    // 判断是否已经生成按钮组span标签，防止Hover多次触发
    if ($("#"+btnGroupId).length > 0){
        return;
    }
    // 找到按钮组的前一个元素
    var anchorId = treeNode.tId + "_a";

    // 准备按钮标签
    // 添加按钮
    var addBtn = "<a id='"+treeNode.id+"' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='添加新子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";

    // 删除按钮
    var removeBtn = "<a id='"+treeNode.id+"' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='删除当前节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";

    // 修改按钮
    var editBtn = "<a id='"+treeNode.id+"' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='修改当前节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";

    // 按照level级别：添加不同的按钮组
    var level = treeNode.level;
    var btnGrp = "";
    // level 0：根节点----按钮：添加
    if(level == 0){
        // 添加按钮组
        btnGrp = addBtn;
    }

    // level 1：分支节点---按钮：修改、删除（拥有叶子节点的除外）、添加
    if (level == 1){
        btnGrp = editBtn + addBtn;
        // 如果当前分支节点拥有叶子节点,则不具备删除功能
        var childrenLength = treeNode.children.length;
        if (childrenLength == 0 ){
            btnGrp = btnGrp + removeBtn;
        }
    }

    // level 2：叶子节点---按钮：修改、删除
    if (level == 2){
        btnGrp = editBtn + removeBtn;
    }
    // 添加按钮组
    $("#"+anchorId).after("<span id='"+ btnGroupId +"'>"+ btnGrp +"</span>");
}

// 鼠标移开时移除相关功能按钮
function removeMyHoverDom(treeId , treeNode) {
    // 获取span按钮组元素id
    var btnGroupId = treeNode.tId + "_btnGrp";
    // 移除span按钮组
    $("#"+btnGroupId).remove();

}

// 初始化树
function initTree() {
    $.ajax({
        "url" : "menu/get/whole/tree.json",
        "type" : "post",
        "dataType" : "json",
        "success" : function(response) {
            // 1.zTree的属性设置
            var setting = {
                "view" : {
                    "addDiyDom": addMyDiyDom,
                    "addHoverDom" : addMyHoverDom,
                    "removeHoverDom" : removeMyHoverDom
                },
                "data":{
                    "key":{
                        "url" : "xxx"
                    }
                }
            };
            var result = response.result;
            if (result == "SUCCESS"){
                // 2.获取节点数据
                var zNodes = response.data;
                // 3.初始化zTree
                $.fn.zTree.init($("#treeDemo"),setting,zNodes);
            }
            if (result == "FAILED"){
                layer.msg(response.message);
            }
        },
        "error" : function(response) {
            layer.msg("响应异常状态码："+response.status + "异常信息：" + response.statusText);
        }
    })
}