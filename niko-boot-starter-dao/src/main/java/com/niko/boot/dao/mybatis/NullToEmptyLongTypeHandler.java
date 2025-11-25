package com.niko.boot.dao.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * Null 转空 Long 类型转换器
 * 参考 chok2.devwork.dao.mybatis.NullToEmptyLongTypeHandler 实现
 */
public class NullToEmptyLongTypeHandler implements TypeHandler<Long>
{

	@Override
	public Long getResult(ResultSet rs, String columnName) throws SQLException
	{
		String value = rs.getString(columnName);
		return (value == null) ? 0L : rs.getLong(columnName);
	}

	@Override
	public Long getResult(ResultSet rs, int columnIndex) throws SQLException
	{
		String value = rs.getString(columnIndex);
		return (value == null) ? 0L : rs.getLong(columnIndex);
	}

	@Override
	public Long getResult(CallableStatement cs, int columnIndex) throws SQLException
	{
		String value = cs.getString(columnIndex);
		return (value == null) ? 0L : cs.getLong(columnIndex);
	}

	@Override
	public void setParameter(PreparedStatement ps, int arg1, Long l, JdbcType jdbcType) throws SQLException
	{
	}

}

