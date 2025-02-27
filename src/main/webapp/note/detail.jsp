<%--
  Created by IntelliJ IDEA.
  User: MyraZ
  Date: 2024/12/25
  Time: 8:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="col-md-9">
    <div class="data_list">
        <div class="data_list_title">
            <span class="glyphicon glyphicon-eye-open"></span>&nbsp;查看云记
        </div>
        <div>
            <div class="note_title"><h2>${note.title}</h2></div>
            <div class="note_info">
                发布时间：『 <fmt:formatDate value="${note.pubTime}" pattern="yyyy-MM-dd HH:mm" /> 』
                &nbsp;&nbsp;云记类别：${note.typeName}
            </div>
            <div class="note_content">
                <p>${note.content}</p>
            </div>
            <div class="note_btn">
                <button class="btn btn-primary" type="button" onclick="updateNote(${note.noteId})">修改</button>
                <button class="btn btn-danger" type="button" onclick="deleteNote(${note.noteId})">删除</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    /**
     * 删除云记
     * @param noteId
     */
    function deleteNote(noteId){
        // 弹出提示框询问用户是否确认删除
        swal({
            title: "",  // 标题
            text: "<h3>您确认要删除该记录吗？</h3>", // 内容
            type: "warning", // 图标  error	  success	info  warning
            showCancelButton: true,  // 是否显示取消按钮
            confirmButtonColor: "orange", // 确认按钮的颜色
            confirmButtonText: "确定", // 确认按钮的文本
            cancelButtonText: "取消" // 取消按钮的文本
        }).then(function(){
            $.ajax({
                type: "post",
                url:"note",
                data:{
                    actionName:"delete",
                    noteId:noteId
                },
                success:function (code){
                    //判断是否删除成功
                    if (code==1){
                        //跳转到首页
                        window.location.href="index"
                    }else{
                        //提示删除失败
                        swal("","<h3>删除失败!</h3>","error");

                    }
                }
            });

        });
    }

    /**
     * 修改云记，进入发布云云记页面
     */
    function updateNote(noteId){
        window.location.href="note?actionName=view&noteId="+noteId;
    }

</script>

