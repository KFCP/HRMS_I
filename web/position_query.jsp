<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("user_login.jsp");
        return;
    }
%>
<html>
<head><title>职位查询</title><link rel="stylesheet" type="text/css" href="css/style.css"></head>
<body>
<h2>职位查询</h2>

<form method="get" action="position_query.jsp">
  职位名称关键词: <input type="text" name="keyword" placeholder="输入职位名称关键字">
  <input type="submit" value="查询">
</form>

<hr>

<%
String keyword = request.getParameter("keyword");
if (keyword != null && !keyword.trim().isEmpty()) {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hrms_db?useSSL=false&serverTimezone=UTC", "root", "password");

    String sql;
    if (keyword.equals("*")) {
        // 如果输入星号，查询所有职位
        sql = "SELECT * FROM positions";
        ps = conn.prepareStatement(sql);
    } else {
        // 正常按职位名称关键词查询
        sql = "SELECT * FROM positions WHERE position_name LIKE ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
    }
    
    rs = ps.executeQuery();

        out.println("<table border='1' cellpadding='5'>");
        out.println("<tr><th>ID</th><th>职位名称</th><th>等级</th></tr>");

        boolean hasResult = false;
        while (rs.next()) {
            hasResult = true;
            out.println("<tr>");
            out.println("<td>" + rs.getInt("id") + "</td>");
            out.println("<td>" + rs.getString("position_name") + "</td>");
            out.println("<td>" + rs.getInt("level") + "</td>");
            out.println("</tr>");
        }
        if (!hasResult) {
            out.println("<tr><td colspan='3'>未找到符合条件的职位</td></tr>");
        }
        out.println("</table>");

    } catch (Exception e) {
        out.println("<p>查询出错：" + e.getMessage() + "</p>");
    } finally {
        if (rs != null) try { rs.close(); } catch (Exception ignored) {}
        if (ps != null) try { ps.close(); } catch (Exception ignored) {}
        if (conn != null) try { conn.close(); } catch (Exception ignored) {}
    }
} else {
    out.println("<p>请输入职位名称关键字进行查询</p>");
}
%>

<p><a href="position_add.jsp">添加职位</a></p>
<p><a href="employee_query.jsp">查询员工</a></p>
<p><a href="user_logout.jsp">退出登录</a></p>

</body>
</html>
