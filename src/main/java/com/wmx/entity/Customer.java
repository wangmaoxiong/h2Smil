package com.wmx.entity;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 客户实体类
 * 这里不建议 index 和 name 同时用，要么一个对象只用index，要么一个对象只用name去匹配
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/6/27 10:55
 */
public class Customer {
    private Integer id;

    /**
     * @ExcelProperty：读取数据使用 通过指定index可以对应读取的excel里面的列，然后定义的数据类型就对应到excel里面具体的值
     */
    @ExcelProperty(value = "用户名")
    private String name;

    @ExcelProperty(value = "年龄")
    private Integer age;

    @ExcelProperty(index = 2)
    private String gender;


    @ExcelProperty(index = 3)
    private String phoneNumber;

    public Customer() {
    }

    public Customer(Integer id, String name, String gender, Integer age, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

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

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
