package com.wmx.controller;

import com.wmx.entity.Tv;
import com.wmx.service.TvService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * h2 数据库 Spring Data JPA CRUD
 *
 * @author wangmaoxiong
 * Created by Administrator on 2019/4/27.
 */
@Controller
public class TvController {
    @Resource
    private TvService tvService;

    private static Logger logger = Logger.getAnonymousLogger();

    /**
     * http://localhost:8080/tv/findAll
     * 查询所有
     */
    @GetMapping("tv/findAll")
    @ResponseBody
    public List<Tv> findAll() {
        logger.info("请求所有数据...");
        return tvService.findAll();
    }

    /**
     * http://localhost:8080/tv/findAllByIds?ids=1,2,3,4,5
     * 根据 id 列表同时查询多条数据
     *
     * @param ids ：格式 1,2,33,45
     * @return
     */
    @GetMapping("tv/findAllByIds")
    @ResponseBody
    public List<Tv> findAllByIds(String ids) {
        List<String> idList = new ArrayList<>();
        if (ids != null && !"".equals(ids)) {
            idList = Arrays.asList(ids.split(","));
        }
        List<Integer> idListInt = new ArrayList<>(idList.size());
        for (String s : idList) {
            idListInt.add(Integer.parseInt(s));
        }
        return tvService.findAllById(idListInt);
    }

    /**
     * http://localhost:8080/tv/findById?id=2
     * 根据id查询
     */
    @GetMapping("tv/findById")
    @ResponseBody
    public Tv findById(Integer id) {
        logger.info("根据Id进行检索...");
        id = id == null ? -1 : id;
        Tv tv = tvService.findById(id);
        return tv;
    }

    /**
     * http://localhost:8080/tv/findOne?name=59607b1a-4d5a-4b69-8409-b8fd80611839
     * 条件查询单条数据
     *
     * @param name
     * @return
     */
    @GetMapping("tv/findOne")
    @ResponseBody
    public Tv findOne(String name) {
        //以实体非null的字段进行调节查询
        Tv tv = new Tv();
        tv.setTvName(name);
        tv = tvService.findOne(tv);
        return tv;
    }

    /**
     * http://localhost:8080/tv/findAllSort
     * 查询所有，并指定以 tvId 倒序，tvPrice 升序
     *
     * @return
     */
    @GetMapping("tv/findAllSort")
    @ResponseBody
    public List<Tv> findAllSort() {
        return tvService.findAllSort();
    }

    /**
     * http://localhost:8080/tv/findPageable?page=1&size=50
     * 分页查询
     *
     * @param page ：查询的页码，从1开始
     * @param size ：每页的条数
     * @return
     */
    @GetMapping("tv/findPageable")
    @ResponseBody
    public List<Tv> findPageable(Integer page, Integer size) {
        page = page == null ? 1 : page;
        size = size == null ? 2 : size;
        Page<Tv> tvPage = tvService.findPageable(page, size);

        logger.info("表中总记录数：" + tvPage.getTotalElements());
        //无数据时，返回0
        logger.info("总页数：" + tvPage.getTotalPages());
        //无数据时，返回空列表
        List<Tv> tvList = tvPage.getContent();
        return tvList;
    }

    /**
     * http://localhost:8080/tv/getOneById?id=110
     * 根据主键 ID 查询
     *
     * @param id
     * @return
     */
    @GetMapping("tv/getOneById")
    @ResponseBody
    public Tv getOneById(String id) {
        Integer idInt = id == null ? 0 : Integer.parseInt(id);
        Tv tv = tvService.getOneById(idInt);
        return tv;
    }

    /**
     * http://localhost:8080/tv/findAllExample?name=2dc98f41-8efd-49bf-ae7b-bf9af31d3bf0
     * 条件查询多条数据
     *
     * @param name
     * @return
     */
    @GetMapping("tv/findAllExample")
    @ResponseBody
    public List<Tv> findAllExample(String name) {
        //可以指定实体任意非null字段作为条件进行查询
        Tv tv = new Tv();
        tv.setTvName(name);
        return tvService.findAllExample(tv);
    }

    /**
     * http://localhost:8080/tv/findAllExample2?name=哇哈哈
     * 条件查询，指定排序字段
     *
     * @param name
     * @return
     */
    @GetMapping("tv/findAllExample2")
    @ResponseBody
    public List<Tv> findAllExample2(String name) {
        //可以指定实体任意非null字段作为条件进行查询
        Tv tv = new Tv();
        tv.setTvName(name);

        //以 tvId 倒序查询，当 tvId 相同时，使用 tvPrice 升序
        Sort sort = Sort.by(Sort.Order.desc("tvId"), Sort.Order.asc("tvPrice"));
        return tvService.findAll(tv, sort);
    }

    /**
     * http://localhost:8080/tv/count
     * 查询数据库总记录条数
     *
     * @return
     */
    @GetMapping("tv/count")
    @ResponseBody
    public String count() {
        return tvService.count() + "";
    }

    /**
     * http://localhost:8080/tv/save
     * 添加或者更新单个
     *
     * @return
     */
    @GetMapping("tv/save")
    public String save() {
        Tv tv = new Tv();
        tv.setTvName(UUID.randomUUID().toString());
        tv.setTvPrice(7880.0F + new Random().nextFloat() * 1000);
        tv.setDateOfProduction(new Date());
//        tv.setTvId(1);//主键为null，一直是新增
        tvService.saveOrUpdate(tv);
        return "redirect:/tv/findAll";
    }

