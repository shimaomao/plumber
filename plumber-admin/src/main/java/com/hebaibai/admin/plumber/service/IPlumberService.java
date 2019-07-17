package com.hebaibai.admin.plumber.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hebaibai.admin.common.entity.QueryRequest;
import com.hebaibai.admin.plumber.entity.ColumnKey;
import com.hebaibai.admin.plumber.entity.Plumber;

import java.util.List;

/**
 * 数据同步 Service接口
 *
 * @author hejiaxuan
 * @date 2019-07-11 13:53:38
 */
public interface IPlumberService extends IService<Plumber> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param plumber plumber
     * @return IPage<Plumber>
     */
    IPage<Plumber> findPlumbers(QueryRequest request, Plumber plumber);

    /**
     * 查询（所有）
     *
     * @param plumber plumber
     * @return List<Plumber>
     */
    List<Plumber> findPlumbers(Plumber plumber);

    /**
     * 新增
     *
     * @param plumber plumber
     */
    void createPlumber(Plumber plumber);

    /**
     * 修改
     *
     * @param plumber plumber
     */
    void updatePlumber(Plumber plumber);

    /**
     * 删除
     *
     * @param plumberId
     */
    void deletePlumberAllById(String plumberId);
}
