package com.agan.exam.server.impl;

import cn.hutool.core.collection.CollUtil;
import com.agan.exam.dao.QuestionDAO;
import com.agan.exam.dao.StuAnswerRecordDAO;
import com.agan.exam.dao.StudentDAO;
import com.agan.exam.model.Question;
import com.agan.exam.model.Score;
import com.agan.exam.model.StuAnswerRecord;
import com.agan.exam.model.Student;
import com.agan.exam.model.dto.StuAnswerRecordDto;
import com.agan.exam.model.dto.StudentAnswerDto;
import com.agan.exam.server.ScoreService;
import com.agan.exam.server.StuAnswerRecordService;
import com.agan.exam.util.BeanUtil;
import com.agan.exam.util.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StuAnswerRecordServiceImpl extends ServiceImpl<StuAnswerRecordDAO, StuAnswerRecord>
    implements StuAnswerRecordService {

    private final StuAnswerRecordDAO stuAnswerRecordDAO;
    private final StudentDAO studentDAO;
    private final ScoreService scoreService;
    private final QuestionDAO questionDAO;

    /**
     * 根据试卷ID查找复查试题记录
     * @param paperId 试卷ID
     * @return 答题信息
     */
    @Override
    public List<StuAnswerRecord> selectByPaperId(Integer paperId) {
        // 构造通过试卷 ID 查詢答题记录的条件
        LambdaQueryWrapper<StuAnswerRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(StuAnswerRecord::getPaperId, paperId);
        List<StuAnswerRecord> result = this.stuAnswerRecordDAO.selectList(qw);
        // 检查集合对象的情况
        if (CollUtil.isEmpty(result)) {
            throw new ServiceException("这场试卷不存待复查的主观题");
        } else {
            return result;
        }
    }

    /**
     * 通过试卷ID查询学生答题记录集合传输对象
     *
     * @param paperId 试卷ID
     * @return 集合对象
     */
    @Override
    public List<StuAnswerRecordDto> listStuAnswerRecordDto(Integer paperId) {
        // 学生答题记录数据传输集合
        List<StuAnswerRecordDto> stuAnswerRecordDtoList = Lists.newArrayList();
        List<StuAnswerRecord> stuAnswerRecords = this.selectByPaperId(paperId);

        // 存在主观题，进行排序和过滤
        Collection<List<StuAnswerRecord>> values =
                stuAnswerRecords.stream()
                        // 根据问题的 ID 排序
                        .sorted(Comparator.comparingInt(StuAnswerRecord::getQuestionId))
                        // 根据学生 ID 分组
                        .collect(Collectors.groupingBy(StuAnswerRecord::getStuId))
                        // 取出集合
                        .values();

        // 循环设置参数
        for (List<StuAnswerRecord> record : values) {
            // 取出索引位置 0 的学生 ID
            int stuId = record.get(0).getStuId();
            // 查询到该学生学生信息
            Student student = this.studentDAO.selectById(stuId);
            // 查询该考生该本考试的成绩信息
            Score score = this.scoreService.selectByStuIdAndPaperId(stuId, paperId);

            // 拷贝对象信息
            List<StudentAnswerDto> rec = BeanUtil.copyList(record, StudentAnswerDto.class);
            // 遍历查询并封装题目名称
            for (StudentAnswerDto studentAnswerDto : rec) {
                Question qs = this.questionDAO.selectById(studentAnswerDto.getQuestionId());
                studentAnswerDto.setQuestionName(qs.getQuestionName());
            }

            // 封装学生答题记录数据传输对象信息
            StuAnswerRecordDto dto = new StuAnswerRecordDto();
            // 封装传输对象参数
            dto.setStudent(student).setScore(score).setRecords(rec);
            // 加入学生答题记录数据传输集合中
            stuAnswerRecordDtoList.add(dto);
        }
        return stuAnswerRecordDtoList;
    }
}
