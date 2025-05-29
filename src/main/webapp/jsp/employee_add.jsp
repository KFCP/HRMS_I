<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>添加员工</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <h2>添加新员工</h2>

    <c:if test="${not empty errorMessage}">
        <p class="error-message"><c:out value="${errorMessage}"/></p>
    </c:if>
    <%-- Success messages are now displayed on the list page via flash attributes --%>

    <form method="post" action="${pageContext.request.contextPath}/employee/add">
        <label for="name">姓名:</label>
        <input type="text" id="name" name="name" value="<c:out value='${employee.name}'/>" required><br>

        <label for="gender">性别:</label>
        <select id="gender" name="gender" required>
            <option value="">--选择性别--</option>
            <option value="男" ${employee.gender == '男' ? 'selected' : ''}>男</option>
            <option value="女" ${employee.gender == '女' ? 'selected' : ''}>女</option>
            <option value="其他" ${employee.gender == '其他' ? 'selected' : ''}>其他</option>
        </select><br>

        <label for="age">年龄:</label>
        <input type="number" id="age" name="age" value="<c:out value='${employee.age}'/>" required min="18" max="100"><br>

        <label for="phone">电话:</label>
        <input type="tel" id="phone" name="phone" value="<c:out value='${employee.phone}'/>" pattern="[0-9]{10,15}" title="请输入10到15位数字的电话号码"><br>

        <label for="email">邮箱:</label>
        <input type="email" id="email" name="email" value="<c:out value='${employee.email}'/>"><br>

        <label for="positionId">职位:</label>
        <select id="positionId" name="positionId" required>
            <option value="">--选择职位--</option>
            <c:forEach var="pos" items="${positionList}"> <%-- Changed var name to avoid conflict if employee had a 'position' field --%>
                <option value="${pos.id}" ${employee.positionId == pos.id ? 'selected' : ''}>
                    <c:out value="${pos.positionName}"/> (ID: <c:out value="${pos.id}"/>)
                </option>
            </c:forEach>
        </select><br>

        <input type="submit" value="添加">
    </form>

    <p><a href="${pageContext.request.contextPath}/employee/list">查看员工列表</a></p>
    <p><a href="${pageContext.request.contextPath}/">返回主页</a></p> <%-- Changed to context root --%>
    <p><a href="${pageContext.request.contextPath}/logout">退出登录</a></p>
</div>
</body>
</html>
