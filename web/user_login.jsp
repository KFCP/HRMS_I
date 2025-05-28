<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="javax.servlet.*" %>
<html>
<head><title>管理员登录</title><link rel="stylesheet" type="text/css" href="css/style.css"></head>
<body>
<div class="container">
<h2>管理员登录</h2>

<form method="post" action="user_login.jsp">
    <label for="username">用户名:</label> <input type="text" id="username" name="username" required><br>
    <label for="password">密码:</label> <input type="password" id="password" name="password" required><br>
    <input type="submit" value="登录">
</form>

<%
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");


        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hrms_db?useSSL=false&serverTimezone=UTC", "root", "password");

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password); // 这里简单明文，生产环境需加密
            rs = ps.executeQuery();

            if (rs.next()) {
                // 登录成功，保存用户信息到session
                session.setAttribute("user", username);
                response.sendRedirect("index.jsp"); // 登录成功跳转到人员查询页面
            } else {
                out.println("<p class=\\\"error-message\\\">用户名或密码错误</p>");
            }
        } catch (Exception e) {
            out.println("<p class=\\\"error-message\\\">错误：" + e.getMessage() + "</p>");
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception ignored) {
            }
            if (ps != null) try {
                ps.close();
            } catch (Exception ignored) {
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception ignored) {
            }
        }
    }
%>
</div>
</body>
</html>
