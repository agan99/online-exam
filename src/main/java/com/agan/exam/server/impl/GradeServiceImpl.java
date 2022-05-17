package com.agan.exam.server.impl;

import cn.hutool.core.util.StrUtil;
import com.agan.exam.dao.GradeDAO;
import com.agan.exam.model.Grade;
import com.agan.exam.model.dto.ImportGradeDto;
import com.agan.exam.model.dto.QueryGradeDto;
import com.agan.exam.model.vo.GradeVo;
import com.agan.exam.server.GradeService;
import com.agan.exam.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GradeServiceImpl extends ServiceImpl<GradeDAO, Grade> implements GradeService {

    private final GradeDAO gradeDAO;

    /**
     * 获取 vo 对象
     *
     * @param id 班级id
     * @return 班级vo
     */
    @Override
    public GradeVo selectVoById(Integer id) {
        return this.gradeDAO.getVoById(id);
    }

    /**
     * 分页查询班级信息
     *
     * @param page   分页条件
     * @param entity 模糊搜索条件
     * @return 分页信息
     */
    @Override
    public Map<String, Object> listPage(Page<Grade> page, QueryGradeDto entity) {
        IPage<GradeVo> result = gradeDAO.pageVo(page, entity);
        return PageUtil.toPage(result);
    }

    /**
     * 增加班级
     *
     * @param entity 班级信息
     */
    @Override
    public void save(ImportGradeDto entity) {
        List<Grade> grades = this.listByMajorId(entity.getMajorId());
        int level = entity.getLevel();
        List<Integer> gradeNumbers = grades.stream().filter(grade -> grade.getLevel().equals(level))
                .map(Grade::getGradeNumber).collect(Collectors.toList());
        String[] numbers = StrUtil.splitToArray(entity.getGradeNumbers(), ',');
        Grade grade = new Grade();
        grade.setLevel(entity.getLevel());
        grade.setMajorId(entity.getMajorId());
        for (String number : numbers) {
            int n = Integer.parseInt(number);
            if (!gradeNumbers.contains(n)) {
                grade.setGradeNumber(n);
                this.gradeDAO.insert(grade);
            }
        }
    }

    @Override
    public List<Grade> listByMajorId(Integer majorId) {
        LambdaQueryWrapper<Grade> qw = new LambdaQueryWrapper<>();
        qw.eq(Grade::getMajorId, majorId);
        return gradeDAO.selectList(qw);
    }
}
