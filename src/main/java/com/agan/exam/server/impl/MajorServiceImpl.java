package com.agan.exam.server.impl;

import com.agan.exam.dao.MajorDAO;
import com.agan.exam.model.Major;
import com.agan.exam.server.MajorService;
import com.agan.exam.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl extends ServiceImpl<MajorDAO, Major> implements MajorService  {

    private final MajorDAO majorDAO;

    /**
     * 分页查询专业信息
     * @param page  分页信息
     * @param major 模糊查询条件
     * @return 专业集合信息
     */
    @Override
    public Map<String, Object> listPageMajor(Page<Major> page, Major major) {
        return PageUtil.toPage(this.majorDAO.listPageMajorVo(page, major));
    }

    /**
     * 通过学院 ID 获取专业集合
     * @param academyId 学院 ID
     * @return 集合信息
     */
    @Override
    public List<Major> listByAcademyId(Integer academyId) {
        LambdaQueryWrapper<Major> qw = new LambdaQueryWrapper<>();
        qw.eq(Major::getAcademyId, academyId);
        return this.majorDAO.selectList(qw);
    }
}
