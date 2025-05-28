<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("user_login.jsp");
        return;
    }

    String empIdStr = request.getParameter("id");
    if (empIdStr == null || empIdStr.trim().isEmpty()) {
        out.println("<p>未指定员工ID。</p>");
        return;
    }

    int empId = Integer.parseInt(empIdStr);

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    String name = "";
    String gender = "";
    int age = 0;
    String phone = "";
    String email = "";
    int positionId = 0;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hrms_db?useSSL=false&serverTimezone=UTC", "root", "password");

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            // 处理提交更新
            name = request.getParameter("name");
            gender = request.getParameter("gender");
            age = Integer.parseInt(request.getParameter("age"));
            phone = request.getParameter("phone");
            email = request.getParameter("email");
            positionId = Integer.parseInt(request.getParameter("position_id"));

            String updateSql = "UPDATE employees SET name=?, gender=?, age=?, phone=?, email=?, position_id=? WHERE id=?";
            ps = conn.prepareStatement(updateSql);
            ps.setString(1, name);
            ps.setString(2, gender);
            ps.setInt(3, age);
            ps.setString(4, phone);
            ps.setString(5, email);
            ps.setInt(6, positionId);
            ps.setInt(7, empId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                out.println("<p>员工信息更新成功！</p>");
            } else {
                out.println("<p>更新失败！</p>");
            }
            ps.close();
        }

        // 查询当前员工信息显示在表单
        String querySql = "SELECT * FROM employees WHERE id=?";
        ps = conn.prepareStatement(querySql);
        ps.setInt(1, empId);
        rs = ps.executeQuery();
        if (rs.next()) {
            name = rs.getString("name");
            gender = rs.getString("gender");
            age = rs.getInt("age");
            phone = rs.getString("phone");
            email = rs.getString("email");
            positionId = rs.getInt("position_id");
        } else {
            out.println("<p>未找到指定员工。</p>");
            return;
        }
    } catch (Exception e) {
        out.println("<p>错误：" + e.getMessage() + "</p>");
        return;
    } finally {
        if (rs != null) try { rs.close(); } catch (Exception ignored) {}
        if (ps != null) try { ps.close(); } catch (Exception ignored) {}
        if (conn != null) try { conn.close(); } catch (Exception ignored) {}
    }
%>

<html>
<head><title>人事调整</title><link rel="stylesheet" type="text/css" href="css/style.css"></head>
<body>
<h2>编辑员工信息</h2>

<form method="post" action="employee_edit.jsp?id=<%= empId %>">
  姓名: <input type="text" name="name" value="<%= name %>" required><br>
  性别: <input type="text" name="gender" value="<%= gender %>" required><br>
  年龄: <input type="number" name="age" value="<%= age %>" required><br>
  电话: <input type="text" name="phone" value="<%= phone %>"><br>
  邮箱: <input type="email" name="email" value="<%= email %>"><br>
  职位ID: <input type="number" name="position_id" value="<%= positionId %>" required><br>
  <input type="submit" value="更新信息">
</form>

<p><a href="employee_query.jsp">返回员工列表</a></p>
<p><a href="user_logout.jsp">退出登录</a></p>
</body>
</html>
