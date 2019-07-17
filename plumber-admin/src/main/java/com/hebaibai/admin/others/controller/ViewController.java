package com.hebaibai.admin.others.controller;

import com.hebaibai.admin.common.entity.FebsConstant;
import com.hebaibai.admin.common.utils.FebsUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author MrBird
 */
@Controller("othersView")
@RequestMapping(FebsConstant.VIEW_PREFIX + "others")
public class ViewController {

    @GetMapping("admin/form")
    @RequiresPermissions("admin:form:view")
    public String febsForm() {
        return FebsUtil.view("others/admin/form");
    }

    @GetMapping("admin/form/group")
    @RequiresPermissions("admin:formgroup:view")
    public String febsFormGroup() {
        return FebsUtil.view("others/admin/formGroup");
    }

    @GetMapping("admin/tools")
    @RequiresPermissions("admin:tools:view")
    public String febsTools() {
        return FebsUtil.view("others/admin/tools");
    }

    @GetMapping("admin/icon")
    @RequiresPermissions("admin:icons:view")
    public String febsIcon() {
        return FebsUtil.view("others/admin/icon");
    }

    @GetMapping("admin/others")
    @RequiresPermissions("others:admin:others")
    public String febsOthers() {
        return FebsUtil.view("others/admin/others");
    }

    @GetMapping("apex/line")
    @RequiresPermissions("apex:line:view")
    public String apexLine() {
        return FebsUtil.view("others/apex/line");
    }

    @GetMapping("apex/area")
    @RequiresPermissions("apex:area:view")
    public String apexArea() {
        return FebsUtil.view("others/apex/area");
    }

    @GetMapping("apex/column")
    @RequiresPermissions("apex:column:view")
    public String apexColumn() {
        return FebsUtil.view("others/apex/column");
    }

    @GetMapping("apex/radar")
    @RequiresPermissions("apex:radar:view")
    public String apexRadar() {
        return FebsUtil.view("others/apex/radar");
    }

    @GetMapping("apex/bar")
    @RequiresPermissions("apex:bar:view")
    public String apexBar() {
        return FebsUtil.view("others/apex/bar");
    }

    @GetMapping("apex/mix")
    @RequiresPermissions("apex:mix:view")
    public String apexMix() {
        return FebsUtil.view("others/apex/mix");
    }

    @GetMapping("map")
    @RequiresPermissions("map:view")
    public String map() {
        return FebsUtil.view("others/map/gaodeMap");
    }

    @GetMapping("eximport")
    @RequiresPermissions("others:eximport:view")
    public String eximport() {
        return FebsUtil.view("others/eximport/eximport");
    }

    @GetMapping("eximport/result")
    public String eximportResult() {
        return FebsUtil.view("others/eximport/eximportResult");
    }
}
