package com.hebaibai.plumber.core.handler;

import com.hebaibai.plumber.Main;
import com.hebaibai.plumber.core.utils.TableMateData;
import io.vertx.core.logging.JULLogDelegateFactory;
import io.vertx.core.spi.logging.LogDelegate;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

/**
 * 公用默认实现
 *
 * @author hjx
 */
public abstract class AbstractEventHandler implements EventHandler {

    static protected LogDelegate log = new JULLogDelegateFactory().createDelegate(AbstractEventHandler.class.getName());

    protected String targetDatabase;

    protected String targetTable;

    protected String sourceDatabase;

    protected String sourceTable;

    protected boolean status = true;

    protected TableMateData sourceTableMateData;

    protected TableMateData targetTableMateData;

    protected Map<String, String> mapping;

    protected Set<String> keys;

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
    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }

}
