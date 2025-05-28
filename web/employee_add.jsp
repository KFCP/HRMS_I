<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%
    // 登录校验，未登录则跳转到登录页
    if (session.getAttribute("user") == null) {
        response.sendRedirect("user_login.jsp");
        return;
    }
%>
<html>
<head><title>添加员工</title><link rel="stylesheet" type="text/css" href="css/style.css"></head>
<body>
<div class="container">
<h2>添加新员工</h2>

<form method="post" action="employee_add.jsp">
  <label for="name">姓名:</label> <input type="text" id="name" name="name" required><br>
  <label for="gender">性别:</label> <input type="text" id="gender" name="gender" required><br>
  <label for="age">年龄:</label> <input type="number" id="age" name="age" required><br>
  <label for="phone">电话:</label> <input type="text" id="phone" name="phone"><br>
  <label for="email">邮箱:</label> <input type="email" id="email" name="email"><br>
  <label for="position_id">职位ID:</label> <input type="number" id="position_id" name="position_id" required><br>
  <input type="submit" value="添加">
</form>

<%
if ("POST".equalsIgnoreCase(request.getMethod())) {
    String name = request.getParameter("name");
    String gender = request.getParameter("gender");
    String ageStr = request.getParameter("age");
    String phone = request.getParameter("phone");
    String email = request.getParameter("email");
    String positionIdStr = request.getParameter("position_id");

    if (name != null && gender != null && ageStr != null && positionIdStr != null) {
        int age = Integer.parseInt(ageStr);
        int positionId = Integer.parseInt(positionIdStr);

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hrms_db?useSSL=false&serverTimezone=UTC", "root", "password");

            String sql = "INSERT INTO employees (name, gender, age, phone, email, position_id) VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, gender);
            ps.setInt(3, age);
            ps.setString(4, phone);
            ps.setString(5, email);
            ps.setInt(6, positionId);

            int result = ps.executeUpdate();
            if (result > 0) {
                out.println("<p class=\\\"success-message\\\">员工添加成功！</p>");
            } else {
                out.println("<p class=\\\"error-message\\\">添加失败。</p>");
            }
        } catch (Exception e) {
            out.println("<p class=\\\"error-message\\\">错误: " + e.getMessage() + "</p>");
        } finally {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }
}
%>

<p><a href="employee_query.jsp">查看员工列表</a></p>
<p><a href="user_logout.jsp">退出登录</a></p>
</div>
</body>
</html>
