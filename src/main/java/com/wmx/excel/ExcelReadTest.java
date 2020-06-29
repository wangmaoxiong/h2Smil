package com.wmx.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.converters.DefaultConverterLoader;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.wmx.excel.listener.CommonReadListener;
import com.wmx.excel.listener.MapDataListener;
import com.wmx.excel.listener.SimpleDataListener;
import com.wmx.excel.pojo.ConverterData;
import com.wmx.excel.pojo.IndexOrNameData;
import com.wmx.excel.pojo.SimpleData;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * easyExcel 读取数据
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/6/27 13:30
 */
public class ExcelReadTest {

    /**
     * 最简单/基本的读取 excel 文件内容
     * 1. 创建 excel 对应的实体对象 {@link SimpleData}
     * 2. 默认会逐行读取 excel，需要创建一个读取回调监听器 {@link SimpleDataListener
     * <p>
     * ExcelReaderBuilder read(String pathName, Class head, ReadListener readListener)
     * 1、pathName：excel 文件路径
     * 2、head：读取结果封装的对象类型
     * 3、readListener：读取监听器，能够在读取到每一行数据时，都能进入回调方法，然后进行处理，比如数据清洗、校验、设置读取进度、向数据库存储等。
     * 4、特别注意：readListener 不能交由 spring 容器管理，每次读取都通过 new 对象，如果自定义的 readListener 里面需要用到哪个 bean，则可以通过构造器参数传入。
     * </p>
     */
    @Test
    public void simpleRead() {
        //获取类路径下指定文件的路径
        String fileName = ExcelReadTest.class.getResource("/").getPath() + "excelTemplates" + File.separator + "客户信息模板.xlsx";
        System.out.println("读取 [" + fileName + "]文件...");
        /**
         * ExcelReaderSheetBuilder sheet()：默认读取 excel 文件中的第一页 sheet（版面）
         * ExcelReaderSheetBuilder sheet(Integer sheetNo)：读取指定的第几页 sheet，从0开始
         * ExcelReaderSheetBuilder sheet(String sheetName)：读取指定名称的 sheet
         * <p></p>
         * headRowNumber(Integer headRowNumber)：设置 excel 文件的标题占用几行。
         * 1、如 0：表示没有标题，第一行就是数据；1：表示第一行是标题，第二行开始是数据；表示前两行是标题，第三行开始数据；依此类推。
         * 2、headRowNumber 默认为 1，此时 headRowNumber(Integer headRowNumber) 可以不用指定.
         * <p></p>
         * void doRead() :正式读取文件数据，读取完成内部会调用 finish() 方法，关闭流，释放资源。
         * List<T> doReadSync()：同步读取，然后返回读取结果。对于数据量大时，不建议这么做，因为都会临时放入到缓存中。
         */
        EasyExcel.read(fileName, SimpleData.class, new SimpleDataListener()).sheet(0).headRowNumber(2).doRead();
    }

    /**
     * 上面 {@link ExcelReadTest#simpleRead()}方法中，一行代码就能读取 excel 文件，这里是它的分解步骤写法。
     */
    @Test
    public void simpleRead2() {
        String fileName = ExcelReadTest.class.getResource("/").getPath() + "excelTemplates" + File.separator + "客户信息模板.xlsx";
        System.out.println("读取 [" + fileName + "]文件...");
        ExcelReader excelReader = null;
        try {
            //构建 ExcelReader 对象，指定读取的文件，实体对象，读取监听器
            excelReader = EasyExcel.read(fileName, SimpleData.class, new SimpleDataListener()).build();
            //构建 ReadSheet 对象，读取的第一页 sheet，表头占用了 2 行
            ReadSheet readSheet = EasyExcel.readSheet(0).headRowNumber(2).build();
            //excel 读取器读取 sheet 中的内容
            excelReader.read(readSheet);
        } finally {
            /**结尾必须手动结束，释放资源，读取的时候会创建临时文件，不处理的话，时间一长，到时磁盘会崩的*/
            if (excelReader != null) {
                excelReader.finish();
            }
        }
    }

