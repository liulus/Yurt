package com.github.liulus.yurt.jdbc;

import java.util.Collection;
import java.util.List;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
public interface SQLExecutor {

    <E> long insert(E entity);

    <E> int updateIgnoreNull(E entity);

    <E> int deleteById(Class<E> eClass, Long id);

    <E> E selectById(Class<E> eClass, Long id);

    <E> List<E> selectByIds(Class<E> eClass, Collection<Long> ids);

    <E> E selectForObject(SQL sql, Object params, Class<E> requiredType);

    <E> List<E> selectForList(SQL sql, Object params, Class<E> requiredType);

    <E> List<E> selectForPage(SQL sql, Object params, int pageNum, int pageSize, Class<E> requiredType);

    long count(SQL var1, Object params);

    int update(SQL var1, Object params);


}
