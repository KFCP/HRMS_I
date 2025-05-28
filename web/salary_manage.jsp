<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.time.LocalDate" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("user_login.jsp");
        return;
    }

    String empIdStr = request.getParameter("employee_id");
    String action = request.getParameter("action");
    
    // 处理添加薪酬记录的表单提交
    if ("add_salary".equals(action) && empIdStr != null && !empIdStr.equals("*")) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            double amount = Double.parseDouble(request.getParameter("salary_amount"));
            String payDate = request.getParameter("pay_date");
            int empId = Integer.parseInt(empIdStr);
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hrms_db?useSSL=false&serverTimezone=UTC", "root", "password");
            
            String insertSql = "INSERT INTO salaries (employee_id, salary_amount, pay_date) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(insertSql);
            ps.setInt(1, empId);
            ps.setDouble(2, amount);
            ps.setString(3, payDate);
            
            ps.executeUpdate();
            out.println("<p style='color:green;'>薪酬记录添加成功!</p>");
            
        } catch (Exception e) {
            out.println("<p style='color:red;'>添加薪酬记录失败: " + e.getMessage() + "</p>");
        } finally {
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
            if (conn != null) try { conn.close(); } catch (Exception ignored) {}
        }
    }
%>

<html>
<head><title>员工薪酬管理</title><link rel="stylesheet" type="text/css" href="css/style.css"></head>
<body>
<h2>员工薪酬信息管理</h2>

<form method="get" action="salary_manage.jsp">
  员工ID: <input type="text" name="employee_id" value="<%= empIdStr != null ? empIdStr : "" %>" required>
  <input type="submit" value="查询薪酬">
</form>

<%
if (empIdStr != null && !empIdStr.trim().isEmpty()) {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hrms_db?useSSL=false&serverTimezone=UTC", "root", "password");

    // 查询薪酬记录
    String sql;
    if (empIdStr.equals("*")) {
        // 如果输入星号，查询所有员工的薪资记录
        sql = "SELECT s.salary_amount, s.pay_date, e.name as employee_name, e.id as employee_id " +
              "FROM salaries s JOIN employees e ON s.employee_id = e.id ORDER BY s.pay_date DESC";
        ps = conn.prepareStatement(sql);
    } else {
        // 按指定员工ID查询
        int empId = Integer.parseInt(empIdStr);
        sql = "SELECT salary_amount, pay_date FROM salaries WHERE employee_id = ? ORDER BY pay_date DESC";
        ps = conn.prepareStatement(sql);
        ps.setInt(1, empId);
    }
    rs = ps.executeQuery();

    out.println("<table border='1' cellpadding='5'>");
    
    // 根据查询类型显示不同的表头
    if (empIdStr.equals("*")) {
        out.println("<tr><th>员工ID</th><th>员工姓名</th><th>薪资金额</th><th>支付日期</th></tr>");
    } else {
        out.println("<tr><th>薪资金额</th><th>支付日期</th></tr>");
    }

    boolean hasRecord = false;
    while (rs.next()) {
        hasRecord = true;
        out.println("<tr>");
        if (empIdStr.equals("*")) {
            out.println("<td>" + rs.getInt("employee_id") + "</td>");
            out.println("<td>" + rs.getString("employee_name") + "</td>");
        }
        out.println("<td>" + rs.getDouble("salary_amount") + "</td>");
        out.println("<td>" + rs.getDate("pay_date") + "</td>");
        out.println("</tr>");
    }
    if (!hasRecord) {
        if (empIdStr.equals("*")) {
            out.println("<tr><td colspan='4'>无薪酬记录</td></tr>");
        } else {
            out.println("<tr><td colspan='2'>无薪酬记录</td></tr>");
        }
    }
    out.println("</table>");
    
    // 添加薪酬记录表单 - 只在查询特定员工时显示
    if (!empIdStr.equals("*")) {
        %>
        <h3>添加薪酬记录</h3>
        <form method="post" action="salary_manage.jsp">
            <input type="hidden" name="action" value="add_salary">
            <input type="hidden" name="employee_id" value="<%= empIdStr %>">
            薪酬金额: <input type="number" step="0.01" name="salary_amount" required><br>
            支付日期: <input type="date" name="pay_date" value="<%= LocalDate.now() %>" required><br>
            <input type="submit" value="添加薪酬记录">
        </form>
        <%
    }
    
    } catch (Exception e) {
        out.println("<p>错误：" + e.getMessage() + "</p>");
    } finally {
        if (rs != null) try { rs.close(); } catch (Exception ignored) {}
        if (ps != null) try { ps.close(); } catch (Exception ignored) {}
        if (conn != null) try { conn.close(); } catch (Exception ignored) {}
    }
}
%>

<p><a href="employee_query.jsp">返回员工列表</a></p>
<p><a href="user_logout.jsp">退出登录</a></p>

</body>
</html>
