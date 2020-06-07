package com.wmx.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wmx.entity.Article;
import com.wmx.repository.ArticleRepository;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * spring-data-elasticsearch 之  ElasticsearchRepository 操作 es
 *
 * @author wangmaoxiong
 * @version 1.0
 * @date 2020/6/4 19:18
 */
@RestController
public class ArticleEsController {
    @Resource
    private ArticleRepository articleRepository;

    /**
     * 查询所有文档
     * http://localhost:8080/article/es/findAll
     *
     * @return
     */
    @GetMapping("article/es/findAll")
    public String findArticle() {
        String result = "{}";
        try {
            /**
             * 1、返回的 Iterable<Article> 实质是一个 Json 对象，其中包含 maxScore mapOfFacets total content pageable
             * 2、其中 maxScore 表示最高得分，total 表示总记录数，content 表示实际的文档数据，pageable 表示分页信息
             * 3、如果直接使用 Iterator<Article> iterator = articles.iterator(); 进行迭代，则获取的至少 content 的内容
             */
            //不存在时返回 {"total":0,"content":[],"pageable":"INSTANCE"}
            Iterable<Article> articles = articleRepository.findAll();
            result = new Gson().toJson(articles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据 id 列表查询多个文档
     * http://localhost:8080/article/es/findAllById
     *
     * @param ids ["1","2"]
     * @return
     */
    @GetMapping("article/es/findAllById")
    public String findAllById(@RequestBody List<String> ids) {
        //返回值里面只要文档数据，没有多余的 maxScore mapOfFacets total content pageable 属性
        //当无数据时，返回一个空迭代器，不用担心空指针异常
        Iterable<Article> articles = articleRepository.findAllById(ids);
        return new Gson().toJson(articles);
    }

    /**
     * 根据 id 查询单个文档
     * http://localhost:8080/article/es/findById?id=xxx
     *
     * @param id
     * @return
     */
    @GetMapping("article/es/findById")
    public String findById(@RequestParam String id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            return new Gson().toJson(article);
        }
        return "no exist";
    }

    /**
     * search 方法查询.     http://localhost:8080/article/es/search?keyword=特朗普
     * <p>
     * search(QueryBuilder query)：查询检索
     * search(QueryBuilder query, Pageable pageable)：带分页检索
     * search(SearchQuery searchQuery): NativeSearchQuery
     * searchSimilar(T entity, String[] fields, Pageable pageable) 相似搜索
     * </p>
     * <p>
     * {@link QueryBuilders} 工具类，其中提供了大量的创建 QueryBuilder 的静态方法，比如：
     * QueryBuilders.matchAllQuery()：查询所有文档
     * matchQuery(String name, Object text)：为提供的字段名和文本创建类型为“BOOLEAN”的匹配查询。
     * multiMatchQuery(Object text, String... fieldNames): 同时对多个字段检索
     * commonTermsQuery(String fieldName, Object text):为提供的字段名和文本创建公共查询。
     * matchPhraseQuery(String name, Object text): 为提供的字段名和文本创建类型为“PHRASE”的文本查询。
     * ...
     * </p>
     *
     * @param keyword 检索的关键字
     * @param page    页码，从 0 开始
     * @param size    每页显示的条数
     * @return
     */
    @GetMapping("article/es/search")
    public Iterable<Article> search(@RequestParam String keyword, Integer page, Integer size) {
        page = page == null ? 0 : page;
        size = size == null ? 10 : size;
        //构建分页查询对象。设置查询的页码与条数，以及排序的方式为"publishTime-发布日期倒序"
        //默认会按着结果的相关性由高到低排序
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("publishTime")));
        MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword, "from", "title", "content");
        Iterable<Article> articles = articleRepository.search(queryBuilder, pageRequest);
        return articles;
    }

    /**
     * 根据关键字从 content 字段进行全文检索（模糊查询）
     * http://localhost:8080/article/es/findByContentLike?keyword=英国
     *
     * @param keyword
     * @return
     */
    @GetMapping("article/es/findByContentLike")
    public List<Article> findByContentLike(@RequestParam String keyword) {
        //强制去掉查询关键字中的空格，否则会报错
        keyword = keyword.replaceAll("\\s", "");
        List<Article> contentLike = articleRepository.findByContentLike(keyword);
        System.out.println(new Gson().toJson(contentLike));
        return contentLike;
    }

    /**
     * http://localhost:8080/article/es/findByTitle?keyword=国安法&page=0&size=10
     *
     * @param keyword ：待查询的关键字
     * @param page    页码，从 0 开始
     * @param size    每页显示的条数
     * @return ：查询结果默认按相关性从高到低排列，即相关性高的靠前
     */
    @GetMapping("article/es/findByTitle")
    public List<Article> findByTitle(@RequestParam String keyword, Integer page, Integer size) {
        page = page == null ? 0 : page;
        size = size == null ? 10 : size;
        //强制去掉关键字中的空格，否则存储库中的自定义方法会抛异常
        keyword = keyword.replaceAll("\\s", "");
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articlePage = articleRepository.findTitle(keyword, pageable);
        List<Article> articleList = articlePage.getContent();
        return articleList;
    }

    /**
     * http://localhost:8080/article/es/findKeyword?keyword=英国首相 喊话特朗普&page=0&size=10
     *
     * @param keyword ：待查询的关键字
     * @param page    页码，从 0 开始
     * @param size    每页显示的条数
     * @return ：查询结果默认按相关性从高到低排列，即相关性高的靠前
     */
    @GetMapping("article/es/findKeyword")
    public List<Article> findByTitleLikeAAndContent(@RequestParam String keyword, Integer page, Integer size) {
        page = page == null ? 0 : page;
        size = size == null ? 20 : size;
        //强制去掉关键字中的空格，否则存储库中的自定义方法会抛异常
        keyword = keyword.replaceAll("\\s", "");
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articlePage = articleRepository.findTitleContent(keyword, pageable);
        List<Article> articleList = articlePage.getContent();
        return articleList;
    }

    /**
     * 保存文档
     * 1、article 有主键 id 时，则使用传入的 id，没有传入主键 id 时，则会自动生成
     * 2、如果 id 已经存在，则执行更新操作，如果不存在，则新增
     * http://localhost:8080/article/es/save
     *
     * @param article {"title":"二英国首相罕见喊话特朗普：种族主义在我们的社会没有立足之地","publishTime":"2020-05-31T11:12:00","from":"搜狐新闻","visits":11000,"content":"英国首相罕见喊话特朗普：种族主义在我们的社会没有立足之地"}
     * @return
     */
    @PostMapping("article/es/save")
    public String saveArticle(@RequestBody Article article) {
        /**
         * saveAll(Iterable<S> entities)：支持同时保存多个
         * save(S entity) 保存实体，返回保存成功的实体
         * index(S entity)：保存实体对象，底层也是调用 save(S entity)
         */
        Article save = articleRepository.save(article);
        articleRepository.index(article);
        return new Gson().toJson(save);
    }

    /**
     * 获取索引下的文档总数
     * http://localhost:8080/article/es/count
     *
     * @return
     */
    @GetMapping("article/es/count")
    public String getCount() {
        //没有时返回 0
        long count = articleRepository.count();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", 200);
        jsonObject.addProperty("count", count);
        return jsonObject.toString();
    }

    /**
     * 删除实体：根据主键删除文档
     * http://localhost:8080/article/es/delete
     *
     * @param article ：实体中只需要包含主键 id 属性即可，其它字段可以不需要，id 不存在时会报错。
     *                {"id":"9nAsh3IBxA9BlkRxmNpM"}
     * @return
     */
    @PostMapping("article/es/delete")
    public String deleteArticle(@RequestBody Article article) {
        //delete(T entity) ：删除实体。底层也是使用 deleteById
        articleRepository.delete(article);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", 200);
        jsonObject.add("data", new Gson().toJsonTree(article));
        return jsonObject.toString();
    }

    /**
     * 根据文档 id 删除文档
     * http://localhost:8080/article/es/deleteById?id=xxx
     *
     * @param id
     * @return
     */
    @GetMapping("article/es/deleteById")
    public String deleteById(@RequestParam String id) {
        articleRepository.deleteById(id);
        return new Gson().toJson(id);
    }

    /**
     * 删除索引下的所有文档，相当于删库
     * http://localhost:8080/article/es/deleteAll
     *
     * @return
     */
    @GetMapping("article/es/deleteAll")
    public String deleteAll() {
        articleRepository.deleteAll();
        return "success";
    }

    /**
     * 根据 id 判断文档是否存在
     * http://localhost:8080/article/es/existsById?id=xxx
     *
     * @param id
     * @return
     */
    @GetMapping("article/es/existsById")
    public String existsById(@RequestParam String id) {
        boolean exists = articleRepository.existsById(id);
        return new Gson().toJson(exists);
    }

}

