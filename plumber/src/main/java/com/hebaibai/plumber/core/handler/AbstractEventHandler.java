package com.hebaibai.plumber.core.handler;

import com.hebaibai.plumber.DataSourceConfig;
import com.hebaibai.plumber.core.utils.TableMateData;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;

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

    protected DataSourceConfig dataSourceConfig;

    protected TableMateData sourceTableMateData;

    protected TableMateData targetTableMateData;

    protected Map<String, String> mapping;

    protected Set<String> keys;

    @Override
    public void setSource(TableMateData sourceTableMateData) {
        this.sourceDatabase = sourceTableMateData.getDataBase();
        this.sourceTable = sourceTableMateData.getNama();
        this.sourceTableMateData = sourceTableMateData;
    }

    @Override
    public void setTarget(TableMateData targetTableMateData) {
        this.targetDatabase = targetTableMateData.getDataBase();
        this.targetTable = targetTableMateData.getNama();
        this.targetTableMateData = targetTableMateData;
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
    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }

}
