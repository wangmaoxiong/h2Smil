package com.wmx.excel.pojo;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Date;

/**
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/6/28 9:14
 */
public class IndexData {
    /**
     * 列索引为 0，2，4，6，8，则插入到 excel 效果是奇数列有值，偶数列是空的
     */
    @ExcelProperty(value = "编号", index = 0)
    private Integer id;

    @ExcelProperty(value = "名称", index = 2)
    private String name;

    @ExcelProperty(value = "数量", index = 4)
    private Integer number;

    @ExcelProperty(value = "单价", index = 6)
    private Float univalent;

    @ExcelProperty(value = "出货日期", index = 8)
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
        return "IndexData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", univalent=" + univalent +
                ", shipDate=" + shipDate +
                '}';
    }
}
