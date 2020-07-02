package com.wmx.excel.test.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.converters.string.StringImageConverter;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

/**
 * 图片导出类
 * 1、如果导出的 excel 文件需要插入图片，则可以参考本实体类
 * 2、这里展示了 5 张不同的方式插入图片，实质就是属性的类型的区别，easyExcel 会自动将这些属性类型判断为文件，然后进行插入
 * 3、实际中一个实体并不是有这么多的文件类型，仅仅只是演示，实际开发时，任选一种即可。
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/6/29 20:32
 */
@ContentRowHeight(100)
@ColumnWidth(100 / 8)
public class WriterImageData {

    @ExcelProperty(value = {"图片导出", "编号"})
    private Integer id;

    @ExcelProperty(value = {"图片导出", "File 类型图片"})
    private File file;

    @ExcelProperty(value = {"图片导出", "InputStream 类型图片"})
    private InputStream inputStream;
    /**
     * 如果 string 类型 必须指定转换器，因为 String 类型默认会作为普通字符串类型处理.
     */
    @ExcelProperty(converter = StringImageConverter.class, value = {"图片导出", "String 类型图片地址"})
    private String string;

    @ExcelProperty(value = {"图片导出", "字节数组图片类型"})
    private byte[] byteArray;
    /**
     * 根据url导出
     *
     * @since 2.1.1
     */
    @ExcelProperty(value = {"图片导出", "url 类型图片"})
    private URL url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "WriterImageData{" +
                "file=" + file +
                ", inputStream=" + inputStream +
                ", string='" + string + '\'' +
                ", byteArray=" + Arrays.toString(byteArray) +
                ", url=" + url +
                '}';
    }
}