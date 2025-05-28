<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("user_login.jsp");
        return;
    }
%>
<html>
<head><title>系统用户管理</title><link rel="stylesheet" type="text/css" href="css/style.css"></head>
<body>
<h2>添加新系统用户</h2>

<form method="post" action="user_manage.jsp">
  用户名: <input type="text" name="username" required><br>
  密码: <input type="password" name="password" required><br>
  <input type="submit" value="添加用户">
</form>

<%
if ("POST".equalsIgnoreCase(request.getMethod())) {
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    if (username != null && password != null) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hrms_db?useSSL=false&serverTimezone=UTC", "root", "password");

            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password); // 简单明文，生产环境需加密

            int result = ps.executeUpdate();
            if (result > 0) {
                out.println("<p>用户添加成功！</p>");
            } else {
                out.println("<p>添加失败。</p>");
            }
        } catch (Exception e) {
            out.println("<p>错误：" + e.getMessage() + "</p>");
        } finally {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }
}
%>

<p><a href="user_logout.jsp">退出登录</a></p>
</body>
</html>
