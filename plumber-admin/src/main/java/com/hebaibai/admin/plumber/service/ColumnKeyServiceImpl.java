package com.hebaibai.admin.plumber.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.plumber.entity.ColumnKey;
import com.hebaibai.admin.plumber.entity.Plumber;
import com.hebaibai.admin.plumber.mapper.ColumnKeyMapper;
import com.hebaibai.admin.plumber.mapper.PlumberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 键 Service实现
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:36
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ColumnKeyServiceImpl extends ServiceImpl<ColumnKeyMapper, ColumnKey> implements IColumnKeyService {

    @Autowired
    private ColumnKeyMapper columnKeyMapper;

    @Override
    public IPage<ColumnKey> findColumnKeys(QueryRequest request, ColumnKey columnKey) {
        LambdaQueryWrapper<ColumnKey> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<ColumnKey> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<ColumnKey> findColumnKeys(ColumnKey columnKey) {
	    LambdaQueryWrapper<ColumnKey> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createColumnKey(ColumnKey columnKey) {
        this.save(columnKey);
    }

    @Override
    @Transactional
    public void updateColumnKey(ColumnKey columnKey) {
        this.saveOrUpdate(columnKey);
    }

    @Override
    @Transactional
    public void deleteColumnKey(ColumnKey columnKey) {
        LambdaQueryWrapper<ColumnKey> wapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wapper);
	}
}
