package com.agan.exam.server.impl;

import cn.hutool.core.collection.CollUtil;
import com.agan.exam.dao.PaperDAO;
import com.agan.exam.dao.ScoreDAO;
import com.agan.exam.dao.StudentDAO;
import com.agan.exam.model.Score;
import com.agan.exam.model.dto.AnswerEditDto;
import com.agan.exam.server.ScoreService;
import com.agan.exam.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl extends ServiceImpl<ScoreDAO, Score> implements ScoreService {

    private final ScoreDAO scoreDAO;
    private final StudentDAO studentDAO;
    private final PaperDAO paperDAO;

    /**
     * 获取某场考试的的某个班级的分数信息
     *
     * @param paperId 试卷id
     * @param gradeId 班级id
     * @return 分数集合
     */
    @Override
    public List<Score> selectByPaperIdAndGradeId(Integer paperId, Integer gradeId) {
        List<Score> scores = this.selectByPaperId(paperId);
        // 过滤出该班级的成绩集合
        scores = scores.stream()
                .filter(score -> this.studentDAO.selectById(score.getStuId()).getGradeId().equals(gradeId))
                .collect(Collectors.toList());
        return scores;
    }

    /**
     * 通过学生ID和试卷ID查询成绩详情
     *
     * @param stuId   学生ID
     * @param paperId 试卷ID
     * @return Score：成绩详情
     */
    @Override
    public Score selectByStuIdAndPaperId(Integer stuId, Integer paperId) {
        // 构造学生id和试卷id查询的查询条件
        LambdaQueryWrapper<Score> qw = new LambdaQueryWrapper<>();
        qw.eq(Score::getStuId, stuId).eq(Score::getPaperId, paperId);
        // 返回查询到的数据
        return this.scoreDAO.selectOne(qw);
    }

    /**
     * 通过试卷id 查询成绩集合
     * @param paperId 试卷id
     * @return 成绩集合
     */
    @Override
    public List<Score> selectByPaperId(Integer paperId) {
        // 构造试卷id查询的查询条件
        LambdaQueryWrapper<Score> qw = new LambdaQueryWrapper<>();
        qw.eq(Score::getPaperId, paperId);
        // 返回查询到的数据
        return this.scoreDAO.selectList(qw);
    }

    /**
     * 通过学号查询分数分页集合
     *
     * @param page  分页信息
     * @param stuId 学生id
     * @return 分页信息
     */
    @Override
    public Map<String, Object> pageByStuId(Page<Score> page, Integer stuId) {
        // QueryWrapper 条件构造器构造查询 Sql
        LambdaQueryWrapper<Score> qw = new LambdaQueryWrapper<>();
        qw.eq(Score::getStuId, stuId);
        // 返回该学生的分数集合
        Page<Score> pageInfo = scoreDAO.selectPage(page, qw);
        return PageUtil.toPage(pageInfo);
    }

    /**
     * 统计该班级某门考试的平均分
     * @param paperId 试卷ID
     * @param gradeId 班级ID
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> averageGradeScore(Integer paperId, Integer gradeId) {
        Map<String, Object> resultMap = new HashMap<>();
        // 设置考试名称
        resultMap.put("title", this.paperDAO.selectById(paperId).getPaperName());
        // 根据 试卷id 获取分数集合
        List<Score> scores = selectByPaperId(paperId);
        // 如果分数集合为空，则返回空的map
        if (CollUtil.isEmpty(scores)) {
            return resultMap;
        } else {
            // 过滤出该班级的成绩集合
            scores = scores.stream().filter(
                            score -> this.studentDAO.selectById(score.getStuId()).getGradeId().equals(gradeId))
                    .collect(Collectors.toList());
            if (CollUtil.isEmpty(scores)) {
                return resultMap;
            } else {
                int[] avg = new int[5];
                // 遍历所有分数，计算出分数分布情况
                for (Score score : scores) {
                    // 取除数
                    int mdn = Integer.parseInt(score.getScore()) / 10;
                    if (mdn < 6) {
                        avg[0]++;
                    }else if(mdn < 7){
                        avg[1]++;
                    }else if(mdn < 8){
                        avg[2]++;
                    }else if(mdn < 9){
                        avg[3]++;
                    }else {
                        avg[4]++;
                    }
                }
                // "60分以下","60-70分 ","70-80分 ","80-90分 ","90分以上"
                resultMap.put("score", avg);
            }
        }
        return resultMap;
    }

    /**
     * 根据学生ID和试卷ID修改成绩
     * @param dto 修改的信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateScoreByStuIdAndPaperId(AnswerEditDto dto) {
        // 获取改门成绩的信息
        LambdaQueryWrapper<Score> qw = new LambdaQueryWrapper<>();
        qw.eq(Score::getStuId, dto.getStuId()).eq(Score::getPaperId, dto.getPaperId());
        Score res = this.scoreDAO.selectOne(qw);
        // 更新成绩
        LambdaUpdateWrapper<Score> uw = new LambdaUpdateWrapper<>();
        uw.eq(Score::getStuId, dto.getStuId()).eq(Score::getPaperId, dto.getPaperId());
        // 新的总成绩
        int score = Integer.parseInt(res.getScore()) - dto.getOldScore() + dto.getNewScore();
        // 构造条件（学生ID+试卷ID）更新 SQL
        uw.set(Score::getScore, score);
        uw.eq(Score::getStuId, dto.getStuId()).eq(Score::getPaperId, dto.getPaperId());
        this.scoreDAO.update(null, uw);
    }

}
