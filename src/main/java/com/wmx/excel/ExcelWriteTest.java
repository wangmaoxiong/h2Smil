package com.wmx.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.wmx.excel.pojo.WriterDemoData;
import com.wmx.excel.pojo.WriterImageData;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Excel 写操作 API 测试
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/6/28 8:47
 */
public class ExcelWriteTest {

    /**
     * 输出 excel 文件路径
     */
    private String outputFilePath;

    /**
     * 测试方法之前，设置文件输出路径
     */
    @Before
    public void setOutputFilePath() {
        File homeDirectory = FileSystemView.getFileSystemView().getHomeDirectory();
        outputFilePath = homeDirectory.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".xlsx";
    }

    /**
     * 测试方法之后，显示文件输出路径
     */
    @After
    public void showOutFilePath() {
        System.out.printf("%s%n", outputFilePath);
    }

    /**
     * 最简单的写入 excel 文件
     * 1、创建对应 excel 的实体对象 {@link WriterDemoData}，用 @ExcelProperty 注解标识要显示在 excel 文件中的标题
     * 2. EasyExcel.write 实行写入
     */
    @Test
    public void simpleWrite() {
        /**
         * ExcelWriterBuilder write(String pathName, Class head)：excel 写入构建器
         * 1、pathName 将要写入的 excel 文件路径，不存在时会自动新建文件
         * 2、head：对应的数据实体
         * ExcelWriterSheetBuilder sheet(String sheetName)：excel 写入 sheet 构建器，指定 sheet 名称
         * doWrite(List data)：将 data 数据写入到 excel 中
         * 1、doWrite 内部会调用 finish 方法结束操作，释放资源
         * 2、data 参数没有指定具体元素类型，所以可以是 List<POJO> 或者 List<List<Object>>
         * 3、POJO 对象中的属性，以及 List<Object> 中的元素会按顺序写入到 excel 中，成为一行完整的数据
         */
        EasyExcel.write(outputFilePath, WriterDemoData.class).sheet("模板1").doWrite(data());
    }

