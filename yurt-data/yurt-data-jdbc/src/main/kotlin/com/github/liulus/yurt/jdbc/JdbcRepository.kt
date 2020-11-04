package com.github.liulus.yurt.jdbc

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/3
 */
interface JdbcRepository<E> {

    fun insert(entity: E): Long

    fun updateIgnoreNull(entity: E): Int

    fun deleteById(id: Long): Int

    fun selectById(id: Long): E

}