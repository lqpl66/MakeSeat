package com.yl.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yl.bean.StationName;

/*
 * @author  lqpl66
 * @date 创建时间：2017年11月14日 下午1:24:39 
 * @function     
 */
public class DBHelperUtils {
	public static final String url = "jdbc:mysql://192.168.1.198:3306/Seat";
	public static final String name = "com.mysql.jdbc.Driver";
	public static final String user = "root";
	public static final String password = "root";

	public static Connection conn = null;
	public PreparedStatement pst = null;

	public DBHelperUtils(String sql) {
		try {
			Class.forName(name);// 指定连接类型
			conn = DriverManager.getConnection(url, user, password);// 获取连接
			pst = conn.prepareStatement(sql);// 准备执行语句
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addStationName(StationName sn) throws SQLException {
		// 获得数据库连接
		try {
			Class.forName(name);
			conn = DriverManager.getConnection(url, user, password);// 获取连接
			String sql = "insert into station(stationCode,stationName,stationNameAttr,stationNameAttr1,stationNameFull,stationNum) values(?,?,?,?,?,?)";
			PreparedStatement ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, sn.getStationCode());
			ptmt.setString(2, sn.getStationName());
			ptmt.setString(3, sn.getStationNameAttr());
			ptmt.setString(4, sn.getStationNameAttr1());
			ptmt.setString(5, sn.getStationNameFull());
			ptmt.setInt(6, sn.getStationNum());
			ptmt.execute();
			conn.close();// 关闭连接
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} // 指定连接类型
	}

	public static void addtrain(String station_train_code, String train_no, String flag, String date)
			throws SQLException {
		// 获得数据库连接
		try {
			Class.forName(name);
			conn = DriverManager.getConnection(url, user, password);// 获取连接
			String sql = "insert into trainlist(station_train_code,train_no,date,stationFlag) values(?,?,?,?)";
			PreparedStatement ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, station_train_code);
			ptmt.setString(2, train_no);
			ptmt.setString(3, date);
			ptmt.setString(4, flag);
			ptmt.execute();
			conn.close();// 关闭连接
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} // 指定连接类型
	}

	public void close() {
		try {
			this.conn.close();
			this.pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
