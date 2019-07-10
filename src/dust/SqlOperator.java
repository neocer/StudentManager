package dust;

import java.sql.*;

public class SqlOperator {
	static private SqlOperator sqloperator;
	private Connection con;
	private PreparedStatement Pst;
	private Statement St;
	
	static {
		try {
			sqloperator = new SqlOperator();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public SqlOperator() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated constructor stub
		String DB_URL = "jdbc:mysql://localhost:3306/student?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT&allowPublicKeyRetrieval=true";
	    String USER = "root";
	  	String PASS = "1024";
	  	Class.forName("com.mysql.cj.jdbc.Driver"); 
	  	con = DriverManager.getConnection(DB_URL,USER,PASS);
	  	St = con.createStatement();
	  	
	}
	
	public static SqlOperator GetInterface() throws ClassNotFoundException, SQLException
	{
		return sqloperator;
	}
	
	public PreparedStatement GetPreStatement()
	{
		return Pst;
	}
	
	public Statement GetStatement() throws SQLException
	{
		return GetNewStatement();
	}
	private Statement GetNewStatement() throws SQLException
	{
		return con.createStatement();
	}
	public Statement GetPreStatement(String sql) throws SQLException
	{
		return con.prepareStatement(sql);
	}
	
	
}
