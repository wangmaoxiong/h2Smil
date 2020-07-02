package com.wmx.excel.web;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * web读写案例
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/7/2 10:23
 */
@Controller
public class ExcelWeb {

    @Autowired
    private ExcelService uploadDAO;

    /**
     * 使用 excel 文件下载数据  http://localhost:8080/excelWeb/download
     * 1、如果失败了，会返回一个有部分数据的 Excel
     * 2. doWrite 的时候，内部会调用 finish 方法，自动关闭 OutputStream,当然外面再关闭流问题不大
     * <p>
     * 实际中应该是用户访问某个数据页面，然后点击"下载"按钮，进入此方法并传入参数，然后根据参数查询数据库，
     * 最后将查询的结果写入到 excel 文件中，输出返回给浏览器自动下载文件。
     * 这里为查询数据库，只做简单的模拟。
     * </p>
     * 3、无论上传还是下载，都可以不用借助已经存在的具体的 excel 文件，都能直接在内存中读/写数据
     */
    @GetMapping("/excelWeb/download")
    public void download(HttpServletResponse response) {
        try {
            /**
             * 1、设置返回的内容类型为 excel 文件类型
             * 2、指定编码为 utf-8
             */
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            /**
             * 1、使用 URLEncoder.encode 对文件名称进行 url 编码，目的防止下载后的文件出现中文乱码
             * 2、设置头信息，设置下载的文件名称等信息
             */
            String fileName = URLEncoder.encode("excel文件下载", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            /**
             * ExcelWriterBuilder write(OutputStream outputStream, Class head)
             * 1、设置将要写入的输出流，以及文件头信息(可以是实体、也可以是普通的 list 指定)
             * 2、sheet(String sheetName)  指定页签的名称
             * 3、doWrite(List data) 写入数据，内部会调用 finish 方法结束操作，释放资源，外部可以不用再处理。
             */
            EasyExcel.write(response.getOutputStream(), ExcelWebData.class)
                    .sheet("黄金客户信息").doWrite(this.dataList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用 excel 文件下载数据     http://localhost:8080/excelWeb/downloadFailedUsingJson
     * 1、如果下载失败，则返回指定的 json 信息，默认失败了会返回一个有部分数据的 Excel
     * <p>
     * 实际中应该是用户访问某个数据页面，然后点击"下载"按钮，进入此方法并传入参数，然后根据参数查询数据库，
     * 最后将查询的结果写入到 excel 文件中，输出返回给浏览器自动下载文件。
     * 这里为查询数据库，只做简单的模拟。
     * </p>
     * 2、无论上传还是下载，都可以不用借助已经存在的具体的 excel 文件，都能直接在内存中读/写数据
     *
     * @since 2.1.1
     */
    @GetMapping("/excelWeb/downloadFailedUsingJson")
    @SuppressWarnings("all")
    public void downloadFailedUsingJson(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

            String fileName = URLEncoder.encode("excel 文件下载2", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            //必须设置不再自动关闭流,因为抛出异常后，还需要此输出流继续输出消息
            EasyExcel.write(response.getOutputStream(), ExcelWebData.class)
                    .autoCloseStream(Boolean.FALSE).sheet("铂金客户信息")
                    .doWrite(this.dataList());
        } catch (Exception e) {
            //必须重置 response，rest 方法会重置上面 setHeader 方法设置的头信息
            //将之前的内容类型修改为普通的 json 或者文本类型，再次设置编码
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
            objectNode.put("status", 500);
            objectNode.put("message", "文件下载失败[" + e.getMessage() + "]");
            try {
                response.getWriter().println(objectNode.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * excel 模板文件下载     http://localhost:8080/excelWeb/templateDownload
     * 1、用户使用 excel 文件上传数据之前，通常需要一个模板文件，用户必须按着要求填写数据，这样上传只会才能按着既定规则解析
     * 2、对于模板文件下载，可以使用 servlet 的方式、也可以使用其它任意方式。这里直接使用 easyExcel 进行下载.
     * 3、流程：获取下载文件路径 -> 设置响应头信息 -> 使用输出流输出文件内容 -> 浏览器自动下载文件
     *
     * @param response
     */
    @GetMapping("/excelWeb/templateDownload")
    @SuppressWarnings("all")
    public void templateDownload(HttpServletResponse response) {
        try {
            /**
             * 1、确定模板文件的路径，可以是类路径下的资源文件，服务器本地文件，也可以是网络文件
             * 2、模板文件的标识，比如文件 id、文件名称，实际中通过参数传入，这里为了简单，直接写死为类路径下的指定文件
             * 3、对模板文件的名称必须进行 url 编码，否则用户下载的文件，中文会出现乱码.
             */
            String templateFileName = ExcelWeb.class.getResource("/").getPath() + "excelTemplates" + File.separator + "订单模板.xlsx";
            String fileName = new File(templateFileName).getName();
            fileName = URLEncoder.encode(fileName, "UTF-8");

            //4、设置响应的内容类型与头信息，头信息中的 filename 属性值就是用户下载后的文件名称
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);

            /**
             * 5、ExcelWriterBuilder write(OutputStream outputStream) ：数据将要写入的目的地，可以是 File、String、输出流等
             * 6、ExcelWriterBuilder withTemplate(String pathName)：根据模板写入
             * 7、doWrite(List data)：将数据写入到模板中，这里让数据为空，于是就达到了只下载模板的目的.
             */
            EasyExcel.write(response.getOutputStream()).withTemplate(templateFileName).sheet(0).doWrite(Arrays.asList());
        } catch (Exception e) {
            //必须重置 response，rest 方法会重置上面 setHeader 方法设置的头信息
            //将之前的内容类型修改为普通的 json 或者文本类型，再次设置编码
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
            objectNode.put("status", 500);
            objectNode.put("message", "模板文件下载失败[" + e.getMessage() + "]");
            try {
                response.getWriter().write(objectNode.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 使用 excel 文件上传数据     http://localhost:8080/excelWeb/upload
     * 1. 创建 excel 对应的实体对象 {@link ExcelWebData}
     * 2. 默认一行行的读取 excel，所以指定读取监听器，用于读到数据进行回调，然后处理读取到的数据
     * 3. void doRead() 读取数据，无返回值，所以必须指定读取监听器
     * 4、List<T> doReadSync()：读取数据，会将最终的读取结果数据进行返回，此种方式可以指定读取监听器，也可以指定
     * 5、官方推荐 doRead，因为 doReadSync 需要返回最终数据，所以会将读取的数据临时缓存在内存中，当量比较大时，性能会降低
     * 6、无论上传还是下载，都可以不用借助已经存在的具体的 excel 文件，都能直接在内存中读/写数据
     */
    @PostMapping("/excelWeb/upload")
    @ResponseBody
    public Map<String, Object> upload(MultipartFile file) {
        Map<String, Object> dataMap = new HashMap<>(2);
        try {
            EasyExcel.read(file.getInputStream(), ExcelWebData.class, new UploadDataListener(uploadDAO)).sheet().doRead();
            dataMap.put("code", 200);
            dataMap.put("msg", "success");
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("code", 500);
            dataMap.put("msg", "fail");
        }
        return dataMap;
    }

    /**
     * 模拟数据
     *
     * @return
     */
    private List<ExcelWebData> dataList() {
        List<ExcelWebData> list = new ArrayList<ExcelWebData>();
        int count = 20;
        for (int i = 10; i < count; i++) {
            ExcelWebData data = new ExcelWebData();
            data.setId(i);
            data.setAge(5 + i);
            data.setGender("男");
            data.setName("华为P1" + i);
            data.setPhoneNumber("181245645" + i);
            data.setBirthday(new Date());
            list.add(data);
        }
        return list;
    }
}