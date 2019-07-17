package com.hebaibai.admin.plumber.service;

import com.hebaibai.admin.plumber.entity.*;
import com.hebaibai.plumber.Config;
import com.hebaibai.plumber.DataSourceConfig;
import com.hebaibai.plumber.DataTargetConfig;
import com.hebaibai.plumber.PlumberLancher;
import com.hebaibai.plumber.core.handler.EventHandler;
import com.hebaibai.plumber.core.handler.InsertUpdateDeleteEventHandlerImpl;
import com.hebaibai.plumber.core.utils.TableMateData;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hjx
 */
@SuppressWarnings("ALL")
@Slf4j
@Service
public class PlumberOperationService {

    @Autowired
    private IColumnKeyService columnKeyService;
    @Autowired
    private IColumnMappingService columnMappingService;
    @Autowired
    private IDataConfigService dataConfigService;
    @Autowired
    private IDataMappingService dataMappingService;
    @Autowired
    private IPlumberService plumberService;

    @Autowired
    private Vertx vertx;

    @Autowired
    private Context context;

    /**
     * 创建实例
     *
     * @param plumber
     */
    synchronized public PlumberLancher createLancher(Plumber plumber) {
        log.info("createLancher PlumberLancher");
        Config config = new Config();
        //数据源
        Integer dataSourceId = plumber.getDataSourceId();
        DataConfig dataSource = dataConfigService.getById(dataSourceId);
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        config.setDataSourceConfig(dataSourceConfig);
        dataSourceConfig.setHostname(dataSource.getHost());
        dataSourceConfig.setPort(dataSource.getPort());
        dataSourceConfig.setUsername(dataSource.getUser());
        dataSourceConfig.setPassword(dataSource.getPwd());
        //数据目标
        Integer dataTargetId = plumber.getDataTargetId();
        DataConfig dataTarget = dataConfigService.getById(dataTargetId);
        DataTargetConfig dataTargetConfig = new DataTargetConfig();
        config.setDataTargetConfig(dataTargetConfig);
        dataTargetConfig.setHost(dataTarget.getHost());
        dataTargetConfig.setPort(dataTarget.getPort());
        dataTargetConfig.setUsername(dataTarget.getUser());
        dataTargetConfig.setPassword(dataTarget.getPwd());

        //获取实例的EventHandler
        List<DataMapping> dataMappings = dataMappingService.findDataMappings(new DataMapping() {{
            setPlumberId(plumber.getId());
        }});
        Set<EventHandler> eventHandlers = new HashSet<>();
        for (DataMapping dataMapping : dataMappings) {
            EventHandler eventHandler = getEventHandler(dataMapping);
            eventHandlers.add(eventHandler);
        }
        config.setEventHandlers(eventHandlers);
        PlumberLancher plumberLancher = new PlumberLancher(config);
        plumberLancher.setVertx(vertx);
        plumberLancher.setContext(context);
        return plumberLancher;
    }

    synchronized public void updateLancher(PlumberLancher plumberLancher, Plumber plumber) {
        log.info("updateLancher PlumberLancher");
        Config config = new Config();
        //数据源
        Integer dataSourceId = plumber.getDataSourceId();
        DataConfig dataSource = dataConfigService.getById(dataSourceId);
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        config.setDataSourceConfig(dataSourceConfig);
        dataSourceConfig.setHostname(dataSource.getHost());
        dataSourceConfig.setPort(dataSource.getPort());
        dataSourceConfig.setUsername(dataSource.getUser());
        dataSourceConfig.setPassword(dataSource.getPwd());
        //数据目标
        Integer dataTargetId = plumber.getDataTargetId();
        DataConfig dataTarget = dataConfigService.getById(dataTargetId);
        DataTargetConfig dataTargetConfig = new DataTargetConfig();
        config.setDataTargetConfig(dataTargetConfig);
        dataTargetConfig.setHost(dataTarget.getHost());
        dataTargetConfig.setPort(dataTarget.getPort());
        dataTargetConfig.setUsername(dataTarget.getUser());
        dataTargetConfig.setPassword(dataTarget.getPwd());

        //获取实例的EventHandler
        List<DataMapping> dataMappings = dataMappingService.findDataMappings(new DataMapping() {{
            setPlumberId(plumber.getId());
        }});
        Set<EventHandler> eventHandlers = new HashSet<>();
        for (DataMapping dataMapping : dataMappings) {
            EventHandler eventHandler = getEventHandler(dataMapping);
            eventHandlers.add(eventHandler);
        }
        config.setEventHandlers(eventHandlers);
        plumberLancher.setConfig(config);
    }


    /**
     * 获取binlog处理器
     *
     * @param dataMapping
     * @return
     */
    private EventHandler getEventHandler(DataMapping dataMapping) {
        log.info("load EventHandler from: {}.{}, to: {}.{}", dataMapping.getSourceDatabase(), dataMapping.getSourceTable(), dataMapping.getTargetDatabase(), dataMapping.getTargetTable());
        //键
        Set<String> keys = columnKeyService.findColumnKeys(new ColumnKey() {{
            setDataMappingId(dataMapping.getId());
        }}).stream().map(ColumnKey::getKeyName).collect(Collectors.toSet());
        //字段映射
        List<ColumnMapping> columnMappings = columnMappingService.findColumnMappings(new ColumnMapping() {{
            setDataMappingId(dataMapping.getId());
        }});

        EventHandler eventHandler = new InsertUpdateDeleteEventHandlerImpl();
        TableMateData source = new TableMateData();
        source.setNama(dataMapping.getSourceTable());
        source.setDataBase(dataMapping.getSourceDatabase());
        source.setColumns(columnMappings.stream().map(ColumnMapping::getSource).collect(Collectors.toList()));
        eventHandler.setSource(source);

        TableMateData target = new TableMateData();
        target.setNama(dataMapping.getTargetTable());
        target.setDataBase(dataMapping.getTargetDatabase());
        eventHandler.setTarget(target);

        eventHandler.setKeys(keys);
        Map<String, String> mapping = columnMappings.stream().filter(o -> StringUtils.isNotBlank(o.getTarget())).collect(Collectors.toMap(
                k -> k.getSource(),
                v -> v.getTarget()
        ));
        eventHandler.setMapping(mapping);
        eventHandler.setStatus(true);
        return eventHandler;
    }

    /**
     * 获取数据库链接
     *
     * @param host
     * @param user
     * @param pwd
     * @param port
     * @return
     * @throws SQLException
     */
    private Connection connection(String host, String user, String pwd, int port) throws SQLException {
        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "?characterEncoding=utf-8";
        Connection connection = DriverManager.getConnection(jdbcUrl, user, pwd);
        return connection;
    }
}
