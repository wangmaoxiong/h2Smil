package com.wmx.controller;

import com.wmx.entity.Article;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import io.searchbox.indices.DeleteIndex;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * jest 客户端操作 elasticsearch 搜索引擎
 *
 * @author wangmaoxiong
 * @version 1.0
 * @date 2020/5/31 17:12
 */
@RestController
public class JestController {
    /**
     * 当导入了 io.searchbox.jest 依赖后，程序启动时，{@link JestAutoConfiguration} 配置类会自动往容器中添加 {@link JestClient}
     */
    @Resource
    private JestClient jestClient;

    /**
     * 添加、修改文档
     * 1、文档主键 id 不传时，es 会自动生成
     * 2、文档主键 id 已经存在时，做更新操作，否则添加文档
     * post 请求 http://localhost:8080/jest/add?indexName=international&type=news
     *
     * @param indexName ：索引名称，相当于 mysql 的数据库
     * @param type      ：类型名称，相当于 mysql 的表
     * @param article   {"id":1,"title":"二战以来首次！明尼苏达州动员全州国民警卫队 ","publishTime":"2020-05-31T11:12:00","from":"搜狐新闻","visits":11000,"content":"二战以来首次！明尼苏达州动员全州国民警卫队"}
     * @return
     */
    @PostMapping("jest/add")
    public String addDocument(@RequestParam String indexName, @RequestParam String type, @RequestBody Article article) {
        String result;
        try {
            //创建索引。article 是需要新增的文档，指定索引与类型
            Index index = new Index.Builder(article).index(indexName).type(type).build();
            //execute(Action<T> clientRequest): 执行动作。所有的增删改查都是同样的套路
            DocumentResult documentResult = jestClient.execute(index);
            result = documentResult.getJsonString();
        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }

    /**
     * 全文搜索文档
     * http://localhost:8080/jest/get?indexName=international&type=news
     *
     * @param indexName ：索引名称，相当于 mysql 的数据库
     * @param type      ：类型名称，相当于 mysql 的表
     * @param query     ：es 全文索引语法，{"query":{"match":{"content":"明尼苏达州"}}}
     * @return
     */
    @GetMapping("jest/get")
    public String getDocument(@RequestParam String indexName, @RequestParam String type, @RequestBody String query) {
        String result;
        try {
            Search search = new Search.Builder(query).addIndex(indexName).addType(type).build();
            SearchResult searchResult = jestClient.execute(search);
            result = searchResult.getJsonString();
        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }

    /**
     * 根据主键 id 删除文档
     * http://localhost:8080/jest/deleteDoc?indexName=international&type=news&id=1
     *
     * @param indexName ：索引名称，相当于 mysql 的数据库
     * @param type      ：类型名称，相当于 mysql 的表
     * @param id        文档主键 id
     * @return
     */
    @GetMapping("jest/deleteDoc")
    public String deleteDocument(@RequestParam String indexName, @RequestParam String type, @RequestParam String id) {
        String result;
        try {
            Delete delete = new Delete.Builder(id).index(indexName).type(type).build();
            DocumentResult documentResult = jestClient.execute(delete);
            result = documentResult.getJsonString();
        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }

    /**
     * 删除整个 es 索引，相当于删除整个数据库
     * http://localhost:8080/jest/deleteIndex?indexName=international&type=news
     *
     * @param indexName ：索引名称，相当于 mysql 的数据库
     * @return
     */
    @GetMapping("jest/deleteIndex")
    public String deleteIndex(@RequestParam String indexName) {
        String result;
        try {
            DeleteIndex delete = new DeleteIndex.Builder(indexName).build();
            JestResult jestResult = jestClient.execute(delete);
            result = jestResult.getJsonString();
        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }
}
