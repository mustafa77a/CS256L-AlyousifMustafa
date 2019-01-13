package edu.kings.fall2018.cs256.mustafaalyousif.lab04;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Lab04
 */
@WebServlet("/Lab04")
public class Lab04 extends HttpServlet {
	
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
    public Lab04() {
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
		out.append("<title>My Fourth Dynamic Web Page</title>");
		out.append("</head>");
		out.append("<body>");
		
		out.append("<h1>Part 1</h1>");
		out.append("<form action=\"Lab04\" method=\"get\">");
		out.append("<ul>");
		out.append("<li>");
		out.append("<label style=\"margin: 5px; display: inline-block;width:130px;\" for=\"firstname\">First Name:</label>");
		out.append("<input type=\"text\" name=\"firstname\" id=\"firstname\">");
		out.append("</li>");
		out.append("<li>");
		out.append("<label style=\"margin: 5px; display: inline-block;width:130px;\" for=\"middlename\">Middle Name:</label>");
		out.append("<input type=\"text\" name=\"middlename\" id=\"middlename\">");
		out.append("</li>");
		out.append("<li>");
		out.append("<label style=\"margin: 5px; display: inline-block;width:130px;\" for=\"lastname\">Last Name:</label>");
		out.append("<input type=\"text\" name=\"lastname\" id=\"lastname\">");
		out.append("</li>");
		out.append("<li>");
		out.append("<label style=\"margin: 5px; display: inline-block;width:130px;\" for=\"submit\"></label>");
		out.append("<input type=\"submit\">");
		out.append("</li>");
		out.append("</ul>");
		out.append("</form>");
		
		out.append("<h1>Part 2</h1>");
		
		//parameter map
		Map<String, String[]> parameters = request.getParameterMap();
		
		if (parameters.isEmpty()) 
		{//1. Someone loaded the page without clicking the form button, in which case the map will be empty.
			out.append("<p>Please fill in the form to receive a personalized greeting.</p>");
		}else {
			//2. Someone pressed the submit button without entering data into the textboxes,
			//in which case the map will
			//contain a key for each input, all mapped to the empty string.
			//
			//3. Someone pressed the submit button after having entered data into some of the textboxes, 
			//in which case the
			//map will contain a key for each input, mapped to the values that were entered 
			//or the empty string.
			if (parameters.get("firstname") != null && parameters.get("firstname").length == 1 &&
					!parameters.get("firstname")[0].equals("") && parameters.get("lastname") != null &&
					parameters.get("lastname").length == 1 &&
							!parameters.get("lastname")[0].equals("")) 
			{//case 3
				if (parameters.get("middlename") != null && parameters.get("middlename").length == 1 &&
						!parameters.get("middlename")[0].equals("")) {
					out.append("<p>Hello, " + parameters.get("firstname")[0] + " " +
							parameters.get("middlename")[0].toUpperCase().charAt(0) + ". " +
							parameters.get("lastname")[0] + "!</p>");
				}else {
					out.append("<p>Hello, " + parameters.get("firstname")[0] + " " +
							parameters.get("lastname")[0] + "!</p>");
				}
				
				out.append("<h1>Part 3</h1>");
				try {			

					Class.forName("org.postgresql.Driver");
					
					//print students
					try(Connection conn = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD)) {	
							
						
						String query = "SELECT * FROM instructor WHERE name = '" + parameters.get("lastname")[0] + "';";
						
						try(ResultSet insRS = conn.createStatement().executeQuery(query)){
							
							if (!insRS.isBeforeFirst()) {
								
								out.append("<p>You must type a last name to see an instructor's schedule.</p>");
								
							}else {
								
								query = "SELECT course.title, section.sec_id, section.semester, section.year  FROM instructor, teaches, section, course"
										+ " WHERE instructor.id = teaches.id"
										+ " and teaches.course_id = section.course_id"
										+ " and teaches.sec_id = section.sec_id"
										+ " and teaches.semester = section.semester"
										+ " and teaches.year = section.year"
										+ " and section.course_id = course.course_id"
										+ " and instructor.name = '" + parameters.get("lastname")[0] + "';";
								
								try(ResultSet rs = conn.createStatement().executeQuery(query)){
								
									out.append("<table>");
									out.append("<tr>");
									out.append("<td>Course title</td>");
									out.append("<td>Session ID</td>");
									out.append("<td>Semester</td>");
									out.append("<td>Year</td>");
									out.append("</tr>");		
								
									//read row and print it
									while(rs.next()) {
										
										out.append("<tr>");
										out.append("<td>" + rs.getString("title") + "</td>");
										out.append("<td>" + rs.getInt("sec_id") + "</td>");
										out.append("<td>" + rs.getString("semester") + "</td>");
										out.append("<td>" + rs.getInt("year") + "</td>");
										out.append("</tr>");
									}
									
									out.append("</table>");
								}
							}
						}
					
					}
					catch(Exception e) {
						out.append("Error: " + e.getMessage());				
					}
				
				}catch(Exception e) {
					// any error-handling code goes here
					out.append("Error: " + e.getMessage());
					
				} 
			}else {//case 2
				out.append("<p>You must enter both a first and last name.</p>");
			}
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
