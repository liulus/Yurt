package com.github.liulus.yurt.model;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
@Table(name = "sign_product")
public class Goods {

    private Long id;
    private String code;
    private String fullName;
    private Integer inventory;
    private Double price;
    private LocalDateTime gmtCreated;
    @Column(name = "is_deleted")
    private Boolean deleted;
    private LocalDateTime gmtDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public LocalDateTime getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(LocalDateTime gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getGmtDeleted() {
        return gmtDeleted;
    }

    public void setGmtDeleted(LocalDateTime gmtDeleted) {
        this.gmtDeleted = gmtDeleted;
    }
}
