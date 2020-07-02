package com.wmx.excel.test.pojo;

import java.util.Date;

/**
 * 基本数据对象
 * 1、和平时的 POJO 完全一样，easyExcel 默认会将 excel 文件的列与实体中的属性按顺序对应解析。
 * 2、比如默认会将第一列的值解析为 String 类型并赋给 name 属性，将第二列的值解析为 Integer 类型赋给 age 属性，依此类推，写入文件也是同理。
 * 3、如果第二列的年龄，用户填写的是 "18岁"，显然 Integer.parseInt(str) 会抛异常。
 * 4、表格某列的值未填写时，自动解析为  null；表格中的列多余实体的属性个数时不影响；表格中的列少余实体的属性个数时，自动设值为 null；
 * 5、日期类型也能轻松处理，如表格中为 1999/9/15、1999-09-15 等等日期各式，读取时都能解析成功
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/6/27 13:59
 */
public class SimpleData {

    private String name;

    private Integer age;

    private String gender;

    private String phoneNumber;

    private Date birthday;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
        return "SimpleData{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
