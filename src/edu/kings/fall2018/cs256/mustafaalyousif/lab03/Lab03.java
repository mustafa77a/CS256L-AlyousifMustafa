package edu.kings.fall2018.cs256.mustafaalyousif.lab03;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Lab03
 */
@WebServlet("/Lab03")
public class Lab03 extends HttpServlet {
	
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
    public Lab03() {
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
		out.append("<title>My Third Dynamic Web Page</title>");
		out.append("</head>");
		out.append("<body>");
		
		try {			

			Class.forName("org.postgresql.Driver");
			
			//print students
			try(Connection conn = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD)) {
	
				out.append("<h1>Student Information</h1>");	
				
				out.append("<table>");
				out.append("<tr>");
				out.append("<td>#</td>");
				out.append("<td>Name</td>");
				out.append("<td>Dept Name</td>");
				out.append("<td>Credit</td>");
				out.append("</tr>");		
				
				try(ResultSet results = conn.createStatement().executeQuery("SELECT * FROM student")){
					
					//read row and print it
					while(results.next()) {
						
						out.append("<tr>");
						out.append("<td>" + results.getInt("id") + "</td>");
						out.append("<td>" + results.getString("name") + "</td>");
						out.append("<td>" + results.getString("dept_name") + "</td>");
						out.append("<td>" + results.getInt("tot_cred") + "</td>");
						out.append("</tr>");
					}
				}
				
				out.append("</table>");
			
			}
			catch(Exception e) {
				out.append("Error: " + e.getMessage());				
			} 
			
			//print students
			try(Connection conn = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD)) {
	
				out.append("<h1>Classroom Information</h1>");			
				
				out.append("<table>");
				out.append("<tr>");
				out.append("<td>Building</td>");
				out.append("<td>Room #</td>");
				out.append("<td>Capacity</td>");
				out.append("</tr>");		
				
				try(ResultSet results = conn.createStatement().executeQuery("SELECT * FROM classroom")){
					
					//read row and print it
					while(results.next()) {
						
						out.append("<tr>");
						out.append("<td>" + results.getString("building") + "</td>");
						out.append("<td>" + results.getInt("room_number") + "</td>");
						out.append("<td>" + results.getInt("capacity") + "</td>");
						out.append("</tr>");
					}
				}
				
				out.append("</table>");
			
			}
			catch(Exception e) {
				out.append("Error: " + e.getMessage());				
			} 
		
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
