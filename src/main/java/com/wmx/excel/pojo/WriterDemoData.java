package com.wmx.excel.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Date;

/**
 * 实体对象
 * 1、如果想要忽略某个字段不与 excel 中的列匹配，请使用 {@link @com.alibaba.excel.annotation.ExcelIgnore}
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/6/28 8:58
 */
public class WriterDemoData {

    @ExcelProperty(value = "编号")
    private Integer id;

    @ExcelProperty(value = "名称")
    private String name;

    @ExcelProperty(value = "数量")
    private Integer number;

    @ExcelProperty(value = "单价")
    private Float univalent;

    @ExcelProperty(value = "出货日期")
    private Date shipDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Float getUnivalent() {
        return univalent;
    }

    public void setUnivalent(Float univalent) {
        this.univalent = univalent;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    @Override
    public String toString() {
        return "WriterDemoData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", univalent=" + univalent +
                ", shipDate=" + shipDate +
                '}';
    }
}
