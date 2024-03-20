package com.payroll;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/PrintEmployeeRecordsServlet")
public class PrintEmployeeRecordsServlet extends HttpServlet {
    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/payrollnew";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "password";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/html");

        try (
            // Establish a connection to MySQL database
            Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            // SQL query to select all records from the employee table
            PreparedStatement statement = con.prepareStatement("SELECT * FROM employeefull");
            // Execute the query and obtain the ResultSet
            ResultSet resultSet = statement.executeQuery();
            // PrintWriter to write response
            PrintWriter out = response.getWriter();
        ) {
            // Print HTML header
            out.println("<html><head><title>Employee Records</title>");
            // CSS for table styling
            out.println("<style>");
            out.println("table { border-collapse: collapse; width: 100%; }");
            out.println("th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<h1>Employee Records</h1>");
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Designation</th><th>Month</th><th>Year</th></tr>");

            // Iterate over the ResultSet and print each record
            while (resultSet.next()) {
                out.println("<tr>");
                out.println("<td>" + resultSet.getString("id") + "</td>");
                out.println("<td>" + resultSet.getString("designation") + "</td>");
                out.println("<td>" + resultSet.getString("month") + "</td>");
                out.println("<td>" + resultSet.getString("year") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");

            // Button to go to admincontrol.html
            out.println("<button onclick=\"goToAdminControl()\">Go to Admin Control</button>");

            // JavaScript function to redirect to admincontrol.html
            out.println("<script>");
            out.println("function goToAdminControl() {");
            out.println("  window.location.href = 'admincontrol.html';");
            out.println("}");
            out.println("</script>");

            out.println("</body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
