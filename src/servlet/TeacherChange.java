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
 * Servlet implementation class TeacherChange
 */
@WebServlet("/TeacherChange")
public class TeacherChange extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TeacherChange() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String teacherid = request.getParameter("teacherid");
		String college = request.getParameter("college");
		String major = request.getParameter("major");
		String sex = request.getParameter("sex");
		if(username==null)
		{
			response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("δ�ύ�û���").replace("\\", "\\\\")+"\"}");
			return;
		}
		if(username == null  || name == null || teacherid == null || college == null ||major== null || sex == null )
		{
			response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("�ύ������ȫ").replace("\\", "\\\\")+"\"}");
			return;
		}
		SqlOperator sql;
		try {
			sql = SqlOperator.GetInterface();
			Statement st = sql.GetStatement();
			ResultSet rs = st.executeQuery("select * from user where username ='" +username + "' and usertype=2;");
			if(rs.next())
			{
				int persion = rs.getInt("person");
				if(password !=null && (!password.equals("")))
				{
					st.execute("update user set password ='" + password + "' where username='"+username + "';");
				}
				st.execute("update teacher set name ='" + name + "',teacherid='" +teacherid + "' ,sex=" + sex + ",college=" + college + ",major=" + major+ " where id='"+persion + "';");
				response.getWriter().print("{\"code\":1,\"message\":\""+cnToUnicode("�޸ĳɹ�").replace("\\", "\\\\")+"\"}");
				return;
			}
			else
			{
				response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("�û���������").replace("\\", "\\\\")+"\"}");
				return;
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.toString());
			response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("���ݿ�����").replace("\\", "\\\\")+"\"}");
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
     
    private static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
          returnStr += "\\u" + Integer.toString(chars[i], 16);
        }
        return returnStr;
    }
    

}
