package com.wmx.excel.test.listener;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AnalysisEventListener：分析事件监听器
 * 1、它是一个抽象类，继承之后必须实现 invoke 方法，AnalysisEventListener 实现了 ReadListener 接口，
 * 2、EasyExcel.read 读取 excel 文件的时候，需要传入 ReadListener 监听器
 * 3、读取监听器的目的在于能捕获读取到的每一条数据，这样可以轻松的对每一条数据进行处理，比如存库、清洗、验证、记录日志等
 * 4、特别注意：读取监听器不能交由 spring 容器管理，EasyExcel.read 时通过 new 对象，如果本类中需要用到哪个 bean，则可以通过构造器参数传入。
 * 比如需要将读取的数据存储到数据库，显然需要 XxxServer 或者 XxxDao，这些 bean 是交由 Spring 容器管理的，所以可以通过本来的构造器参数传入。
 * 5、这里使用 Map<Integer, String> 类型,每一个 map 表示 excel 中的一行数据，key 为列的索引（从0开始），value 为值
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/6/27 14:39
 */
public class MapDataListener extends AnalysisEventListener<Map<Integer, String>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapDataListener.class);
    /**
     * BATCH_COUNT：批处理的条数，比如每 50 条数据存入一次数据库
     * dataList：用于临时存放读取到的数据
     */
    private static final int BATCH_COUNT = 50;
    List<Map<Integer, String>> dataList = new ArrayList<Map<Integer, String>>();

    /**
     * 假如本来需要 XxxServer 或者 XxxDao 则可以通过此构造器参数传入
     */
    public MapDataListener() {
    }

    /**
     * 每成功解析一条数据，都进入此方法
     *
     * @param data    :读取到的数据
     * @param context ：excel 读取器的上下文对象
     */
    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        LOGGER.info("读取数据为：{}", data);
        dataList.add(data);
        // 达到批处理次数时，就存储一次数据库，防止成千上万条数据占用内存，出现 OOM
        if (dataList.size() >= BATCH_COUNT) {
            this.saveData();
            // 存储完成后清空列表
            dataList.clear();
        }
    }

    /**
     * 所有数据解析完成后进入此方法
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        LOGGER.info("所有数据解析完成！");
        // 对最后遗留的数据存储到数据库
        this.saveData();
    }


    /**
     * 解析异常时进入本方法，如果抛出异常，则停止读取，否则继续读取下一行
     * 如果是某一个单元格的转换异常（ExcelDataConvertException），则可以获取到具体行号
     * 如果要获取表头信息，则需要配合 invokeHeadMap 使用
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        LOGGER.error("解析失败，继续下一行:{}", exception.getMessage());
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            LOGGER.error("第 {} 行 {} 列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
        }
    }

    /**
     * 模拟数据库操作
     */
    private void saveData() {
        try {
            LOGGER.info("{}条数据，开始存储数据库！", dataList.size());
            //模拟操作数据库耗时
            Thread.sleep(300);
            LOGGER.info("存储数据库成功！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}