package edu.kings.fall2018.cs256.mustafaalyousif.lab02;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Lab02
 */
@WebServlet("/Lab02")
public class Lab02 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Lab02() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Random random = new Random();
		
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
		
		out.append(".number {\r\n" + 
				"  font-family: monospace;\r\n" + 
				"}");
		
		out.append("</style>");
		
		out.append("<meta charset=\"UTF-8\">");
		out.append("<title>My First Dynamic Web Page</title>");
		out.append("</head>");
		out.append("<body>");

		out.append("<p>A Java program wrote this document.</p>");
		
		out.append("<h1>Your lucky numbers for today</h1>");

		//create random array
		int[] randomArray = new int[20];
		
		for (int idx = 0; idx < randomArray.length; ++idx) 
		{
			randomArray[idx] = random.nextInt();
		}
		
		//create table
		out.append("<table>");
		
		for (int idx = 0; idx < randomArray.length; ++idx) 
		{			
			//row
			out.append("<tr>");
			
			//column
			out.append("<td>");		
			out.append(String.valueOf(randomArray.length - idx));			
			out.append("</td>");
			//column			
			out.append("<td class='number'>");	
			out.append(String.valueOf(randomArray[idx]));	
			out.append("</td>");
			
			out.append("</tr>");
		}
		
		out.append("</table>");
		
		//last is odd or even
		if (randomArray[randomArray.length -1] % 2 == 0) 
		{//even
			out.append("Your luckiest number is even!");
		}
		else 
		{
			out.append("Your luckiest number is odd!");
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
