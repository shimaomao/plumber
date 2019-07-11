package com.hebaibai.plumber;

/**
 * 注册的事件
 */
public interface ConsumerAddress {

    String EXECUTE_SQL_QUERY = "execute-sql-query";
    String EXECUTE_SQL_INSERT = "execute-sql-insert";
    String EXECUTE_SQL_DELETE = "execute-sql-delete";
    String EXECUTE_SQL_UPDATE = "execute-sql-update";
}
