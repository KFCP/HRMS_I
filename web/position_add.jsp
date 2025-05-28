<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("user_login.jsp");
        return;
    }
%>
<html>
<head><title>添加职位等级</title><link rel="stylesheet" type="text/css" href="css/style.css"></head>
<body>
<div class="container">
<h2>添加职位等级</h2>

<form method="post" action="position_add.jsp">
  <label for="position_name">职位名称:</label> <input type="text" id="position_name" name="position_name" required><br>
  <label for="level">职位等级（数字）:</label> <input type="number" id="level" name="level" required><br>
  <input type="submit" value="添加职位">
</form>

<%
if ("POST".equalsIgnoreCase(request.getMethod())) {
    String positionName = request.getParameter("position_name");
    String levelStr = request.getParameter("level");

    if (positionName != null && levelStr != null) {
        int level = Integer.parseInt(levelStr);

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hrms_db?useSSL=false&serverTimezone=UTC", "root", "password");

            String sql = "INSERT INTO positions (position_name, level) VALUES (?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, positionName);
            ps.setInt(2, level);

            int result = ps.executeUpdate();
            if (result > 0) {
                out.println("<p class=\\\"success-message\\\">职位添加成功！</p>");
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

<p><a href="position_query.jsp">查看职位列表</a></p>
<p><a href="employee_add.jsp">添加员工</a></p>
<p><a href="user_logout.jsp">退出登录</a></p>
</div>
</body>
</html>
