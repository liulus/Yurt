package com.github.liulus.yurt.repository;

import com.github.liulus.yurt.convention.data.Page;
import com.github.liulus.yurt.jdbc.JdbcRepository;
import com.github.liulus.yurt.jdbc.annotation.If;
import com.github.liulus.yurt.jdbc.annotation.Param;
import com.github.liulus.yurt.jdbc.annotation.Select;
import com.github.liulus.yurt.model.Goods;
import com.github.liulus.yurt.model.GoodsQuery;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
public interface GoodsRepository extends JdbcRepository<Goods> {

    @Select(where = "code = :code")
    Goods selectByCode(@Param("code") String code);

    @Select(columns = "count(*)", where = "code = :code")
    int countByCode(@Param("code") String code);

    @Select(testWheres = {
            @If(test = "code != null and code != ''", value = "code = :code"),
            @If(test = "codes != null and codes.size() > 0", value = "code in ( :codes )"),
            @If(test = "inventory != null", value = "inventory > :inventory"),
            @If(test = "startTime != null", value = "gmt_created > :startTime"),
            @If(test = "fullName != null", value = "full_name like :fullName"),
    }, orderBy = "gmt_created desc", isPageQuery = true)
    Page<Goods> selectByQuery(GoodsQuery query);

}
