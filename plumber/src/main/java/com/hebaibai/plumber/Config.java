package com.hebaibai.plumber;

import com.hebaibai.plumber.core.handler.EventHandler;
import lombok.Data;

import java.util.Set;

@Data
public class Config {

    private DataSourceConfig dataSourceConfig;

    private DataTargetConfig dataTargetConfig;

    private Set<EventHandler> eventHandlers;

}
