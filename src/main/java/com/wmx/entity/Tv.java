package com.wmx.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author wangmaoxiong
 * Created by Administrator on 2019/2/27 0027.
 * 1、电视机实体。应用启动时自动，配置文件中配置 ddl-auto: update：如果数据库不存在，则自动新建，否则不再新建。
 * 2、javax.persistence.Entity、javax.persistence.Id 注解必须要写，否则启动报错！
 */
@Entity
public class Tv {
    /**
     * 标识为主键
     * 指定主键生成的方式，AUTO 指定 H2 数据库主键自动增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer tvId;

    /**
     * 下面没标识的属性都会以默认值和数据库表的字段进行映射对应
     * 如果修改默认值，又不属性的，可以参考：https://blog.csdn.net/wangmx1993328/article/details/82048775
     * 中的 "domain Area" 部分
     */
    /**
     * 电视名称
     */
    private String tvName;
    /**
     * 电视价格
     */
    private Float tvPrice;
    /**
     * 生产日期
     */
    private Date dateOfProduction;

    public Date getDateOfProduction() {
        return dateOfProduction;
    }

    public void setDateOfProduction(Date dateOfProduction) {
        this.dateOfProduction = dateOfProduction;
    }

    public Integer getTvId() {
        return tvId;
    }

    public void setTvId(Integer tvId) {
        this.tvId = tvId;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public Float getTvPrice() {
        return tvPrice;
    }

    public void setTvPrice(Float tvPrice) {
        this.tvPrice = tvPrice;
    }

    @Override
    public String toString() {
        return "Tv{" +
                "tvId=" + tvId +
                ", tvName='" + tvName + '\'' +
                ", tvPrice=" + tvPrice +
                ", dateOfProduction=" + dateOfProduction +
                '}';
    }
}
