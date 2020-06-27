package com.wmx.excel.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.wmx.excel.converter.CustomStringStringConverter;

/**
 * 实体对象
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/6/27 14:10
 */
public class ConverterData {
    /**
     * converter：指定自定义转换器，对读取的值自己定义规则进行转换
     */
    @ExcelProperty(value = "用户名", converter = CustomStringStringConverter.class)
    private String name;

    /**
     * DateTimeFormat：日期格式化，即从 excel 中先将内容作为日期类型读取出来，然后再转为下面格式的字符串
     * 1、比如 excel 中的 "生日" 列有值为 2000/7/24，则实际读取的结果为 "2000年07月24日 00时00分00秒"
     */
    @ExcelProperty(value = "生日")
    @DateTimeFormat("yyyy年MM月dd日 HH时mm分ss秒")
    private String birthday;

    /**
     * NumberFormat：数字格式化，即从 excel 中先将内容作为数值类型读取出来，然后转为百分号字符串
     * 1、 excel 中的 "回购率" 列有值为 0.689，则实际读取的结果为 "68.9%"
     */
    @ExcelProperty(value = "回购率")
    @NumberFormat("#.##%")
    private String repurchasing;

    public String getRepurchasing() {
        return repurchasing;
    }

    public void setRepurchasing(String repurchasing) {
        this.repurchasing = repurchasing;
    }

    public String getString() {
        return name;
    }

    public void setString(String string) {
        this.name = string;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ConverterData{" +
                "name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ",  repurchasing ='" + repurchasing + '\'' +
                '}';
    }
}