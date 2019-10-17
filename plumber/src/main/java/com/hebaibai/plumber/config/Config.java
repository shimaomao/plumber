package com.hebaibai.plumber.config;

import com.hebaibai.plumber.core.EventHandler;
import com.hebaibai.plumber.core.EventDataExecuter;
import lombok.Data;

import java.util.List;
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

    private Set<EventHandler> eventHandlers;

    private List<EventDataExecuter> sqlEventDataExecuters;

}
