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
 * Servlet implementation class AddTeacher
 */
@WebServlet("/AddTeacher")
public class AddTeacher extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddTeacher() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String teacherid = request.getParameter("teacherid");
		String sex = request.getParameter("sex");
		String college = request.getParameter("college");
		String major = request.getParameter("major");
		if(name!=null&&teacherid!=null&&sex!=null&&college!=null&&major!=null )
		{
			SqlOperator sql = null;
			try {
				sql = SqlOperator.GetInterface();
				Statement st = sql.GetStatement();
				ResultSet rs = st.executeQuery("select *from teacher where teacherid='" +teacherid + "';");
				if(rs.next())
				{
					response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("该工号已经存在！").replace("\\", "\\\\")+"\"}");
					return;
				}
				rs = st.executeQuery("select *from college where id=" +college + ";");
				if(!rs.next())
				{
					response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("该学院不存在！").replace("\\", "\\\\")+"\"}");
					return;
				}
				rs = st.executeQuery("select *from major where id=" +major + " and college=" + college+";");
				if(!rs.next())
				{
					response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("该教研室不存在！").replace("\\", "\\\\")+"\"}");
					return;
				}
				st.execute("INSERT INTO teacher (name, teacherid,sex,college,major) VALUES ('"+name+"', '"+teacherid+"',"+sex+","+college+","+major+");");
				rs = st.executeQuery("select *from teacher where teacherid='" +teacherid + "';");
				if(!rs.next())
				{
					response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("教师信息插入失败！").replace("\\", "\\\\")+"\"}");
					return;
				}
				int personid = rs.getInt("id");
				st.execute("INSERT INTO user (username, password,usertype,person) VALUES ('T"+teacherid+"','T"+teacherid+"',2,"+personid+")");
				rs = st.executeQuery("select *from user where username='" +"T"+ teacherid+ "';");
				if(!rs.next())
				{
					response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("教师账户插入失败！").replace("\\", "\\\\")+"\"}");
					return;
				}
				response.getWriter().print("{\"code\":1,\"message\":\""+cnToUnicode("添加成功！账户密码均为【T工号】").replace("\\", "\\\\")+"\"}");
				return;

			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("数据库问题！").replace("\\", "\\\\")+"\"}");
				return;
			}
		}
		else
		{

			response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("参数不全！").replace("\\", "\\\\")+"\"}");
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

	@SuppressWarnings("unused")
	private static String cnToUnicode(String cn) {
		char[] chars = cn.toCharArray();
		String returnStr = "";
		for (int i = 0; i < chars.length; i++) {
			returnStr += "\\u" + Integer.toString(chars[i], 16);
		}
		return returnStr;
	}
}
