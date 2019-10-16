package com.hebaibai.plumber.core.handler.plugin;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author hjx
 */
@Data
public class EventPluginData {

    private Set<String> keys;
    private String targetDatabase;
    private String targetTable;
    private String sourceDatabase;
    private String sourceTable;
    private Map<String, String> afterData;
}
