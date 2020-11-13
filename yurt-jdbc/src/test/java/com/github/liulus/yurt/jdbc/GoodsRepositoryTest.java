package com.github.liulus.yurt.jdbc;

import com.github.liulus.yurt.configure.SpringTestConfig;
import com.github.liulus.yurt.model.GoodsQuery;
import com.github.liulus.yurt.repository.GoodsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class)
@Sql(scripts = "/sql/init_schema.sql")
public class GoodsRepositoryTest {

    @Resource
    private SQLExecutor sqlExecutor;
    @Resource
    private GoodsRepository goodsRepository;

    private Class<GoodsRepository> interfaceClass = GoodsRepository.class;

    @Test
    public void testSelectByQuery() throws NoSuchMethodException {
        GoodsQuery query = new GoodsQuery();
        query.setPageNum(1);
        query.setPageSize(2);
        query.setStartTime(LocalDateTime.now().minusYears(50L));

        Method method = interfaceClass.getDeclaredMethod("selectByQuery", GoodsQuery.class);

        Object result = SQLContext.getContext(interfaceClass, method).execute(sqlExecutor, new Object[]{query});
        System.out.println(method.getName());
    }


}
