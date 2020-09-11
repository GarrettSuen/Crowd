<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/views/common/common-head.jsp" %>
<link rel="stylesheet" href="css/pagination.css">
<link rel="stylesheet" href="ztree/zTreeStyle.css"/>
<script src="jquery/jquery.pagination.js"></script>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="crowd-js/role.js"></script>
<script type="text/javascript">
    $(function () {
        // 分页数据初始化
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";
        // 执行分页查询
        generatePage();

        // 为查询按钮绑定单响函数
        $("#searchBtn").click(function () {
            // 设置keyword
            window.keyword = $("#keywordInput").val();
            // 执行分页查询
            generatePage();
        });
        // 点击新增按钮显示模态框
        $("#showAddModelBtn").click(function () {
            $("#addModal").modal("show");
        });

        // 保存新增角色
        $("#saveRoleBtn").click(function () {
            // 1.获取用户输入的角色名称
            var roleName = $.trim($("#addModal [name=roleName]").val());
            // 2.发起ajax请求
            $.ajax({
                "url" : "role/save.json",
                "data" : {
                   "roleName" : roleName
                },
                "type" : "post",
                "dataType" : "json",
                "success" : function (response) {
                    var result = response.result;
                    if (result == "SUCCESS"){
                        layer.msg("新增成功！")
                        window.pageNum = 9999999;
                        generatePage();
                    }else {
                        layer.msg("新增失败！失败原因："+ response.message);
                    }
                },
                "error" : function (response) {
                    layer.msg(response.status + " " +response.statusText);
                }
            });
            // 3.关闭模态框
            $("#addModal").modal("hide");
            // 4.清除模态框文本框内容
            $("#addModal [name=roleName]").val("");
        });

        // 单击修改按钮显示修改模态框
        $("#roleTBody").on("click",".pencilBtn",function () {
            // 打开修改模态框
            $("#editModal").modal("show");
            // 获取角色名称
            var roleName = $(this).parent().prev().text();
            window.roleId = this.id;
            $("#editModal [name=roleName]").val(roleName);
        });

        // 更新按钮单响函数
        $("#updateRoleBtn").click(function () {
            var roleName = $("#editModal [name=roleName]").val();
            $.ajax({
                "url" : "role/update.json",
                "type" : "post",
                "data" : {
                    "id" : window.roleId,
                    "roleName" : roleName
                },
                "dataType" : "json",
                "success" : function (response) {
                    var result = response.result;
                    if (result == "SUCCESS"){
                        layer.msg("修改成功！")
                        generatePage();
                    }else {
                        layer.msg("修改失败！失败原因："+ response.message);
                    }
                },
                "error" : function (response) {
                    layer.msg(response.status + " " +response.statusText);
                }
            });
            $("#editModal").modal("hide");
        });

        // 删除模态框删除按钮单击函数
        $("#removeRoleBtn").click(function () {
            var requestBody = JSON.stringify(window.roleidArray);
            $.ajax({
                "url" : "role/remove/id/array.json",
                "type" : "post",
                "data" : requestBody,
                "contentType" : "application/json;charset=utf-8",
                "dataType" : "json",
                "success" : function (response) {
                    var result = response.result;
                    if (result == "SUCCESS"){
                        layer.msg("删除成功！")
                        generatePage();
                    }else {
                        layer.msg("删除失败！失败原因："+ response.message);
                    }
                },
                "error" : function (response) {
                    layer.msg(response.status + " " +response.statusText);
                }
            });
            $("#confirmModal").modal("hide");
            // 解决勾选全选删除后，全选checkBox仍然选中Bug
            $("#summaryBox").removeAttr("checked");
        });

        // 单条删除
        $("#roleTBody").on("click",".removeBtn",function () {
            // 获取当前角色名称
            var roleName = $(this).parent().prev().text();
            // 创建role对象存入数组
            var roleArray = [
                {
                    "id" : this.id,
                    "roleName" : roleName
                }
            ];
            showConfrimModel(roleArray);
        });

        // 批量选中
        $("#roleTBody").on("click",".itemBox",function () {
            // 获取全部选中的子checkBox
            var checkBoxCount = $(".itemBox:checked").length;
            // 获取全部.itemBox的数量
            var totalBoxCount = $(".itemBox").length;
            // 选中全选checkBox
            $("#summaryBox").prop("checked",checkBoxCount == totalBoxCount);
        });

        // 单击全选checkBox选中所有子checkBox
        $("#summaryBox").click(function () {
            $(".itemBox").prop("checked",this.checked);
        });

        // 批量删除按钮单击函数
        $("#batchRemoveBtn").click(function () {
            var roleArray = [];
            $(".itemBox:checked").each(function () {
                // 获取roleId、roleName
                var roleId = this.id;
                var roleName = $(this).parent().next().text();
                // 将属性封装存入roleArray数组中
                roleArray.push({
                    "id" : roleId,
                    "roleName" : roleName
                });
            });
            if(roleArray.length == 0){
                layer.msg("请至少选中一个角色进行删除！");
                return;
            }
            // 展示删除模态框
            showConfrimModel(roleArray);
        });

        // 给分配权限按钮绑定单击响应函数
        $("#roleTBody").on("click",".checkBtn",function () {
            window.roleId = this.id;
            // 1.显示分配权限模态框
            $("#assignModal").modal("show");
            
            // 2.渲染权限树
            fillAuthTree();            
        });
        
        // 权限模态框分配按钮单击响应函数
        $("#allotBtn").click(function () {
            var authIds = [];
            // 1.获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
            // 2.获取被勾选的节点
            var checkedNodes = zTreeObj.getCheckedNodes();
            // 3.遍历checkedNodes
            for (var i = 0 ; i < checkedNodes.length ; i++){
                var authId = checkedNodes[i].id;
                authIds.push(authId);
            }
            // 4.发送请求
            // 4-1.准备数据
            var requestBody = {
                "authIds" : authIds,
                // 为了避免服务器端接收数据时，需要进行类型转换在这将roleId也存入数组中
                "roleId" : [window.roleId]
            }
            // 4-2.将requestBody转换为JSON数据
            requestBody = JSON.stringify(requestBody);
            $.ajax({
                "url" : "assign/do/assign/auth.json",
                "type" : "post",
                "data" :requestBody,
                "contentType" : "application/json;charset=UTF-8",
                "dataType" : "json",
                "success" : function (response) {
                    var result = response.result;
                    if (result == "SUCCESS"){
                        layer.msg("分配成功",{time:2000,icon:6});
                        $("#assignModal").modal("hide");
                    }
                    if (result == "FAILED"){
                        layer.msg("分配失败！",{time:2000,icon:5});
                    }
                },
                "error" : function (response) {
                    layer.msg("请求处理失败,响应码为：" + response.status + " 失败原因：" + response.statusText, {time: 2000, icon: 5});
                }
            })
        });
    });
</script>
<body>
<%@include file="/WEB-INF/views/common/common-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/views/common/common-sidebar.jsp" %>
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
                                <input class="form-control has-success" id="keywordInput" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button id="batchRemoveBtn" type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button type="button" class="btn btn-primary" style="float:right;" id="showAddModelBtn"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            <tbody id="roleTBody"></tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"></div>
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
<%@include file="/WEB-INF/views/role/modal-role-add.jsp"%>
<%@include file="/WEB-INF/views/role/modal-role-edit.jsp"%>
<%@include file="/WEB-INF/views/role/modal-role-remove.jsp"%>
<%@include file="/WEB-INF/views/role/modal-role-assign-auth.jsp"%>
</body>
</html>