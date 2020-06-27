package com.wmx.excel;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wmx.entity.Customer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * excel 表格处理控制层
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/6/27 10:09
 */
@RestController
public class ExcelController {

    /**
     * excel 模板文件下载
     * 1、http://localhost:8080/excel/downloadTemplate?templateName=excelTemplates/客户信息模板.xlsx
     * 2、支持 .xls 与 .xlsx 格式
     * 3、操作流程为：加载资源->读取资源->写入响应流->异常处理与关闭资源
     *
     * @param response
     * @param templateName ：模板文件名称，路径必须使用左斜杠
     * @throws Exception
     */
    @GetMapping("/excel/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response, @RequestParam String templateName) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        PrintWriter printWriter = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(templateName);
            inputStream = classPathResource.getInputStream();
            String filenameExtension = StringUtils.getFilenameExtension(templateName);
            String filename = StringUtils.getFilename(templateName);
            Workbook workbook = "xls".equalsIgnoreCase(filenameExtension) ? new HSSFWorkbook(inputStream) : new XSSFWorkbook(inputStream);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
            response.setHeader("Access-Control-Expose-Headers", "content-Disposition");
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        } catch (IOException e) {
            response.reset();
            response.setContentType("text/html;charset=UTF-8");
            JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
            ObjectNode objectNode = nodeFactory.objectNode();
            objectNode.put("code", 500);
            if (e instanceof FileNotFoundException) {
                objectNode.put("message", "[" + templateName + "] 模板文件不存在");
            } else {
                objectNode.put("message", "excel 模板下载失败");
            }
            objectNode.put("data", "");
            printWriter = response.getWriter();
            printWriter.write(objectNode.toString());
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }
    }

    /**
     * 导出数据
     * 1、操作流程：定义列标题->创建sheet->自定义字体和风格->构造数据->写入数据->写入到浏览器响应流
     * http://localhost:8080/excel/exportData
     */
    @GetMapping("/excel/exportData")
    public void exportData(HttpServletResponse response) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();

        String[] columnNames = {"用户名", "年龄", "性别", "手机号"};

        Sheet sheet = workbook.createSheet();
        Font titleFont = workbook.createFont();
        titleFont.setFontName("simsun");
        titleFont.setBold(true);
        titleFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        titleStyle.setFont(titleFont);

        Row titleRow = sheet.createRow(0);

        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(titleStyle);
        }
        //模拟构造数据
        List<Customer> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add(new Customer(1 + i, "千叶真" + i, "男", 22 + i, "1386709876" + i));
        }

        //创建数据行并写入值
        for (int j = 0; j < dataList.size(); j++) {
            Customer userExcelModel = dataList.get(j);
            int lastRowNum = sheet.getLastRowNum();
            Row dataRow = sheet.createRow(lastRowNum + 1);
            dataRow.createCell(0).setCellValue(userExcelModel.getName());
            dataRow.createCell(1).setCellValue(userExcelModel.getAge());
            dataRow.createCell(2).setCellValue(userExcelModel.getGender());
            dataRow.createCell(3).setCellValue(userExcelModel.getPhoneNumber());
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("content-Disposition", "attachment;filename=" + URLEncoder.encode("easyexcel.xlsx", "utf-8"));
        response.setHeader("Access-Control-Expose-Headers", "content-Disposition");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * excel 文件读取
     * http://localhost:8080/excel/read
     *
     * @param file
     * @return
     */
    @PostMapping("/excel/read")
    public List<Customer> readExcel(@RequestParam("file") MultipartFile file) {
        List<Customer> list = new ArrayList<>();
        try {
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            list = EasyExcel.read(file.getInputStream(), Customer.class, new ExcelReadListener()).sheet().doReadSync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
