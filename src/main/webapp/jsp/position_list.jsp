<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>职位信息列表</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <h2>职位信息列表</h2>

    <c:if test="${not empty errorMessage}">
        <p class="error-message"><c:out value="${errorMessage}"/></p>
    </c:if>
    <c:if test="${param.success == 'delete'}">
        <p class="success-message">职位删除成功！</p>
    </c:if>
    <c:if test="${param.success == 'update'}">
        <p class="success-message">职位信息更新成功！</p>
    </c:if>
    <c:if test="${param.success == 'add'}">
        <p class="success-message">职位添加成功！</p>
    </c:if>

    <%-- The PositionServlet's listPositions method doesn't currently support search, but adding a placeholder if needed in future --%>
    <%-- <form method="get" action="${pageContext.request.contextPath}/position/list">
        职位名称关键词: <input type="text" name="searchName" placeholder="输入名称或留空显示全部" value="<c:out value='${searchName}'/>">
        <input type="submit" value="查询">
    </form> --%>
    <p><a href="${pageContext.request.contextPath}/position/add">添加新职位</a></p>

    <hr>

    <table border="1" cellpadding="5">
        <tr>
            <th>ID</th>
            <th>职位名称</th>
            <th>等级</th>
            <th>操作</th>
        </tr>
        <c:choose>
            <c:when test="${not empty positionList}">
                <c:forEach var="position" items="${positionList}">
                    <tr>
                        <td><c:out value="${position.id}"/></td>
                        <td><c:out value="${position.positionName}"/></td>
                        <td><c:out value="${position.level}"/></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/position/edit?id=${position.id}">编辑</a>
                            &nbsp;|&nbsp;
                            <a href="${pageContext.request.contextPath}/position/delete?id=${position.id}"
                               onclick="return confirm('确定要删除该职位吗？ (确保没有员工正在担任此职位)');">删除</a>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="4">当前没有职位信息。请先 <a href="${pageContext.request.contextPath}/position/add">添加职位</a>。</td>
                </tr>
            </c:otherwise>
        </c:choose>
    </table>

    <p><a href="${pageContext.request.contextPath}/jsp/index.jsp">返回主页</a></p>
    <p><a href="${pageContext.request.contextPath}/logout">退出登录</a></p>
</div>
</body>
</html>
