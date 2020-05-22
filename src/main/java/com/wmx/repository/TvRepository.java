package com.wmx.repository;

import com.wmx.entity.Tv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangmaoxoing
 * Java 接口可以多继承
 * JpaRepository 中有常用的 CRUD 、分页、排序等方法
 * JpaSpecificationExecutor 可以实现任意的复杂查询
 */
public interface TvRepository extends JpaRepository<Tv, Integer>, JpaSpecificationExecutor<Tv> {
}
