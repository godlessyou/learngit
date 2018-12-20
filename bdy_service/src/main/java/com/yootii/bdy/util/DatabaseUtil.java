package com.yootii.bdy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseUtil {
	
    private static Properties props = null;
	
	private static boolean initFlag = false;
	
	public static boolean isInitFlag() {
		return initFlag;
	}


	public static void setInitFlag(boolean initFlag) {
		DatabaseUtil.initFlag = initFlag;
	}

	private static String dataBaseIp = "localhost";
	
	
	public static void init(String ipAddress) {		
		
		if (!initFlag){	
			if (ipAddress!=null && !ipAddress.equals("")){
				dataBaseIp=ipAddress;
			}			
			initFlag=true;
		}
	}
	
	
	public static void init() {		
		
		if (!initFlag){
			// 第一次加载的properties
			props = FileUtil.readProperties("config.properties");
			if (props != null) {
				String value = props.getProperty("dataBaseIp");
				if (value!=null && !value.equals("")){
					dataBaseIp=value;
				}
			}
	
			initFlag = true;
		}
	}


	public static Connection getConForWpm() throws SQLException,
			java.lang.ClassNotFoundException {

		String driverName = "com.mysql.jdbc.Driver";
		// String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

		// 第一步：加载MySQL的JDBC的驱动
		Class.forName(driverName);

		// 取得连接的url,能访问MySQL数据库的用户名,密码；test：数据库名
		String url = "jdbc:mysql://"+ dataBaseIp+":3306/hgj_db";
		String username = "hgjuser";
		String password = "123456";

		// String url =
		// "jdbc:sqlserver://192.168.0.27:1433;DatabaseName=hgj_db";
		// String username = "hgj_user";
		// String password = "111.com";

		// String url ="jdbc:sqlserver://192.168.0.21:1433;DatabaseName=wpm_db";
		// String username = "mcrpt";
		// String password = "()*765TGBhu*";

		// 第二步：创建与MySQL数据库的连接类的实例
		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}

	public static Connection getConForOldWpm() throws SQLException,
			java.lang.ClassNotFoundException {

		String driverName = "com.mysql.jdbc.Driver";
		// String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

		// 第一步：加载MySQL的JDBC的驱动
		Class.forName(driverName);

		// 取得连接的url,能访问MySQL数据库的用户名,密码；test：数据库名
		// String url = "jdbc:mysql://localhost:3306/hgjdb";
		// String username = "hgjuser";
		// String password = "123456";

		String url = "jdbc:sqlserver://192.168.1.107:1433;DatabaseName=wpm_db";
		String username = "sa";
		String password = "dwq@2016";

		// 第二步：创建与MySQL数据库的连接类的实例
		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}

	public static Connection getConForHgj() throws SQLException,
			java.lang.ClassNotFoundException {

		String driverName = "com.mysql.jdbc.Driver";
		// String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

		// 第一步：加载MySQL的JDBC的驱动
		Class.forName(driverName);

		// 取得连接的url,能访问MySQL数据库的用户名,密码；test：数据库名
		// String url = "jdbc:mysql://localhost:3306/hgj_db";
		String url = "jdbc:mysql://"+ dataBaseIp+":3306/hgj_db";
		String username = "hgjuser";
		String password = "123456";

		// String url = "jdbc:sqlserver://192.168.1.107:1433;DatabaseName=ipms";
		// String username = "sa";
		// String password = "dwq@2016";

		// 第二步：创建与MySQL数据库的连接类的实例
		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}

	public static Connection getConForGonggao() throws SQLException,
			java.lang.ClassNotFoundException {

		String driverName = "com.mysql.jdbc.Driver";
		// String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

		// 第一步：加载MySQL的JDBC的驱动
		Class.forName(driverName);

		// 取得连接的url,能访问MySQL数据库的用户名,密码；test：数据库名

		String url = "jdbc:mysql://"+ dataBaseIp+":3306/hgjdb";
		String username = "hgjuser";
		String password = "123456";

		// String url = "jdbc:sqlserver://192.168.1.107:1433;DatabaseName=ipms";
		// String username = "sa";
		// String password = "dwq@2016";

		// 第二步：创建与MySQL数据库的连接类的实例
		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}
	
	

	public static Connection getConFromRunTimeDb() throws SQLException,
			java.lang.ClassNotFoundException {

		String driverName = "com.mysql.jdbc.Driver";
		// String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

		// 第一步：加载MySQL的JDBC的驱动
		Class.forName(driverName);

		// 取得连接的url,能访问MySQL数据库的用户名,密码；test：数据库名
		// String url = "jdbc:mysql://localhost:3306/hgj_db";
		String url = "jdbc:mysql://"+ dataBaseIp+":3306/hgjdb";
		String username = "hgjuser";
		String password = "123456";

		// String url = "jdbc:sqlserver://192.168.1.107:1433;DatabaseName=ipms";
		// String username = "sa";
		// String password = "dwq@2016";

		// 第二步：创建与MySQL数据库的连接类的实例
		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}

	

	public static void main(String[] args) {
		// String filePath = "C:\\work\\huiguanjia\\sql\\suning2.xlsx";
		// DatabaseUtil databaseUtil = new DatabaseUtil();
		// try {
		// databaseUtil.insertApplicant(filePath);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		try {
			// updateMonitorTmCase();
			// testTrademarkCase();
			// insertTmTrademark();

			// testTrademarkCase();
			// String filePath = "C:\\work\\huiguanjia\\sql\\t_doc_type.xls";
			// insertT_doc_typeTable(filePath);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
