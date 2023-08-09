package com.mycom.word;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	// DB에 접속해서 연결한 Connection 객체를 가져온다.
	// 한 번만 연결되면 이후에도 가져올 수 있다.
	
	// 전역 변수 선언
	private static Connection conn = null;
	
	
	// Connection 타입의 데이터를 리턴하는 메소드
	public static Connection getConnection() {
		if(conn == null) {
			try {
				Class.forName("org.sqlite.JDBC"); // Driver loading
				try {
					conn = DriverManager.getConnection("jdbc:sqlite:wordDB.db"); // DB를 연결하는 구문
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	public static void closeConnection() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
