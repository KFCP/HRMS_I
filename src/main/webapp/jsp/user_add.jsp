<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>添加新系统用户</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <h2>添加新系统用户</h2>

    <c:if test="${not empty errorMessage}">
        <p class="error-message"><c:out value="${errorMessage}"/></p>
    </c:if>
    <%-- No specific success message for add on this page, as servlet redirects to list with param --%>

    <form method="post" action="${pageContext.request.contextPath}/user/add">
        <label for="username">用户名:</label>
        <input type="text" id="username" name="username" value="<c:out value='${user.username}'/>" required><br>

        <label for="password">密码:</label>
        <input type="password" id="password" name="password" required><br>

        <label for="confirmPassword">确认密码:</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required><br>

        <input type="submit" value="添加用户">
    </form>

    <p><a href="${pageContext.request.contextPath}/user/list">查看用户列表</a></p>
    <p><a href="${pageContext.request.contextPath}/">返回主页</a></p> <%-- Changed to context root --%>
    <p><a href="${pageContext.request.contextPath}/logout">退出登录</a></p>
</div>
</body>
</html>
