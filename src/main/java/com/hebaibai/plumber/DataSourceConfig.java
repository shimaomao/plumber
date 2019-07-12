package com.hebaibai.plumber;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 用于数据库的链接
 *
 * @author hjx
 */
public class DataSourceConfig {

    @Getter
    @Setter
    private String hostname = "localhost";

    @Getter
    @Setter
    private int port = 3306;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String mark;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataSourceConfig dataSourceConfig = (DataSourceConfig) o;
        return port == dataSourceConfig.port &&
                Objects.equals(hostname, dataSourceConfig.hostname) &&
                Objects.equals(username, dataSourceConfig.username) &&
                Objects.equals(password, dataSourceConfig.password) &&
                Objects.equals(mark, dataSourceConfig.mark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostname, port, username, password, mark);
    }
}