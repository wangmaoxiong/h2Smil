package com.wmx.excel.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体对象
 * 1、如果实体中不加 @ExcelProperty 注解指明标题，则 EasyExcel.write 导出时默认使用属性名称作为 excel 文件的标题
 * 2、如果不想导出实体中的某个属性，则为它加上 {@link ExcelIgnore} 注解进行忽略即可
 * 3、@ExcelProperty 注解的 value 属性是一个字符串数组类型，用于指定标题名称，按顺序为 一级标题、二级标题...
 * 4、一个实体对象对应 excel 文件中的一行数据，对象中的属性默认从上到下对应 excel 从左到右的顺序，可以通过 index 指定对应关系
 * 5、@ExcelProperty 的 index 属性指定属性与 excel 中的列的对应关系，从0开始，即 0 对应文件中的第一列，1对应第二列，2对应第三列，依此类推
 * <p>
 * //@HeadRowHeight ：指定写入的 excel 的标题的行高，不包括正文内容，只能标记在类上、接口上、枚举上
 * //@ColumnWidth ：指定写入的 excel 的列宽，包括标题与正文内容，标识在类上对所有列生效，标识在字段上对单个字段生效
 * //@HeadStyle ：自定义excel表格标题单元格样式，比如前/背景色等，fillPatternType 是填充模式，fillForegroundColor 表示前景色，值从 {@link IndexedColors} 枚举中取
 *  默认标题会为灰色，字体为黑色，大小为 14
 * //@HeadFontStyle:自定义表格标题的字体样式、比如大小、颜色、字体、下划线等等。默认为 14，居中显示。fontHeightInPoints 表示字体大小，color 表示样式，值从 {@link IndexedColors} 枚举中取
 * //@ContentStyle：设置表格中正文内容单元格的样式，比如前/背景色等，默认无。同理 @HeadStyle
 * //@ContentFontStyle:设置表格中正文内容的样式，比如字体、颜色、大小等，默认大小为 11。同理 @HeadFontStyle
 * //上面的 @XxxStyle 样式注解可以标识在类、接口、枚举上，也可以标识在类上，它们还有很大有用的属性可以提供使用.
 *
 * @author wangMaoXiong
 * @version 1.0
 *
 * </p>
 * @date 2020/6/28 8:58
 */
@HeadRowHeight(20)
@ColumnWidth(15)
@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 51)
@HeadFontStyle(fontHeightInPoints = 16, color = 17)
@ContentStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 17)
@ContentFontStyle(fontHeightInPoints = 12, color = 9)
public class WriterDemoData implements Serializable {

    /**
     * @ExcelIgnore 表示忽略此字段，即不会往 excel 中写入
     */
    @ExcelIgnore
    private String password;

    /**
     * 这个标题效果就是一级标题会跨列合并据中，然后第二行显示二级标题
     */
    @ExcelProperty(value = {"订单信息汇总表", "编号"})
    @ColumnWidth(10)
    private Integer id;

    @ExcelProperty(value = {"订单信息汇总表", "名称"})
    private String name;

    @ExcelProperty(value = {"订单信息汇总表", "数量"}, index = 2)
    private Integer number;

    @ExcelProperty(value = {"订单信息汇总表", "单价"}, index = 3)
    private Float univalent;

    /**
     * 日期写入到 excel 文件中的默认格式为： 2020-06-30 21:05:47
     * 可以使用 @DateTimeFormat 注解指定日期转成字符串后的格式
     */
    @ExcelProperty(value = {"订单信息汇总表", "出货日期"})
    @DateTimeFormat("yyyy年MM月dd日 HH时mm分ss秒")
    @ColumnWidth(30)
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
                "password='" + password + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", univalent=" + univalent +
                ", shipDate=" + shipDate +
                '}';
    }
}
