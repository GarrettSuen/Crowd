// 执行分页
function generatePage() {
    // 获取pageInfo对象
    var pageInfo = getPageInfoRemote();
    // 渲染数据
    fillTableBody(pageInfo);
}

// 获取PageInfo数据
function getPageInfoRemote() {
    var ajaxResult = $.ajax({
        "url": "role/get/page/info.json",
        "type": "post",
        "data": {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        "dataType": "json",
        "async": false
    });
    // 处理失败请求
    var statsCode = ajaxResult.status;
    if (statsCode != 200) {
        layer.msg("失败！响应状态码="+statusCode+" 说明信息="+ajaxResult.statusText);
        return null;
    }

    // 如果请求成功，获取获取responseJSON
    var resultEntity = ajaxResult.responseJSON;

    // 判断数据是否有效
    var result = resultEntity.result;
    if (result == "FAILED") {
        layer.msg(resultEntity.message);
        return null;
    }
    // 否则获取pageInfo对象
    var pageInfo = resultEntity.data;
    return pageInfo;

}

// 渲染数据
function fillTableBody(pageInfo) {
    // 1.清除tbody中旧的数据，防止后续出现翻页时追加数据的情况
    $("#roleTBody").empty();
    // 为了没有搜索到数据时，不显示导航条
    $("#Pagination").empty();
    // 2.判断pageInfo是否有效
    if (pageInfo == null || pageInfo == undefined || pageInfo.list == null || pageInfo.list.length == 0) {
        $("#roleTBody").append("<tr><td colspan='4' align='center'>抱歉！没有查找到您搜索的数据</td></tr>")
        return;
    }
    // 3.使用pageInfo进行数据渲染
    for (var i = 0; i < pageInfo.list.length; i++) {
        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.roleName;

        var numberTd = "<td>" + (i + 1) + "</td>";
        var checkBoxTd = "<td><input id='" + roleId + "' class='itemBox' type='checkbox'></td>";
        var roleNameTd = "<td>" + roleName + "</td>";
        var checkBtn = "<button id='" + roleId + "' type='button' class='btn btn-success btn-xs checkBtn'><i class=' glyphicon glyphicon-check'></i></button>";
        var pencilBtn = "<button id='" + roleId + "' type='button'  class='btn btn-primary btn-xs pencilBtn'><i class=' glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button id='" + roleId + "' type='button' class='btn btn-danger btn-xs removeBtn'><i class=' glyphicon glyphicon-remove'></i></button>";
        var btnTd = "<td>" + checkBtn + ' ' + pencilBtn + ' ' + removeBtn + "</td>"

        var tr = "<tr>" + numberTd + checkBoxTd + roleNameTd + btnTd + "</tr>";
        $("#roleTBody").append(tr);
    }
    generateNavigator(pageInfo);
}

// 分页导航栏
function generateNavigator(pageInfo) {
    // 1.获取总记录数
    var totalRecord = pageInfo.total;

    // 2.声明属性
    var properties = {
        num_edge_entries : 3,
        num_display_entries: 5,
        callback : paginationCallback,
        items_per_page : pageInfo.pageSize,
        current_page : pageInfo.pageNum - 1,
        prev_text : "上一页",
        next_text : "下一页"
    }
    $("#Pagination").pagination(totalRecord,properties);
}

// 翻页回调函数
function paginationCallback(pageIndex, jQuery) {
    // 修改window对象的pageNum属性
    window.pageNum = pageIndex + 1;

    // 调用generatePage()执行分页
    generatePage();

    // 取消页码按钮默认行为
    return false;
}


// 显示询问模态框,用于确认删除批删角色
function showConfrimModel(roleArray) {
    $("#confirmModal").modal("show");
    $("#roleTab").empty();
    var headlineTr = "<tr><th colspan='2'>您确定删除一下角色吗?</th><</tr>"
    var titleTr = "<tr><td>角色编号</td><td>角色名称</td></tr>";
    $("#roleTab").append(headlineTr,titleTr);
    window.roleidArray = [];
    for (var i = 0 ; i < roleArray.length ; i++){
        var role = roleArray[i];
        var roleId = role.id;
        var idTd = "<td>" + (i + 1) + "</td>";
        var roleNameTd = "<td>" + role.roleName + "</td>";
        var roleTr = "<tr>" + idTd + roleNameTd + "</tr>";
        $("#roleTab").append(roleTr);
        window.roleidArray.push(roleId);
    }
}

// 初始化权限树
function fillAuthTree() {
    // 1.发送ajax请求获取权限数据
    var ajaxResult = $.ajax({
        "url": "auth/get/auth.json",
        "type": "post",
        "dataType": "json",
        "async": false
    });
    // 2.判断响应是否成功
    if (ajaxResult.status != 200) {
        layer.msg("请求处理失败,响应码为：" + ajaxResult.status + " 失败原因：" + ajaxResult.statusText, {time: 2000, icon: 5});
        return;
    }
    // 3.判断结果是否有效
    var result = ajaxResult.responseJSON.result;
    // 3-1.判断是否成功
    if (result == "SUCCESS") {
        // 1-1.zTree属性设置
        var setting = {
            "view": {
                "addDiyDom": addAuthDiyDom
            },
            "data": {
                "simpleData": {
                    "enable": true,
                    "pIdKey" : "categoryId"
                },
                "key" : {
                    "name" : "title"
                }
            },
            "check" : {
                "enable" : true
            }
        };
        // 2-1.获取节点数据
        var zNodes = ajaxResult.responseJSON.data;
        // 2-2.初始化zTree
        $.fn.zTree.init($("#authTreeDemo"), setting, zNodes);
        // 2-3.获取zTreeObj对象
        var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
        // 2-4.调用zTreeObj对象的expandAll()展开节点
        zTreeObj.expandAll(true);
        
        // 3.查询出已分配权限的数组
        ajaxResult = $.ajax({
            "url" : "auth/get/assign/authId.json",
            "type" : "post",
            "data" : {
                "roleId" : window.roleId
            },
            "dataType" : "json",
            "async" : false
        });
        if (ajaxResult.status != 200) {
            layer.msg("请求处理失败,响应码为：" + ajaxResult.status + " 失败原因：" + ajaxResult.statusText, {time: 2000, icon: 5});
            return;
        }
        // 获取已分配的authId
        var authList = ajaxResult.responseJSON.data;
        // 4.回显权限
        for (var i = 0;i < authList.length;i++){ 
            var authId = authList[i];
            // 1.通过authId获取对应的treeNode节点
            var treeNode = zTreeObj.getNodeByParam("id",authId);
            // 2.将treeNode设置为勾选状态,并取消联动效果;参数（节点,是否勾选,是否联动）
            zTreeObj.checkNode(treeNode,true,false);
            
        }
    }
}

// 显示权限树节点自定义图标
function addAuthDiyDom(treeId , treeNode) {
    // 1.获取图标span标签的Id
    var spanId = "#" + treeNode.tId + "_ico";
    // 2.链式操作移除旧span标签的class属性,替换为数据中的icon属性
    $(spanId).removeClass().addClass(treeNode.icon);
}


