package com.lanxi.util.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Test;

import com.lanxi.util.entity.MyOptinal;
import com.lanxi.util.utils.CheckReplaceUtil;
import com.lanxi.util.utils.OtherUtil.IdAnalyst;
import com.mysql.jdbc.Driver;
import static com.lanxi.util.utils.CheckReplaceUtil.*;

import redis.clients.jedis.Jedis;

public class Test20170822 {
	
	@Test
	public void testRedis() {
		Jedis jedis=new Jedis("192.168.1.61", 6379);
		jedis.setex("10086", 100, "移动");
		System.out.println(jedis.get("10086"));
		jedis.close();
	}
	@Test
	public void testId() {
		String[] ids=new String[] {};
		for(String each:ids)
			System.out.println(new IdAnalyst(each).getIdInfo());
	}
	
	@Test
	public void export() throws SQLException {
		Connection connection=null;
		try {
			Class<?> clazz=Class.forName("com.mysql.jdbc.Driver");
			String url="192.168.1.100:3306/tl_xfxd";
			String userName="xfxd";
			String password="xfxd_pwd100";
			connection=DriverManager.getConnection(url, userName, password);
			String sql="select count(*) from t_XFXD_REQ";
			ResultSet resultSet=connection.createStatement().executeQuery(sql);
			ResultSetMetaData metaData=resultSet.getMetaData();
			int columnCount=metaData.getColumnCount();
			while(resultSet.next()) {
				for(int j=0;j<columnCount;j++)
				System.out.println(resultSet.getString(j));
			}
		} catch (Exception e) {
			throw new RuntimeException("时发生异常", e);
		}finally {
			if(!(null==connection))
				connection.close();
		}
		
	}
	
	@Test
	public void testPad() {
		String str1="1234";
		System.out.println(CheckReplaceUtil.pad(str1, "0", 6, false));
	}
	
	@Test
	public void testToNumber() {
		String str="1993";
		System.out.println(isNumber(str));
		System.out.println(isInt(str));
		System.out.println(isLong(str));
		System.out.println(isDouble(str));
		System.out.println(toNumber("1993", Integer.class).maybe(Math::random,System.out::println));
	}
}
