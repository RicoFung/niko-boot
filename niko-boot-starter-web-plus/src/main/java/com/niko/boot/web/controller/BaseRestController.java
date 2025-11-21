package com.niko.boot.web.controller;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.niko.boot.web.util.TimeUtil;
import com.niko.boot.web.util.JasperUtil;
import com.niko.boot.web.util.POIUtil;

/**
 * 增强版Controller类
 * 封装chok2-devwork-controller-heavy的BaseRestController
 * 提供HttpServletRequest和HttpServletResponse的注入
 * 支持Excel导出和报表导出功能
 */
public class BaseRestController {
    
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    
    @ModelAttribute
    public void baseInitialization(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    /**
     * 导出Excel
     * @param list 数据列表
     * @param fileName 文件名
     * @param title 标题
     * @param headerNames 表头名称
     * @param dataColumns 数据列
     * @param exportType 导出类型（xlsx/xls）
     */
    public void export(List<?> list, String fileName, String title, String headerNames, String dataColumns, String exportType) {
        ByteArrayOutputStream ba = null;
        ServletOutputStream out = null;
        try {
            try {
                ba = new ByteArrayOutputStream();
                ba = (ByteArrayOutputStream) POIUtil.writeExcel(ba, 
                        fileName, 
                        title, 
                        headerNames, 
                        dataColumns, 
                        list);
                
                response.reset(); // 清空输出流
                response.setHeader("Content-Disposition", "attachment; filename="
                        + java.net.URLEncoder.encode(fileName, "UTF-8")
                        + "_"
                        + TimeUtil.formatDate(new Date(), "yyyyMMdd_HHmmss") + "." + "xlsx");
                if (exportType.equals("xlsx")) {
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8"); // 定义输出类型:xlsx
                } else {
                    response.setContentType("application/msexcel;charset=UTF-8"); // 定义输出类型:xls
                }
                response.setContentLength(ba.size());
                out = response.getOutputStream();
                ba.writeTo(out);
                out.flush();
            } finally {
                if (out != null) out.close();
                if (ba != null) ba.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 导出_包含单个Table（不含基础控件）
     * @param jasperFileName 模板文件名
     * @param rptFileName 生成文件名
     * @param rptFileFormat 生成文件格式(包括："pdf"/"xlsx"/"html")
     * @param rptBizDatasetTableK Table控件业务数据集KEY
     * @param rptBizDatasetTableV Table控件业务数据集VAL
     * @param rptBizDatasetTableClazz Table控件数据类型 (三种可选：Object.class/Map.class/HashMap.class)
     * @throws Exception
     */
    public void exportRptOneTable(String jasperFileName, String rptFileName, String rptFileFormat, String rptBizDatasetTableK, List<?> rptBizDatasetTableV, Class<?> rptBizDatasetTableClazz) throws Exception {
        LinkedHashMap<String, List<?>> rptBizDatasetTableKV = new LinkedHashMap<String, List<?>>() {
            private static final long serialVersionUID = 1L;
            {
                put(rptBizDatasetTableK, rptBizDatasetTableV);
            }
        };
        exportRptMultiTable(jasperFileName, rptFileName, rptFileFormat, null, rptBizDatasetTableKV, rptBizDatasetTableClazz);
    }
    
    /**
     * 导出_包含多个Table控件（含基础控件）
     * @param rptTemplateName 模板文件名
     * @param rptFileName 生成文件名
     * @param rptFileFormat 生成文件格式(包括："pdf"/"xlsx"/"html")
     * @param rptBizDatasetKV 基础控件业务数据集（KEY-VALUE）
     * @param rptBizDatasetTableKV Table控件业务数据集（KEY-VALUE）
     * @param rptBizDatasetTableClazzes Table控件数据类型 (三种可选：Object.class/Map.class/HashMap.class)
     * @throws Exception
     */
    public void exportRptMultiTable(String rptTemplateName, String rptFileName, String rptFileFormat, Map<String, ?> rptBizDatasetKV, LinkedHashMap<String, List<?>> rptBizDatasetTableKV, Class<?>... rptBizDatasetTableClazzes) throws Exception {
        JasperUtil.export(response, rptTemplateName, rptFileName, rptFileFormat, rptBizDatasetKV, rptBizDatasetTableKV, rptBizDatasetTableClazzes);
    }
}

