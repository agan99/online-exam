package com.agan.exam.dao;

import com.agan.exam.model.Major;
import com.agan.exam.model.vo.MajorVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface MajorDAO extends BaseMapper<Major> {

    /**
     * 查询专业集合
     * @param page 分页信息
     * @param major 专业查询条件
     * @return 专业集合
     */
    IPage<MajorVo> listPageMajorVo(Page<Major> page, @Param("major") Major major);
}
