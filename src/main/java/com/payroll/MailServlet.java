
package com.payroll;



import java.io.ByteArrayOutputStream;

import java.io.IOException;

import java.io.OutputStream;

import java.io.PrintWriter;

import java.util.Properties;



import javax.activation.DataHandler;

import javax.activation.DataSource;

import javax.mail.Authenticator;

import javax.mail.Message;

import javax.mail.MessagingException;

import javax.mail.PasswordAuthentication;

import javax.mail.Session;

import javax.mail.Transport;

import javax.mail.internet.InternetAddress;

import javax.mail.internet.MimeBodyPart;

import javax.mail.internet.MimeMessage;

import javax.mail.internet.MimeMultipart;

import javax.mail.util.ByteArrayDataSource;



import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;

import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;



import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.pdmodel.PDPage;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;



@WebServlet("/MailServlet")

public class MailServlet extends HttpServlet {



   @Override

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

       response.setContentType("text/html;charset=UTF-8");

       PrintWriter out = response.getWriter();



       // Obtain form data

       String name = request.getParameter("name");

       String email = request.getParameter("email");

       String subject = request.getParameter("subject");

       String msg = request.getParameter("message");



       // Sender's email and password

       final String username = "haripriyaprojecttest@outlook.com"; // Update with your email address

       final String password = "priya@1708"; // Update with your email password



       // Recipient's email address

       String recipientEmail = email; // Update with the recipient's email address



       // Mail server properties

       Properties props = new Properties();

       props.put("mail.smtp.auth", "true");

       props.put("mail.smtp.starttls.enable", "true");

       props.put("mail.smtp.host", "smtp.office365.com");

       props.put("mail.smtp.port", "587");



       // Create a session with authentication

       Session session = Session.getInstance(props, new Authenticator() {

           @Override

           protected PasswordAuthentication getPasswordAuthentication() {

               return new PasswordAuthentication(username, password);

           }

       });



