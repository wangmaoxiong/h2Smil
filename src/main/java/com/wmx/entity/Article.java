package com.wmx.entity;

import io.searchbox.annotations.JestId;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

/**
 * 1、@Document 注解用于标识实体（文档）存储的位置，即指定索引、类型，相当于存在在哪个数据库的哪个表中
 * 2、索引、类型不存在时，会自动创建
 * 3、id 属性上面的 @JestId 注解是使用 Jest 客户端时添加的，不使用 Jest 时可以去掉，不影响 spring-data-elasticsearch
 *
 * @author wangmaoxiong
 * @version 1.0
 * @date 2020/5/31 17:15
 */
@Document(indexName = "sports", type = "news")
public class Article {

    /**
     * @JestId ：Jest 客户端将其标识为文档 id，jest 会自动将它作为文档 id 处理，如：
     * 新增文档，用户传入了 id，如果 id 已经存在，则可以直接更新
     * 新增文档，用户没传入 id 时，es 会自动生成
     */
    @JestId
    private String id;
    private String title;
    private Date publishTime;
    private String from;
    private Long visits;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Long getVisits() {
        return visits;
    }

    public void setVisits(Long visits) {
        this.visits = visits;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", publishTime=" + publishTime +
                ", from='" + from + '\'' +
                ", visits=" + visits +
                ", content='" + content + '\'' +
                '}';
    }
}
