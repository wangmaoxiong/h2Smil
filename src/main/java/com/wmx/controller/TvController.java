package com.wmx.controller;

import com.wmx.entity.TV;
import com.wmx.service.TVService;
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
    private TVService tvService;

    @GetMapping("findAll1")
    @ResponseBody
    public String findAll1(String name) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse("2019-04-27 10:00:00");
        Date end = dateFormat.parse("2019-04-27 23:00:00");
        return tvService.findAll(start, end, name).toString();
    }

    @GetMapping("findAll2")
    @ResponseBody
    public String findAll2(Integer page, Integer size) throws ParseException {
        page = page == null ? 1 : page;
        size = size == null ? 2 : size;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse("2019-04-28 08:00:00");
        Page<TV> tvPage = tvService.findAll(start, page, size);

        Logger logger = Logger.getAnonymousLogger();
        logger.info("总记录数：" + tvPage.getTotalElements());
        logger.info("总页数：" + tvPage.getTotalPages());
        List<TV> tvList = tvPage.getContent();
        return tvList.toString();
    }

    @GetMapping("findAll3")
    @ResponseBody
    public String findAll3(String like) throws ParseException {
        return tvService.findAllLike(like).toString();
    }
}