       try {

           // Create PDF

           ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

           createPDF(byteArrayOutputStream, name, email, subject, msg);

           byte[] pdfContent = byteArrayOutputStream.toByteArray();



           // Create a MimeMessage object

           Message message = new MimeMessage(session);

           message.setFrom(new InternetAddress(username));

           message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)); // Set recipient's email

           message.setSubject(subject);



           // Create the message part

           MimeBodyPart messageBodyPart = new MimeBodyPart();

           messageBodyPart.setText("Please find the attached PDF.");



           // Create the attachment part

           MimeBodyPart attachmentBodyPart = createAttachment(pdfContent);



           // Create Multipart

           MimeMultipart multipart = new MimeMultipart();

           multipart.addBodyPart(messageBodyPart);

           multipart.addBodyPart(attachmentBodyPart);



           // Set the content

           message.setContent(multipart);



           // Send the message

           Transport.send(message);



           // Display success message

           out.println("<center><h2 style='color:green;'>Email Sent Successfully.</h2>");

           out.println("Thank you " + name + ", your message has been submitted.</center>");

       } catch (Exception e) {

           // Display error message

           out.println("<center><h2 style='color:red;'>Error sending email.</h2>");

           out.println("Please try again later.</center>");

           e.printStackTrace(out);

       }

   }



   private void createPDF(OutputStream outputStream, String name, String email, String subject, String msg) throws IOException {

       try (PDDocument document = new PDDocument()) {

           PDPage page = new PDPage();

           document.addPage(page);

           PDRectangle pageSize = page.getMediaBox();

           float pageWidth = pageSize.getWidth();

           float pageHeight = pageSize.getHeight();

           try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

               PDFont headingFont = PDType1Font.HELVETICA_BOLD;

               int headingFontSize = 25;

               int smallHeadingFontSize = 14;

               PDFont font = PDType1Font.HELVETICA;

               int fontSize = 12;

               // Header

               String companyName = "Company Name";

               float headingWidth = headingFont.getStringWidth(companyName) / 1000 * headingFontSize;

               float headingX = (pageWidth - headingWidth) / 2;

               contentStream.beginText();

               contentStream.setFont(headingFont, headingFontSize);

               contentStream.newLineAtOffset(headingX, pageHeight - 50);

               contentStream.showText(companyName);

               contentStream.endText();

               // Address and Salary Slip Label

               String address = "[Address]";

               String salarySlip = "Salary Slip";

               contentStream.beginText();

               contentStream.setFont(headingFont, smallHeadingFontSize);

               contentStream.newLineAtOffset(headingX + 53, pageHeight - 72);

               contentStream.showText(address);

               contentStream.endText();

               contentStream.beginText();

               contentStream.setFont(headingFont, smallHeadingFontSize);

               contentStream.newLineAtOffset(headingX + 50, pageHeight - 90);

               contentStream.showText(salarySlip);

               contentStream.endText();

               // Employee Details

               String[] employeeDetails = {

                   "Employee Name: " + name,

                   "Designation: _______________",

                   "Month & Year: _______________"

               };

               // Drawing Employee Details

               float detailsY = pageHeight - 150;

               for (String detail : employeeDetails) {

                   contentStream.beginText();

                   contentStream.setFont(font, fontSize);

                   contentStream.newLineAtOffset(100, detailsY);

                   contentStream.showText(detail);

                   contentStream.endText();

                   detailsY -= 20;

               }
    
               // Sample data for earnings and deductions tables (You should replace it with real data)

               String[][] earningsData = {

                   {"Basic & DA", "5200.00"},

                   {"HRA", "3000.00"},

                   {"Conveyance", "500.00"},

                   {"", ""},

                   {"Total Addition", "8700.00"}

               };
    
               String[][] deductionsData = {

                   {"Provident Fund", "358.00"},

                   {"ESI", "120.00"},

                   {"Total Deduction", "478.00"},

                   {"NET Salary", "8222.00"}

               };
    
               // Drawing tables

               drawTable(contentStream, 100, detailsY - 30, earningsData, font, fontSize);

               drawTable(contentStream, 300, detailsY - 30, deductionsData, font, fontSize);
               
               
               



contentStream.beginText();


contentStream.setFont(headingFont, smallHeadingFontSize);
contentStream.newLineAtOffset(100,pageHeight - 425);
contentStream.showText("Dollars Eight Thousand Two Hundred Twenty Two Only");
contentStream.endText();
contentStream.beginText();


contentStream.setFont(headingFont, smallHeadingFontSize);


contentStream.newLineAtOffset(100,pageHeight - 425);


contentStream.showText("Dollars Eight Thousand Two Hundred Twenty Two Only");


contentStream.endText();



contentStream.beginText();


contentStream.setFont(headingFont, smallHeadingFontSize);


contentStream.newLineAtOffset(100,pageHeight - 450);


contentStream.showText("Cheque No.:____________");


contentStream.endText();

contentStream.beginText();


contentStream.setFont(headingFont, smallHeadingFontSize);


contentStream.newLineAtOffset(300,pageHeight - 450);


contentStream.showText("Name of Bank:____________");


contentStream.endText();

contentStream.beginText();


contentStream.setFont(headingFont, smallHeadingFontSize);


contentStream.newLineAtOffset(100,pageHeight - 475);


contentStream.showText("Date:____________");


contentStream.endText();


contentStream.beginText();


contentStream.setFont(headingFont, smallHeadingFontSize);


contentStream.newLineAtOffset(100,pageHeight - 500);


contentStream.showText("Signature:____________");


contentStream.endText();


contentStream.beginText();


contentStream.setFont(headingFont, smallHeadingFontSize);


contentStream.newLineAtOffset(300,pageHeight - 500);


contentStream.showText("Director:____________");

           }
    
           document.save(outputStream);

       }

   }




   private MimeBodyPart createAttachment(byte[] pdfContent) throws MessagingException {

       DataSource dataSource = new ByteArrayDataSource(pdfContent, "application/pdf");

       MimeBodyPart attachmentBodyPart = new MimeBodyPart();

       attachmentBodyPart.setDataHandler(new DataHandler(dataSource));

       attachmentBodyPart.setFileName("salary_slip.pdf");

       return attachmentBodyPart;

   }



private void drawTable(PDPageContentStream contentStream, float x, float y, String[][] data, PDFont font, int fontSize) throws IOException {
    final float rowHeight = 20f;
    final float tableWidth = 200f;
    final int cols = data[0].length;
    final int rows = data.length;
    final float colWidth = tableWidth / cols;
    final float tableHeight = rowHeight * rows;
    final float cellMargin = 5f;
 
    // Draw the rows
    float nexty = y;
    for (int i = 0; i <= rows; i++) {
        contentStream.moveTo(x, nexty);
        contentStream.lineTo(x + tableWidth, nexty);
        contentStream.stroke();
        nexty -= rowHeight;
    }
 
    // Draw the columns
    float nextx = x;
    for (int i = 0; i <= cols; i++) {
        contentStream.moveTo(nextx, y);
        contentStream.lineTo(nextx, y - tableHeight);
        contentStream.stroke();
        nextx += colWidth;
    }
 
    // Adding the content
    float textx = x + cellMargin;
    float texty = y - 15;
    for (String[] row : data) {
        for (String text : row) {
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(textx, texty);
            contentStream.showText(text != null ? text : "");
            contentStream.endText();
            textx += colWidth;
        }
        texty -= rowHeight;
        textx = x + cellMargin;
    }
}
}