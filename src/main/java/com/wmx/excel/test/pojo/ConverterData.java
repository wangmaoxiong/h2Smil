package com.wmx.excel.test.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.wmx.excel.test.converter.CustomStringStringConverter;

import java.util.Date;

/**
 * 实体对象
 * 1、如果对于导出的数据需要格式化，比如日期格式，数字格式化为百分号，字符串格式化等，可以参考本实体。
 * <p>
 * //@DateTimeFormat：日期格式化，即从 excel 中先将内容作为日期类型读取出来，然后再转为下面格式的字符串
 * 1、对于日期类型，默认输出的格式为 "2020-07-02 16:12:51"，读取的时候，只要日期自字段的值按着常规的 -，/等分割写法，都能自动解析为日期
 * 2、如果万一想要修改输出的日期格式，则可以使用此注解指定，读取的时候用户可以不使用此格式，使用其它常规的日期格式，也能解析成功。
 * </p>
 * <p>
 * //@NumberFormat：数值格式化，比如 @NumberFormat("#.##%") 表示将数字使用百分号输出，保留2位小数，如 0.45，写入到 excel 中为 45%
 * 反过来读取的时候，用户也应该按着此格式书写，否则将使用原值，不做格式化，比如 excel 文件中是 45%，则读取解析后为 0.45，而如果 -
 * excel 中为 45，则读取后仍然为 45，不会除以100.
 * </p>
 * <p>
 *
 * @author wangMaoXiong
 * @version 1.0
 * @ExcelProperty 注解中的 converter 属性用于自定义转换规则
 * </p>
 * @date 2020/6/27 14:10
 */
public class ConverterData {
    /**
     * @ExcelProperty value 属性指定 excel 中的标题，假如 excel 实际是二级标题，value 属性确只指定了二级标题，也是可以的,读取数据仍然可以读取成功。
     */
    @ExcelProperty(value = "用户名", converter = CustomStringStringConverter.class)
    private String name;

    @ExcelProperty(value = "生日")
    @DateTimeFormat("yyyy年MM月dd日 HH时mm分ss秒")
    private Date birthday;

    @ExcelProperty(value = "回购率")
    @NumberFormat("#.##%")
    private Float repurchasing;

    public Float getRepurchasing() {
        return repurchasing;
    }

    public void setRepurchasing(Float repurchasing) {
        this.repurchasing = repurchasing;
    }

    public String getString() {
        return name;
    }

    public void setString(String string) {
        this.name = string;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
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