    /**
         * http://localhost:8080/tv/saveAll
     * 添加或者更新多个
     *
     * @return
     */
    @GetMapping("tv/saveAll")
    public String saveAll() {
        List<Tv> tvList = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            Tv tv = new Tv();
            tv.setTvName(UUID.randomUUID().toString());
            tv.setTvPrice(6880.0F + new Random().nextFloat() * 1000);
            tv.setDateOfProduction(new Date());
            tvList.add(tv);
//        tv.setTvId(1);//主键为null，一直是新增
        }
        tvService.saveAll(tvList);
        return "redirect:/tv/findAll";
    }

    /**
     * http://localhost:8080/tv/deleteById?id=4
     * 根据主键删除，或者删除所有
     *
     * @param id
     * @return
     */
    @GetMapping("tv/deleteById")
    public String deleteById(Integer id) {
        if (id == null) {
            tvService.deleteAll();
        } else {
            tvService.deleteById(id);
        }
        return "redirect:/tv/findAll";
    }

    /**
     * http://localhost:8080/tv/delete?id=5
     * 根据 id 删除
     *
     * @param id
     * @return
     */
    @GetMapping("tv/delete")
    public String delete(Integer id) {
        //void delete(T entity)：只会根据实体的主键 id 进行删除，所以如果设置主键以外的属性是没用的
        Tv tv = new Tv();
        tv.setTvId(id);
        tvService.delete(tv);
        return "redirect:/tv/findAll";
    }

    /**
     * http://localhost:8080/tv/deleteAllByIds?ids=1,2,4
     * 删除多条
     *
     * @param ids
     * @return
     */
    @GetMapping("tv/deleteAllByIds")
    public String deleteAllByIds(String ids) {
        String[] strings = ids.split(",");
        List<Tv> tvList = new LinkedList<>();
        for (int i = 0; i < strings.length; i++) {
            Tv tv = new Tv();
            tv.setTvId(Integer.parseInt(strings[i]));
            tvList.add(tv);
        }
        tvService.deleteAll(tvList);
        return "redirect:/tv/findAll";
    }

    /**
     * http://localhost:8080/tv/deleteBatch?ids=1,2,4
     * 批量删除多条数据
     *
     * @param ids
     * @return
     */
    @GetMapping("tv/deleteBatch")
    public String deleteBatch(String ids) {
        String[] strings = ids.split(",");
        List<Tv> tvList = new ArrayList<>(strings.length);
        for (String s : strings) {
            Tv tv = new Tv();
            tv.setTvId(Integer.parseInt(s));
            tvList.add(tv);
        }
        tvService.deleteInBatch(tvList);
        return "redirect:/tv/findAll";
    }

    /**
     * http://localhost:8080/tv/deleteAllBatch
     * 批量删除表中所有数据
     *
     * @return
     */
    @GetMapping("tv/deleteAllBatch")
    public String deleteAllBatch() {
        tvService.deleteAllBatch();
        return "redirect:/tv/findAll";
    }

    /**
     * http://localhost:8080/tv/findAll1?name=5b727779-3191-46d3-a44f-cca7491a396c
     * 条件查询时间范围在 [start,end] 之间的数据。如果 tvName 不为空，加上名称条件
     *
     * @param name
     * @return
     * @throws ParseException
     */
    @GetMapping("tv/findAll1")
    @ResponseBody
    public List<Tv> findAll1(String name) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse("2019-04-27 10:00:00");
        Date end = dateFormat.parse("2029-04-27 23:00:00");
        return tvService.findAll(start, end, name);
    }

    /**
     * http://localhost:8080/tv/findAll2
     * 查询生产日期大于等于 start 的数据，且进行分页查询
     *
     * @param page
     * @param size
     * @return
     * @throws ParseException
     */
    @GetMapping("tv/findAll2")
    @ResponseBody
    public List<Tv> findAll2(Integer page, Integer size) throws ParseException {
        page = page == null ? 1 : page;
        size = size == null ? 2 : size;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse("2019-04-28 08:00:00");
        Page<Tv> tvPage = tvService.findAll(start, page, size);

        Logger logger = Logger.getAnonymousLogger();
        logger.info("总记录数：" + tvPage.getTotalElements());
        logger.info("总页数：" + tvPage.getTotalPages());
        List<Tv> tvList = tvPage.getContent();
        return tvList;
    }

    /**
     * http://localhost:8080/tv/findAll3?like=8d157
     * 模糊查询 like
     *
     * @param like
     * @return
     * @throws ParseException
     */
    @GetMapping("tv/findAll3")
    @ResponseBody
    public List<Tv> findAll3(String like) {
        return tvService.findAllLike(like);
    }

    /**
     * http://localhost:8080/tv/findAll4
     * 条件查询，加分页。同时指定以 tvId 倒序排序
     *
     * @param page
     * @param size
     * @param name ：为空时，相当于不加条件
     * @return
     */
    @GetMapping("tv/findAll4")
    @ResponseBody
    public Page<Tv> findAll4(Integer page, Integer size, String name) {
        //根据实体中非null字段进行条件查询。都为null是默认查询所有
        Tv tv = new Tv();
        tv.setTvName(name);
        page = page == null ? 1 : page;
        size = size == null ? 2 : size;
        Page<Tv> tvPage = tvService.findAll(tv, page, size);
        Logger logger = Logger.getAnonymousLogger();
        logger.info("表中总记录数：" + tvPage.getTotalElements());
        //无数据时，返回0
        logger.info("总页数：" + tvPage.getTotalPages());
        //无数据时，返回空列表
        List<Tv> tvList = tvPage.getContent();
        return tvPage;
    }
}
