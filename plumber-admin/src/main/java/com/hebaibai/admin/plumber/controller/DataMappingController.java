package com.hebaibai.admin.plumber.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hebaibai.admin.common.annotation.Log;
import com.hebaibai.admin.common.controller.BaseController;
import com.hebaibai.admin.common.entity.FebsResponse;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.common.exception.FebsException;
import com.hebaibai.admin.plumber.entity.DataMapping;
import com.hebaibai.admin.plumber.service.DataBaseService;
import com.hebaibai.admin.plumber.service.IDataMappingService;
import com.hebaibai.plumber.core.utils.TableMateData;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据映射 Controller
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:43
 */
@Slf4j
@Validated
@Controller
public class DataMappingController extends BaseController {

    @Autowired
    private IDataMappingService dataMappingService;

    @Autowired
    private DataBaseService dataBaseService;


    @GetMapping("dataMapping")
    @ResponseBody
    @RequiresPermissions("dataMapping:list")
    public FebsResponse getAllDataMappings(DataMapping dataMapping) {
        return new FebsResponse().success().data(dataMappingService.findDataMappings(dataMapping));
    }

    @GetMapping("dataMapping/list")
    @ResponseBody
    @RequiresPermissions("dataMapping:list")
    public FebsResponse dataMappingList(QueryRequest request, DataMapping dataMapping) {
        Map<String, Object> dataTable = getDataTable(this.dataMappingService.findDataMappings(request, dataMapping));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 保存映射
     *
     * @param json
     * @return
     */
    @Log("新增DataMapping")
    @PostMapping("dataMapping/save")
    @ResponseBody
    public FebsResponse dataMappingSave(@RequestBody String json) throws FebsException {
        try {
            DataMapping dataMapping = dataMappingService.saveDataMapping(json);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增DataMapping失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("删除DataMapping")
    @GetMapping("dataMapping/{id}/delete")
    @ResponseBody
    public FebsResponse deleteDataMapping(@PathVariable String id) throws FebsException {
        try {
            this.dataMappingService.deleteDataMapping(id);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除DataMapping失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * @param plumberId
     * @return
     * @throws FebsException
     */
    @GetMapping("dataMapping/database")
    @ResponseBody
    public FebsResponse findAllDataBase(String plumberId) throws FebsException {
        try {
            Map<String, List<String>> databases = dataBaseService.findAllDataBase(plumberId);
            return new FebsResponse().success().data(databases);
        } catch (Exception e) {
            String message = "获取数据库信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * @param plumberId
     * @param dataBase
     * @param type
     * @return
     * @throws FebsException
     */
    @GetMapping("dataMapping/table")
    @ResponseBody
    public FebsResponse findAllTable(int type, String plumberId, String dataBase) throws FebsException {
        try {
            List<String> tables = dataBaseService.findAllTable(type, dataBase, plumberId);
            return new FebsResponse().success().data(tables);
        } catch (Exception e) {
            String message = "获取数据库信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @GetMapping("dataMapping/column")
    @ResponseBody
    public FebsResponse findAllColumn(int type, String plumberId, String dataBase, String table) throws FebsException {
        try {
            TableMateData tableMateData = dataBaseService.getTableMateData(type, dataBase, plumberId, table);
            return new FebsResponse().success().data(tableMateData);
        } catch (Exception e) {
            String message = "获取数据库信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
