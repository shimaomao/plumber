package com.hebaibai.admin.plumber.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.plumber.entity.ColumnMapping;
import com.hebaibai.admin.plumber.entity.Plumber;
import com.hebaibai.admin.plumber.mapper.ColumnMappingMapper;
import com.hebaibai.admin.plumber.mapper.PlumberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字段映射 Service实现
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:34
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ColumnMappingServiceImpl extends ServiceImpl<ColumnMappingMapper, ColumnMapping> implements IColumnMappingService {

    @Autowired
    private ColumnMappingMapper columnMappingMapper;

    @Override
    public IPage<ColumnMapping> findColumnMappings(QueryRequest request, ColumnMapping columnMapping) {
        LambdaQueryWrapper<ColumnMapping> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<ColumnMapping> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<ColumnMapping> findColumnMappings(ColumnMapping columnMapping) {
        LambdaQueryWrapper<ColumnMapping> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ColumnMapping::getDataMappingId, columnMapping.getDataMappingId());
        queryWrapper.orderByAsc(ColumnMapping::getOrder);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createColumnMapping(ColumnMapping columnMapping) {
        this.save(columnMapping);
    }

    @Override
    @Transactional
    public void updateColumnMapping(ColumnMapping columnMapping) {
        this.saveOrUpdate(columnMapping);
    }

    @Override
    @Transactional
    public void deleteColumnMapping(ColumnMapping columnMapping) {
        LambdaQueryWrapper<ColumnMapping> wapper = new LambdaQueryWrapper<>();
        // TODO 设置删除条件
        this.remove(wapper);
    }
}
