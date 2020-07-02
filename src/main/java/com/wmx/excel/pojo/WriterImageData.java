package com.wmx.excel.pojo;

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
     * 如果string类型 必须指定转换器，string默认转换成string
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