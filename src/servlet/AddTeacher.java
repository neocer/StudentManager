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
					response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("�ù����Ѿ����ڣ�").replace("\\", "\\\\")+"\"}");
					return;
				}
				rs = st.executeQuery("select *from college where id=" +college + ";");
				if(!rs.next())
				{
					response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("��ѧԺ�����ڣ�").replace("\\", "\\\\")+"\"}");
					return;
				}
				rs = st.executeQuery("select *from major where id=" +major + " and college=" + college+";");
				if(!rs.next())
				{
					response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("�ý����Ҳ����ڣ�").replace("\\", "\\\\")+"\"}");
					return;
				}
				st.execute("INSERT INTO teacher (name, teacherid,sex,college,major) VALUES ('"+name+"', '"+teacherid+"',"+sex+","+college+","+major+");");
				rs = st.executeQuery("select *from teacher where teacherid='" +teacherid + "';");
				if(!rs.next())
				{
					response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("��ʦ��Ϣ����ʧ�ܣ�").replace("\\", "\\\\")+"\"}");
					return;
				}
				int personid = rs.getInt("id");
				st.execute("INSERT INTO user (username, password,usertype,person) VALUES ('T"+teacherid+"','T"+teacherid+"',2,"+personid+")");
				rs = st.executeQuery("select *from user where username='" +"T"+ teacherid+ "';");
				if(!rs.next())
				{
					response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("��ʦ�˻�����ʧ�ܣ�").replace("\\", "\\\\")+"\"}");
					return;
				}
				response.getWriter().print("{\"code\":1,\"message\":\""+cnToUnicode("��ӳɹ����˻������Ϊ��T���š�").replace("\\", "\\\\")+"\"}");
				return;

			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("���ݿ����⣡").replace("\\", "\\\\")+"\"}");
				return;
			}
		}
		else
		{

			response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("������ȫ��").replace("\\", "\\\\")+"\"}");
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
		/** �� \ u �ָ��Ϊjavaע��Ҳ��ʶ��unicode������м����һ���ո�*/
		String[] strs = unicode.split("\\\\u");
		String returnStr = "";
		// ����unicode�ַ����� \ u ��ͷ����˷ָ���ĵ�һ���ַ���""��
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
