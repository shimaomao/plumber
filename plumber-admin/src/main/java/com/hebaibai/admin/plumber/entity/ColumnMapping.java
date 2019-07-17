package com.hebaibai.admin.plumber.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 字段映射 Entity
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:34
 */
@Data
@TableName("b_column_mapping")
public class ColumnMapping {

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * data_mapping表id
     */
    @TableField("data_mapping_id")
    private Integer dataMappingId;

    /**
     * 目标名称
     */
    @TableField("target")
    private String target;

    /**
     * 来源名称
     */
    @TableField("source")
    private String source;

    /**
     * 目标数据类型
     */
    @TableField("target_type")
    private String targetType;

    /**
     * 来源数据类型
     */
    @TableField("source_type")
    private String sourceType;

    /**
     *
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * data_mapping表id
     */
    @TableField("`order`")
    private Integer order;

}