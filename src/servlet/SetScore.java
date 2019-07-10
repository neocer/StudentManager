package servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dust.SqlOperator;

/**
 * Servlet implementation class SetScore
 */
@WebServlet("/SetScore")
public class SetScore extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetScore() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String studentid =  request.getParameter("studentid");
		String lessonid = request.getParameter("lesson");
		String score = request.getParameter("score");
		if(Integer.valueOf(score) < 0)
			score="0";
		else if(Integer.valueOf(score)>100)
			score="100";
		if(studentid !=null && lessonid!=null && score!=null )
		{
			SqlOperator sql;
			try {
				sql = SqlOperator.GetInterface();
				Statement st = sql.GetStatement();
				
				st.execute("UPDATE lessonforstudent SET score =" +score + " WHERE student =" + studentid + " and lesson=" +lessonid+ ";");
				
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
