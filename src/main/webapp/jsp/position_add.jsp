<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>添加职位</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <h2>添加新职位</h2>

    <c:if test="${not empty errorMessage}">
        <p class="error-message"><c:out value="${errorMessage}"/></p>
    </c:if>
    <%-- Success messages are now displayed on the list page via flash attributes --%>

    <form method="post" action="${pageContext.request.contextPath}/position/add">
        <label for="positionName">职位名称:</label>
        <input type="text" id="positionName" name="positionName" value="<c:out value='${position.positionName}'/>" required><br>

        <label for="level">职位等级 (数字):</label>
        <input type="number" id="level" name="level" value="<c:out value='${position.level}'/>" required min="1"><br>

        <input type="submit" value="添加职位">
    </form>

    <p><a href="${pageContext.request.contextPath}/position/list">查看职位列表</a></p>
    <p><a href="${pageContext.request.contextPath}/">返回主页</a></p> <%-- Changed to context root --%>
    <p><a href="${pageContext.request.contextPath}/logout">退出登录</a></p>
</div>
</body>
</html>
