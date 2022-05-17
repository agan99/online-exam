package com.agan.exam.server;

import com.agan.exam.model.Score;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 分数业务接口
 *
 * @author chachae
 * @since 2020-02-06 22:53:25
 */
public interface ScoreService extends IService<Score> {
    /**
     * 获取某场考试的的某个班级的分数信息
     *
     * @param paperId 试卷id
     * @param gradeId 班级id
     * @return 分数集合
     */
    List<Score> selectByPaperIdAndGradeId(Integer paperId, Integer gradeId);
    /**
     * 通过学生ID和试卷ID查询成绩详情
     *
     * @param stuId   学生ID
     * @param paperId 试卷ID
     * @return Score：成绩详情
     */
    Score selectByStuIdAndPaperId(Integer stuId, Integer paperId);

    /**
     * 通过试卷id 查询成绩集合
     * @param paperId 试卷id
     * @return 成绩集合
     */
    List<Score> selectByPaperId(Integer paperId);

    /**
     * 通过学号查询分数分页集合
     * @param page  分页信息
     * @param stuId 学生id
     * @return 分页信息
     */
    Map<String, Object> pageByStuId(Page<Score> page, Integer stuId);

    /**
     * 统计该班级某门考试的平均分
     *
     * @param gradeId 班级ID
     * @param paperId 试卷ID
     * @return Map<String, Object>
     */
    Map<String, Object> averageGradeScore(Integer paperId, Integer gradeId);


}