package com.wmx.excel.test.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import java.util.Date;

/**
 * 使用 ExcelProperty 注解将实体属性与 excel 文件的列对应起来，否则默认是按顺序匹配的
 * 日期类型也能轻松处理，如表格中为 1999/9/15、1999-09-15 等等日期各式都能解析成功
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/6/27 14:02
 */
@ColumnWidth(15)
public class IndexOrNameData {

    /**
     * 忽略这个字段不与 excel 中的类匹配
     */
    @ExcelIgnore
    private Integer id;

    /**
     * @ExcelProperty 注解常用属性
     * index：表示下表匹配，从0开始，即 name 属性对应表格中的第1列的值，依此类推
     * value：表示名称匹配，直接指明对应表中的哪一列，值是一个数组，按顺序为一级标题、二级标题，相同时，会自动合并.
     * 官方不建议 index 和 name 同时使用，同一个对象中，要么只用 index，要么只用 name。
     */
    @ExcelProperty(index = 0, value = {"客户信息汇总表", "姓名"})
    private String name;

    @ColumnWidth(12)
    @ExcelProperty(index = 1, value = {"客户信息汇总表", "性别"})
    private String gender;

    @ExcelProperty(index = 2, value = {"客户信息汇总表", "年龄"})
    @ColumnWidth(12)
    private Integer age;

    @ExcelProperty(index = 3, value = {"客户信息汇总表", "手机号码"})
    private String phoneNumber;

    @ExcelProperty(index = 4, value = {"客户信息汇总表", "生日"})
    @ColumnWidth(20)
    private Date birthday;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "IndexOrNameData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
