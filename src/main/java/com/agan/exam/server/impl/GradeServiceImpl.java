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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GradeServiceImpl extends ServiceImpl<GradeDAO, Grade> implements GradeService {

    private final GradeDAO gradeDAO;

    /**
     * 获取 vo 对象
     * @param id 班级id
     * @return 班级vo
     */
    @Override
    public GradeVo selectVoById(Integer id) {
        return this.gradeDAO.getVoById(id);
    }

    /**
     * 分页查询班级信息
     * @param page   分页条件
     * @param entity 模糊搜索条件
     * @return 分页信息
     */
    @Override
    public Map<String, Object> listPage(Page<Grade> page, QueryGradeDto entity) {
        return PageUtil.toPageList(gradeDAO.pageVo(page, entity));
    }

    /**
     * 增加班级
     * @param entity 班级信息
     */
    @Override
    public int save(ImportGradeDto entity) {
        List<Grade> grades = this.getGradesByMajorId(entity.getMajorId());
        // 过滤掉其他年级，只留下本年级的班级编号
        List<Integer> gradeNumbers = grades.stream().filter(grade -> grade.getLevel().equals(entity.getLevel()))
                .map(Grade::getGradeNumber).collect(Collectors.toList());
        // 切割前端班级集合(1,2,3,4)
        String[] numbers = StrUtil.splitToArray(entity.getGradeNumbers(), ',');
        Grade grade = new Grade();
        grade.setLevel(entity.getLevel());
        grade.setMajorId(entity.getMajorId());
        for (String number : numbers) {
            int n = Integer.parseInt(number);
            if (!gradeNumbers.contains(n)) {
                grade.setId(null);
                grade.setGradeNumber(n);
                this.gradeDAO.insert(grade);
            }
        }
        return 1;
    }

    /**
     * 通过专业id查询班级集合
     * @param majorId 专业id
     * @return 班级集合
     */
    @Override
    public List<Grade> getGradesByMajorId(Integer majorId) {
        LambdaQueryWrapper<Grade> qw = new LambdaQueryWrapper<>();
        return gradeDAO.selectList(qw.eq(Grade::getMajorId, majorId));
    }
}
