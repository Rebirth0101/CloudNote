<%--
  Created by IntelliJ IDEA.
  User: MyraZ
  Date: 2024/12/21
  Time: 17:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="col-md-9">
    <div class="data_list">
        <div class="data_list_title"><span class="glyphicon glyphicon glyphicon-th-list"></span>&nbsp;
            云记列表 </div>
        <%-- 判断云记列表是否存在 --%>
        <c:if test="${empty page}">
            <h2>暂未查询到云记记录</h2>
        </c:if>
        <c:if test="${!empty page}">
            <%-- 遍历获取云记列表 --%>

            <div class="note_datas">
                <ul>
                    <c:forEach items="${page.dataList}" var="item">
                        <li>
                            『 <fmt:formatDate value="${item.pubTime}" pattern="yyyy-MM-dd"/> 』&nbsp;&nbsp;
                            <a href="note?actionName=detail&noteId=${item.noteId}">${item.title}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <%-- 设置分页导航 --%>
            <nav style="text-align: center">
                <ul class="pagination   center">
                    <c:if test="${page.pageNum>1}">
                        <li>
                            <a href="index?pageNum=${page.prePage}&actionName=${action}&title=${title}&date=${date}&typeId=${typeId}">
                                <span>«</span>
                            </a>
                        </li>
                    </c:if>

                     <%-- 导航页数 --%>
                    <c:forEach begin="${page.startNavPage}" end="${page.endNavPage}" var="p">

                        <li <c:if test="${page.pageNum==p}">class="active"</c:if> >
                            <a href="index?pageNum=${p}&actionName=${action}&title=${title}&date=${date}&typeId=${typeId}">${p}</a>
                        </li>
                    </c:forEach>
                    <c:if test="${page.pageNum<page.totalPages}">
                        <li>
                            <a href="index?pageNum=${page.nextPage}&actionName=${action}&title=${title}&date=${date}&typeId=${typeId}">
                                <span>»</span>
                            </a>
                        </li>
                    </c:if>

                </ul>
            </nav>
        </c:if>
    </div>

</div>
