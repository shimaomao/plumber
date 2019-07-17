package com.hebaibai.admin.plumber.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 数据映射 Entity
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:43
 */
@Data
@TableName("b_data_mapping")
public class DataMapping {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField("plumber_id")
    private Integer plumberId;

    /**
     * 来源数据库
     */
    @TableField("source_database")
    private String sourceDatabase;

    /**
     * 来源表
     */
    @TableField("source_table")
    private String sourceTable;

    /**
     * 目标数据库
     */
    @TableField("target_database")
    private String targetDatabase;

    /**
     * 目标表
     */
    @TableField("target_table")
    private String targetTable;

    /**
     * 
     */
    @TableField("mark")
    private String mark;

    /**
     * 
     */
    @TableField("create_time")
    private Date createTime;

}