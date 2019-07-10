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

import com.mysql.cj.protocol.Resultset;

import dust.SqlOperator;

/**
 * Servlet implementation class GetMajor
 */
@WebServlet("/GetMajor")
public class GetMajor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMajor() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String college = request.getParameter("college");
		if(college==null)
		{
			response.getWriter().print("{\"code\":0}");
			return;
		}
		try {
			SqlOperator sql = null;
			sql = SqlOperator.GetInterface();
			Statement st = sql.GetStatement();
			ResultSet rs; 
			rs= st.executeQuery("select * from major where college=" + college+";");
			String returnString = "{\"code\":1,\"major\": [";
			boolean isfirst = true;
			while(rs.next())
			{
				if(isfirst ==false)
					returnString+=",";
				
				returnString +="{\"id\":" +rs.getInt("id") + ",";
				returnString +="\"name\":\"" +cnToUnicode(rs.getString("name")).replace("\\", "\\\\")   + "\"}";
				
					
				isfirst= false;
			}
			
			returnString +="]}";

			response.getWriter().print(returnString);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
