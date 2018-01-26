package yl.test;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringEscapeUtils;

import com.yl.Utils.DBHelperUtils;

/*
 * @author  lqpl66
 * @date 创建时间：2018年1月5日 下午4:21:21 
 * @function     
 */
public class sql {
	static String sql = null;
	static DBHelperUtils db1 = null;
	static ResultSet ret = null;
	
	public  static void main(String[] args){
		sql="select * from feedback order by id desc limit 10";// SQL语句
		db1=new DBHelperUtils(sql);// 创建DBHelper对象
		try
		{
			db1.pst.executeUpdate("SET names utf8mb4");
			ret = db1.pst.executeQuery();// 执行语句，得到结果集
			while (ret.next()) {
				String uid = ret.getString(5);
				  String newContent = StringEscapeUtils.unescapeJava(uid);
				  System.out.println(uid);
				System.out.println(newContent);
			} // 显示数据
			ret.close();
			db1.close();// 关闭连接
		}catch(
		SQLException e)
		{
			e.printStackTrace();
		}
	}
}
