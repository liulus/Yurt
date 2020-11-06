package com.github.liulus.yurt.jdbc

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/4
 */
interface SQLExecutor {

    fun <E> insert(entity: E): Long

    fun <E> updateIgnoreNull(entity: E): Int

    fun <E> deleteById(eClass: Class<E>, id: Long): Int

    fun <E> selectById(eCLass: Class<E>, id: Long): E?

    fun <E> selectByIds(eClass: Class<E>, ids: Collection<Long>): List<E>

    fun <E> selectForObject(sql: SQL, params: Any?, requiredType: Class<E>): E?

    fun <E> selectForList(sql: SQL, params: Any?, requiredType: Class<E>): List<E>

    fun count(sql: SQL, params: Any?): Long

    fun update(sql: SQL, params: Any?): Int
}