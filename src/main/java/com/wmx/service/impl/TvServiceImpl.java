package com.wmx.service.impl;

import com.wmx.entity.Tv;
import com.wmx.repository.TvRepository;
import com.wmx.service.TvService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
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
import java.util.Optional;

/**
 * @author wangmaoxiong
 */
@Service
public class TvServiceImpl implements TvService {
    @Resource
    private TvRepository tvRepository;

    /**
     * Cacheable ：对结果进行缓存
     * cacheNames : 缓存的名称，用于确定目标缓存.
     *
     * @return
     */
    @Override
    @Cacheable(cacheNames = "com.wmx.service.impl.TvServiceImpl.findAll")
    public List<Tv> findAll() {
        //org.springframework.data.jpa.repository.JpaRepository.findAll():没有数据时，返回空列表，不会为null
        System.out.println("查询所有数据.");
        return tvRepository.findAll();
    }

    @Override
    @Cacheable(cacheNames = "com.wmx.service.impl.TvServiceImpl.findAllById")
    public List<Tv> findAllById(List<Integer> ids) {
        System.out.println("根据 id列表 查询数据.");
        return tvRepository.findAllById(ids);
    }

    @Override
    public Tv findById(int id) {
        Optional<Tv> tvOptional = tvRepository.findById(id);
        //判断是否存在. 存在时取值
        if (tvOptional.isPresent()) {
            return tvOptional.get();
        }
        return null;
    }

    @Override
    public Tv findOne(Tv tv) {
        Example<Tv> tvExample = Example.of(tv);
        //<S extends T> Optional<S> findOne(Example<S> example)：条件查询单条数据
        //以 Example 中的实体中的非null字段为条件进行查询。无数据时Optional.isPresent为false
        //如果查询的结果多余1条，则 findOne 抛异常：NonUniqueResultException
        Optional<Tv> tvOptional = tvRepository.findOne(tvExample);
        if (tvOptional.isPresent()) {
            return tvOptional.get();
        }
        return null;
    }

    @Override
    public List<Tv> findAllSort() {
        //org.springframework.data.domain.Sort：指定排序字段
        //by(Direction direction, String... properties)：Direction：方向、趋势，properties：排序的字段
        //asc：顺序排序，desc：倒序排序
        Sort sort1 = Sort.by(Sort.Direction.DESC, "tvId");
        //同时指定多个字段进行排序。以 tvId 倒序查询，当 tvId 相同时，使用 tvPrice 升序
        Sort sort2 = Sort.by(Sort.Order.desc("tvId"), Sort.Order.asc("tvPrice"));
        return tvRepository.findAll(sort2);
    }

