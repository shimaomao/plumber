package com.hebaibai.admin.plumber.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 数据同步 Entity
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:38
 */
@Data
@TableName("b_plumber")
public class Plumber {

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    @TableField("name")
    private String name;

    /**
     *
     */
    @TableField("data_source_id")
    private Integer dataSourceId;

    /**
     *
     */
    @TableField("data_target_id")
    private Integer dataTargetId;

    /**
     *
     */
    @TableField("create_time")
    private Date createTime;

    /**
     *
     */
    @TableField("status")
    private Integer status;

    /**
     *
     */
    @TableField("last_run")
    private Date lastRun;

    /**
     * 数据库配置：来源
     */
    @TableField(exist = false)
    private DataConfig source;

    /**
     * 数据库配置：目标
     */
    @TableField(exist = false)
    private DataConfig target;

    /**
     * 运行状态
     */
    @TableField(exist = false)
    private String runStatus;

}