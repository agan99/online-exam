package com.agan.exam.server.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.agan.exam.dao.PaperDAO;
import com.agan.exam.dao.QuestionDAO;
import com.agan.exam.dao.TeacherDAO;
import com.agan.exam.model.Paper;
import com.agan.exam.model.Question;
import com.agan.exam.model.Teacher;
import com.agan.exam.model.dto.QueryQuestionDto;
import com.agan.exam.model.dto.StuAnswerRecordDto;
import com.agan.exam.model.dto.StudentAnswerDto;
import com.agan.exam.model.vo.QuestionVo;
import com.agan.exam.server.CourseService;
import com.agan.exam.server.QuestionService;
import com.agan.exam.server.TypeService;
import com.agan.exam.util.BeanUtil;
import com.agan.exam.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionDAO, Question> implements QuestionService {

    private final QuestionDAO questionDAO;
    private final CourseService courseService;
    private final TypeService typeService;
    private final TeacherDAO teacherDAO;
    private final PaperDAO paperDAO;

    /**
     * 显示试题库并分页
     * @param page   分页信息
     * @param entity 模糊条件
     * @return 分页信息结果集
     */
    @Override
    public Map<String, Object> listPage(Page<Question> page, QueryQuestionDto entity) {
        List<Integer> courseIds = this.courseService.listIdByTeacherId(1);

        // 没有课程，直接返回一个空的数据集合
        if (CollUtil.isEmpty(courseIds)) {
            return null;
        }
        LambdaQueryWrapper<Question> qw = new LambdaQueryWrapper<>();
        qw.in(Question::getCourseId, courseIds);

        // 条件情况判断
        // 课程 ID 不为空，加入判断条件
        if (entity.getCourseId() != null) {
            qw.eq(Question::getCourseId, entity.getCourseId());
        }
        // 题型 ID 不为空，加入判断条件
        if (entity.getTypeId() != null) {
            qw.eq(Question::getTypeId, entity.getTypeId());
        }
        if (entity.getQuestionName() != null) {
            qw.like(Question::getQuestionName, entity.getQuestionName());
        }
        // 查询试题集合信息
        Page<Question> pageInfo = questionDAO.selectPage(page, qw);

        return PageUtil.toPage(pageInfo);
    }

    /**
     * 通过试卷答题记录查询问题的正确答案集合
     *
     * @param entity 答题记录数据传输对象
     * @return 题目集合
     */
    @Override
    public List<Question> listByStuAnswerRecordDto(StuAnswerRecordDto entity) {
        // 获取记录对象集合
        List<StudentAnswerDto> records = entity.getRecords();
        List<Question> result = Lists.newArrayList();
        // 循环集合
        records.forEach(record -> result.add(this.questionDAO.selectById(record.getQuestionId())));
        // 根据问题的 ID 排序
        result.sort(Comparator.comparingInt(Question::getId));
        return result;
    }

    /**
     * 通过 ID 查询试题的 VO 信息
     * @param id 试题 ID
     * @return 试题 VO 信息
     */
    @Override
    public QuestionVo selectVoById(Integer id) {
        Question question = this.questionDAO.selectById(id);
        QuestionVo result = BeanUtil.copyObject(question, QuestionVo.class);
        result.setCourse(this.courseService.getById(question.getCourseId()));
        result.setType(this.typeService.getById(question.getTypeId()));
        // 获取题目的教师信息
        int teacherId = question.getTeacherId();
        Teacher teacher = this.teacherDAO.selectById(teacherId);
        result.setTeacher(teacher);
        return result;
    }

    /**
     * 根据 paperId 和试题类型查找该类型题目集合
     * @param paperId 试卷ID
     * @param typeId  试卷类型
     * @return 问题 List 集合
     */
    @Override
    public List<Question> selectByPaperIdAndType(Integer paperId, Integer typeId) {
        // 通过 ID 查询试卷信息
        Paper paper = this.paperDAO.selectById(paperId);
        // 获取试卷的题目序号集合，Example:（1,2,3,4,5,6,7）
        String qIds = paper.getQuestionId();
        // 分割题目序号
        String[] ids = StrUtil.splitToArray(qIds, StrUtil.C_COMMA);
        List<Question> questions = Lists.newArrayList();
        // 遍历试题 ID，找出对应类型 ID 的问题并加入 Set 集合当中
        for (String id : ids) {
            // 通过题目 ID 获取问题的信息
            Question question = questionDAO.selectById(id);
            // 类型id为空或者类型id与当前题目的类型一致
            if (typeId == null || typeId.equals(question.getTypeId())) {
                questions.add(question);
            }
        }
        return questions;
    }

    /**
     * 通过题目类型 ID 和课程 ID 获取问题 List 集合
     *
     * @param typeId   题目类型ID
     * @param courseId 课程ID
     * @return 问题集合
     */
    @Override
    public List<Question> listByTypeIdAndCourseId(Integer typeId, Integer courseId) {
        // 使用 QueryWrapper 条件构造器构造 Sql 条件
        LambdaQueryWrapper<Question> qw = new LambdaQueryWrapper<>();
        qw.eq(Question::getTypeId, typeId).eq(Question::getCourseId, courseId);
        // 获取所有对应条件的问题集合
        return questionDAO.selectList(qw);
    }

}