    @Override
    public Page<Tv> findPageable(int page, int size) {
        //of(int page, int size, Sort sort)：分页请求,page 表示请求的页码，从 0 开始，size 表示每页的条数
        //还有 PageRequest of(int page, int size)、of(int page, int size, Direction direction, String... properties)
        page--;
        page = page < 0 ? 0 : page;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "tvId"));
        //Page<T> findAll(Pageable pageable)：分页查询
        //查询无数据时，不会有影响，只是数据大小为0而已
        return tvRepository.findAll(pageable);
    }

    @Override
    public Page<Tv> findAll(Tv tv, int page, int size) {
        page--;
        page = page < 0 ? 0 : page;
        //设置查询条件
        Example<Tv> tvExample = Example.of(tv);
        //pageable 分页请求可以设置分页以及排序字段
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("tvId")));
        return tvRepository.findAll(tvExample, pageable);
    }

    @Override
    public Tv getOneById(int id) {
        if (tvRepository.existsById(id)) {
            //T getOne(ID id)：根据id查询单条数据。如果 id 不存在，则抛异常：EntityNotFoundException
            return tvRepository.getOne(id);
        }
        return null;
    }

    @Override
    public List<Tv> findAllExample(Tv tv) {
        //static <T> Example<T> of(T probe)
        Example<Tv> tvExample = Example.of(tv);
        //<S extends T> List<S> findAll(Example<S> example)
        // 根据 example 的 probe 中非 null 的字段进行调节查询.不存在时，返回一个空list
        //如果实体的所有字段都为null，则默认查询所有。
        return tvRepository.findAll(tvExample);
    }

    @Override
    public List<Tv> findAll(Tv tv, Sort sort) {
        Example<Tv> tvExample = Example.of(tv);//static <T> Example<T> of(T probe)
        //以 tvId 倒序查询，当 tvId 相同时，使用 tvPrice 升序:Sort.by(Sort.Order.desc("tvId"), Sort.Order.asc("tvPrice"));
        return tvRepository.findAll(tvExample, sort);
    }

    @Override
    public long count() {
        //查询表中的数据条数，表中无数据时，返回0
        return tvRepository.count();
    }

    @Override
    public void saveOrUpdate(Tv tv) {
        //如果实体的主键为null，直接新增数据；如果id不为null，则查询数据库是否有此id，有则修改，没果则新增（主键以主键生成策略进行生成）
        tvRepository.save(tv);
    }

    @Override
    public void saveAll(List<Tv> tvList) {
        tvRepository.saveAll(tvList);//同时保持与更新多条数据。底层也是调用 save 方法
    }

    @Override
    public void deleteById(int id) {
        if (tvRepository.existsById(id)) {//先判断是否存在
            //如果主键为id 的数据不存在，则抛异常：EmptyResultDataAccessException
            tvRepository.deleteById(id);//根据主键Id删除
        }
    }

    @Override
    public void deleteAll() {
        tvRepository.deleteAll();//删除表中所有数据。数据不存在时，也没问题
    }

    @Override
    public void delete(Tv tv) {
        //本质也是根据实体的主键 id 进行删除
        //所以如果设置主键以外的属性是没用的
        tvRepository.delete(tv);
    }

    @Override
    public void deleteAll(List<Tv> tvList) {
        //deleteAll(Iterable<? extends T> entities)
        //一次删除多个实体主键对应的多条数据，本质也就是多次的 delete(T entity)。值不存在时，不会影响
        tvRepository.deleteAll(tvList);
    }

    @Override
    public void deleteInBatch(List<Tv> tvList) {
        //deleteInBatch(Iterable<T> entities)：根据实体主键批量删除。值不存在时，不影响
        tvRepository.deleteInBatch(tvList);
    }

    @Override
    public void deleteAllBatch() {
        //批量删除表中所有数据。表中无数据时，不影响。
        tvRepository.deleteAllInBatch();
    }

    @Override
    public List<Tv> findAll(Date start, Date end, String tvName) {
        //直接使用匿名内部类实现接口
        Specification specification = new Specification<Tv>() {
            @Override
            public Predicate toPredicate(Root<Tv> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                //条件1：查询 tvName 为 海信 的数据，root.get 中的值与 Tv 实体中的属性名称对应
                if (tvName != null && !"".equals(tvName)) {
                    predicateList.add(cb.equal(root.get("tvName").as(String.class), tvName));
                }

                //条件2：Tv 生产日期（dateOfProduction）大于等于 start 的数据，root.get 中的 dateOfProduction 必须对应 Tv 中的属性
                predicateList.add(cb.greaterThanOrEqualTo(root.get("dateOfProduction").as(Date.class), start));

                //条件3：Tv 生产日期（dateOfProduction）小于等于 end
                predicateList.add(cb.lessThanOrEqualTo(root.get("dateOfProduction").as(Date.class), end));

                Predicate[] pre = new Predicate[predicateList.size()];
                pre = predicateList.toArray(pre);
                return query.where(pre).getRestriction();
            }
        };
        //没有数据时，返回空列表
        return tvRepository.findAll(specification);
    }

    @Override
    public Page<Tv> findAll(Date start, int page, int size) {
        //page 为页码，数据库从0页开始
        page--;
        page = page < 0 ? 0 : page;
        //可以使用重载的 of(int page, int size, Sort sort) 方法指定排序字段
        Pageable pageable = PageRequest.of(page, size);
        //创建查询规范
        Specification<Tv> tvSpecification = new Specification<Tv>() {
            @Override
            public Predicate toPredicate(Root<Tv> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                //查询生产日期在 start 与当期时间之间的数据，闭区间
                predicateList.add(cb.between(root.get("dateOfProduction").as(Date.class), start, new Date()));
                Predicate[] predicates = new Predicate[predicateList.size()];
                return query.where(predicateList.toArray(predicates)).getRestriction();
            }
        };
        //无数据时返回空列表
        return tvRepository.findAll(tvSpecification, pageable);
    }

    @Override
    public List<Tv> findAllLike(String tvNameLike) {
        Specification<Tv> tvSpecification = new Specification<Tv>() {
            @Override
            public Predicate toPredicate(Root<Tv> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
