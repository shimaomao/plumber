package com.hebaibai.plumber.core.handler;

import com.hebaibai.plumber.core.Auth;
import com.hebaibai.plumber.core.utils.TableMateData;
import com.hebaibai.plumber.core.utils.TableMateDataUtils;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class AbstractEventHandler implements EventHandler {

    protected DataSource targetDataSource;

    protected String targetDatabase;

    protected String targetTable;

    protected String sourceDatabase;

    protected String sourceTable;

    protected String name;

    protected boolean status = true;

    protected Auth auth;

    protected TableMateData sourceTableMateData;

    protected TableMateData targetTableMateData;

    protected Map<String, String> mapping;

    protected Set<String> keys;

    @Override
    public void setSource(Auth auth, String database, String table) throws SQLException {
        this.sourceDatabase = database;
        this.sourceTable = table;
        this.auth = auth;
        this.sourceTableMateData = initMateDate(auth.getHostname(), database, table, auth.getPort(), auth.getUsername(), auth.getPassword());
    }

    @Override
    public void setTarget(DataSource dataSource, String database, String table) throws SQLException {
        this.targetDataSource = dataSource;
        this.targetDatabase = database;
        this.targetTable = table;
        this.targetTableMateData = initMateDate(dataSource, database, table);
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

    /**
     * 获取数据库的元数据
     *
     * @param host
     * @param database
     * @param port
     * @param user
     * @param pwd
     * @return
     * @throws SQLException
     */
    private TableMateData initMateDate(String host, String database, String table, int port, String user, String pwd) throws SQLException {
        log.info("Load TableMateData: {}.{}", database, table);
        TableMateDataUtils tableMateDataUtils = new TableMateDataUtils(
                host, port, user,
                pwd, database, table
        );
        return tableMateDataUtils.getTableMateData();
    }

    /**
     * 获取数据库的元数据
     *
     * @param dataSource
     * @param database
     * @param table
     * @return
     * @throws SQLException
     */
    private TableMateData initMateDate(DataSource dataSource, String database, String table) throws SQLException {
        log.info("Load TableMateData: {}.{}", database, table);
        TableMateDataUtils tableMateDataUtils = new TableMateDataUtils(dataSource, database, table);
        return tableMateDataUtils.getTableMateData();
    }

}
