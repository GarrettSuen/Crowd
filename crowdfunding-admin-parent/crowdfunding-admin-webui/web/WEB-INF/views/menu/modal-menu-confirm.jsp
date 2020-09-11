<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="menuConfirmModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">权限菜单删除</h4>
            </div>
            <form>
                <div class="modal-body">
                    您真的要删除<span id="removeNodeSpan"></span>这个节点吗？
                </div>
                <div class="modal-footer">
                    <button id="confirmBtn" type="button" class="btn btn-danger"><i class="glyphicon glyphicon-ok"></i> 删除</button>
                </div>
            </form>
        </div>
    </div>
</div>