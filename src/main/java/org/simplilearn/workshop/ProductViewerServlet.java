package org.simplilearn.workshop;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.simplilearn.workshop.util.StringUtil;

@WebServlet("/ProductViewer")
public class ProductViewerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int pid=-1;
	@Override
    public void init() throws ServletException{
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		System.out.println("JDBC Driver Loaded");
    	}
    	catch(ClassNotFoundException e) {
    		e.printStackTrace();
    	}
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		sendPageHeader(response);
		sendSearchForm(response);
		sendPageFooter(response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		pid=Integer.parseInt(request.getParameter("id"));
		sendPageHeader(response);
		sendSearchForm(response);
		try {
			sendSearchResult(response);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		sendPageFooter(response);
	}
	private void sendSearchForm(HttpServletResponse response) 
			throws ServletException, IOException 
	{
		PrintWriter out = response.getWriter();
		out.println("<br><h1>Search Form</h1><br>");
		out.println("<br><h2>Enter ID of the product</h2>");
		out.println("<br><form method=post>");
		out.println("Product ID: <INPUT TYPE=TEXT NAME=id VALUE="+Integer.toString(pid)+" >");
		out.println("<INPUT type=SUBMIT>");
		out.println("</form><br>");

	}
	private void sendPageHeader(HttpServletResponse response) 
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE><br><h2>Displying records of selected Product ID</h2></TITLE>");
		out.println("</HEAD>");
		out.println("<BODY>");
		out.println("<CENTER>");
		
	}
	private void sendPageFooter(HttpServletResponse response)
			throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		out.println("</CENTER>");
		out.println("</BODY>");
		out.println("</HTML>");
	}
	private void sendSearchResult(HttpServletResponse response) throws IOException, SQLException {
		PrintWriter out = response.getWriter();
		try {
		int flag=0;
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/teksystems", "root", "admin");
		System.out.println("got connection");
		Statement s= con.createStatement();
		
		String sql="SELECT id, ProductName, ProductColor, ProductPrice from product where id="+Integer.toString(pid);
		ResultSet rs = s.executeQuery(sql);
		while(rs.next()) {
			out.println("<TABLE border=1 padding=1 margin=0.5>");
			out.println("<TR>");
			out.println("<TH>Product ID </TH>");
			out.println("<TH>Product Name</TH>");
			out.println("<TH>Product Color</TH>");
			out.println("<TH>Product Price</TH>");
			out.println("</TR>");
			out.println("<TR>");
			out.println("<TD>"+StringUtil.encodeHtmlTag(rs.getString(1)) + "</TD>");
			out.println("<TD>"+StringUtil.encodeHtmlTag(rs.getString(2)) + "</TD>");
			out.println("<TD>"+StringUtil.encodeHtmlTag(rs.getString(3)) + "</TD>");
			out.println("<TD>"+StringUtil.encodeHtmlTag(rs.getString(4)) + "</TD>");
			out.println("</TR>");
			flag=1;
		}
		if(flag==0) {
			out.println("<h3>Product with id:"+Integer.toString(pid)+" not found in product database!</h3>");
		}
		rs.close();
		s.close();
		con.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		out.println("</TABLE>");
		
	}


}
