package com.wmx.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 监听类，有特殊业务需求的都可以在这个类里面自定义实现，比如边读边写库啊，数据过滤和处理等等
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/6/27 12:45
 */
public class ExcelReadListener extends AnalysisEventListener {
    private List<Object> datas = new ArrayList<>();

    /**
     * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
     */
    @Override
    public void invoke(Object data, AnalysisContext context) {
        //数据存储到list，供批量处理，或后续自己业务逻辑处理。
        System.out.println("读取到数据：" + data);
        datas.add(data);
        //根据业务自行处理，可以写入数据库等等
    }

    //所以的数据解析完了调用
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("所有数据解析完成");
    }
}