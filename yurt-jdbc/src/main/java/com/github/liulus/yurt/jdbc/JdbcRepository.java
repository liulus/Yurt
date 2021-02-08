package com.github.liulus.yurt.jdbc;

import java.util.Collection;
import java.util.List;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
public interface JdbcRepository<E> {

    /**
     * 插入一条数据, 返回新增的id, 并同时设置到实体对象上
     *
     * @param entity 实体对象
     * @return id
     */
    Long insert(E entity);

    /**
     * 批量插入数据, 以第一条的插入数据为准
     *
     * @param eList 实体对象列表
     * @return 插入记录数
     */
    int batchInsert(Collection<E> eList);

    /**
     * 更新数据 忽略null值
     *
     * @param entity 实体对象
     * @return 记录数
     */
    int updateIgnoreNull(E entity);

    int deleteById(Long id);

    /**
     * 逻辑删除, 更改删除时间和删除标识
     *
     * @param id id
     * @return 记录数
     */
    int deleteLogicalById(Long id);

    E selectById(Long id);

    List<E> selectByIds(Collection<Long> ids);

}
