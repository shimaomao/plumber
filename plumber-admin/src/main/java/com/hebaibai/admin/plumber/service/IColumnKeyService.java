package com.hebaibai.admin.plumber.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.plumber.entity.ColumnKey;

import java.util.List;

/**
 * 键 Service接口
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:36
 */
public interface IColumnKeyService extends IService<ColumnKey> {
    /**
     * 查询（分页）
     *
     * @param request   QueryRequest
     * @param columnKey columnKey
     * @return IPage<ColumnKey>
     */
    IPage<ColumnKey> findColumnKeys(QueryRequest request, ColumnKey columnKey);

    /**
     * 查询（所有）
     *
     * @param columnKey columnKey
     * @return List<ColumnKey>
     */
    List<ColumnKey> findColumnKeys(ColumnKey columnKey);

    /**
     * 新增
     *
     * @param columnKey columnKey
     */
    void createColumnKey(ColumnKey columnKey);

    /**
     * 修改
     *
     * @param columnKey columnKey
     */
    void updateColumnKey(ColumnKey columnKey);

    /**
     * 删除
     *
     * @param columnKey columnKey
     */
    void deleteColumnKey(ColumnKey columnKey);
}
