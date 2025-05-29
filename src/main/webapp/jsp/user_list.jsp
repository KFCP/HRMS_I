<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>系统用户管理</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <h2>系统用户列表</h2>

    <c:if test="${not empty errorMessage}">
        <p class="error-message"><c:out value="${errorMessage}"/></p>
    </c:if>
    <%-- Updated to use flash attributes for success messages --%>
    <c:if test="${not empty successMessage}">
        <p class="success-message"><c:out value="${successMessage}"/></p>
    </c:if>

    <p><a href="${pageContext.request.contextPath}/user/add">添加新用户</a></p>

    <hr>

    <table border="1" cellpadding="5">
        <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>操作</th>
        </tr>
        <c:choose>
            <c:when test="${not empty userList}">
                <c:forEach var="user" items="${userList}">
                    <tr>
                        <td><c:out value="${user.id}"/></td>
                        <td><c:out value="${user.username}"/></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/user/edit?id=${user.id}">编辑</a>
                            &nbsp;|&nbsp;
                            <%-- Prevent deleting the currently logged-in user or a superuser if needed --%>
                            <c:if test="${sessionScope.loggedInUser.username ne user.username}">
                                <a href="${pageContext.request.contextPath}/user/delete?id=${user.id}"
                                   onclick="return confirm('确定要删除用户 \'${user.username}\' 吗？');">删除</a>
                            </c:if>
                            <c:if test="${sessionScope.loggedInUser.username eq user.username}">
                                (当前用户)
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="3">当前没有用户信息。请先 <a href="${pageContext.request.contextPath}/user/add">添加用户</a>。</td>
                </tr>
            </c:otherwise>
        </c:choose>
    </table>

    <p><a href="${pageContext.request.contextPath}/">返回主页</a></p> <%-- Changed to context root --%>
    <p><a href="${pageContext.request.contextPath}/logout">退出登录</a></p>
</div>
</body>
</html>
