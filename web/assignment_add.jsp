<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("user_login.jsp");
        return;
    }
%>
<html>
<head><title>职位安排</title><link rel="stylesheet" type="text/css" href="css/style.css"></head>
<body>
<h2>给员工安排职位</h2>

<form method="post" action="assignment_add.jsp">
  员工ID: <input type="number" name="employee_id" required><br>
  职位ID: <input type="number" name="position_id" required><br>
  <input type="submit" value="安排">
</form>

<%
if ("POST".equalsIgnoreCase(request.getMethod())) {
    String empIdStr = request.getParameter("employee_id");
    String posIdStr = request.getParameter("position_id");

    if (empIdStr != null && posIdStr != null) {
        int empId = Integer.parseInt(empIdStr);
        int posId = Integer.parseInt(posIdStr);

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hrms_db?useSSL=false&serverTimezone=UTC", "root", "password");

            String sql = "UPDATE employees SET position_id = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, posId);
            ps.setInt(2, empId);

            int result = ps.executeUpdate();
            if (result > 0) {
                out.println("<p>职位安排成功！</p>");
            } else {
                out.println("<p>员工ID不存在，安排失败。</p>");
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

<p><a href="employee_query.jsp">查询员工</a></p>
<p><a href="position_query.jsp">查询职位</a></p>
<p><a href="user_logout.jsp">退出登录</a></p>

</body>
</html>
