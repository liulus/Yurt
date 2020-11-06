package com.github.liulus.yurt.repository

import com.github.liulus.yurt.jdbc.JdbcRepository
import com.github.liulus.yurt.jdbc.annotation.If
import com.github.liulus.yurt.jdbc.annotation.Param
import com.github.liulus.yurt.jdbc.annotation.Select
import com.github.liulus.yurt.model.Goods

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/6
 */
interface GoodsRepository : JdbcRepository<Goods> {

    @Select(where = ["code = :code"])
    fun selectByCode(@Param("code") code: String): Goods

    @Select(testWheres = [
        If(test = "code != null and code != ''", value = "code = :code"),
        If(test = "inventory != null", value = "inventory > :inventory"),
        If(test = "gmtCreated != null", value = "gmt_created > :gmtCreated"),
    ], orderBy = "gmt_created desc")
    fun selectByQuery(query: Goods): List<Goods>


}

