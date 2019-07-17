package com.hebaibai.admin.plumber.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.plumber.entity.ColumnKey;
import com.hebaibai.admin.plumber.entity.DataMapping;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * 数据映射 Service接口
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:43
 */
public interface IDataMappingService extends IService<DataMapping> {
    /**
     * 查询（分页）
     *
     * @param request     QueryRequest
     * @param dataMapping dataMapping
     * @return IPage<DataMapping>
     */
    IPage<DataMapping> findDataMappings(QueryRequest request, DataMapping dataMapping);

    /**
     * 查询（所有）
     *
     * @param dataMapping dataMapping
     * @return List<DataMapping>
     */
    List<DataMapping> findDataMappings(DataMapping dataMapping);

    /**
     * 新增
     *
     * @param dataMapping dataMapping
     */
    void createDataMapping(DataMapping dataMapping);

    /**
     * 修改
     *
     * @param dataMapping dataMapping
     */
    void updateDataMapping(DataMapping dataMapping);


    /**
     * 保存
     *
     * @param json
     * @return
     */
    DataMapping saveDataMapping(String json) throws SQLException;

    void deleteDataMapping(String id);
}
