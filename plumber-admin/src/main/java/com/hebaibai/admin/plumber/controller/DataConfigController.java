package com.hebaibai.admin.plumber.controller;

import com.hebaibai.admin.common.annotation.Log;
import com.hebaibai.admin.common.controller.BaseController;
import com.hebaibai.admin.common.entity.FebsResponse;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.common.exception.FebsException;
import com.hebaibai.admin.plumber.entity.DataConfig;
import com.hebaibai.admin.plumber.service.IDataConfigService;
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
 * 数据目标 Controller
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:41
 */
@Slf4j
@Validated
@Controller
public class DataConfigController extends BaseController {

    @Autowired
    private IDataConfigService dataConfigService;


    @GetMapping("dataConfig")
    @ResponseBody
    @RequiresPermissions("dataConfig:list")
    public FebsResponse getAllDataConfigs(DataConfig dataConfig) {
        List<DataConfig> dataConfigs = dataConfigService.findDataConfigs(dataConfig);
        return new FebsResponse().success().data(dataConfigs);
    }

    @GetMapping("dataConfig/list")
    @ResponseBody
    @RequiresPermissions("dataConfig:list")
    public FebsResponse dataConfigList(QueryRequest request, DataConfig dataConfig) {
        Map<String, Object> dataTable = getDataTable(this.dataConfigService.findDataConfigs(request, dataConfig));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增DataConfig")
    @PostMapping("dataConfig")
    @ResponseBody
    @RequiresPermissions("dataConfig:add")
    public FebsResponse addDataConfig(@Valid DataConfig dataConfig) throws FebsException {
        try {
            this.dataConfigService.createDataConfig(dataConfig);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增DataConfig失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除DataConfig")
    @GetMapping("dataConfig/delete")
    @ResponseBody
    @RequiresPermissions("dataConfig:delete")
    public FebsResponse deleteDataConfig(DataConfig dataConfig) throws FebsException {
        try {
            this.dataConfigService.deleteDataConfig(dataConfig);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除DataConfig失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改DataConfig")
    @PostMapping("dataConfig/update")
    @ResponseBody
    @RequiresPermissions("dataConfig:update")
    public FebsResponse updateDataConfig(DataConfig dataConfig) throws FebsException {
        try {
            this.dataConfigService.updateDataConfig(dataConfig);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改DataConfig失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("dataConfig/excel")
    @ResponseBody
    @RequiresPermissions("dataConfig:export")
    public void export(QueryRequest queryRequest, DataConfig dataConfig, HttpServletResponse response) throws FebsException {
        try {
            List<DataConfig> dataConfigs = this.dataConfigService.findDataConfigs(queryRequest, dataConfig).getRecords();
            ExcelKit.$Export(DataConfig.class, response).downXlsx(dataConfigs, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
