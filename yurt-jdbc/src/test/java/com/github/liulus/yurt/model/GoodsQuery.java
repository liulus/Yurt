package com.github.liulus.yurt.model;

import com.github.liulus.yurt.convention.data.PageQuery;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
public class GoodsQuery extends PageQuery {

    private String code;
    private List<String> codes;
    private String fullName;
    private Integer inventory;
    private Double price;
    private LocalDateTime startTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
