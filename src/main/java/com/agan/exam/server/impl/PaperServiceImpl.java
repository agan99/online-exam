package com.agan.exam.server.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.agan.exam.base.PaperTemplate;
import com.agan.exam.dao.PaperDAO;
import com.agan.exam.dao.PaperFormDAO;
import com.agan.exam.dao.StuAnswerRecordDAO;
import com.agan.exam.dao.TypeDAO;
import com.agan.exam.model.*;
import com.agan.exam.model.dto.MarkInfoDto;
import com.agan.exam.model.dto.PaperChartDto;
import com.agan.exam.model.dto.QueryPaperDto;
import com.agan.exam.server.GradeService;
import com.agan.exam.server.PaperService;
import com.agan.exam.server.QuestionService;
import com.agan.exam.server.ScoreService;
import com.agan.exam.util.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaperServiceImpl extends ServiceImpl<PaperDAO, Paper> implements PaperService {

    private final TypeDAO typeDAO;
    private final PaperDAO paperDAO;
    private final ScoreService scoreService;
    private final StuAnswerRecordDAO stuAnswerRecordDAO;
    private final PaperFormDAO paperFormDAO;
    private final QuestionService questionService;
    private final GradeService gradeService;

    /**
     * 教师批改试卷
     *
     * @param stuId   学生ID
     * @param paperId 试卷ID
     * @param request request 对象.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markPaper(Integer stuId, Integer paperId, HttpServletRequest request) {
        // 通过 ID 获取试卷信息
        Paper paper = paperDAO.selectById(paperId);
        // 试卷名称
        String paperName = paper.getPaperName();
        // 通过 ID 获取试卷模板信息
        PaperForm paperForm = paperFormDAO.selectById(paper.getPaperFormId());
        // 获取模板各个题型的题目分值
        int choiceScore = NumberUtil.strToInteger(paperForm.getQChoiceScore());
        int mulChoiceScore = NumberUtil.strToInteger(paperForm.getQMulChoiceScore());
        int tofScore = NumberUtil.strToInteger(paperForm.getQTofScore());
        int fillScore = NumberUtil.strToInteger(paperForm.getQFillScore());
        double saqScore = NumberUtil.strToDouble(paperForm.getQSaqScore());
        double programScore = NumberUtil.strToDouble(paperForm.getQProgramScore());

        // 错题集
        List<String> wrongIds = new ArrayList<>();
        // 定义默认分值
        int score = 0;

        /* -------------------------- 开始评分 -------------------------- */
        // 加同步锁
        synchronized (this) {
            // 获取试卷中全部部分题型的问题信息
            List<Question> questions = questionService.selectByPaperIdAndType(paperId, null);
            // 试题集合按照类型排序+分组
            Collection<List<Question>> collection = questions.stream()
                    .sorted(Comparator.comparingInt(Question::getTypeId))
                    .collect(Collectors.groupingBy(Question::getTypeId)).values();

            for (List<Question> questionList : collection) {
                switch (questionList.get(0).getTypeId()) {
                    case (1):
                        // 单选题批改
                        MarkInfoDto choiceMark = PaperMarkUtil.mark(questionList, choiceScore, request);
                        score += choiceMark.getScore();
                        wrongIds.addAll(choiceMark.getWrongIds());
                        break;
                    case (2):
                        // 多选题批改
                        MarkInfoDto mulChoiceMark = PaperMarkUtil
                                .mulMark(questionList, mulChoiceScore, request);
                        score += mulChoiceMark.getScore();
                        wrongIds.addAll(mulChoiceMark.getWrongIds());
                        break;
                    case (3):
                        // 判断题批改
                        MarkInfoDto tofMark = PaperMarkUtil.mark(questionList, tofScore, request);
                        score += tofMark.getScore();
                        wrongIds.addAll(tofMark.getWrongIds());
                        break;
                    case (4):
                        // 填空题批改
                        MarkInfoDto fillMark = PaperMarkUtil.mark(questionList, fillScore, request);
                        score += fillMark.getScore();
                        wrongIds.addAll(fillMark.getWrongIds());
                        break;
                    case (5):
                        // 简答题批改并将答题记录存入数据库
                        MarkInfoDto essayMark = PaperMarkUtil.essayMark(questionList, saqScore, request);
                        score += essayMark.getScore();
                        wrongIds.addAll(essayMark.getWrongIds());
                        // 通过循环的方式依次将主观题的错题信息插入学生答题记录表中
                        for (StuAnswerRecord record : essayMark.getStuAnswerRecord()) {
                            // 封装学生、试卷、分数信息
                            record.setPaperId(paperId).setStuId(stuId);
                            stuAnswerRecordDAO.insert(record);
                        }
                        break;
                    default:
                        // 编程题批改
                        MarkInfoDto programMark = PaperMarkUtil.essayMark(questionList, programScore, request);
                        score += programMark.getScore();
                        wrongIds.addAll(programMark.getWrongIds());
                        break;
                }
            }
            // 组装错题集合信息
            StringBuilder builder = new StringBuilder();
            for (String id : wrongIds) {
                builder.append(id).append(',');
            }
            // 最后一个逗号去除
            String wrong = builder.toString();
            // 预备一个空错题字符串
            String wrongStr = null;
            // 如果没有错题，就直赋值空，长度大于0就说明包含错题
            if (wrong.length() > 0) {
                wrongStr = wrong.substring(0, wrong.length() - 1);
            }

            // 封装分数参数，并将分数信息插入到分数表中
            Score scoreResult = new Score(stuId, paperId, paperName, String.valueOf(score), wrongStr);
            // 此处调用插入接口
            this.scoreService.save(scoreResult);
        }
    }

        /* -------------------------- 结束评分 -------------------------- */

    /**
     * 分页查询试卷信息
     *
     * @param page   分页信息
     * @param entity 模糊搜索条件
     * @return 分页结果集
     */
    @Override
    public Map<String, Object> pagePaper(Page<Paper> page, QueryPaperDto entity) {
        LambdaQueryWrapper<Paper> qw = new LambdaQueryWrapper<>();

        // 试卷类型
        if (StrUtil.isNotBlank(entity.getPaperType())) {
            qw.eq(Paper::getPaperType, entity.getPaperType());
        }

        // 所属专业
        if (entity.getMajorId() != null) {
            qw.eq(Paper::getMajorId, entity.getMajorId());
        }

        // 所属学院
        if (entity.getAcademyId() != null) {
            qw.eq(Paper::getAcademyId, entity.getAcademyId());
        }

        // 出卷老师
        if (entity.getTeacherId() != null) {
            qw.eq(Paper::getTeacherId, entity.getTeacherId());
        }

        // 所属课程
        if (entity.getCourseId() != null) {
            qw.eq(Paper::getCourseId, entity.getCourseId());
        }

        // 试卷名称
        if (StrUtil.isNotBlank(entity.getPaperName())) {
            qw.like(Paper::getPaperName, entity.getPaperName());
        }

        // 试卷年级
        if (entity.getLevel() != null) {
            qw.eq(Paper::getLevel, entity.getLevel());
        }

        Page<Paper> pageInfo = this.paperDAO.selectPage(page, qw);
        // 班级不为空，数据也不为0
        if (entity.getGradeId() != null && pageInfo.getTotal() != 0L) {
            List<Paper> records = pageInfo.getRecords();
            // 迭代器内使用remove()，不要使用for循环
            Iterator<Paper> iterator = records.iterator();
            while (iterator.hasNext()) {
                Paper paper = iterator.next();
                List<String> gIds = StrUtil.split(paper.getGradeIds(), ',');
                if (!gIds.contains(String.valueOf(entity.getGradeId()))) {
                    iterator.remove();
                    pageInfo.setTotal(pageInfo.getTotal() - 1L);
                }
            }
        }
        return PageUtil.toPage(pageInfo);
    }

    /**
     * 删除试卷
     *
     * @param id 试卷ID
     */
    @Override
    public void deletePaperById(Integer id) {
        // 查询试卷是否存在
        Paper paper = this.paperDAO.selectById(id);
        if (paper != null) {
            // 检查试卷是否在考试时间范围内，是的话不允许被删除（模拟考可以直接删除）
            if (paper.getPaperType().equals(SysConsts.Paper.PAPER_TYPE_FORMAL)
                    // 已经开始
                    && paper.isStart()) {
                throw new ServiceException("考试已开始或已结束，无法删除！");
            } else {
                // 模拟考试可以删除
                // 删除score表中paperId为传入参数的对象
                List<Score> scores = this.scoreService.selectByPaperId(id);
                // 遍历成绩集合，并逐一删除对应试卷的成绩数据
                scores.forEach(score -> scoreService.removeById(score.getId()));

                // 删除学生与该试卷关联的答题记录， 构造查询条件
                LambdaQueryWrapper<StuAnswerRecord> ansQw = new LambdaQueryWrapper<>();
                ansQw.eq(StuAnswerRecord::getPaperId, id);
                List<StuAnswerRecord> ans = this.stuAnswerRecordDAO.selectList(ansQw);
                // 遍历删除答题记录
                ans.forEach(an -> stuAnswerRecordDAO.deleteById(an.getId()));
                // 获取试卷模板，如果只有他使用，则进行删除
                int paperFormId = paper.getPaperFormId();
                // 排除默认模板，且如果数量等于 1，说明只有本考试使用，直接删除
                if (paperFormId != 1 && this.countPaperByPaperFormId(paperFormId) == 1) {
                    this.paperFormDAO.deleteById(paperFormId);
                }
            }

            // 删除试卷
            paperDAO.deleteById(id);
        }
    }

    /**
     * 通过试卷模板 ID 查询试卷数量
     *
     * @param paperFormId 试卷模板ID
     * @return 数量
     */
    @Override
    public Long countPaperByPaperFormId(Integer paperFormId) {
        // 构造通过试卷模板查询试卷数量条件
        LambdaQueryWrapper<Paper> qw = new LambdaQueryWrapper<>();
        qw.eq(Paper::getPaperFormId, paperFormId);
        return this.paperDAO.selectCount(qw);

    }

    // -------------------------------------智能组卷---------------------------------------
    /**
     * 智能组卷主体部分
     * @param paper 试卷信息
     * @param diff 指定难度
     */
    @Override
    public void randomNewPaper(Paper paper, String diff) {
        // 试卷问题基础集合
        ArrayList<Integer> questionIds = new ArrayList<>();
        // 试卷题型基础模板
        PaperForm1 form = new PaperTemplate().PaperTemplate01();
        // 设置每种题型的数量
        Integer[] questionNumArray =  new Integer[]{form.getQChoiceNum(), form.getQMulChoiceNum(), form.getQFillNum(), form.getQSaqNum()};
        // 题目类型id {暂时}
        int[] types = new int[]{1, 2, 4, 5};

        // 遍历题型调用随机组题方法
        for (int i = 0; i < questionNumArray.length; i++) {
            this.randomQuestions(questionNumArray[i], questionIds, types[i], paper.getCourseId(), diff);
        }
        this.savePaper(paper, questionIds);
    }

    /**
     * 指定难度的随机抽题方法
     * @param typeNum     试题数量
     * @param questionIds 试题的 ID 集合
     * @param typeId      试题类型
     * @param courseId    课程 ID
     * @param diff        难度（传入null代表难度不限定）
     */
    private void randomQuestions(Integer typeNum, List<Integer> questionIds, Integer typeId, Integer courseId, String diff){
        // 获取对应题型和课程题目的 ID 集合
        List<Question> qs = questionService.listByTypeIdAndCourseId(typeId, courseId);
        // 过滤难度（diff为null或者0说明不需要过滤难度）
        if (StrUtil.isNotBlank(diff) && Integer.parseInt(diff) != 0) {
            qs = qs.stream().filter(q -> q.getDifficulty().equals(diff)).collect(Collectors.toList());
        }
        // 问题ID 集合
        List<Integer> idList = qs.stream().map(Question::getId).collect(Collectors.toList());
        // 调用随机抽题方法，添加进试卷问题集合
        questionIds.addAll(getRandomIdList(idList, typeNum));
    }

    /**
     * 从所需题型中随机抽出固定数量的题（随机组题核心代码）
     * @param ids 题库中该题型所有的题目的id集合
     * @param num 题目数量
     * @return 题目集合
     */
    private List<Integer> getRandomIdList(List<Integer> ids, Integer num) {
        // 获取随机数生成器对象
        Random random = RandomUtil.getRandom();
        List<Integer> result = new ArrayList<>();

        int index;
        for (int i = 0; i < num; i++) {
            try {
                // 把题目集合的长度 -1 作为随机因子
                int x = ids.size();
                index = x <= 1 ? 0 : random.nextInt(x - 1);
                // 按随机数取出题目
                result.add(ids.get(index));
                // 删除该题目防止题目重复
                ids.remove(index);
            } catch (Exception e) {
                // 捕捉题序集合索引溢出异常，说明题目数量不够进行随机组题
                throw new ServiceException("试题数量不足，组卷失败！[ 请增加题目或调整难度后重试 ]");
            }
        }
        return result;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 更新试卷指派班级
     *
     * @param paper 试卷信息
     */
    @Override
    public void updateGradeIds(Paper paper) {
        if (paper.getLevel() == null || StrUtil.isBlank(paper.getGradeIds())) {
            throw new ServiceException("请填写年级和班级信息");
        }
        List<Grade> grades = this.gradeService.getGradesByMajorId(paper.getMajorId());
        if (CollUtil.isEmpty(grades)) {
            throw new ServiceException("专业不存在班级，请添加班级");
        }
        List<Integer> numbers = grades.stream().map(Grade::getGradeNumber)
                .collect(Collectors.toList());
        // 获取班级id
        String[] gIds = StrUtil.splitToArray(paper.getGradeIds(), ',');
        for (String gId : gIds) {
            if (!numbers.contains(Integer.parseInt(gId))) {
                throw new ServiceException("班级" + gId + "不存在");
            }
        }
        this.paperDAO.updateById(paper);
    }

    /**
     * 根据老师Id查找已结束试卷
     * @param teacherId 教师ID
     * @return 该教师所属已完成的试卷
     */
    @Override
    public List<Paper> listDoneByTeacherId(Integer teacherId) {
        // 构造通过教师ID查询已经完成的试卷信息
        LambdaQueryWrapper<Paper> qw = new LambdaQueryWrapper<>();
        qw.eq(Paper::getPaperState, SysConsts.Paper.PAPER_STATE_END);
        if (teacherId != null) {
            qw.eq(Paper::getTeacherId, teacherId);
        }
        return paperDAO.selectList(qw);
    }

    /**
     * 检测是否有被改版机考过
     *
     * @param paperId 试卷id
     * @param level   年级
     * @param gradeId 班级表编号
     */
    /**
     * 某场考试的班级分析
     */
    @Override
    public void checkTestedByGradeId(Integer paperId, Integer level, Integer gradeId) {

        Paper paper = getById(paperId);
        if (StrUtil.isBlank(paper.getGradeIds())) {
            throw new ServiceException("试卷未指派班级");
        }
        List<String> idList = StrUtil.split(paper.getGradeIds(), ',');
        Grade grade = gradeService.getById(gradeId);
        if (!paper.getLevel().equals(level) || !idList
                .contains(String.valueOf(grade.getGradeNumber()))) {
            throw new ServiceException("该考试不属于该班级");
        }

        List<Score> scores = this.scoreService.selectByPaperIdAndGradeId(paperId, gradeId);
        if (CollUtil.isEmpty(scores)) {
            throw new ServiceException("该班级没有考试记录");
        }
    }
    /**
     * 试卷保存行为
     * @param paper 试卷
     * @param qs    问题ID集合
     */
    private void savePaper(Paper paper, List<Integer> qs) {
        StringBuilder sb = new StringBuilder();
        // 通过循环的方式组件试卷题目序号集合
        for (Integer id : qs) {
            sb.append(id).append(',');
        }
        String ids = sb.toString();
        // 去除最后一个逗号并封装题序参数
        paper.setQuestionId(ids.substring(0, ids.length() - 1));
        // 将试卷信息插入 paper 表中
        this.save(paper);
    }

}
