package com.payroll;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Update")
public class Update extends HttpServlet {
    // JDBC URL, username, and password of MySQL server
    

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve input parameters
        
    	try {
    	    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/payrollnew", "root", "password");
    	    Class.forName("com.mysql.cj.jdbc.Driver");

    	    String employeeId1 = request.getParameter("employeeId");
    	    String name1 = request.getParameter("name");
    	    String designation1 = request.getParameter("designation");
    	    String month1 = request.getParameter("month");
    	    String year1 = request.getParameter("year");
    	    String nightshift1 = request.getParameter("nightshift");

    	    String sql = "UPDATE employeefull SET name=?, designation=?, month=?, year=?, nightshift=? WHERE id=?";
    	    PreparedStatement statement = con.prepareStatement(sql);
    	  statement.setString(6, employeeId1);
    	    statement.setString(1, name1);
    	    statement.setString(2, designation1);
    	    statement.setString(3, month1);
    	    statement.setString(4, year1);
    	    statement.setString(5, nightshift1);
    	    //statement.setString(6, employeeId1);

    	    statement.executeUpdate();
    	} catch (SQLException e) {
    	    e.printStackTrace();
    	} catch (ClassNotFoundException e) {
    	    e.printStackTrace();
    	}

    }
}
