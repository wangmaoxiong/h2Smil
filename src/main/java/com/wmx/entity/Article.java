package com.wmx.entity;

import io.searchbox.annotations.JestId;

import java.util.Date;

/**
 * @author wangmaoxiong
 * @version 1.0
 * @date 2020/5/31 17:15
 */
public class Article {

    /**
     * @JestId 将其标识为文档id，jest 会自动将它作为文档 id 处理.
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
}
