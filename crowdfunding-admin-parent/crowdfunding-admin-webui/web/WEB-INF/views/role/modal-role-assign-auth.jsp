<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<div id="assignModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">众筹平台-分配权限</h4>
            </div>
            <div class="modal-body">
                <ul id="authTreeDemo" class="ztree"></ul>
            </div>
            <div class="modal-footer">
                <button id="allotBtn" type="button" class="btn btn-primary"> 执行分配 </button>
            </div>
        </div>
    </div>
</div>