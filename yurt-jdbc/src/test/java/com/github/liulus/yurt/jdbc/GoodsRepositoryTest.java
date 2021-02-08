package com.github.liulus.yurt.jdbc;

import com.github.liulus.yurt.configure.SpringTestConfig;
import com.github.liulus.yurt.convention.data.Page;
import com.github.liulus.yurt.model.Goods;
import com.github.liulus.yurt.model.GoodsQuery;
import com.github.liulus.yurt.repository.GoodsRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private GoodsRepository goodsRepository;

    @Test
    public void insert() {
        Goods goods = new Goods();
        goods.setCode("826478");
        goods.setFullName("签约产品一号");
        goods.setInventory(826);
        Long id = goodsRepository.insert(goods);
        Assert.assertTrue(goods.getId() >= 1);
        Assert.assertEquals(goods.getId(), id);
    }

    @Test
    public void batchInsert() {

        List<Goods> productList = Stream.of("123", "456", "789", "012", "345")
                .map(s -> {
                    Goods signProduct = new Goods();
                    signProduct.setCode(s);
                    return signProduct;
                }).collect(Collectors.toList());
        int insert = goodsRepository.batchInsert(productList);

        Assert.assertEquals(productList.size(), insert);
    }

    @Test
    public void update() {
        String upCode = "new_code";
        String upName = "new_full_name";

        Goods old = goodsRepository.selectById(1L);
        Assert.assertNotNull(old);
        Assert.assertNotEquals(old.getCode(), upCode);
        Assert.assertNotEquals(old.getFullName(), upName);

        Goods upProduct = new Goods();
        upProduct.setId(old.getId());
        upProduct.setCode(upCode);
        upProduct.setFullName(upName);
        goodsRepository.updateIgnoreNull(upProduct);
    }

    @Test
    public void updateSelective() {
        String upCode = "new_code";
        String upName = "new_full_name";

        Goods old = goodsRepository.selectById(1L);
        Assert.assertNotNull(old);
        Assert.assertNotEquals(old.getCode(), upCode);
        Assert.assertNotEquals(old.getFullName(), upName);

        Goods upProduct = new Goods();
        upProduct.setId(old.getId());
        upProduct.setCode(upCode);
        upProduct.setFullName(upName);
        goodsRepository.updateIgnoreNull(upProduct);

        Goods newProduct = goodsRepository.selectById(1L);
        Assert.assertNotNull(newProduct);
        Assert.assertEquals(newProduct.getCode(), upCode);
        Assert.assertEquals(newProduct.getFullName(), upName);
    }

    @Test
    public void delete() {
        Goods deleteProduct = new Goods();
        deleteProduct.setId(1L);
        int deleted = goodsRepository.deleteById(1L);
        Assert.assertTrue(deleted >= 1);

        Goods signProduct = goodsRepository.selectById(1L);
        Assert.assertNull(signProduct);
    }

    @Test
    public void deleteById() {
        int deleted = goodsRepository.deleteById(1L);
        Assert.assertEquals(1, deleted);

        Goods signProduct = goodsRepository.selectById(1L);
        Assert.assertNull(signProduct);
    }

    @Test
    public void deleteLogicalById() {
        Goods old = goodsRepository.selectById(1L);
        int i = goodsRepository.deleteLogicalById(old.getId());
        Assert.assertTrue(i > 0);
        Goods goods = goodsRepository.selectById(old.getId());
        Assert.assertNull(goods);
        Goods code = goodsRepository.selectByCode(old.getCode());
        Assert.assertNotNull(code);
    }

    @Test
    public void selectById() {
        Goods signProduct = goodsRepository.selectById(1L);
        Assert.assertNotNull(signProduct);
    }

    @Test
    public void selectByIds() {
        List<Goods> signProducts = goodsRepository.selectByIds(Arrays.asList(1L, 2L));
        Assert.assertEquals(2, signProducts.size());
    }

    @Test
    public void selectByProperty() {
        Goods goods = goodsRepository.selectByCode("893341");
        Assert.assertNotNull(goods);
    }

    @Test
    public void selectForObject() {
        int count = goodsRepository.countByCode("893341");
        Assert.assertEquals(1, count);
    }

    @Test
    public void selectList() {

        GoodsQuery query = new GoodsQuery();
        query.setDisablePage(true);
        List<String> codes = Arrays.asList("893341", "213324", "123456");
        query.setCodes(codes);

        List<Goods> goodsList = goodsRepository.selectByQuery(query).getResults();
        goodsList.forEach(signProduct -> Assert.assertTrue(codes.contains(signProduct.getCode())));

        query.setFullName("%测试%");
        goodsList = goodsRepository.selectByQuery(query).getResults();
        goodsList.forEach(signProduct -> {
            Assert.assertTrue(codes.contains(signProduct.getCode()));
            Assert.assertTrue(signProduct.getFullName().contains("测试"));
        });
    }

    @Test
    public void selectPageList() {
        GoodsQuery query = new GoodsQuery();
        query.setPageSize(2);
        Page<Goods> signProducts = goodsRepository.selectByQuery(query);
        Assert.assertEquals(2, signProducts.getPageSize());
        Assert.assertEquals(query.getPageNum(), signProducts.getPageNum());
        Assert.assertTrue(signProducts.getTotalRecords() >= 2);
        Assert.assertEquals(2, signProducts.getResults().size());

    }


}
