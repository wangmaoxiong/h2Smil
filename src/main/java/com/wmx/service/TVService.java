package com.wmx.service;

import com.wmx.entity.TV;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/4/27.
 */
public interface TVService {

    //条件查询时间范围在 [start,end] 之间的数据。如果 tvName 不为空，加上名称条件
    List<TV> findAll(Date start, Date end, String tvName);

    //查询生产日期大于等于 start 的数据，且进行分页查询
    Page<TV> findAll(Date start, int page, int size);

    //模糊查询 like
    List<TV> findAllLike(String tvNameLike);
}
