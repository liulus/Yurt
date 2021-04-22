package com.github.liulus.yurt.jdbc;

import org.jetbrains.annotations.NotNull;

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
    @NotNull
    Long insert(@NotNull E entity);

    /**
     * 批量插入数据, 以第一条的插入数据为准
     *
     * @param eList 实体对象列表
     * @return 插入记录数
     */
    int batchInsert(@NotNull Collection<E> eList);

    /**
     * 更新数据 忽略null值
     *
     * @param entity 实体对象
     * @return 记录数
     */
    int updateIgnoreNull(@NotNull E entity);

    /**
     * 根据id删除一条记录
     *
     * @param id 记录id
     * @return 删除的记录数
     */
    int deleteById(@NotNull Long id);

    /**
     * 逻辑删除, 更改删除时间和删除标识
     *
     * @param id id
     * @return 记录数
     */
    int deleteLogicalById(@NotNull Long id);

    /**
     * 根据id查询一条记录
     *
     * @param id id
     * @return 查询的记录
     */
    E selectById(@NotNull Long id);

    /**
     * 根据id集合查询多条记录
     *
     * @param ids id列表
     * @return 记录集合
     */
    List<E> selectByIds(@NotNull Collection<Long> ids);

}
