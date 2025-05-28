<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("user_login.jsp");
        return;
    }
%>
<html>
<head><title>人员查询</title><link rel="stylesheet" type="text/css" href="css/style.css"></head>
<body>
<h2>查询员工信息</h2>

<form method="get" action="employee_query.jsp">
  姓名关键词: <input type="text" name="keyword" placeholder="输入姓名关键字">
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
        // 如果输入星号，查询所有员工
        sql = "SELECT e.id, e.name, e.gender, e.age, e.phone, e.email, p.position_name " +
              "FROM employees e LEFT JOIN positions p ON e.position_id = p.id";
        ps = conn.prepareStatement(sql);
    } else {
        // 正常按名称关键词查询
        sql = "SELECT e.id, e.name, e.gender, e.age, e.phone, e.email, p.position_name " +
              "FROM employees e LEFT JOIN positions p ON e.position_id = p.id " +
              "WHERE e.name LIKE ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
    }
    
    rs = ps.executeQuery();

        out.println("<table border='1' cellpadding='5'>");
        out.println("<tr><th>ID</th><th>姓名</th><th>性别</th><th>年龄</th><th>电话</th><th>邮箱</th><th>职位</th></th>操作</th></tr>");

        boolean hasResult = false;
        while (rs.next()) {
            hasResult = true;
            out.println("<tr>");
            out.println("<td>" + rs.getInt("id") + "</td>");
            out.println("<td>" + rs.getString("name") + "</td>");
            out.println("<td>" + rs.getString("gender") + "</td>");
            out.println("<td>" + rs.getInt("age") + "</td>");
            out.println("<td>" + rs.getString("phone") + "</td>");
            out.println("<td>" + rs.getString("email") + "</td>");
            out.println("<td>" + rs.getString("position_name") + "</td>");
            out.println("<td><a href=\"employee_edit.jsp?id=" + rs.getInt("id") + "\">调整职位</a></td>");
            out.println("</tr>");
        }
        if (!hasResult) {
            out.println("<tr><td colspan='8'>未找到符合条件的员工</td></tr>");
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
    out.println("<p>请输入姓名关键字进行查询</p>");
}
%>

<p><a href="employee_add.jsp">添加员工</a></p>
<p><a href="position_add.jsp">添加职位</a></p>
<p><a href="user_logout.jsp">退出登录</a></p>

</body>
</html>
