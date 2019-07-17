package com.hebaibai.admin.generator.servie;

import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.generator.entity.Column;
import com.hebaibai.admin.generator.entity.Table;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author MrBird
 */
public interface IGeneratorService {

    List<String> getDatabases(String databaseType);

    IPage<Table> getTables(String tableName, QueryRequest request, String databaseType, String schemaName);

    List<Column> getColumns(String databaseType, String schemaName, String tableName);
}
