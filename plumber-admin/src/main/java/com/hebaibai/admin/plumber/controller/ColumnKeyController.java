package com.hebaibai.admin.plumber.controller;

import com.hebaibai.admin.common.annotation.Log;
import com.hebaibai.admin.common.controller.BaseController;
import com.hebaibai.admin.common.entity.FebsConstant;
import com.hebaibai.admin.common.entity.FebsResponse;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.common.exception.FebsException;
import com.hebaibai.admin.common.utils.FebsUtil;
import com.hebaibai.admin.plumber.entity.ColumnKey;
import com.hebaibai.admin.plumber.service.IColumnKeyService;
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
 * 键 Controller
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:36
 */
@Slf4j
@Validated
@Controller
public class ColumnKeyController extends BaseController {

    @Autowired
    private IColumnKeyService columnKeyService;
    

    @GetMapping("columnKey")
    @ResponseBody
    @RequiresPermissions("columnKey:list")
    public FebsResponse getAllColumnKeys(ColumnKey columnKey) {
        return new FebsResponse().success().data(columnKeyService.findColumnKeys(columnKey));
    }

    @GetMapping("columnKey/list")
    @ResponseBody
    @RequiresPermissions("columnKey:list")
    public FebsResponse columnKeyList(QueryRequest request, ColumnKey columnKey) {
        Map<String, Object> dataTable = getDataTable(this.columnKeyService.findColumnKeys(request, columnKey));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增ColumnKey")
    @PostMapping("columnKey")
    @ResponseBody
    @RequiresPermissions("columnKey:add")
    public FebsResponse addColumnKey(@Valid ColumnKey columnKey) throws FebsException {
        try {
            this.columnKeyService.createColumnKey(columnKey);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增ColumnKey失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除ColumnKey")
    @GetMapping("columnKey/delete")
    @ResponseBody
    @RequiresPermissions("columnKey:delete")
    public FebsResponse deleteColumnKey(ColumnKey columnKey) throws FebsException {
        try {
            this.columnKeyService.deleteColumnKey(columnKey);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除ColumnKey失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改ColumnKey")
    @PostMapping("columnKey/update")
    @ResponseBody
    @RequiresPermissions("columnKey:update")
    public FebsResponse updateColumnKey(ColumnKey columnKey) throws FebsException {
        try {
            this.columnKeyService.updateColumnKey(columnKey);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改ColumnKey失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("columnKey/excel")
    @ResponseBody
    @RequiresPermissions("columnKey:export")
    public void export(QueryRequest queryRequest, ColumnKey columnKey, HttpServletResponse response) throws FebsException {
        try {
            List<ColumnKey> columnKeys = this.columnKeyService.findColumnKeys(queryRequest, columnKey).getRecords();
            ExcelKit.$Export(ColumnKey.class, response).downXlsx(columnKeys, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
