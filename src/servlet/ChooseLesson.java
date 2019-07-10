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
 * Servlet implementation class ChooseLesson
 */
@WebServlet("/ChooseLesson")
public class ChooseLesson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChooseLesson() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String lessonid = request.getParameter("lesson");
		String studentid = request.getParameter("student");
		if(lessonid!=null && studentid !=null)
		{
			try {
				SqlOperator sql;
				sql = SqlOperator.GetInterface();
				Statement st = sql.GetStatement();
				ResultSet rs = st.executeQuery("select * from lessonforstudent where lesson=" + lessonid + " and student="+studentid+";");
				if(!rs.next())
				{
					st.execute("INSERT INTO lessonforstudent (lesson, student) VALUES ('"+lessonid+"',"+ studentid+ ");");
					response.getWriter().print("{\"code\":1,\"message\":\""+cnToUnicode("ѡ�γɹ���").replace("\\", "\\\\")+"\"}");
					return;
				}
				else
				{
					response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("���Ѿ�ѡ�˸ÿγ�").replace("\\", "\\\\")+"\"}");
					return;
				}
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
		{
			response.getWriter().print("{\"code\":0,\"message\":\""+cnToUnicode("����Ϊ��").replace("\\", "\\\\")+"\"}");
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
