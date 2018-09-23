package com.platform.service;

import com.platform.dao.BaseDao;

import java.util.List;
import java.util.Map;

public interface AbstractService<T> {

    /**
     * 排序字段
     */
    public static final String FIELD_SIDX = "sidx";

    /**
     * 排序方式
     */
    public static final String FIELD_ORDER = "order";

    /**
     * 偏移量
     */
    public static final String FIELD_OFFSET = "offset";

    /**
     * 大小个数限制
     */
    public static final String FIELD_LIMIT = "limit";


    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    T queryObject(Integer id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<T> queryList(Map<String, Object> map);

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存实体
     *
     * @param address 实体
     * @return 保存条数
     */
    int save(T t);

    /**
     * 根据主键更新实体
     *
     * @param address 实体
     * @return 更新条数
     */
    int update(T t);

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    int delete(Integer id);

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    int deleteBatch(Integer[] ids);

    public BaseDao<T> getBaseDao();

    public void setBaseDao(BaseDao<T> baseDao);
}
