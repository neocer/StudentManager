package servlet;

import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dust.SqlOperator;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		Cookie CookieArrary[] = request.getCookies();
		String information = null,username=null;
		if(CookieArrary == null)
		{
			CookieArrary = new Cookie[0];
		}
		for(int i = 0 ; i < CookieArrary.length ; i ++)
		{
			if(CookieArrary[i].getName().equals("username"))
			{
				username = CookieArrary[i].getValue();
			}
			else if(CookieArrary[i].getName().equals("information"))
			{
				information = CookieArrary[i].getValue();
			}
		}
		if(information !=null && username !=null)
		{
			SqlOperator sql;
			try {
				sql = SqlOperator.GetInterface();
				Statement st = sql.GetStatement();
				ResultSet rs = st.executeQuery("select * from user where username ='" +username + "' and cookie ='" + information + "';");
			if(rs.next())
			{
				st.execute("UPDATE user SET cookie = null where username ='" +username + "' and cookie ='" + information + "';");
			}
				} catch (Exception e) {
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
