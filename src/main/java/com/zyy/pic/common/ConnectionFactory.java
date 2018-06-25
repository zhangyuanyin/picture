package com.zyy.pic.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConnectionFactory {
	
	private static Connection conn;
	
	public static Connection getConnnection() {
		Properties properties = new Properties();
		try {
			properties.load(ConnectionFactory.class.getResourceAsStream("jdbc.properties"));
			String url = properties.getProperty("url");
			String username = properties.getProperty("username");
			String password = properties.getProperty("password");
			Class.forName(properties.getProperty("driver"));
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void close(Connection conn, Statement stat) {
		try {
			if(conn != null) {
				conn.close();
			}
			if(stat != null) {
				stat.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Connection conn, Statement stat, ResultSet result) {
		try {
			if(conn != null) {
				conn.close();
			}
			if(stat != null) {
				stat.close();
			}
			if(result != null) {
				result.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		System.out.println(ConnectionFactory.getConnnection());
	}
}
