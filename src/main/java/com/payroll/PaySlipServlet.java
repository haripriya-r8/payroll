package com.payroll;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/PaySlipServlet")
public class PaySlipServlet extends HttpServlet {
    private int id=0;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // PrintWriter to write response
	    response.setContentType("application/pdf");
	    response.setHeader("Content-Disposition", "inline; filename=salary_slip.pdf");
	    String employeeId = request.getParameter("check");
	    id=Integer.parseInt(employeeId);
	    NumberToWordsConverter a=new NumberToWordsConverter();
	    try (PDDocument document = new PDDocument()) {
	        PDPage page = new PDPage();
	        document.addPage(page);

	        // Start a new content stream
	        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
	            // Set font and font size for heading
	            PDFont headingFont = PDType1Font.HELVETICA_BOLD;
	            int headingFontSize = 25;
	            int smallHeadingFontSize = 14;

	            // Set font and font size for content
	            PDFont font = PDType1Font.HELVETICA;
	            int fontSize = 12;

	            // Get page dimensions
	            PDRectangle pageSize = page.getMediaBox();
	            float pageWidth = pageSize.getWidth();
	            float pageHeight = pageSize.getHeight();

	            // Centering calculation
	            float headingWidth = headingFont.getStringWidth("Solartis Techology") / 1000 * headingFontSize;
	            float headingX = (pageWidth - headingWidth) / 2;

	            // Begin text mode
	            contentStream.beginText();

	            // Set font and font size for heading
	            contentStream.setFont(headingFont, headingFontSize);

	            // Set text position for heading
	            contentStream.newLineAtOffset(headingX, pageHeight - 50);
	            // Add heading (Company Name)
	            contentStream.showText("Solartis Techology");

	            // End text mode for heading
	            contentStream.endText();

	            // Draw address
	            contentStream.beginText();
	            contentStream.setFont(headingFont, smallHeadingFontSize);
	            contentStream.newLineAtOffset(headingX + 53, pageHeight - 72);
	            contentStream.showText("[Chennai]");
	            contentStream.endText();

	            // Draw salary slip label
	            contentStream.beginText();
	            contentStream.setFont(headingFont, smallHeadingFontSize);
	            contentStream.newLineAtOffset(headingX + 50, pageHeight - 90);
	            contentStream.showText("Salary Slip");
	            contentStream.endText();

	            // Retrieve employee name from the database
	            String employeeName = getEmployeeNameFromDatabase();
	            String designationa = getdesignationNameFromDatabase();
	            String month=getmonthFromDatabase();
	            String year=getyearFromDatabase();
	            String basic=getbasicFromDatabase();
	            
	            int net=Integer.parseInt(basic);
	            net=net+3500;
	            
	            int deductions=478;
	            
	            net=net-deductions;
	            String str=a.numberToWords(net);
             
	            // Display employee name and designation
	            contentStream.beginText();
	            contentStream.setFont(font, fontSize);
	            float startY = pageHeight - 120; // Initial starting Y position
	            contentStream.newLineAtOffset(100, startY);
	            contentStream.showText("Employee Name: " + employeeName);
	            contentStream.newLineAtOffset(0, -20); // Move down for the next line
	            contentStream.showText("Designation: " + designationa);
	            contentStream.newLineAtOffset(0, -20); // Move down for the next line
	            contentStream.showText("Month & Year: " + month+" / " +year); // Note: Here, employeeName is used for both Month & Year and Employee Name, you might need to replace it with the appropriate variable holding the Month & Year information.
	            contentStream.endText();
/*
	            // Employee details
	            String[] employeeDetails = {
	                    "Designation: _______________",
	                    "Month & Year: _______________"
	            };

	            // Set text position for employee details
	            float textY = pageHeight - 150;
	            for (String detail : employeeDetails) {
	                contentStream.beginText();
	                contentStream.newLineAtOffset(100, textY);
	                contentStream.showText(detail);
	                contentStream.endText();
	                textY -= 20; // Move to the next line
	            }
*/
	            // Sample data for earnings and deductions
	            String[][] earningsData = {
	            		{"Earnings",""},
	                    {"Basic & DA", basic},
	                    {"HRA", "3000.00"},
	                    {"Conveyance", "500.00"},
	                    {"", ""},
	                    {"", ""},
	                    {"", ""},
	                    {"Total Addition", "8700.00"},
	                    {"", ""},
	                    {"",""}
	         
	            };

	            // Sample data for deductions
	            String[][] deductionsData = {
	            		{"Deductions"," "},
	                    {"Provident Fund", "358.00"},
	                    {"ESI", "120.00"},
	                    {"Loan", ""},
	                    {"Profession Tax", ""},
	                    {"TSD/IT", ""},
	                    {"", ""},
	                    {"Total Deduction", "478.00"},
	                    {"NET Salary", String.valueOf(net)},
	                    {"",""}
	        
	            };

	            // Draw earnings table
	            drawTable(contentStream, 100, pageHeight - 220, earningsData, font, fontSize);
	            // Draw deductions table
	            drawTable(contentStream, 300, pageHeight - 220, deductionsData, font, fontSize);
                
	            
	            // Display total earnings and deductions
	            contentStream.beginText();
	            contentStream.setFont(headingFont, smallHeadingFontSize);
	            contentStream.newLineAtOffset(100, pageHeight - 450);
	            contentStream.showText(str);
	            contentStream.endText();

	            // Additional fields like Cheque No., Bank Name, Date, Signature, Director
	            
	            contentStream.beginText();


	            contentStream.setFont(font, smallHeadingFontSize);


	            contentStream.newLineAtOffset(100,pageHeight - 480);


	            contentStream.showText("Cheque No.:____________");


	            contentStream.endText();

	            contentStream.beginText();


	            contentStream.setFont(font, smallHeadingFontSize);


	            contentStream.newLineAtOffset(300,pageHeight - 480);


	            contentStream.showText("Name of Bank:____________");


	            contentStream.endText();

	            contentStream.beginText();


	            contentStream.setFont(font, smallHeadingFontSize);


	            contentStream.newLineAtOffset(100,pageHeight - 500);


	            contentStream.showText("Date:____________");


	            contentStream.endText();


	            contentStream.beginText();


	            contentStream.setFont(font, smallHeadingFontSize);


	            contentStream.newLineAtOffset(100,pageHeight - 550);


	            contentStream.showText("Signature:____________");


	            contentStream.endText();


	            contentStream.beginText();


	            contentStream.setFont(font, smallHeadingFontSize);


	            contentStream.newLineAtOffset(300,pageHeight - 550);


	            contentStream.showText("Director:____________");


	            contentStream.endText();
	            
	            
	            
	        }
	        document.save(response.getOutputStream());
	    } catch (IOException e) {
	        e.printStackTrace();
	        throw new ServletException("Unable to generate PDF", e);
	    }
	}

	// Method to retrieve employee name from the database
	private String getEmployeeNameFromDatabase() {
	    String employeeName = "";
	    String designationa ="";
	    int month=0;
	    int year=0;
	    try {
	    	//int id=104;
	        Class.forName("com.mysql.jdbc.Driver");
	        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payrollnew", "root", "password");
	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM employeefull WHERE id = ?");
	        preparedStatement.setInt(1, id); // Assuming you have an employee ID to retrieve the name
	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            employeeName = resultSet.getString("name");
	            designationa = resultSet.getString("designation");
	            month= resultSet.getInt("month");
	            year=resultSet.getInt("year");

	            
	            
	        }
	        resultSet.close();
	        preparedStatement.close();
	        connection.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return employeeName;
	}

	private String getdesignationNameFromDatabase() {
	   
	    String designationa="";
	    int month=0;
	    int year=0;
	    try {
	    	//int id=104;
	        Class.forName("com.mysql.jdbc.Driver");
	        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payrollnew", "root", "password");
	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT designation FROM employeefull WHERE id = ?");
	        preparedStatement.setInt(1, id); // Assuming you have an employee ID to retrieve the name
	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	          
	            designationa = resultSet.getString("designation");
	            
	            
	            
	        }
	        resultSet.close();
	        preparedStatement.close();
	        connection.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return designationa;
	}
	private String getmonthFromDatabase() {
		   
	  
	    int month=0;
	   
	    try {
	    	
	        Class.forName("com.mysql.jdbc.Driver");
	        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payrollnew", "root", "password");
	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT month FROM employeefull WHERE id = ?");
	        preparedStatement.setInt(1, id); // Assuming you have an employee ID to retrieve the name
	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	           
	            month = resultSet.getInt("month");
	       
	        }
	        resultSet.close();
	        preparedStatement.close();
	        connection.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return  String.valueOf(month);
	}

	private String getyearFromDatabase() {
		   
	    
	    int year=0;
	    try {
	    	//int id=104;
	        Class.forName("com.mysql.jdbc.Driver");
	        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payrollnew", "root", "password");
	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT year FROM employeefull WHERE id = ?");
	        preparedStatement.setInt(1, id); // Assuming you have an employee ID to retrieve the name
	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	          
	            year = resultSet.getInt("year");
	            
	            
	            
	        }
	        resultSet.close();
	        preparedStatement.close();
	        connection.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return  String.valueOf(year);
	}
	private String getbasicFromDatabase() {
	    int basic=0;
	    
	    try {
	    
	        Class.forName("com.mysql.jdbc.Driver");
	        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payrollnew", "root", "password");
	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT base FROM employeefull WHERE id = ?");
	        preparedStatement.setInt(1, id); // Assuming you have an employee ID to retrieve the name
	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            
	            basic= resultSet.getInt("base");
	         
	        }
	        resultSet.close();
	        preparedStatement.close();
	        connection.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return String.valueOf(basic);
	}
	/*private String getnetFromDatabase() {
	    int net=0;
	    try {
	    	
	        Class.forName("com.mysql.jdbc.Driver");
	        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payrollnew", "root", "password");
	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT base FROM employeefull WHERE id = ?");
	        preparedStatement.setInt(1, id); // Assuming you have an employee ID to retrieve the name
	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	        	net= resultSet.getInt("base");

	            
	            
	        }
	        resultSet.close();
	        preparedStatement.close();
	        connection.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return String.valueOf(net);
	}*/
