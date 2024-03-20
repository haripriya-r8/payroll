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

@WebServlet("/SalaryCalculationServlet")
public class SalaryCalculationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve input parameters
        int leavepenalty=0;
        int salary=0;
        
        int tax=0;
        int smonth=0;
        int syear=0;
        

        try {
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/payrollnew","root", "password");
			Class.forName("com.mysql.cj.jdbc.Driver");
			String employeeId = request.getParameter("employeeId");
			String name = request.getParameter("name");
	        String designation = request.getParameter("designation");
	        String department= request.getParameter("department");
	        String leaveDays= request.getParameter("leaveDays");
	        String nightShift=request.getParameter("nightShift");
	        String month= request.getParameter("month");
	        String year= request.getParameter("year");
	        smonth=Integer.parseInt(month);
	        syear=Integer.parseInt(year);

	        
	        int leave=Integer.parseInt(leaveDays);
	        
	        if(designation.equals("Junior"))
	        {
	        	if(department.equals("QA"))
	        	{
	        		salary=600000;
	        	}
	        	else if(department.equals("DevOps"))
	        	{
	        		salary=620000;
	        	}
	        	else if(department.equals("Release"))
	        	{
	        		salary=640000;
	        	}
	        	else if(department.equals("Dev"))
	        	{
	        		salary=660000;
	        	}
	        	else if(department.equals("BA"))
	        	{
	        		salary=680000;
	        	}
	        }
	        else if(designation.equals("Senior"))
	        {
	        	if(department.equals("QA"))
	        	{
	        		salary=700000;
	        	}
	        	else if(department.equals("DevOps"))
	        	{
	        		salary=720000;
	        	}
	        	else if(department.equals("Release"))
	        	{
	        		salary=740000;
	        	}
	        	else if(department.equals("Dev"))
	        	{
	        		salary=760000;
	        	}
	        	else if(department.equals("BA"))
	        	{
	        		salary=780000;
	        	
	        	}
	        }
	        else if(designation.equals("Architect"))
	        {
	        	if(department.equals("QA"))
	        	{
	        		salary=800000;
	        	}
	        	else if(department.equals("DevOps"))
	        	{
	        		salary=820000;
	        	}
	        	else if(department.equals("Release"))
	        	{
	        		salary=840000;
	        	}
	        	else if(department.equals("Dev"))
	        	{
	        		salary=860000;
	        	}
	        	else if(department.equals("BA"))
	        	{
	        		salary=880000;
	        	}
	        }
	       
	        else if(designation.equals("TL"))
	        {
	        	if(department.equals("QA"))
	        	{
	        		salary=900000;
	        	}
	        	else if(department.equals("DevOps"))
	        	{
	        		salary=920000;
	        	}
	        	else if(department.equals("Release"))
	        	{
	        		salary=940000;
	        	}
	        	else if(department.equals("Dev"))
	        	{
	        		salary=960000;
	        	}
	        	else if(department.equals("BA"))
	        	{
	        		salary=1000000;
	        	}
	        }
	        else if(designation.equals("PM"))
	        {
	        	if(department.equals("QA"))
	        	{
	        		salary=1200000;
	        	}
	        	else if(department.equals("DevOps"))
	        	{
	        		salary=1400000;
	        	}
	        	else if(department.equals("Release"))
	        	{
	        		salary=1600000;
	        	}
	        	else if(department.equals("Dev"))
	        	{
	        		salary=1800000;
	        	}
	        	else if(department.equals("BA"))
	        	{
	        		salary=200000;
	        	}
	        }
	       
	       
	        leavepenalty=leave*500;
	       salary=salary-leavepenalty;
	       
	       
	       if(nightShift.equals("Yes")) {
	    	   salary+=10000;
	       }
	        
	        
	        if(salary<300000)
	        {
	        	tax=0;
	        }
	        else if(salary>=300000 && salary<600000)
	        {
	        	tax=(int) (salary*0.05);
	        }
	        else if(salary>=600000 && salary<900000)
	        {
	        	tax=(int) (salary*0.1);
	        }
	        else if(salary>=900000 && salary<1200000)
	        {
	        	tax=(int) (salary*0.15);
	        }
	        else if(salary>=1200000 && salary<1500000)
	        {
	        	tax=(int) (salary*0.20);
	        }
	        else if(salary>=1500000)
	        {
	        	tax=(int) (salary*0.30);
	        }
	        
//	       String sql = "INSERT INTO salarynew (id,salary) VALUES (?,?)";
//	        PreparedStatement statement = con.prepareStatement(sql);
//	        statement.setString(1, employeeId);
//            statement.setInt(2, salary);
//            statement.executeUpdate();
            String sqlone = "INSERT INTO employeefull (id,name,designation,department,month,year,base,leavedays,tax,nightshift) VALUES (?,?,?,?,?,?,?,?,?,?)";
	        PreparedStatement statement1 = con.prepareStatement(sqlone);
	        statement1.setString(1, employeeId);
	        statement1.setString(2, name);
	        statement1.setString(3, designation);
            statement1.setString(4, department);
            statement1.setInt(5, smonth);
            statement1.setInt(6, syear);
            statement1.setInt(7, salary);
            statement1.setInt(8, leavepenalty);
            statement1.setInt(9, tax);
            statement1.setString(10, nightShift);
           
            int x=statement1.executeUpdate();
            if(x==0) {
            	System.out.println("Hello");
            }
            else {
            	System.out.println("NO");
            }
            //System.out.println("hello");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        	catch (ClassNotFoundException e) {
        
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
}
