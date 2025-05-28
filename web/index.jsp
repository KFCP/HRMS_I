<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  if (session.getAttribute("user") == null) {
    response.sendRedirect("user_login.jsp");
    return;
  }
%>
<html>
<head><title>HRMS系统主页</title><link rel="stylesheet" type="text/css" href="css/style.css"></head>
<body>
<div class="container">
<h2>欢迎，<%= session.getAttribute("user") %></h2>
<ul class="main-menu">
  <li><a href="employee_add.jsp">添加员工</a></li>
  <li><a href="employee_query.jsp">查询员工</a></li>
  <li><a href="position_add.jsp">添加职位</a></li>
  <li><a href="position_query.jsp">查询职位</a></li>
  <li><a href="assignment_add.jsp">安排职位</a></li>
  <li><a href="employee_edit.jsp">人事调整</a></li>
  <li><a href="salary_manage.jsp">薪酬管理</a></li>
  <li><a href="user_manage.jsp">系统用户管理</a></li>
  <li><a href="user_logout.jsp">退出系统</a></li>
</ul>
</div>
</body>
</html>