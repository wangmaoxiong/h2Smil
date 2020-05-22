package com.wmx.service;

import com.wmx.entity.Tv;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;

/**
 * @author wangmaoxiong
 * Created by Administrator on 2019/4/27.
 */
public interface TvService {

    /**
     * 查询表中所有数据
     *
     * @return
     */
    List<Tv> findAll();

    /**
     * 根据多个主键Id查询
     *
     * @param ids
     * @return
     */
    List<Tv> findAllById(List<Integer> ids);

    /**
     * 根据主键id查询
     *
     * @param id
     * @return
     */
    Tv findById(int id);

    /**
     * 查询所有并指定排序字段
     *
     * @return
     */
    List<Tv> findAllSort();

    /**
     * 根据主键id查询
     *
     * @param id
     * @return
     */
    Tv getOneById(int id);

    /**
     * 条件查询单条数据
     *
     * @param tv
     * @return
     */
    Tv findOne(Tv tv);

    /**
     * 条件查询多条数据
     *
     * @param tv
     * @return
     */
    List<Tv> findAllExample(Tv tv);

    /**
     * 条件查询，且指定排序字段
     *
     * @param tv
     * @param sort
     * @return
     */
    List<Tv> findAll(Tv tv, Sort sort);

    /**
     * 分页查询，以 tvId 倒序排序
     *
     * @param page
     * @param size
     * @return
     */
    Page<Tv> findPageable(int page, int size);

    /**
     * 条件查询，加分页。同时指定以 tvId 倒序排序
     *
     * @param tv
     * @param page
     * @param size
     * @return
     */
    Page<Tv> findAll(Tv tv, int page, int size);

    /**
     * 查询总数
     *
     * @return
     */
    long count();

    /**
     * 添加或更新
     *
     * @param tv
     */
    void saveOrUpdate(Tv tv);

    /**
     * 同时添加多个
     *
     * @param tvList
     */
    void saveAll(List<Tv> tvList);

    /**
     * 根据主键id查询
     *
     * @param id
     */
    void deleteById(int id);

    /**
     * 删除所有
     */
    void deleteAll();

    /**
     * 条件删除单个
     *
     * @param tv
     */
    void delete(Tv tv);

    /**
     * 删除多个实体
     *
     * @param tvList
     */
    void deleteAll(List<Tv> tvList);

    /**
     * 批量删除多个
     *
     * @param tvList
     */
    void deleteInBatch(List<Tv> tvList);

    /**
     * 批量删除所有数据
     */
    void deleteAllBatch();

    /**
     * 条件查询时间范围在 [start,end] 之间的数据。如果 tvName 不为空，加上名称条件
     *
     * @param start
     * @param end
     * @param tvName
     * @return
     */
    List<Tv> findAll(Date start, Date end, String tvName);

    /**
     * 查询生产日期大于等于 start 的数据，且进行分页查询
     *
     * @param start
     * @param page
     * @param size
     * @return
     */
    Page<Tv> findAll(Date start, int page, int size);

    /**
     * 模糊查询 like
     *
     * @param tvNameLike
     * @return
     */
    List<Tv> findAllLike(String tvNameLike);
}
