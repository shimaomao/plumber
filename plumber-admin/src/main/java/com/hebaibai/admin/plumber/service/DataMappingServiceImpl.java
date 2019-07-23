package com.hebaibai.admin.plumber.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.plumber.TableMateDataUtils;
import com.hebaibai.admin.plumber.entity.ColumnKey;
import com.hebaibai.admin.plumber.entity.ColumnMapping;
import com.hebaibai.admin.plumber.entity.DataMapping;
import com.hebaibai.admin.plumber.entity.Plumber;
import com.hebaibai.admin.plumber.mapper.ColumnKeyMapper;
import com.hebaibai.admin.plumber.mapper.ColumnMappingMapper;
import com.hebaibai.admin.plumber.mapper.DataMappingMapper;
import com.hebaibai.plumber.core.utils.TableMateData;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

/**
 * 数据映射 Service实现
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:43
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DataMappingServiceImpl extends ServiceImpl<DataMappingMapper, DataMapping> implements IDataMappingService {

    @Autowired
    private DataMappingMapper dataMappingMapper;

    @Autowired
    private IPlumberService iPlumberService;

    @Autowired
    private ColumnMappingMapper columnMappingMapper;

    @Autowired
    private ColumnKeyMapper columnKeyMapper;

    @Override
    public IPage<DataMapping> findDataMappings(QueryRequest request, DataMapping dataMapping) {
        LambdaQueryWrapper<DataMapping> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<DataMapping> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<DataMapping> findDataMappings(DataMapping dataMapping) {
        LambdaQueryWrapper<DataMapping> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        queryWrapper.eq(DataMapping::getPlumberId, dataMapping.getPlumberId());
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public DataMapping saveDataMapping(String json) throws Exception {

        JSONObject jsonObject = JSONObject.parseObject(json);
        int plumberId = jsonObject.getInteger("plumberId");
        Plumber plumber = iPlumberService.getById(plumberId);
        String sourceDatabase = jsonObject.getString("sourceDatabase");
        String sourceTable = jsonObject.getString("sourceTable");
        String targetDatabase = jsonObject.getString("targetDatabase");
        String targetTable = jsonObject.getString("targetTable");

        if (sourceDatabase.equals(targetDatabase) && sourceTable.equals(targetTable)) {
            throw new UnsupportedOperationException("来源数据不能与目标数据相同");
        }

        //保存
        DataMapping dataMapping = new DataMapping();
        dataMapping.setPlumberId(plumberId);
        dataMapping.setCreateTime(new Date());
        dataMapping.setSourceDatabase(sourceDatabase);
        dataMapping.setSourceTable(sourceTable);
        dataMapping.setTargetDatabase(targetDatabase);
        dataMapping.setTargetTable(targetTable);
        dataMapping.setMark("add");
        this.saveOrUpdate(dataMapping);
        Integer id = dataMapping.getId();

        String key = jsonObject.getString("key");
        ColumnKey columnKey = new ColumnKey();
        columnKey.setDataMappingId(id);
        columnKey.setKeyName(key);
        columnKey.setCreateTime(new Date());
        columnKeyMapper.insert(columnKey);

        TableMateData sourceTableMateData = new TableMateDataUtils(
                plumber.getSource(), sourceDatabase, sourceTable
        ).getTableMateData();

        TableMateData targetTableMateData = new TableMateDataUtils(
                plumber.getTarget(), targetDatabase, targetTable
        ).getTableMateData();

        List<String> sourceColumns = sourceTableMateData.getColumns();

        List<String> targetColumns = targetTableMateData.getColumns();

        JSONArray jsonArray = jsonObject.getJSONArray("mapping");

        for (int i = 0; i < sourceColumns.size(); i++) {
            String column = sourceColumns.get(i);
            String target = null;
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONObject object = jsonArray.getJSONObject(j);
                String name = object.getString("name");
                if (column.equals(name)) {
                    target = object.getString("target");
                }
            }
            if (StringUtils.isBlank(target)) {
                continue;
            }
            if (targetColumns.indexOf(target) == -1) {
                throw new UnsupportedOperationException("字段: " + target + " 不存在");
            }
            ColumnMapping columnMapping = new ColumnMapping();
            columnMapping.setDataMappingId(id);
            columnMapping.setOrder(i);
            columnMapping.setCreateTime(new Date());
            columnMapping.setSource(column);
            columnMapping.setTarget(target);
            columnMappingMapper.insert(columnMapping);
        }
        return dataMapping;
    }

    @Override
    @Transactional
    public void createDataMapping(DataMapping dataMapping) {
        this.save(dataMapping);
    }

    @Override
    @Transactional
    public void updateDataMapping(DataMapping dataMapping) {
        this.saveOrUpdate(dataMapping);
    }

    @Override
    @Transactional
    public void deleteDataMapping(String id) {
        remove(new LambdaQueryWrapper<DataMapping>().eq(DataMapping::getId, id));
        columnMappingMapper.delete(new LambdaQueryWrapper<ColumnMapping>().eq(ColumnMapping::getDataMappingId, id));
        columnKeyMapper.delete(new LambdaQueryWrapper<ColumnKey>().eq(ColumnKey::getDataMappingId, id));
    }
}
