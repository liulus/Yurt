package com.github.liulus.yurt.jdbc;


import com.github.liulus.yurt.convention.data.Page;
import com.github.liulus.yurt.convention.data.PageList;
import com.github.liulus.yurt.convention.data.Result;
import com.github.liulus.yurt.convention.util.Asserts;
import com.github.liulus.yurt.convention.util.FormatUtils;
import com.github.liulus.yurt.convention.util.Pages;
import com.github.liulus.yurt.convention.util.Results;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/10/29
 */
public class KtTest {

    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3);
        PageList<Integer> integerPageList = new PageList<>(integers);
        integerPageList.setPageNum(2);
        integerPageList.setPageSize(50);
        integerPageList.setTotalRecords(500000);


        Page<Integer> page = Pages.page(integerPageList);
        System.out.println(page.getTotalPages());
        System.out.println(page);

        Result<Object> success = Results.success();
        String s = FormatUtils.formatMessage("33,{},{}|123", 434, 55);
        String[] sa = {"ss", ""};
        Asserts.isEmpty(sa, "数组必须空{}  ", "额");
        Asserts.isEmpty(Collections.singletonMap("123", ""), "参数{} 不能为空", "222");
        System.out.println(s);


    }


}
