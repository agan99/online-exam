package com.agan.exam.server;


import com.agan.exam.model.Grade;
import com.agan.exam.model.dto.ImportGradeDto;
import com.agan.exam.model.dto.QueryGradeDto;
import com.agan.exam.model.vo.GradeVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface GradeService extends IService<Grade> {

    /**
     * 获取 vo 对象
     *
     * @param id 班级id
     * @return 班级vo
     */
    GradeVo selectVoById(Integer id);

    /**
     * 分页查询班级信息
     * @param page   分页条件
     * @param entity 模糊搜索条件
     * @return 分页信息
     */
    Map<String, Object> listPage(Page<Grade> page, QueryGradeDto entity);

    /**
     * 增加班级
     * @param entity 班级信息
     */
    int save(ImportGradeDto entity);

    /**
     * 通过专业id查询班级
     * @param majorId 专业id
     * @return 班级集合
     */
    List<Grade> getGradesByMajorId(Integer majorId);
}
