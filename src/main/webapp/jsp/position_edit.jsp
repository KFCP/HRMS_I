<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>编辑职位信息</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <h2>编辑职位信息</h2>

    <c:if test="${not empty errorMessage}">
        <p class="error-message"><c:out value="${errorMessage}"/></p>
    </c:if>
    <%-- Success messages are now displayed on the list page via flash attributes --%>

    <c:if test="${not empty position}">
        <form method="post" action="${pageContext.request.contextPath}/position/update"> <%-- Changed action to /position/update --%>
            <input type="hidden" name="id" value="<c:out value="${position.id}"/>">

            <label for="positionName">职位名称:</label>
            <input type="text" id="positionName" name="positionName" value="<c:out value="${position.positionName}"/>" required><br>

            <label for="level">职位等级 (数字):</label>
            <input type="number" id="level" name="level" value="<c:out value="${position.level}"/>" required min="1"><br>

            <input type="submit" value="更新信息">
        </form>
    </c:if>
    <c:if test="${empty position && empty errorMessage}">
        <p>未找到指定职位。 <a href="${pageContext.request.contextPath}/position/list">返回列表</a></p>
    </c:if>

    <p><a href="${pageContext.request.contextPath}/position/list">返回职位列表</a></p>
    <p><a href="${pageContext.request.contextPath}/">返回主页</a></p> <%-- Changed to context root --%>
    <p><a href="${pageContext.request.contextPath}/logout">退出登录</a></p>
</div>
</body>
</html>
