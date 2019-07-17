package com.hebaibai.admin.plumber.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 数据目标 Entity
 *
 * @author hejiaxuan
 * @date 2019-07-11 17:05:18
 */
@Data
@TableName("b_data_config")
public class DataConfig {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField("host")
    private String host;

    /**
     * 
     */
    @TableField("user")
    private String user;

    /**
     * 
     */
    @TableField("port")
    private Integer port;

    /**
     * 
     */
    @TableField("pwd")
    private String pwd;

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

    /**
     * 1:source; 0:target
     */
    @TableField("type")
    private Integer type;

}