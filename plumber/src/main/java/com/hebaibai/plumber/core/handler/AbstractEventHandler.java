package com.hebaibai.plumber.core.handler;

import com.alibaba.fastjson.JSONObject;
import com.hebaibai.plumber.core.EventHandler;
import com.hebaibai.plumber.core.SqlEventDataExecuter;
import com.hebaibai.plumber.core.utils.TableMateData;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 公用默认实现
 *
 * @author hjx
 */
@Slf4j
public abstract class AbstractEventHandler implements EventHandler {

    protected String targetDatabase;

    protected String targetTable;

    protected String sourceDatabase;

    protected String sourceTable;

    protected boolean status = true;

    protected TableMateData sourceTableMateData;

    protected TableMateData targetTableMateData;

    protected Map<String, String> mapping;

    protected String key;

    protected List<SqlEventDataExecuter> eventPlugins = new ArrayList<>();

    @Override
    public void setSource(TableMateData tableMateData) {
        this.sourceDatabase = tableMateData.getDataBase();
        this.sourceTable = tableMateData.getNama();
        this.sourceTableMateData = tableMateData;
    }

    @Override
    public void setTarget(TableMateData tableMateData) {
        this.targetDatabase = tableMateData.getDataBase();
        this.targetTable = tableMateData.getNama();
        this.targetTableMateData = tableMateData;
    }

    @Override
    public void setStatus(boolean isRun) {
        this.status = isRun;
    }

    @Override
    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void addPlugin(SqlEventDataExecuter eventPlugin) {
        if (eventPlugin == null) {
            return;
        }
        this.eventPlugins.add(eventPlugin);
    }

    @Override
    public String toString() {
        Map map = new HashMap() {{
            put("name", getClass().getSimpleName());
            put("targetDatabase", targetDatabase);
            put("targetTable", targetTable);
            put("sourceDatabase", sourceDatabase);
            put("sourceTable", sourceTable);
            put("key", key);
            put("mapping", mapping);
        }};
        return JSONObject.toJSONString(map);
    }
}
