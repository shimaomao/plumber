package com.hebaibai.plumber.core.utils;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 数据库表元信息
 *
 * @author hjx
 */
@Data
public class TableMateData {

    private String nama;

    private String dataBase;

    private List<String> columns;

    private Map<String, String> columnType;

}
