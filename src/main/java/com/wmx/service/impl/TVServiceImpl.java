package com.wmx.service.impl;

import com.wmx.entity.TV;
import com.wmx.repository.TVRepository;
import com.wmx.service.TVService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TVServiceImpl implements TVService {
    @Resource
    private TVRepository tvRepository;

    @Override
    public List<TV> findAll(Date start, Date end, String tvName) {
        //直接使用匿名内部类实现接口
        Specification specification = new Specification<TV>() {
            @Override
            public Predicate toPredicate(Root<TV> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                //条件1：查询 tvName 为 海信 的数据，root.get 中的值与 TV 实体中的属性名称对应
                if (tvName != null && !"".equals(tvName)) {
                    predicateList.add(cb.equal(root.get("tvName").as(String.class), tvName));
                }

                //条件2：TV 生产日期（dateOfProduction）大于等于 start 的数据，root.get 中的 dateOfProduction 必须对应 TV 中的属性
                predicateList.add(cb.greaterThanOrEqualTo(root.get("dateOfProduction").as(Date.class), start));

                //条件3：TV 生产日期（dateOfProduction）小于等于 end
                predicateList.add(cb.lessThanOrEqualTo(root.get("dateOfProduction").as(Date.class), end));

                Predicate[] pre = new Predicate[predicateList.size()];
                pre = predicateList.toArray(pre);
                return query.where(pre).getRestriction();
            }
        };
        return tvRepository.findAll(specification);//没有数据时，返回空列表
    }

    @Override
    public Page<TV> findAll(Date start, int page, int size) {
        page--;
        page = page < 0 ? 0 : page;//page 为页码，数据库从0页开始
        //可以使用重载的 of(int page, int size, Sort sort) 方法指定排序字段
        Pageable pageable = PageRequest.of(page, size);
        //创建查询规范
        Specification<TV> tvSpecification = new Specification<TV>() {
            @Override
            public Predicate toPredicate(Root<TV> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                //查询生产日期在 start 与当期时间之间的数据，闭区间
                predicateList.add(cb.between(root.get("dateOfProduction").as(Date.class), start, new Date()));
                Predicate[] predicates = new Predicate[predicateList.size()];
                return query.where(predicateList.toArray(predicates)).getRestriction();
            }
        };
        return tvRepository.findAll(tvSpecification, pageable);//无数据时返回空列表
    }

    @Override
    public List<TV> findAllLike(String tvNameLike) {
        Specification<TV> tvSpecification = new Specification<TV>() {
            @Override
            public Predicate toPredicate(Root<TV> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate[] predicates = new Predicate[1];
                //like(Expression<String> x, String pattern):参数 pattern 表示匹配的格式
                predicates[0] = cb.like(root.get("tvName").as(String.class), "%" + tvNameLike + "%");
                //同理以 xxx 开头，则为 tvNameLike + "%"
                return query.where(predicates).getRestriction();
            }
        };
        //规范查询的同时，指定以主键 tvId 倒序排序
        return tvRepository.findAll(tvSpecification, Sort.by(Sort.Direction.DESC, "tvId"));
    }
}
