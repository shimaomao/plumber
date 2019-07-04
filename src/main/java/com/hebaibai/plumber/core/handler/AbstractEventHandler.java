package com.hebaibai.plumber.core.handler;

import com.hebaibai.plumber.core.Auth;
import com.hebaibai.plumber.core.utils.TableMateData;
import com.hebaibai.plumber.core.utils.TableMateDataUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Getter
@Setter
@Slf4j
public abstract class AbstractEventHandler implements EventHandler {

    protected String database;

    protected String table;

    protected String name;

    protected boolean status = true;

    protected Auth auth;

    protected TableMateData tableMateData;

    @Override
    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    @Override
    public void setStatus(boolean isRun) {
        this.status = isRun;
    }

    /**
     * 获取数据库的元数据
     *
     * @param renew 是否更新
     * @throws SQLException
     */
    public void initMateDate(boolean renew) throws SQLException {
        log.info("加载表: {}.{} 元数据. 是否更新: {}", database, table, renew);
        if (auth == null) {
            throw new UnsupportedOperationException("不支持的操作");
        }
        if (renew || tableMateData == null) {
            TableMateDataUtils tableMateDataUtils = new TableMateDataUtils(
                    auth.getHostname(), auth.getPort(), auth.getUsername(),
                    auth.getPassword(), database, table
            );
            tableMateData = tableMateDataUtils.getTableMateData();
        }
    }

}
