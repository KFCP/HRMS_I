<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>管理员登录</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <h2>管理员登录</h2>

    <c:if test="${not empty error}"> <%-- Changed from errorMessage to error --%>
        <p class="error-message"><c:out value="${error}"/></p>
    </c:if>
    <c:if test="${param.logout == 'true'}">
        <p class="success-message">您已成功退出登录。</p>
    </c:if>
     <c:if test="${param.auth == 'required'}">
        <p class="error-message">请先登录以访问该页面。</p>
    </c:if>


    <form method="post" action="${pageContext.request.contextPath}/login">
        <label for="username">用户名:</label>
        <input type="text" id="username" name="username" value="<c:out value='${param.username}'/>" required><br>

        <label for="password">密码:</label>
        <input type="password" id="password" name="password" required><br>

        <input type="submit" value="登录">
    </form>
    <%-- Optional: Link to a registration page if available in the future --%>
    <%-- <p>没有账户? <a href="${pageContext.request.contextPath}/user/register">注册新用户</a></p> --%>
</div>
</body>
</html>
