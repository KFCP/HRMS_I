<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>员工信息列表</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <h2>员工信息列表</h2>

    <c:if test="${not empty errorMessage}">
        <p class="error-message"><c:out value="${errorMessage}"/></p>
    </c:if>
    <%-- Updated to use flash attributes for success messages --%>
    <c:if test="${not empty successMessage}">
        <p class="success-message"><c:out value="${successMessage}"/></p>
    </c:if>

    <form method="get" action="${pageContext.request.contextPath}/employee/list">
        姓名关键词: <input type="text" name="searchName" placeholder="输入姓名或留空显示全部" value="<c:out value='${searchName}'/>">
        <input type="submit" value="查询">
    </form>
    <p><a href="${pageContext.request.contextPath}/employee/add">添加新员工</a></p>

    <hr>

    <table border="1" cellpadding="5">
        <tr>
            <th>ID</th>
            <th>姓名</th>
            <th>性别</th>
            <th>年龄</th>
            <th>电话</th>
            <th>邮箱</th>
            <th>职位</th>
            <th>操作</th>
        </tr>
        <c:choose>
            <c:when test="${not empty employeeList}">
                <c:forEach var="employee" items="${employeeList}">
                    <tr>
                        <td><c:out value="${employee.id}"/></td>
                        <td><c:out value="${employee.name}"/></td>
                        <td><c:out value="${employee.gender}"/></td>
                        <td><c:out value="${employee.age}"/></td>
                        <td><c:out value="${employee.phone}"/></td>
                        <td><c:out value="${employee.email}"/></td>
                        <td><c:out value="${employee.positionName}"/> (ID: <c:out value="${employee.positionId}"/>)</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/employee/edit?id=${employee.id}">编辑</a>
                            &nbsp;|&nbsp;
                            <a href="${pageContext.request.contextPath}/employee/delete?id=${employee.id}"
                               onclick="return confirm('确定要删除该员工吗？');">删除</a>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="8">
                        <c:choose>
                            <c:when test="${not empty searchName}">
                                未找到姓名为 "<c:out value='${searchName}'/>" 的员工。
                            </c:when>
                            <c:otherwise>
                                当前没有员工信息。请先 <a href="${pageContext.request.contextPath}/employee/add">添加员工</a>。
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:otherwise>
        </c:choose>
    </table>

    <p><a href="${pageContext.request.contextPath}/">返回主页</a></p> <%-- Changed to context root --%>
    <p><a href="${pageContext.request.contextPath}/logout">退出登录</a></p>
</div>
</body>
</html>
