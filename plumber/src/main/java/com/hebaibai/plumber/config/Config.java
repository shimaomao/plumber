package com.hebaibai.plumber.config;

import com.hebaibai.plumber.core.EventHandler;
import lombok.Data;

import java.util.Set;

/**
 * 程序配置
 *
 * @author hjx
 */
@Data
public class Config {

    /**
     * binlog name
     */
    private String logName;

    /**
     * binlog 位置
     */
    private Long position;

    private DataSourceConfig dataSourceConfig;

    private DataTargetConfig dataTargetConfig;

    private Set<EventHandler> eventHandlers;

}
