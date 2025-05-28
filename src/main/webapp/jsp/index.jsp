<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>HRMS系统主页</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <h2>欢迎，<c:out value="${sessionScope.username}"/>!</h2>
    <ul class="main-menu">
        <li><a href="${pageContext.request.contextPath}/employee/add">添加员工</a></li>
        <li><a href="${pageContext.request.contextPath}/employee/list">查询/管理员工</a></li>
        <li><a href="${pageContext.request.contextPath}/position/add">添加职位</a></li>
        <li><a href="${pageContext.request.contextPath}/position/list">查询/管理职位</a></li>
        <%-- assignment_add.jsp might be covered by employee edit. For now, link to employee list or a specific servlet if functionality is distinct --%>
        <%-- <li><a href="${pageContext.request.contextPath}/assignment/add">安排职位</a></li> --%>
        <li><a href="${pageContext.request.contextPath}/employee/list">人事调整 (通过员工列表编辑)</a></li>
        <%-- salary_manage.jsp might be covered by employee edit or a new servlet. For now, link to employee list --%>
        <%-- <li><a href="${pageContext.request.contextPath}/salary/manage">薪酬管理</a></li> --%>
        <li><a href="${pageContext.request.contextPath}/user/list">系统用户管理</a></li>
        <li><a href="${pageContext.request.contextPath}/logout">退出系统</a></li>
    </ul>
</div>
</body>
</html>