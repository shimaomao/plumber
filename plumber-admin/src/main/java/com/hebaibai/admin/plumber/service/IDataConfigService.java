package com.hebaibai.admin.plumber.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.plumber.entity.ColumnKey;
import com.hebaibai.admin.plumber.entity.DataConfig;

import java.util.List;

/**
 * 数据目标 Service接口
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:41
 */
public interface IDataConfigService extends IService<DataConfig> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param dataConfig dataConfig
     * @return IPage<DataConfig>
     */
    IPage<DataConfig> findDataConfigs(QueryRequest request, DataConfig dataConfig);

    /**
     * 查询（所有）
     *
     * @param dataConfig dataConfig
     * @return List<DataConfig>
     */
    List<DataConfig> findDataConfigs(DataConfig dataConfig);

    /**
     * 新增
     *
     * @param dataConfig dataConfig
     */
    void createDataConfig(DataConfig dataConfig);

    /**
     * 修改
     *
     * @param dataConfig dataConfig
     */
    void updateDataConfig(DataConfig dataConfig);

    /**
     * 删除
     *
     * @param dataConfig dataConfig
     */
    void deleteDataConfig(DataConfig dataConfig);
}
