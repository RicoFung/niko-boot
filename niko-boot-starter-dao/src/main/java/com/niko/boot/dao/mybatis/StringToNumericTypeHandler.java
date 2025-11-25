package com.niko.boot.dao.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * String 到 NUMERIC 类型转换器
 * 参考 chok2.devwork.dao.mybatis.StringToNumericTypeHandler 实现
 */
public class StringToNumericTypeHandler extends BaseTypeHandler<String>
{
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException 
	{
		ps.setInt(i, Integer.valueOf(parameter));
	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName) throws SQLException 
	{
		return rs.getString(columnName);
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException 
	{
		return rs.getString(columnIndex);
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException 
	{
		return cs.getString(columnIndex);
	}

}

