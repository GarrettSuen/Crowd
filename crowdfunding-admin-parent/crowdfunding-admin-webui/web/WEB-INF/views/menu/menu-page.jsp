<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/views/common/common-head.jsp" %>
<link rel="stylesheet" href="ztree/zTreeStyle.css"/>
<script type="text/javascript" src="crowd-js/menuTree.js"></script>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
    $(function() {
        // 调优初始化方法，渲染树
        initTree();

        // 添加模态框
        // 为添加按钮绑定单响函数
        $("#treeDemo").on("click",".addBtn",function() {
            // 获取pid
            window.pid = this.id;

            // 显示模态框
            $("#menuAddModal").modal("show");

            // 取消超链接动作
            return false;
        });
        // 为添加模态框保存按钮添加单响函数
        $("#menuSaveBtn").click(function() {
            // 1.收集数据
            var nodeName = $.trim($("#menuAddModal [name=name]").val());
            var url = $.trim($("#menuAddModal [name=url]").val());
            var icon = $("#menuAddModal [name=icon]:checked").val();

            // 2.发送Ajax请求
            $.ajax({
                "url" : "menu/save.json",
                "type" : "post",
                "data" : {
                    "pid" : window.pid,
                    "name" : nodeName,
                    "url" : url,
                    "icon" : icon
                },
                "dataType" : "json",
                "success" : function(response) {
                    var result = response.result;
                    if (result == "SUCCESS"){
                        layer.msg("新增成功");
                        // 重新渲染菜单树
                        initTree();
                    }
                    if (response == "FAILDED"){
                        layer.msg("新增失败！原因：" + response.message);
                    }
                },
                "error" : function(response) {
                    layer.msg("异常响应状态码："+response.status + "异常信息："+response.statusText);
                }
            });
            // 关闭并重置模态框
            $("#menuAddModal").modal("hide");
            $("#menuResetBtn").click();
        });

        // 修改模态框
        // 为修改按钮绑定单响函数
        $("#treeDemo").on("click",".editBtn",function() {
            // 保存当前节点的id
            window.id = this.id;
           
            // 获取数据
            // 1.获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
            // 2.调用zTreeObj.getNodeByParam获取当前节点
            var currentNode = zTreeObj.getNodeByParam("id",window.id);
    
            // 回显数据
            $("#menuEditModal [name=name]").val(currentNode.name);
            $("#menuEditModal [name=url]").val(currentNode.url);
            $("#menuEditModal [name=icon]").val([currentNode.icon]);
            // 打开模态框
            $("#menuEditModal").modal("show");

            // 取消超链接动作
            return false;
        });
        
        // 为修改按钮绑定单响函数
        $("#menuEditBtn").click(function() {
            // 收集表单数据
            var nodeName = $.trim($("#menuEditModal [name=name]").val());
            var url = $.trim($("#menuEditModal [name=url]").val());
            var icon = $("#menuEditModal [name=icon]:checked").val();
            
            // 发送Ajax请求
            $.ajax({
                "url" : "menu/update.json",
                "type" : "post",
                "data" : {
                    "id" : window.id,
                    "name" : nodeName,
                    "url" : url,
                    "icon" : icon
                },
                "dataType" : "json",
                "success" : function(response) {
                    var result = response.result;
                    if (result == "SUCCESS"){
                        layer.msg("修改成功");
                        // 重新渲染菜单树
                        initTree();
                    }
                    if (response == "FAILDED"){
                        layer.msg("修改失败！原因：" + response.message);
                    }
                },
                "error" : function(response) {
                    layer.msg("异常响应状态码："+response.status + "异常信息："+response.statusText);
                }
            });
            // 关闭模态框
            $("#menuEditModal").modal("hide");
        });

        // 删除模态框
        // 为删除按钮绑定单响函数
        $("#treeDemo").on("click",".removeBtn",function() {
            // 获取当前id
            window.id = this.id;

            // 获取数据
            // 1.获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
            // 2.调用zTreeObj.getNodeByParam获取当前节点
            var currentNode = zTreeObj.getNodeByParam("id",window.id);
            
            // 显示删除的节点名称
            $("#removeNodeSpan").html("&nbsp;[<i class='"+currentNode.icon+"'>"+currentNode.name+"</i>]&nbsp;");
            $("#menuConfirmModal").modal("show");

            // 取消超链接动作
            return false;
        });
        
        // 为删除按钮绑定单响函数
        $("#confirmBtn").click(function() {
            // 发送ajax请求
            $.ajax({
                "url" : "menu/remove.json",
                "type" : "post",
                "data" : {
                    "id" : window.id    
                },
                "dataType" : "json",
                "success" : function(response) {
                    var result = response.result;
                    if (result == "SUCCESS"){
                        layer.msg("删除成功！");
                        initTree();
                    }
                    if (result == "FAILDED"){
                        layer.msg("删除失败！原因："+response.message);    
                    }
                },
                "error" : function(response) {
                    layer.msg("异常响应状态码："+response.status + "异常信息："+response.statusText);
                }
            });
            // 关闭模态框
            $("#menuConfirmModal").modal("hide");
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
                        <i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                        <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i> </div>
                    </div>
                    <div class="panel-body">
                        <ul id="treeDemo" class="ztree"></ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="myModalLabel">帮助</h4>
                </div>
                <div class="modal-body">
                    <div class="bs-callout bs-callout-info">
                        <h4>没有默认类</h4>
                        <p>警告框没有默认类，只有基类和修饰类。默认的灰色警告框并没有多少意义。所以您要使用一种有意义的警告类。目前提供了成功、消息、警告或危险。</p>
                    </div>
                    <div class="bs-callout bs-callout-info">
                        <h4>没有默认类</h4>
                        <p>警告框没有默认类，只有基类和修饰类。默认的灰色警告框并没有多少意义。所以您要使用一种有意义的警告类。目前提供了成功、消息、警告或危险。</p>
                    </div>
                </div>
                <!--
                <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
                </div>
                -->
            </div>
        </div>
    </div>
    <%@include file="/WEB-INF/views/menu/modal-menu-add.jsp"%>
    <%@include file="/WEB-INF/views/menu/modal-menu-edit.jsp"%>
    <%@include file="/WEB-INF/views/menu/modal-menu-confirm.jsp"%>
</body>
</html>