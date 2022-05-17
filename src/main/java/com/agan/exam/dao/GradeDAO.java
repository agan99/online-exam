package com.agan.exam.dao;

import com.agan.exam.model.Grade;
import com.agan.exam.model.dto.QueryGradeDto;
import com.agan.exam.model.vo.GradeVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface GradeDAO extends BaseMapper<Grade> {

    /**
     * 通过条件查询班级 List 集合
     * @param page   分页信息
     * @param entity 班级模糊信息
     * @return 班级 List 集合
     */
    IPage<GradeVo> pageVo(Page<Grade> page, @Param("entity") QueryGradeDto entity);

    /**
     * 通过条件查询班级
     */
    GradeVo getVoById(Integer id);
}
