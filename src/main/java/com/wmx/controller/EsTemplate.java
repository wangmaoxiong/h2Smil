package com.wmx.controller;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wangmaoxiong
 * @version 1.0
 * @date 2020/6/4 16:58
 */
@RestController
public class EsTemplate {
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    public void t(){
//        elasticsearchTemplate.f
    }
}
