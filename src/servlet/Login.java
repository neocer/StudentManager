package servlet;

import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import dust.SqlOperator;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		try {
			if(username !=null && password !=null)	//未传参
			{
				SqlOperator sql = SqlOperator.GetInterface();
				Statement st = sql.GetStatement();
				ResultSet rs = st.executeQuery("select * from user where username ='" +username + "' and password ='" + password + "';");
				if(!rs.next())
				{
					response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("用户名或者密码错误！").replace("\\", "\\\\")+"\"}");
					return;
					
				}
				else
				{

					String cookiesSource = username + password + System.currentTimeMillis();
					cookiesSource = DigestUtils.md5Hex(cookiesSource);
					st.execute("UPDATE user SET cookie = '" +cookiesSource + "' WHERE username = '" +username + "';");
					Cookie name = new Cookie("username", username);
					Cookie information = new Cookie("information",cookiesSource);
					name.setMaxAge(60*60); 
					information.setMaxAge(60*60); 
					response.addCookie(name);
					response.addCookie( information );
					response.getWriter().print("{\"code\":1,\"message\":\""+cnToUnicode("登录成功").replace("\\", "\\\\")+"\"}");
					return;
				}
			}
			else
			{
				response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("用户名或密码不能为空").replace("\\", "\\\\")+"\"}");
				return;
			}
		}
		catch(Exception e)
		{
			response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("服务器异常").replace("\\", "\\\\")+"\"}");
			return;
		}
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
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
        String[] strs = unicode.split("\\\\u");
        String returnStr = "";
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
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
