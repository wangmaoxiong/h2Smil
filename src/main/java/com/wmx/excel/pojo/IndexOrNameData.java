package com.wmx.excel.pojo;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Date;

/**
 * 使用 ExcelProperty 注解将实体属性与 excel 文件的列对应起来，否则默认是按顺序匹配的
 * 日期类型也能轻松处理，如表格中为 1999/9/15、1999-09-15 等等日期各式都能解析成功
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/6/27 14:02
 */
public class IndexOrNameData {

    private Integer id;

    /**
     * @ExcelProperty 注解常用属性
     * index：表示下表匹配，从0开始，即 name 属性对应表格中的第1列的值，依此类推
     * value：表示名称匹配，直接指明对应表中的哪一列
     * 官方不建议 index 和 name 同时使用，同一个对象中，要么只用 index，要么只用 name。
     */
    @ExcelProperty(index = 0)
    private String name;

    @ExcelProperty("性别")
    private String gender;

    @ExcelProperty(index = 1)
    private Integer age;

    @ExcelProperty(value = "手机号码")
    private String phoneNumber;

    @ExcelProperty(value = "生日")
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
