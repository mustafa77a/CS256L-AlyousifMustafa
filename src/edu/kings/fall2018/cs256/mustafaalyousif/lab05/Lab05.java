package edu.kings.fall2018.cs256.mustafaalyousif.lab05;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Lab05
 */
@WebServlet("/Lab05")
public class Lab05 extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * password to database
	 */
	private static final String PASSWORD = "";

	/**
	 * user name to database
	 */
	private static final String USERNAME = "mustafaalyousif";

	/**
	 * connection URL
	 */
	private static final String CONNECTION_URL = "jdbc:postgresql://cs256.kings.edu/exampleUniversity";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Lab05() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		
		
		out.append("<!DOCTYPE html>");
		out.append("<html lang=\"en\">");
		out.append("<head>");
		
		//style
		out.append("<style>");
		
		out.append("table, tr, td {\r\n" + 
				"  border: 1px solid black;\r\n" + 
				"}");
		
		out.append("table{\r\n" + 
				"  margin-left: auto; \r\n" + 
				"  margin-right: auto; \r\n" + 
				"}");
		
		out.append("</style>");
		
		out.append("<meta charset=\"UTF-8\">");
		out.append("<title>My Fifth Dynamic Web Page</title>");
		out.append("</head>");
		out.append("<body>");
		
		out.append("<h1>Prepared Statements Practice</h1>");
		
		try {			

			Class.forName("org.postgresql.Driver");
			
			//print students
			try(Connection conn = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD)) {	
					
				
				String query = "SELECT name FROM instructor WHERE dept_name = ? AND salary > ?";
				
				try(PreparedStatement stmtNames = conn.prepareStatement( query)) {
					
					stmtNames.setString(1, "Comp. Sci."); 
					stmtNames.setDouble(2, 50000);
					
					out.append("<ul>");
					
					try(ResultSet rs = stmtNames.executeQuery()){
						
						while(rs.next()) {
							out.append("<li>");
							out.append(rs.getString(1));
							out.append("</li>");
						}
					}
					
					out.append("</ul>");
				}
				
				//parameter map
				Map<String, String[]> parameters = request.getParameterMap();
				
				if (parameters.isEmpty()) 
				{//1. Someone loaded the page without clicking the form button, in which case the map will be empty.
					out.append("<p>Please fill in the form.</p>");
				}else {
					//2. Someone pressed the submit button without entering data into the textboxes,
					//in which case the map will
					//contain a key for each input, all mapped to the empty string.
					//
					//3. Someone pressed the submit button after having entered data into some of the textboxes, 
					//in which case the
					//map will contain a key for each input, mapped to the values that were entered 
					//or the empty string.
					if (parameters.get("student_id") != null && parameters.get("student_id").length == 1 &&
							!parameters.get("student_id")[0].equals("") && parameters.get("semester") != null &&
							parameters.get("semester").length == 1 &&
									!parameters.get("semester")[0].equals("") && parameters.get("year") != null &&
											parameters.get("year").length == 1 &&
											!parameters.get("year")[0].equals("")) 
					{//case 3
						
						//year must be the integer
						try {
							
							//student id
							String studentID = parameters.get("student_id")[0];
							//semester
							String semester = parameters.get("semester")[0];
							//year
							int year = Integer.parseInt(parameters.get("year")[0]);							
							
							query = "SELECT * FROM student WHERE id = ?";
							
							try(PreparedStatement stmtStudent = conn.prepareStatement( query)) {
							
								stmtStudent.setString(1, studentID);
								
								try(ResultSet studentRS = stmtStudent.executeQuery()){
									
									if (!studentRS.isBeforeFirst()) {
										
										out.append("<p>You must type a student ID to see the student's schedule.</p>");
										
									}else {
										
										query = "SELECT course.title, section.sec_id, instructor.name, section.building, section.room_number "
												+ " FROM student, takes, section, course, teaches, instructor"
												+ " WHERE student.id = takes.id"
												+ " and takes.course_id = section.course_id"
												+ " and takes.sec_id = section.sec_id"
												+ " and takes.semester = section.semester"
												+ " and takes.year = section.year"
												+ " and section.course_id = course.course_id"
												+ " and teaches.course_id = section.course_id"
												+ " and teaches.sec_id = section.sec_id"
												+ " and teaches.semester = section.semester"
												+ " and teaches.year = section.year"
												+ " and instructor.id = teaches.id"
												+ " and student.id = ?"
												+ " and section.semester = ?"
												+ " and section.year = ?";
										
										try(PreparedStatement stmtSchedule = conn.prepareStatement(query)) {											
											
											stmtSchedule.setString(1, studentID);
											stmtSchedule.setString(2, semester);
											stmtSchedule.setInt(3, year);
											
											try(ResultSet rs = stmtSchedule.executeQuery()){
											
												out.append("<table>");
												out.append("<tr>");
												out.append("<td>Course title</td>");
												out.append("<td>Session ID</td>");
												out.append("<td>Instructor name</td>");
												out.append("<td>Building</td>");
												out.append("<td>Room number</td>");
												out.append("</tr>");		
											
												//read row and print it
												while(rs.next()) {
													
													out.append("<tr>");
													out.append("<td>" + rs.getString("title") + "</td>");
													out.append("<td>" + rs.getInt("sec_id") + "</td>");
													out.append("<td>" + rs.getString("name") + "</td>");
													out.append("<td>" + rs.getString("building") + "</td>");
													out.append("<td>" + rs.getString("room_number") + "</td>");
													out.append("</tr>");
												}
												
												out.append("</table>");
											}
										}
									}
								}
							}
							
						}catch(NumberFormatException e) {
							out.append("<p>You must enter valid year.</p>");
						}
					}else {//case 2
						out.append("<p>You must enter student ID, semester and year.</p>");
					}
				}
			
			}
			catch(Exception e) {
				out.append("Error: " + e.getMessage());				
			}			
			
			
			//add a form in which the user enters three things: a student id, a
			//semester, and a year.
			out.append("<form action=\"Lab05\" method=\"get\">");
			out.append("<ul>");
			out.append("<li>");
			out.append("<label style=\"margin: 5px; display: inline-block;width:130px;\" for=\"student_id\">Student ID:</label>");
			out.append("<input type=\"text\" name=\"student_id\" id=\"student_id\">");
			out.append("</li>");
			out.append("<li>");
			out.append("<label style=\"margin: 5px; display: inline-block;width:130px;\" for=\"semester\">Semester:</label>");
			out.append("<input type=\"text\" name=\"semester\" id=\"semester\">");
			out.append("</li>");
			out.append("<li>");
			out.append("<label style=\"margin: 5px; display: inline-block;width:130px;\" for=\"year\">Year:</label>");
			out.append("<input type=\"text\" name=\"year\" id=\"year\">");
			out.append("</li>");
			out.append("<li>");
			out.append("<label style=\"margin: 5px; display: inline-block;width:130px;\" for=\"submit\"></label>");
			out.append("<input type=\"submit\">");
			out.append("</li>");
			out.append("</ul>");
			out.append("</form>");
			
			
		
		}catch(Exception e) {
			// any error-handling code goes here
			out.append("Error: " + e.getMessage());
			
		} 
		
		out.append("</body>");
		out.append("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
