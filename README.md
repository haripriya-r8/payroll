# payroll

Payroll System
Done by:Haripriya R

  Workflow explanation:
      1)Form submission using HTML:All the details are fetched from the user using html form.CSS was used to enhance style and javascript was used to check
      validity.
      
      2)Servlet to interact with database:Servlet files written in java were used to get details from the html form,perform necessary manipulations and store it in the
      database.SQL queries were also employed to take care of database maintenance.
      
      3)SQL database:SQL database was used to store employee details.
      
  Database:
      ● Database name:payrollnew
      ● Table name:employeefull
      
  User roles and functions:
    Home page:
      ● Home.html
      
    Login Approval:
      ● Admin:AdminLoginApproval.html
      ● Employee:EmployeeloginApproval.html

    Admin functions:
      Add employee details:
        ● HTML:salaryenter.html
        ● Servlet:salarycalculationservlet.java
        
      Update employee details:
        ● HTML:update.html
        ● Servlet:update.java
        
      Print all employee details:
        ● HTML:PrintAllEmployeeDetails.html
        ● Servlet:PrintEmployeeRecords.java
        
      Pdf generation:
        ● HTML:pdfgeneration.html
        ● Servlet:payslipservlet.java
        
      Send payslip to mail:
        ● HTML:SendMail.html
        ● Servlet:MailServlet.java
        
    Employee functions:
      View details:
        ● HTML:ViewDetailsEmp.html
        ● Servlet:ViewDetailsEmp.java
        
      View salary:
        ● HTML:ViewSalary.html
        ● Servlet:SeeSalary.java

        
  Explanation:
    The payroll system is developed using HTML,CSS,Javascript for frontend and Java and SQL for backend.Tomcat server was deployed to host local server.
    The payroll system consists of 2 user options-Administrator and Employee.Administrator has 5 functions namely adding employee details,updating employee
    details,printing all the employee details,pdf generation of payslip and sending payslip in mail.
  Login approval:
    Admin login approval function and employee login approval function check credibility of admin credentials and employee credentials respectively.
  Admin functions:
    1. Add employee details:This function gets the details of employee in a form and stores it in the database.It calls the salary calculation servlet and all the
       tax,leave,night shift allowance calculations are done there.
    3. Update employees function:This function also gets the details in a form and can be used to update employee details if any change is needed.
    4. Print all employees:This function displays all the rows in the employee table.
    5. Pdf generation:This function gets the id of the employee and generates the employee’s payslip as pdf.
    6. Send Mail:This gets the name and email id of the employee and sends their payslip via mail with pdf as attachment.It was implemented using SMTP protocol.
  Employee functions:
    1. View details:The employee can view their details using this function.
    2. View salary:This function enables the employee to view their salary.
