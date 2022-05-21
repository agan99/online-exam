package com.agan.exam.server;


import com.agan.exam.model.Major;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


public interface MajorService extends IService<Major> {

    /**
     * 分页查询专业信息
     * @param page 分页信息
     * @param major 模糊查询条件
     * @return 专业集合信息
     */
    Map<String, Object> listByPageMajor(Page<Major> page, Major major);

    /**
     * 通过学院 ID 获取专业集合
     * @param academyId 学院 ID
     * @return 集合信息
     */
    List<Major> listByAcademyId(Integer academyId);
}
