package com.niko.boot.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

/**
 * Dao基础类
 * 封装chok2-devwork-dao的BaseDao
 * 提供通用的数据库操作方法
 */
public abstract class BaseDao {
    
    /**
     * 返回 SqlSession
     * @return
     */
    protected abstract SqlSession getSqlSession();
    
    /**
     * 用于返回命名空间的全路径 Class.getName()
     * @return Class
     */
    protected abstract String getSqlNamespace();
    
    private String _statement = null;
    
    /**
     * 获取需要操作sql的id，当getEntityClass().getName()无法满足时，可以重载此方法
     * @param statementName SQL的ID(不包含namespace)
     * @return String
     */
    protected String getStatementName(String statementName) {
        if (_statement == null) {
            _statement = getSqlNamespace() + ".";
        }
        return _statement + statementName;
    }
    
    public int create(String statementName, Object entity) {
        return this.getSqlSession().insert(getStatementName(statementName), entity);
    }
    
    public int remove(String statementName, String[] ids) {
        return this.getSqlSession().delete(getStatementName(statementName), ids);
    }
    
    public int modify(String statementName, Object entity) {
        return this.getSqlSession().update(getStatementName(statementName), entity);
    }
    
    public int create(Object entity) {
        return this.getSqlSession().insert(getStatementName("create"), entity);
    }
    
    public int remove(String[] ids) {
        return this.getSqlSession().delete(getStatementName("remove"), ids);
    }
    
    public int modify(Object entity) {
        return this.getSqlSession().update(getStatementName("modify"), entity);
    }
    
    public <T> T queryOne(String statementName, Object param) {
        return this.getSqlSession().selectOne(getStatementName(statementName), param);
    }
    
    public <E> List<E> queryList(String statementName, Object param) {
        return this.getSqlSession().selectList(getStatementName(statementName), param);
    }
    
    public int queryCount(String statementName, Object param) {
        return this.getSqlSession().selectOne(getStatementName(statementName), param);
    }
    
    public <T> T queryOne(Object param) {
        return this.getSqlSession().selectOne(getStatementName("queryOne"), param);
    }
    
    public <E> List<E> queryList(Object param) {
        return this.getSqlSession().selectList(getStatementName("queryList"), param);
    }
    
    public int queryCount(Object param) {
        return this.getSqlSession().selectOne(getStatementName("queryCount"), param);
    }
}

