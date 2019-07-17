package com.hebaibai.admin.plumber.controller;

import com.hebaibai.admin.common.annotation.Log;
import com.hebaibai.admin.common.controller.BaseController;
import com.hebaibai.admin.common.entity.FebsResponse;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.common.exception.FebsException;
import com.hebaibai.admin.plumber.entity.ColumnMapping;
import com.hebaibai.admin.plumber.service.IColumnMappingService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 字段映射 Controller
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:34
 */
@Slf4j
@Validated
@Controller
public class ColumnMappingController extends BaseController {

    @Autowired
    private IColumnMappingService columnMappingService;


    @GetMapping("columnMapping")
    @ResponseBody
    @RequiresPermissions("columnMapping:list")
    public FebsResponse getAllColumnMappings(ColumnMapping columnMapping) {
        return new FebsResponse().success().data(columnMappingService.findColumnMappings(columnMapping));
    }

    @GetMapping("columnMapping/list")
    @ResponseBody
    @RequiresPermissions("columnMapping:list")
    public FebsResponse columnMappingList(QueryRequest request, ColumnMapping columnMapping) {
        Map<String, Object> dataTable = getDataTable(this.columnMappingService.findColumnMappings(request, columnMapping));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增ColumnMapping")
    @PostMapping("columnMapping")
    @ResponseBody
    @RequiresPermissions("columnMapping:add")
    public FebsResponse addColumnMapping(@Valid ColumnMapping columnMapping) throws FebsException {
        try {
            this.columnMappingService.createColumnMapping(columnMapping);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增ColumnMapping失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除ColumnMapping")
    @GetMapping("columnMapping/delete")
    @ResponseBody
    @RequiresPermissions("columnMapping:delete")
    public FebsResponse deleteColumnMapping(ColumnMapping columnMapping) throws FebsException {
        try {
            this.columnMappingService.deleteColumnMapping(columnMapping);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除ColumnMapping失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改ColumnMapping")
    @PostMapping("columnMapping/update")
    @ResponseBody
    @RequiresPermissions("columnMapping:update")
    public FebsResponse updateColumnMapping(ColumnMapping columnMapping) throws FebsException {
        try {
            this.columnMappingService.updateColumnMapping(columnMapping);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改ColumnMapping失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("columnMapping/excel")
    @ResponseBody
    @RequiresPermissions("columnMapping:export")
    public void export(QueryRequest queryRequest, ColumnMapping columnMapping, HttpServletResponse response) throws FebsException {
        try {
            List<ColumnMapping> columnMappings = this.columnMappingService.findColumnMappings(queryRequest, columnMapping).getRecords();
            ExcelKit.$Export(ColumnMapping.class, response).downXlsx(columnMappings, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
