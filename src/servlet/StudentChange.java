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
 * Servlet implementation class StudentChange
 */
@WebServlet("/StudentChange")
public class StudentChange extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentChange() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//response.getWriter().print("{\"code\":1,\"message\":\""+cnToUnicode("原密码错误！").replace("\\", "\\\\")+"\"}");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String studentid = request.getParameter("studentid");
		String college = request.getParameter("college");
		String major = request.getParameter("major");
		String sex = request.getParameter("sex");
		String classnumber = request.getParameter("class");
		if(username==null)
		{
			response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("未提交用户名").replace("\\", "\\\\")+"\"}");
			return;
		}
		if(username == null  || name == null || studentid == null || college == null ||major== null || sex == null || classnumber == null )
		{
			response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("提交参数不全").replace("\\", "\\\\")+"\"}");
			return;
		}
		SqlOperator sql;
		try {
			sql = SqlOperator.GetInterface();
			Statement st = sql.GetStatement();
			ResultSet rs = st.executeQuery("select * from user where username ='" +username + "' and usertype=3;");
			if(rs.next())
			{
				int persion = rs.getInt("person");
				if(password !=null && (!password.equals("")))
				{
					st.execute("update user set password ='" + password + "' where username='"+username + "';");
				}
				st.execute("update student set name ='" + name + "',studentid='" +studentid + "' ,sex=" + sex + ",college=" + college + ",major=" + major+ ",class="+ classnumber+ " where id='"+persion + "';");
				response.getWriter().print("{\"code\":1,\"message\":\""+cnToUnicode("修改成功").replace("\\", "\\\\")+"\"}");
				return;
			}
			else
			{
				response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("用户名不存在").replace("\\", "\\\\")+"\"}");
				return;
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("数据库问题").replace("\\", "\\\\")+"\"}");
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
