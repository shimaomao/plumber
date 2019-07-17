package com.hebaibai.admin.plumber.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 键 Entity
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:36
 */
@Data
@TableName("b_column_key")
public class ColumnKey {

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
     * 键名称
     */
    @TableField("key_name")
    private String keyName;

    /**
     * 
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField("mark")
    private String mark;

}