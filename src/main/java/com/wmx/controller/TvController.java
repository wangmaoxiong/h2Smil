package com.wmx.controller;

import com.wmx.entity.Tv;
import com.wmx.service.TvService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author wangmaoxiong
 * Created by Administrator on 2019/4/27.
 */
@Controller
public class TvController {
    @Resource
    private TvService tvService;

    /**
     * http://localhost:8080/tv/findAll1
     *
     * @param name
     * @return
     * @throws ParseException
     */
    @GetMapping("tv/findAll1")
    @ResponseBody
    public String findAll1(String name) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse("2019-04-27 10:00:00");
        Date end = dateFormat.parse("2019-04-27 23:00:00");
        return tvService.findAll(start, end, name).toString();
    }

    /**
     * http://localhost:8080/tv/findAll2
     *
     * @param page
     * @param size
     * @return
     * @throws ParseException
     */
    @GetMapping("tv/findAll2")
    @ResponseBody
    public String findAll2(Integer page, Integer size) throws ParseException {
        page = page == null ? 1 : page;
        size = size == null ? 2 : size;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse("2019-04-28 08:00:00");
        Page<Tv> tvPage = tvService.findAll(start, page, size);

        Logger logger = Logger.getAnonymousLogger();
        logger.info("总记录数：" + tvPage.getTotalElements());
        logger.info("总页数：" + tvPage.getTotalPages());
        List<Tv> tvList = tvPage.getContent();
        return tvList.toString();
    }

    /**
     * http://localhost:8080/tv/findAll3
     *
     * @param like
     * @return
     * @throws ParseException
     */
    @GetMapping("tv/findAll3")
    @ResponseBody
    public String findAll3(String like) {
        return tvService.findAllLike(like).toString();
    }
}
