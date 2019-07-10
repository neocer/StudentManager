package servlet;

import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dust.SqlOperator;

/**
 * Servlet implementation class CreatLesson
 */
@WebServlet("/CreatLesson")
public class CreatLesson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreatLesson() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String name = request.getParameter("name");
		String day = request.getParameter("day");
		String hour = request.getParameter("hour");
		String teacherid = request.getParameter("teacherid");
		String classroom = request.getParameter("classroom");
		if(name !=null && day!=null && hour!=null &&teacherid!=null&&classroom!=null)
		{
			if(name.equals("") || day.equals("") || hour.equals("")|| teacherid.equals("")||classroom.equals(""))
			{

				response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("��������Ϊ�գ�").replace("\\", "\\\\")+"\"}");
			}
			else
			{
				SqlOperator sql;
				try {
					sql = SqlOperator.GetInterface();
					Statement st = sql.GetStatement();
					ResultSet rs = st.executeQuery("select * from teacher where id=" + teacherid + ";");
					if(rs.next())
					{
						rs = st.executeQuery("select * from lesson where hour=" + hour + " and day="+day+" and classroom='"+classroom+"';");
						if(!rs.next())
						{
							//System.out.println("INSERT INTO lesson (name, hour,day,teacher,classroom) VALUES ('"+name+"',"+hour+","+day+","+teacherid+",'" + classroom+ "');");
							st.execute("INSERT INTO lesson (name, hour,day,teacher,classroom) VALUES ('"+name+"',"+hour+","+day+","+teacherid+",'" + classroom+ "');");
							response.getWriter().print("{\"code\":1,\"message\":\""+cnToUnicode("����ɹ���").replace("\\", "\\\\")+"\"}");
							return;
						}
						else
						{
							response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("��ʱ������ѱ�ռ�ã�").replace("\\", "\\\\")+"\"}");
							return;
						}
					}
					else
					{
						response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("û�иý�ʦ��").replace("\\", "\\\\")+"\"}");
						return;
					}
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
					response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("���ݿ����").replace("\\", "\\\\")+"\"}");
					return;
				}
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
     
    private static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
          returnStr += "\\u" + Integer.toString(chars[i], 16);
        }
        return returnStr;
    }

}