    /**
     * 实体对象中通过注解为属性指定列的下标或者列名，从而让属性与表中的列一一对应.
     * <p>
     * 1. 创建excel对应的实体对象 {@link IndexOrNameData}，使用 {@link ExcelProperty} 注解指定列的下表或者列名
     * 2. 创建读取回调监听器 {@link CommonReadListener}
     * <p>
     */
    @Test
    public void indexOrNameRead() {
        try {
            //读取类路径下的指定文件
            InputStream inputStream = ExcelReadTest.class.getResource("/excelTemplates/客户信息模板.xlsx").openStream();
            /**
             * ExcelReaderBuilder read(InputStream inputStream, Class head, ReadListener readListener)
             * 1、读取文件内容，与上面基本一致，只是将第一个参数由文件路径改为输入流
             */
            EasyExcel.read(inputStream, IndexOrNameData.class, new CommonReadListener()).sheet(0).headRowNumber(2).doRead();

            /**
             * 1、假如 excel 文件中有多个 sheet，则可以改变 sheet(sheetNo) 的值，多次读取
             * 2、不过注意因为每次 read 完成后，都关闭了流，释放资源，所以后续继续读取 sheet 时，需要重新读取文件
             * 3、虽然这也是一种方法，但因为每次都需要重新读取文件，建议使用下面的 {@link ExcelReadTest#repeatedRead1()}方式
             */
            inputStream = ExcelReadTest.class.getResource("/excelTemplates/客户信息模板.xlsx").openStream();
            EasyExcel.read(inputStream, IndexOrNameData.class, new CommonReadListener()).sheet(1).headRowNumber(2).doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取 excel 的全部 sheet
     * 1、上面的 {@link ExcelReadTest#indexOrNameRead()} 中已经介绍了一种方式读取多个 sheet，这里再介绍一种读取所有 sheet
     * 2、ExcelReaderSheetBuilder.doReadAll() 表示读取全部的 sheet
     */
    @Test
    public void repeatedRead1() throws IOException {
        /**
         * 1、ExcelReaderSheetBuilder.doReadAll() 表示读取全部的 sheet
         * 2、按顺序逐个 sheet 进行读取，每读取完成一页 sheet，则触发一次 ReadListener 接口的 doAfterAllAnalysed 方法
         */
        InputStream inputStream = ExcelReadTest.class.getResource("/excelTemplates/客户信息模板.xlsx").openStream();
        /**
         * read(String pathName, Class head, ReadListener readListener)：读取指定文件，指定读取监听器
         * void doReadAll()：表示读取所以的 sheet
         * List<T> doReadAllSync()：同步读取所有的 sheet，最后将结果返回
         */
        EasyExcel.read(inputStream, SimpleData.class, new SimpleDataListener()).headRowNumber(2).doReadAll();
        /**
         * 注意事项：
         * 通常 excel 中的多个 sheet 数据格式都是不同的，比如第一页可能是采购信息、第二页可能是销售信息、第三页是客户信息等等，
         * 这些不同的 sheet 使用的是不同的格式，比如表头，此时实体类型也应该是不同的实体类型了，无法统一指定了，需要注意。
         */
    }

    /**
     * 读取 excel 的多个 sheet
     * 1、上面的 {@link ExcelReadTest#indexOrNameRead()} 中已经介绍了读取多个 sheet 的方式，这里再介绍一种
     * 2、使用 ExcelReader 逐个读取 sheet
     */
    @Test
    public void repeatedRead2() throws IOException {
        //读取类路径下的文件
        InputStream inputStream = ExcelReadTest.class.getResource("/excelTemplates/客户信息模板.xlsx").openStream();
        ExcelReader excelReader = null;
        try {
            //构建一个 ExcelReader 对象，只指定了读取的文件路径，实体对象以及读取监听器放到 ReadSheet 中去构建
            excelReader = EasyExcel.read(inputStream).build();
            /**
             * 1、为了演示方便，假如两个 sheet 的格式一样，使用相同的实体对象，以及相同的读取监听器，
             * 实际中对于不同的 sheet 通常格式不一样，业务需求也不一样，所以应该是不同的实体以及读取监听器
             * 2、ExcelReaderSheetBuilder readSheet(Integer sheetNo)：创建具体 sheet 的实例，然后指定标题行数以及实体对象和读取监听器
             * 3、这里是操作两个 sheet ，更多时也是同理
             */
            ReadSheet readSheet1 =
                    EasyExcel.readSheet(0).headRowNumber(2).head(SimpleData.class).registerReadListener(new SimpleDataListener()).build();
            ReadSheet readSheet2 =
                    EasyExcel.readSheet(1).headRowNumber(2).head(SimpleData.class).registerReadListener(new SimpleDataListener()).build();
            /**
             * ExcelReader read(ReadSheet... readSheet：读取 sheet 中的内容，将多个 readSheet 同时传入
             * ExcelReader read(List<ReadSheet> readSheetList)
             */
            excelReader.read(readSheet1, readSheet2);
        } finally {
            if (excelReader != null) {
                // 结束时必须结束操作，释放资源，因为读的时候会创建临时文件，日积月累会使磁盘满的
                excelReader.finish();
            }
        }
    }

    /**
     * 日期、数字、自定义格式转换
     * 1、默认读的转换器 {@link DefaultConverterLoader#loadDefaultReadConverter()}
     * 2. 格式转换的规则定义在实体对象中 {@link ConverterData}.使用注解 {@link DateTimeFormat}、{@link NumberFormat}
     */
    @Test
    public void converterRead() {
        String fileName = ExcelReadTest.class.getResource("/").getPath() + "excelTemplates" + File.separator + "客户信息模板.xlsx";
        EasyExcel.read(fileName, ConverterData.class, new CommonReadListener()).sheet(1).headRowNumber(2).doRead();
    }

    /**
     * 读取表头数据
     * 1、读取的时候 headRowNumber(Integer headRowNumber)  设置表头行数，所以会自动将前 headRowNumber 行作为表头
     * 2、读取表头会自动进入 {@link AnalysisEventListener#invokeHeadMap(java.util.Map, com.alibaba.excel.context.AnalysisContext)} 方法
     */
    @Test
    public void headerRead() {
        String fileName = ExcelReadTest.class.getResource("/").getPath() + "excelTemplates" + File.separator + "客户信息模板.xlsx";
        EasyExcel.read(fileName, SimpleData.class, new CommonReadListener()).sheet(1).headRowNumber(2).doRead();
    }

    /**
     * 不定义实体对象读取 excel，直接使用 Map 来接收
     * 如读取某一行的数据为：{0=李清华, 1=26, 2=女, 3=18510010452, 4=1994/5/14, 5=null, 6=0.567}
     */
    @Test
    public void noModelRead() {
        String fileName = ExcelReadTest.class.getResource("/").getPath() + "excelTemplates" + File.separator + "客户信息模板.xlsx";
        EasyExcel.read(fileName, new MapDataListener()).sheet(1).headRowNumber(2).doRead();
    }

    /**
     * List<T> doReadSync()：同步读取，返回读取的最终结果集，如果数据量很大，则不推荐使用，因为这大量的数据都会临时放到内存里面
     * 1、doReadSync 内部会调用 finish 释放资源
     */
    @Test
    public void synchronousRead() {
        String fileName = ExcelReadTest.class.getResource("/").getPath() + "excelTemplates" + File.separator + "客户信息模板.xlsx";
        List<SimpleData> list = EasyExcel.read(fileName).head(SimpleData.class).sheet(0).headRowNumber(2).doReadSync();
        list.stream().forEach(item -> System.out.println("读取到数据：" + item));

        System.out.println("-------------------------------------");

        // 不指定实体对象时，返回一个 List<Map<Integer, String>>,每一个 map 表示 excel 中的一行数据，key 为列的索引（从0开始），value 为值
        //日期也会解析为普通的字符串，如 2000/6/14
        List<Map<Integer, String>> listMap = EasyExcel.read(fileName).sheet(1).headRowNumber(2).doReadSync();
        listMap.stream().forEach(item -> System.out.println("读取数据为：" + item));

        System.out.println("-------------------------------------");
        //同样可以使用读取监听器
        List<IndexOrNameData> objectList = EasyExcel.read(fileName, IndexOrNameData.class, new CommonReadListener()).sheet(0).headRowNumber(2).doReadSync();
        objectList.stream().forEach(item -> System.out.println("doReadSync 读取：" + item));
    }

}
