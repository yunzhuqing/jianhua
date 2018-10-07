package com.platform.service;

import java.util.List;
import java.util.Map;

import com.platform.dao.BaseDao;

/**
 * 抽象业务类实现, 实现基础的业务接口，提供基本功能实现
 * 业务实现类注入依赖对象即可
 * @author yunzhuqing
 *
 * @param <T>
 */
public abstract class AbstractDefaultService<T> implements  AbstractService<T> {
	
	protected BaseDao<T> baseDao;
	
	 /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
	 @Override
    public T queryObject(Long id) {
    	return baseDao.queryObject(id);
    }

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    @Override
    public List<T> queryList(Map<String, Object> map) {
    	return baseDao.queryList(map);
    }

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    @Override
    public int queryTotal(Map<String, Object> map) {
    	return baseDao.queryTotal();
    }

    /**
     * 保存实体
     *
     * @param address 实体
     * @return 保存条数
     */
    @Override
    public int save(T t) {
    	return baseDao.save(t);
    }

    /**
     * 根据主键更新实体
     *
     * @param address 实体
     * @return 更新条数
     */
    @Override
    public int update(T t) {
    	return baseDao.update(t);
    }

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    @Override
    public int delete(Long id) {
    	return baseDao.delete(id);
    }

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    @Override
    public  int deleteBatch(Integer[] ids) {
    	return baseDao.deleteBatch(ids);
    }

    @Override
	public BaseDao<T> getBaseDao() {
		return baseDao;
	}

    @Override
	public void setBaseDao(BaseDao<T> baseDao) {
		this.baseDao = baseDao;
	}
}