    /**
     * 上面 {@link ExcelWriteTest#simpleWrite()} 方法的拆开写法
     */
    @Test
    public void simpleWrite2() {
        ExcelWriter excelWriter = null;
        try {
            /**
             * 先构建 ExcelWriter 对象，然后构建 WriteSheet，最后写入
             * 结尾必须手动 finish，关流，释放资源
             */
            excelWriter = EasyExcel.write(outputFilePath, WriterDemoData.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("模板2").build();
            excelWriter.write(this.data(), writeSheet);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 根据参数只导出指定列，比如 POJO 中的某些属性不让它导出，只让部分属性导出
     * 1、可以通过编码指定需要排除的列，也可以在实体中通过注解指定
     */
    @Test
    public void excludeOrIncludeWrite() {
        /**
         * excludeColumnFiledNames(Collection<String> excludeColumnFiledNames)：自定义想要忽略的列
         * excludeColumnFiledNames(Collection<String> excludeColumnFiledNames)：只输出自定义的列
         */
        Set<String> excludeColumnFiledNames = new HashSet<String>();
        //假设想要忽略 WriterDemoData 实体中的 id 属性不写入到 excel 文件中去.
        excludeColumnFiledNames.add("id");

        EasyExcel.write(outputFilePath, WriterDemoData.class)
                .excludeColumnFiledNames(excludeColumnFiledNames)
                .sheet("模板3")
                .doWrite(data());
    }

    /**
     * 图片导出
     * 1. 创建 excel 对应的实体对象 {@link WriterImageData}，与非图片导出的实体对象基本相同
     * 2、EasyExcel.write 与非图片导出时相同，它会自动根据实体对象的属性类型进行区分，比如 File、URL 类型，会自动作为文件进行处理.
     */
    @Test
    public void imageWrite() throws Exception {
        InputStream inputStream = null;
        try {
            //被写入的图片支持本地文件和网络文件
            String imagePath = "C:\\wmx\\desktop\\csdn.png";
            URL url = new URL("https://avatar.csdnimg.cn/6/D/4/1_wangmx1993328.jpg");

            //创建需要写入的实体对象，为属性赋值，下面展示五种不同的属性类型来写入图片（实际使用只要选一种即可）
            WriterImageData imageData = new WriterImageData();
            imageData.setId(9527);
            imageData.setByteArray(FileUtils.readFileToByteArray(new File(imagePath)));
            imageData.setFile(new File(imagePath));
            imageData.setString(imagePath);
            inputStream = FileUtils.openInputStream(new File(imagePath));
            imageData.setInputStream(inputStream);
            imageData.setUrl(url);

            //将实体对象加入到 列表中，列表中的每个元素对应 excel 文件中的一行
            List<WriterImageData> list = new ArrayList<>();
            list.add(imageData);

            EasyExcel.write(outputFilePath, WriterImageData.class).sheet("图片写入模板").doWrite(list);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 根据模板写入
     * 1、实测的效果是，数据是写入到了模板中，但是模板里面原有的内容会存在，而且新导入的内容与模板的旧内容会隔开，导出的内容会有自己的标题
     * 实际就是两个内容在一个文件中了
     * 2、指定导出模板的重载方法如下：
     * ExcelWriterBuilder withTemplate(File templateFile)
     * ExcelWriterBuilder withTemplate(InputStream templateInputStream)
     * ExcelWriterBuilder withTemplate(String pathName)
     */
    @Test
    public void templateWrite() {
        String templateFileName = ExcelWriteTest.class.getResource("/").getPath() + "excelTemplates" + File.separator + "订单模板.xlsx";
        EasyExcel.write(outputFilePath, WriterDemoData.class)
                .withTemplate(templateFileName)
                .sheet("根据模板写入").doWrite(data());
    }

    /**
     * 可变标题处理
     * 1、excel 写入时默认使用实体对象属性名称或者属性上面 @ExcelProperty 注解定义的列名称
     * 2、如果想动态改变标题的名称，则可以 head(List<List<String>> head) 方法自定义标题名称，只是改变名称，样式仍然采用原实体对象的
     */
    @Test
    public void variableTitleWrite() {
        EasyExcel.write(outputFilePath, WriterDemoData.class).head(this.variableTitleHead()).sheet("模板7").doWrite(data());

    }

    /**
     * 动态头，实时生成头写入，不使用实体对象
     * 1、思路是先创建 List<String> 头格式的 sheet，仅仅写入头，然后通过 table 以不写入头的方式 去写入数据
     */
    @Test
    public void dynamicHeadWrite() {
        EasyExcel.write(outputFilePath).head(variableTitleHead()).sheet("模板6").doWrite(data());
    }

    /**
     * 不创建对象的写入 excel
     * 1、registerWriteHandler(WriteHandler writeHandler)：自定义写处理器，如自定义样式
     * 2、head(List<List<String>> head)：指定标题信息
     * 3、doWrite(List data)：指定写入的正文数据
     */
    @Test
    public void noModelWrite() {
        EasyExcel.write(outputFilePath).registerWriteHandler(handlerStyleWrite()).head(variableTitleHead()).sheet("模板8").doWrite(dataList());
    }

    /**
     * 只写入正文，不写入表头标题
     */
    @Test
    public void noModelWrite2() {
        EasyExcel.write(outputFilePath).sheet("模板8").doWrite(dataList());
    }

    /**
     * 使用 table 写入数据
     */
    @Test
    public void tableWrite() {
        ExcelWriter excelWriter = null;
        try {
            //1、先创建 ExcelWriter 写对象。这里使用 WriteTable 多次写入数据。
            excelWriter = EasyExcel.write(outputFilePath, WriterDemoData.class).build();
            /**
             * 1、创建 WriteSheet 页对象 与 WriteTable 表格对象。
             * 2、一个 WriteSheet 相当于 excel 文件中的一页，WriteTable 是其中的一个表格，前者包含后者
             * 3、WriteSheet与 WriteTable 都可以设置标题，所以如果 WriteSheet 设置了标题，则第一个 WriteTable 就不需要再设置，否则头部会有两个标题
             * 4、WriteTable 会继承 WriteSheet与 的配置.
             */
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").needHead(Boolean.FALSE).build();
            //上面 WriteSheet 没有设置标题，这里 WriteTable 设置标题，效果就是一页中有两个表格，两个都有标题
            WriteTable writeTable0 = EasyExcel.writerTable(0).needHead(Boolean.TRUE).build();
            WriteTable writeTable1 = EasyExcel.writerTable(1).needHead(Boolean.TRUE).build();
            /**
             * write(List data, WriteSheet writeSheet, WriteTable writeTable)
             * 1、将数据 data 按表格（writeTable0 ）形式写入到 writeSheet 中
             * 2、第二次的数据会写在第一次的后面（紧挨着）
             */
            excelWriter.write(this.data(), writeSheet, writeTable0);
            excelWriter.write(this.data(), writeSheet, writeTable1);
        } finally {
            //手动结束、释放资源
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 重复多次写入到同一页
     * 1、同样是上面 {@link ExcelWriteTest#tableWrite()} 的写法，只是去掉了 WriteTable
     */
    @Test
    public void repeatedWrite() {
        ExcelWriter excelWriter = null;
        try {
            //1）先创建 ExcelWriter 写对象
            excelWriter = EasyExcel.write(outputFilePath, WriterDemoData.class).build();
            //2）再创建 WriteSheet 页对象
            WriteSheet writeSheet = EasyExcel.writerSheet("模板10").build();
            //3）分批次写入数据，模拟对分页查询数据库的结果进行写入
            int count = 5;
            for (int i = 0; i < count; i++) {
                //写入数据
                List<WriterDemoData> data = this.data();
                excelWriter.write(data, writeSheet);
            }
        } finally {
            //手动结束、释放资源
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 重复多次写入到不同页
     */
    @Test
    public void repeatedWrite2() {
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputFilePath, WriterDemoData.class).build();
            //1、假设每次数据量比较大，需要放在不同的页
            int count = 3;
            for (int i = 0; i < count; i++) {
                /**
                 * writerSheet(Integer sheetNo, String sheetName)
                 * 1、每次创建 writeSheet 时必须指定 sheetNo，而且 sheetName 必须不一样
                 * 2、sheetNo 表示页码，sheetName 表示页签的名称
                 */
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板2" + i).build();
                List<WriterDemoData> data = this.data();
                excelWriter.write(data, writeSheet);
            }
        } finally {
            //手动结束、释放资源
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 重复多次写入到不同页
     * 1、类似上面的 {@link ExcelWriteTest#repeatedWrite2()}，只是将标题（head）从 ExcelWriter 移动到了  WriteSheet 上去设置。
     * 2、ExcelWriter 设置的 head 对所有 WriteSheet 有效，反之 WriteSheet 可以设置自己每一页的标题
     */
    @Test
    public void repeatedWrite3() {
        ExcelWriter excelWriter = null;
        try {
            //1、只指定了文件，未指定 head(表头)
            excelWriter = EasyExcel.write(outputFilePath).build();
            int count = 5;
            for (int i = 0; i < count; i++) {
                //2、创建多页时，必须指定不同的 页码和页名称。head(Class clazz) 为每一页设置标题，这里为了简单才设置成了同一个
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板3" + i)
                        .head(WriterDemoData.class).build();
                List<WriterDemoData> data = this.data();
                excelWriter.write(data, writeSheet);
            }
        } finally {
            //手动结束、释放资源
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }


    /**
     * 拦截器形式自定义样式（推荐使用注解 参照{@link WriterDemoData}）
     */
    public HorizontalCellStyleStrategy handlerStyleWrite() {
        /**
         * 为标题/表头 设置样式
         * 1、WriteCellStyle：表示单元格格样式
         * 2、WriteFont：表示字体样式
         * 3、setFillForegroundColor(Short fillForegroundColor) ：设置前景色
         * 4、setFontHeightInPoints(Short fontHeightInPoints)：设置字体大小
         */
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 16);

        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headWriteCellStyle.setWriteFont(headWriteFont);
        /**
         * 为正文内容设置样式，与设置表头基本一致
         * 1、setFillPatternType(FillPatternType fillPatternType)：设置颜色填充类型
         */
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 14);

        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        contentWriteCellStyle.setWriteFont(contentWriteFont);

        //水平单元样式策略，设置表头和正文内容样式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        return horizontalCellStyleStrategy;
    }

    /**
     * 自定义 excel 的标题，和实体对象中 @ExcelProperty(value = {"订单信息汇总表", "编号"}) 的类型，按顺序为一级、二级标题
     *
     * @return
     */
    private List<List<String>> variableTitleHead() {
        List<List<String>> list = new ArrayList<>();
        list.add(Arrays.asList("VIP客户信息汇总表", "序号"));
        list.add(Arrays.asList("VIP客户信息汇总表", "名称"));
        list.add(Arrays.asList("VIP客户信息汇总表", "数量"));
        list.add(Arrays.asList("VIP客户信息汇总表", "单价"));
        list.add(Arrays.asList("VIP客户信息汇总表", "出货日期"));
        return list;
    }

    /**
     * 普通 List<Object> 类型的 list，每一个 List<Object>  都是 excel 中的一行数据，所以适合采用 List 类型，而不是 map
     *
     * @return
     */
    private List<List<Object>> dataList() {
        List<List<Object>> list = new ArrayList<>();
        int count = 30;
        for (int i = 0; i < count; i++) {
            list.add(Arrays.asList(i + 1, "苹果S1" + i, new Random().nextInt(100), new Random().nextInt(99999), new Date()));
        }
        return list;
    }

    /**
     * POJO 实体 List
     *
     * @return
     */
    private List<WriterDemoData> data() {
        List<WriterDemoData> list = new ArrayList<WriterDemoData>();
        for (int i = 0; i < 10; i++) {
            WriterDemoData data = new WriterDemoData();
            data.setId(i + 1);
            data.setNumber(new Random().nextInt(500));
            data.setName("华为 P" + i);
            data.setShipDate(new Date());
            data.setUnivalent(new Random().nextFloat() * 10000);
            list.add(data);
        }
        return list;
    }

}
