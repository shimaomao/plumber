package com.hebaibai.admin.plumber.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hebaibai.admin.common.annotation.Log;
import com.hebaibai.admin.common.controller.BaseController;
import com.hebaibai.admin.common.entity.FebsResponse;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.common.exception.FebsException;
import com.hebaibai.admin.plumber.LancherCache;
import com.hebaibai.admin.plumber.entity.Plumber;
import com.hebaibai.admin.plumber.service.PlumberOperationService;
import com.hebaibai.admin.plumber.service.IPlumberService;
import com.hebaibai.plumber.Config;
import com.hebaibai.plumber.PlumberLancher;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 数据同步 Controller
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:38
 */
@Slf4j
@Validated
@Controller
public class PlumberController extends BaseController {

    @Autowired
    private IPlumberService plumberService;

    @Autowired
    private LancherCache lancherCache;

    @Autowired
    private PlumberOperationService plumberOperationService;

    @GetMapping("plumber")
    @ResponseBody
    @RequiresPermissions("plumber:list")
    public FebsResponse plumber(Plumber plumber) {
        List<Plumber> plumbers = this.plumberService.findPlumbers(plumber);
        return new FebsResponse().success().data(plumbers);
    }

    @GetMapping("plumber/list")
    @ResponseBody
    @RequiresPermissions("plumber:list")
    public FebsResponse plumberList(QueryRequest request, Plumber plumber) {
        IPage<Plumber> plumberIPage = this.plumberService.findPlumbers(request, plumber);
        List<Plumber> plumbers = plumberIPage.getRecords();
        for (Plumber obj : plumbers) {
            String id = obj.getId().toString();
            PlumberLancher plumberLancher = lancherCache.getPlumberLancher();
            if (plumberLancher == null) {
                obj.setRunStatus("no-publish");
            } else if (plumberLancher.isRun()) {
                obj.setRunStatus("run");
            } else if (!plumberLancher.isRun()) {
                obj.setRunStatus("stop");
            }
        }
        Map<String, Object> dataTable = getDataTable(plumberIPage);
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增Plumber")
    @PostMapping("plumber/add")
    @ResponseBody
    @RequiresPermissions("plumber:add")
    public FebsResponse addPlumber(@Valid Plumber plumber) throws FebsException {
        try {
            List<Plumber> plumbers = this.plumberService.findPlumbers(plumber);
            if (plumbers.size() == 0) {
                this.plumberService.createPlumber(plumber);
                return new FebsResponse().success();
            }
            return new FebsResponse().fail().message("只能添加一个");
        } catch (Exception e) {
            String message = "新增Plumber失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除Plumber")
    @GetMapping("plumber/{id}/delete")
    @ResponseBody
    @RequiresPermissions("plumber:delete")
    public FebsResponse deletePlumber(@PathVariable String id) throws FebsException {
        //实例在运行中
        if (lancherCache.isRun()) {
            return new FebsResponse().fail().message("实例运行中，请先停掉实例");
        }
        try {
            Plumber plumber = this.plumberService.getById(id);
            if (plumber == null) {
                return new FebsResponse().fail().message("实例不存在");
            }
            this.plumberService.deletePlumberAllById(id);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除Plumber失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改Plumber")
    @PostMapping("plumber/update")
    @ResponseBody
    @RequiresPermissions("plumber:update")
    public FebsResponse updatePlumber(Plumber plumber) throws FebsException {
        //实例在运行中
        if (lancherCache.isRun()) {
            return new FebsResponse().fail().message("实例运行中，请先停掉实例");
        }
        try {
            this.plumberService.updatePlumber(plumber);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改Plumber失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


    @Log("启动Plumber")
    @GetMapping("plumber/{id}/run")
    @ResponseBody
    synchronized public FebsResponse run(@PathVariable String id) throws FebsException {
        //实例在运行中
        if (lancherCache.isRun()) {
            return new FebsResponse().fail().message("实例运行中，请先停掉实例");
        }
        Plumber plumber = this.plumberService.getById(id);
        if (plumber == null || plumber.getStatus() == 0) {
            return new FebsResponse().fail().message("实例状态异常");
        }
        try {
            Config lancherConfig = plumberOperationService.getLancherConfig(plumber);
            lancherCache.start(lancherConfig);
            plumber.setLastRun(new Date());
            plumberService.updatePlumber(plumber);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "启动Plumber失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("停掉Plumber")
    @GetMapping("plumber/{id}/stop")
    @ResponseBody
    synchronized public FebsResponse stop(@PathVariable String id) throws FebsException {
        //实例在运行中
        if (!lancherCache.isRun()) {
            return new FebsResponse().fail().message("实例已经停止");
        }
        Plumber plumber = this.plumberService.getById(id);
        if (plumber == null || plumber.getStatus() == 0) {
            return new FebsResponse().fail().message("实例状态异常");
        }
        try {
            lancherCache.stop();
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "停止Plumber失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
