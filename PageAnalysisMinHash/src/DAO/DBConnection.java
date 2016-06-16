package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DAO.DBConnection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBConnection {
	private static ComboPooledDataSource dataSource = null;
	private static String synth="" ;		// 用于同步控制，无实际意义
	
	public static ComboPooledDataSource getInstance() {
		if(dataSource==null)
		{
			synchronized (synth) {
				if(dataSource==null)
				{
					System.setProperty("com.mchange.v2.c3p0.cfg.xml", "conf/c3p0-config.xml");
					dataSource = new ComboPooledDataSource();
				}
			}
		}
		return dataSource;

	}
	
	/**
	 * 获得数据库连接
	 * @return
	 */
	public Connection getConnect()
	{
		 ComboPooledDataSource dataSource = DBConnection.getInstance();
		 
		 Connection conn = null;
		 try {	
			 	conn = dataSource.getConnection();
			 	
		 	 } catch (SQLException e) {
		 		 // TODO Auto-generated catch block
		 		 e.printStackTrace();
		 	 }
		return conn;
	}
	
	/**
	 * 关闭数据库连接
	 * @param con
	 * @param ps
	 * @param rs
	 */
	public void connectClose(Connection con, PreparedStatement ps, ResultSet rs) 
	{
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (con != null) {
				con.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void connectClose(Connection con, Statement ps, ResultSet rs) 
	{
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (con != null) { 
				con.close();
			}

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
