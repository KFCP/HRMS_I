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
    <%-- Display success message from servlet if any (e.g., after redirect) --%>
    <c:if test="${param.success == 'add'}">
        <p class="success-message">员工添加成功！</p>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/employee/add">
        <label for="name">姓名:</label>
        <input type="text" id="name" name="name" value="<c:out value='${param.name}'/>" required><br>

        <label for="gender">性别:</label>
        <select id="gender" name="gender" required>
            <option value="">--选择性别--</option>
            <option value="男" ${param.gender == '男' ? 'selected' : ''}>男</option>
            <option value="女" ${param.gender == '女' ? 'selected' : ''}>女</option>
            <option value="其他" ${param.gender == '其他' ? 'selected' : ''}>其他</option>
        </select><br>

        <label for="age">年龄:</label>
        <input type="number" id="age" name="age" value="<c:out value='${param.age}'/>" required min="18" max="100"><br>

        <label for="phone">电话:</label>
        <input type="tel" id="phone" name="phone" value="<c:out value='${param.phone}'/>" pattern="[0-9]{10,15}" title="请输入10到15位数字的电话号码"><br>

        <label for="email">邮箱:</label>
        <input type="email" id="email" name="email" value="<c:out value='${param.email}'/>"><br>

        <label for="positionId">职位:</label>
        <select id="positionId" name="positionId" required>
            <option value="">--选择职位--</option>
            <c:forEach var="position" items="${positionList}">
                <option value="${position.id}" ${param.positionId == position.id ? 'selected' : ''}>
                    <c:out value="${position.positionName}"/> (ID: <c:out value="${position.id}"/>)
                </option>
            </c:forEach>
        </select><br>

        <input type="submit" value="添加">
    </form>

    <p><a href="${pageContext.request.contextPath}/employee/list">查看员工列表</a></p>
    <p><a href="${pageContext.request.contextPath}/jsp/index.jsp">返回主页</a></p>
    <p><a href="${pageContext.request.contextPath}/logout">退出登录</a></p>
</div>
</body>
</html>
