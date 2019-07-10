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
 * Servlet implementation class ChangePw
 */
@WebServlet("/ChangePw")
public class ChangePw extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePw() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String username = null ; // = request.getParameter("username");
		String password = request.getParameter("password");
		String newpassword = request.getParameter("newpassword");
		Cookie CookieArrary[] = request.getCookies();
		String information = null;
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
		
		if(username ==null ||information == null|| password ==null || newpassword == null)	//δ����
		{
			response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("δ����").replace("\\", "\\\\")+"\"}");
			return ;
		}
		else
		{
			SqlOperator sql = null;
			try {
				sql = SqlOperator.GetInterface();
			}  catch (Exception e1) {
				// TODO Auto-generated catch block
				
				response.getWriter().print("{\"code\":0,\"message\":\""+e1.toString()+"\"}");
				return;
			}
			Statement st ;
			ResultSet rs;
			try {
				st= sql.GetStatement();
				rs = st.executeQuery("select * from user where username ='" +username + "' and password ='" + password + "';");
				if(rs.next())
				{
					if(!information.equals(rs.getString("cookie")))
					{
						response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("��¼��Ϣ�ѹ���").replace("\\", "\\\\")+"\"}");
						return;
					}
					if(newpassword.length() >=6)
					{

						st.execute("UPDATE user SET password ='" +newpassword + "' , cookie = '' WHERE username = '" + username + "';");
						

						response.getWriter().print("{\"code\":1,\"message\":\""+cnToUnicode("�޸ĳɹ�").replace("\\", "\\\\")+"\"}");
						
						return;
						
					}
					else
					{

						response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("�����볤�ȵ���6λ").replace("\\", "\\\\")+"\"}");
						return;
					}
				
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.getWriter().print("{\"code\":0,\"message\":\"" +cnToUnicode(e.toString()).replace("\\", "\\\\")+"\"}");
			}
			response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("ԭ�������").replace("\\", "\\\\")+"\"}");
			return;
		
		}
		//response.getWriter().print("success");
	}	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	@SuppressWarnings("unused")
	private static String unicodeToCn(String unicode) {
        /** �� \ u �ָ��Ϊjavaע��Ҳ��ʶ��unicode������м����һ���ո�*/
        String[] strs = unicode.split("\\\\u");
        String returnStr = "";
        // ����unicode�ַ����� \ u ��ͷ����˷ָ���ĵ�һ���ַ���""��
        for (int i = 1; i < strs.length; i++) {
          returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        return returnStr;
    }
     
    private static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
          returnStr += "\\u" + Integer.toString(chars[i], 16);
        }
        return returnStr;
    }

}
