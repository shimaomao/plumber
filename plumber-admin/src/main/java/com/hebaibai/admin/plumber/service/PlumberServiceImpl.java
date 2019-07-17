package com.hebaibai.admin.plumber.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.generator.entity.Column;
import com.hebaibai.admin.plumber.entity.*;
import com.hebaibai.admin.plumber.mapper.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 数据同步 Service实现
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:38
 */
@SuppressWarnings("ALL")
@Service
public class PlumberServiceImpl extends ServiceImpl<PlumberMapper, Plumber> implements IPlumberService {

    @Autowired
    private PlumberMapper plumberMapper;

    @Autowired
    private DataConfigMapper dataConfigMapper;

    @Autowired
    private DataMappingMapper dataMappingMapper;

    @Autowired
    private ColumnMappingMapper columnMappingMapper;

    @Autowired
    private ColumnKeyMapper columnKeyMapper;

    @Override
    public IPage<Plumber> findPlumbers(QueryRequest request, Plumber plumber) {
        LambdaQueryWrapper<Plumber> queryWrapper = new LambdaQueryWrapper<>();
        Page<Plumber> page = new Page<>(request.getPageNum(), request.getPageSize());
        IPage<Plumber> plumberIPage = this.page(page, queryWrapper);
        List<Plumber> plumbers = plumberIPage.getRecords();
        for (Plumber record : plumbers) {
            DataConfig source = dataConfigMapper.selectById(record.getDataSourceId());
            DataConfig target = dataConfigMapper.selectById(record.getDataTargetId());
            record.setSource(source);
            record.setTarget(target);
        }
        return plumberIPage;
    }

    @Override
    public List<Plumber> findPlumbers(Plumber plumber) {
        LambdaQueryWrapper<Plumber> queryWrapper = new LambdaQueryWrapper<>();
        List<Plumber> plumbers = this.baseMapper.selectList(queryWrapper);
        for (Plumber record : plumbers) {
            DataConfig source = dataConfigMapper.selectById(record.getDataSourceId());
            DataConfig target = dataConfigMapper.selectById(record.getDataTargetId());
            record.setSource(source);
            record.setTarget(target);
        }
        return plumbers;
    }

    /**
     * 新增实例
     *
     * @param plumber plumber
     */
    @Override
    @Transactional
    public void createPlumber(Plumber plumber) {
        plumber.setCreateTime(new Date());
        this.save(plumber);
    }

    @Override
    @Transactional
    public void updatePlumber(Plumber plumber) {
        this.saveOrUpdate(plumber);
    }

    /**
     * 删除实例
     * 并删除其他相关所有信息
     *
     * @param plumber plumber
     */
    @Override
    @Transactional
    public void deletePlumberAllById(@NonNull String plumberId) {
        plumberMapper.delete(new LambdaQueryWrapper<Plumber>().eq(Plumber::getId, plumberId));
        List<DataMapping> dataMappings = dataMappingMapper.selectList(new LambdaQueryWrapper<DataMapping>().eq(DataMapping::getPlumberId, plumberId));
        for (DataMapping dataMapping : dataMappings) {
            columnMappingMapper.delete(new LambdaQueryWrapper<ColumnMapping>().eq(ColumnMapping::getDataMappingId, dataMapping.getId()));
            columnKeyMapper.delete(new LambdaQueryWrapper<ColumnKey>().eq(ColumnKey::getDataMappingId, dataMapping.getId()));
        }
    }

    @Override
    public Plumber getById(Serializable id) {
        Plumber plumber = super.getById(id);
        DataConfig source = dataConfigMapper.selectById(plumber.getDataSourceId());
        DataConfig target = dataConfigMapper.selectById(plumber.getDataTargetId());
        plumber.setSource(source);
        plumber.setTarget(target);
        return plumber;
    }
}
