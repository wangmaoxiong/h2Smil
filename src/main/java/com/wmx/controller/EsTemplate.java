package com.wmx.controller;

import com.wmx.entity.Article;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangmaoxiong
 * @version 1.0
 * @date 2020/6/4 16:58
 */
@RestController
public class EsTemplate {
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 查询文档总数
     * http://localhost:8080/article/temple/count
     *
     * @return
     */
    @GetMapping("article/temple/count")
    public long count() {
        //matchAllQuery 查询所有文档
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        NativeSearchQuery searchQuery = new NativeSearchQuery(queryBuilder);
        //count(SearchQuery searchQuery, Class<T> clazz): clazz 表示查询哪个索引与类型
        long count = elasticsearchTemplate.count(searchQuery, Article.class);
        return count;
    }

    /**
     * 全文检索
     * http://localhost:8080/article/temple/query?keyword=种族歧视
     *
     * @param keyword 检索的关键字
     * @param page    页码，从0开始
     * @param size    每页的条数
     * @return
     */
    @GetMapping("article/temple/query")
    public List<Article> query(@RequestParam String keyword, String page, String size) {
        List<Article> articles = null;
        try {
            //如果传入的页码或者显示条数为空，或者是非法数字，则使用默认值。isNumeric 对负数("-100")也是返回 false
            page = page == null ? "0" : StringUtils.isNumeric(page) ? page : "0";
            size = size == null ? "10" : StringUtils.isNumeric(size) ? size : "10";

            QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword, "title", "content");
            NativeSearchQuery searchQuery = new NativeSearchQuery(queryBuilder);
            //分页设置
            PageRequest pageRequest = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
            searchQuery.setPageable(pageRequest);
            articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

}
