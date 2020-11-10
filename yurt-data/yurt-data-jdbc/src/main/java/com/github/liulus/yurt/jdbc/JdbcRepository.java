package com.github.liulus.yurt.jdbc;

import java.util.Collection;
import java.util.List;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
public interface JdbcRepository<E> {

    Long insert(E entity);

    int updateIgnoreNull(E entity);

    int deleteById(Long id);

    E selectById(Long id);

    List<E> selectByIds(Collection<Long> ids);

}
