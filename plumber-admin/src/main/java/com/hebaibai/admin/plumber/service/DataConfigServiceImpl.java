package com.hebaibai.admin.plumber.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.plumber.entity.DataConfig;
import com.hebaibai.admin.plumber.mapper.DataConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据目标 Service实现
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:41
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DataConfigServiceImpl extends ServiceImpl<DataConfigMapper, DataConfig> implements IDataConfigService {

    @Autowired
    private DataConfigMapper dataConfigMapper;

    @Override
    public IPage<DataConfig> findDataConfigs(QueryRequest request, DataConfig dataConfig) {
        Page<DataConfig> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, new LambdaQueryWrapper<DataConfig>());
    }

    @Override
    public List<DataConfig> findDataConfigs(DataConfig dataConfig) {
        LambdaQueryWrapper<DataConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataConfig::getType, dataConfig.getType());
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createDataConfig(DataConfig dataConfig) {
        this.save(dataConfig);
    }

    @Override
    @Transactional
    public void updateDataConfig(DataConfig dataConfig) {
        this.saveOrUpdate(dataConfig);
    }

    @Override
    @Transactional
    public void deleteDataConfig(DataConfig dataConfig) {
        LambdaQueryWrapper<DataConfig> wapper = new LambdaQueryWrapper<>();
        // TODO 设置删除条件
        this.remove(wapper);
    }
}
