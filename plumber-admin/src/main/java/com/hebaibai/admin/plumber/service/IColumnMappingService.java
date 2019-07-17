package com.hebaibai.admin.plumber.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.plumber.entity.ColumnKey;
import com.hebaibai.admin.plumber.entity.ColumnMapping;

import java.util.List;
/**
 * 字段映射 Service接口
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:34
 */
public interface IColumnMappingService extends IService<ColumnMapping> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param columnMapping columnMapping
     * @return IPage<ColumnMapping>
     */
    IPage<ColumnMapping> findColumnMappings(QueryRequest request, ColumnMapping columnMapping);

    /**
     * 查询（所有）
     *
     * @param columnMapping columnMapping
     * @return List<ColumnMapping>
     */
    List<ColumnMapping> findColumnMappings(ColumnMapping columnMapping);

    /**
     * 新增
     *
     * @param columnMapping columnMapping
     */
    void createColumnMapping(ColumnMapping columnMapping);

    /**
     * 修改
     *
     * @param columnMapping columnMapping
     */
    void updateColumnMapping(ColumnMapping columnMapping);

    /**
     * 删除
     *
     * @param columnMapping columnMapping
     */
    void deleteColumnMapping(ColumnMapping columnMapping);
}