// Method to draw a table


private static void drawTable(PDPageContentStream contentStream, float x, float y, String[][] data, PDFont font, int fontSize) throws IOException {


final int rows = data.length;


final int cols = data[0].length;


final float rowHeight = 20f;


final float tableWidth = 200f;


final float tableHeight = rowHeight * rows;


final float colWidth = tableWidth / (float) cols;


final float cellMargin = 5f;






// Draw the rows


float nextY = y;


for (int i = 0; i <= rows; i++) {


contentStream.moveTo(x, nextY);


contentStream.lineTo(x + tableWidth, nextY);


contentStream.stroke();


nextY -= rowHeight;


}






// Draw the columns


float nextX = x;


for (int i = 0; i <= cols; i++) {


contentStream.moveTo(nextX, y);


contentStream.lineTo(nextX, y - tableHeight);


contentStream.stroke();


nextX += colWidth;


}






// Draw the data


contentStream.setFont(font, fontSize);


float textX = x + cellMargin;


float textY = y - 15; // Adjust text position vertically center
for (int i = 0; i < data.length; i++) {
for (int j = 0; j < data[i].length; j++) {
contentStream.beginText();
contentStream.newLineAtOffset(textX, textY);
contentStream.showText(data[i][j]);
contentStream.endText();
textX += colWidth;
}


textY -= rowHeight;


textX = x + cellMargin;


}


}
class NumberToWordsConverter {

    private static final String[] UNITS = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
    private static final String[] TEENS = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    private static final String[] TENS = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
    private static final String[] THOUSANDS = {"", "Thousand", "Million", "Billion", "Trillion"};

    public static String numberToWords(int number) {
        if (number == 0) {
            return "Zero";
        }

        int i = 0;
        String words = "";

        do {
            int n = number % 1000;
            if (n != 0) {
                words = convertLessThanOneThousand(n) + THOUSANDS[i] + " " + words;
            }
            i++;
            number /= 1000;
        } while (number > 0);

        return words;
    }

    public static String convertLessThanOneThousand(int number) {
        String current;

        if (number % 100 < 20) {
            current = UNITS[number % 100];
            number /= 100;
        } else {
            current = UNITS[number % 10];
            number /= 10;

            current = TENS[number % 10] + " " + current;
            number /= 10;
        }

        if (number == 0) {
            return current;
        }

        return UNITS[number] + " Hundred " + current;
    }
}


}