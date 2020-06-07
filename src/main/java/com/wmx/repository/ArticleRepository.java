package com.wmx.repository;

import com.wmx.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 1、像 JPA 一样，继承一个 ElasticsearchRepository 接口，便可以获得常用的增删改查方法
 * 2、接口上不需要写 @Repository 注解，会自动注入到容器中
 * 3、ElasticsearchRepository<T, ID extends Serializable>：T 是数据类型，ID 是主键类型
 * 4、往 es 中添加文档时，如果没有设置文档主键，则会自动生成
 *
 * @author wangmaoxiong
 * @version 1.0
 * @date 2020/6/4 19:14
 */
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {

    /**
     * 根据 content 字段模糊查询
     * 1、根据关键字对 content 内容字段进行全文检索
     * 2、只需要定义方法即可，不需要实现
     * 3、方法名称按着约定进行定义（IDEA会自动提示，非常智能贴心），比如下面就是按着 content 字段进行模糊(like)查询
     * 4、经过实测，参数 keyword 的值中间不能有空格，否则会报错
     * 5、方法定义的规则可以参考官网：https://docs.spring.io/spring-data/elasticsearch/docs/4.0.0.RELEASE/reference/html/#elasticsearch.repositories
     * 6、这个方法命名 spring data elasticsearch 会自动转为 es 命令： {"query":{"bool":{"must":[{"query_string":{"query":"?*","fields":["content"]}}]}}}
     * 7、查询结果默认会按相关性从高到低进行输出
     *
     * @param keyword
     * @return
     */
    List<Article> findByContentLike(String keyword);

    /**
     * 根据 title 字段模糊查询
     * 1、除了使用 spring data elasticsearch 规范命名方法名称，也可以直接将 es 命令使用 @Query 注解写在方法上，此时方法名称可以随意定义
     * 2、spring data elasticsearch 会自动转为 es 命令 ：{"query":{"match":{"title":{"query":"keyword"}}}}
     * 3、官网示例：https://docs.spring.io/spring-data/elasticsearch/docs/4.0.0.RELEASE/reference/html/#elasticsearch.query-methods.at-query
     * 4、如果不想写 es 命令，则可以使用方法名称：findByTitleLike
     * 5、特别注意：spring-data-elasticsearch 版本不一样，@Query 中的命令语法也不太一样
     * 6、特别注意：@Query 注解的命令与 ES 实际的命令语法稍微有些区别，需要注意，比如开始 query 属性注解中不需要写，以及占位符有个 0
     *
     * @param keyword  ：待检索的关键字
     * @param pageable ：分页设置，需要包含查询的页码与每页的条数，页码从 0 开始
     * @return
     */
    @Query("{\"match\": {\"title\": {\"query\": \"?0\"}}}")
    Page<Article> findTitle(String keyword, Pageable pageable);

    /**
     * 从 title 与 content 字段进行模糊查询
     * 1、下面 @Query 的 es 命令等价于方法名称 findByTitleAndContentLike
     * 2、不写命令可以修改方法名称为 findByTitleAndContentLike 即可
     *
     * @param keyword
     * @param pageable
     * @return
     */
    @Query("{\"bool\":{\"must\":[{\"query_string\":{\"query\":\"?0\",\"fields\":[\"content\",\"title\"]}}]}}")
    Page<Article> findTitleContent(String keyword, Pageable pageable);

}
