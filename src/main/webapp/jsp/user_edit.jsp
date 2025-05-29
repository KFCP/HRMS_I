<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>编辑系统用户信息</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <h2>编辑系统用户信息</h2>

    <c:if test="${not empty errorMessage}">
        <p class="error-message"><c:out value="${errorMessage}"/></p>
    </c:if>
    <%-- Success messages are handled on the list page --%>

    <c:if test="${not empty user}">
        <form method="post" action="${pageContext.request.contextPath}/user/update">
            <input type="hidden" name="id" value="<c:out value="${user.id}"/>">

            <label for="username">用户名:</label>
            <input type="text" id="username" name="username" value="<c:out value="${user.username}"/>" required><br>

            <p>要修改密码，请输入新密码。留空则不修改密码。</p>
            <label for="newPassword">新密码:</label>
            <input type="password" id="newPassword" name="newPassword"><br>

            <label for="confirmNewPassword">确认新密码:</label>
            <input type="password" id="confirmNewPassword" name="confirmNewPassword"><br>

            <input type="submit" value="更新信息">
        </form>
    </c:if>
    <c:if test="${empty user && empty errorMessage}">
        <p>未找到指定用户。 <a href="${pageContext.request.contextPath}/user/list">返回列表</a></p>
    </c:if>

    <p><a href="${pageContext.request.contextPath}/user/list">返回用户列表</a></p>
    <p><a href="${pageContext.request.contextPath}/">返回主页</a></p>
    <p><a href="${pageContext.request.contextPath}/logout">退出登录</a></p>
</div>
</body>
</html>
