package com.hebaibai.admin.plumber;

import com.hebaibai.admin.common.entity.FebsConstant;
import com.hebaibai.admin.common.exception.FebsException;
import com.hebaibai.admin.common.utils.FebsUtil;
import com.hebaibai.admin.plumber.entity.DataMapping;
import com.hebaibai.admin.plumber.entity.Plumber;
import com.hebaibai.admin.plumber.service.IDataMappingService;
import com.hebaibai.admin.plumber.service.IPlumberService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 数据同步 plumber 视图控制器
 */
@Controller
public class PlumberViewController {

    @Autowired
    private IPlumberService iPlumberService;


    /**
     * 数据库设置
     *
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "dataConfig")
    private String dataMappingIndex() {
        return FebsUtil.view("plumber/dataConfig/list");
    }

    /**
     * plumber实例列表
     *
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "plumber")
    private String plumberIndex() {
        return FebsUtil.view("plumber/list");
    }

    /**
     * plumber实例添加
     *
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "plumber/add")
    private String plumberAdd() {
        return FebsUtil.view("plumber/add");
    }

    /**
     * plumber实例修改
     *
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "plumber/{id}/update")
    private String plumberUpdate(@PathVariable String id, ModelMap modelMap) {
        Plumber plumber = iPlumberService.getById(id);
        modelMap.put("plumber", plumber);
        return FebsUtil.view("plumber/update");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "dataMapping")
    private String dataMappingList(ModelMap modelMap) {
        return FebsUtil.view("plumber/dataMapping/list");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "dataMapping/add")
    private String dataMappingAdd(ModelMap modelMap) {
        return FebsUtil.view("plumber/dataMapping/add");
    }

}
