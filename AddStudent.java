package com.wxx.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

public class AddStudent extends HttpServlet {

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		String strStudentID="";
		String strStudentName="";
		strStudentID=request.getParameter("ID");
		strStudentName=request.getParameter("Name");
		
		Connection con=null;
		Statement state=null;
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";  //加载JDBC驱动    
	    String dbURL = "jdbc:sqlserver://127.0.0.1:1433; DatabaseName=dbWebcrud;user=sa;password=sa"; 
	    
	    try{
	    	Class.forName(driverName);
	    	con=DriverManager.getConnection(dbURL);
	    	String strSql = "insert into Student Values('" + strStudentID+"',N'"+strStudentName+"')" ;    
			state = con.createStatement();  
			state.execute(strSql);
	    }
	    catch(Exception e){
	    	response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.println("添加失败!");
			out.close();
	    }
	    
		//跳转
		response.sendRedirect("../StudentList.jsp");
	}

}
