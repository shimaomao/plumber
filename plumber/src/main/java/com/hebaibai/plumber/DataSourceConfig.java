package com.hebaibai.plumber;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 收据来源配置
 *
 * @author hjx
 */
@Data
public class DataSourceConfig {

    private String host = "localhost";

    private int port = 3306;

    private String username;

    private String password;

    private String mark;

    private String database;

}