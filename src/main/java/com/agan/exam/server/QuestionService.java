package com.agan.exam.server;

import com.agan.exam.model.Question;
import com.agan.exam.model.dto.QueryQuestionDto;
import com.agan.exam.model.dto.StuAnswerRecordDto;
import com.agan.exam.model.vo.QuestionVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface QuestionService  extends IService<Question> {

    /**
     * 查询题库
     * @param page   分页信息
     * @param entity 模糊条件
     * @return 返回题库列表
     */
    Map<String, Object> listPage(Page<Question> page, QueryQuestionDto entity);

    /**
     * 通过试卷答题记录查询问题的正确答案集合
     * @param entity 答题记录数据传输对象
     * @return 题目集合
     */
    List<Question> listByStuAnswerRecordDto(StuAnswerRecordDto entity);

    /**
     * 通过 ID 查询试题的 VO 信息
     * @param id 试题 ID
     * @return 试题 VO 信息
     */
    QuestionVo selectVoById(Integer id);


    /**
     * 根据paperId和试题类型查找该类型题目集合
     * @param paperId 试卷ID
     * @param typeId  试卷类型
     * @return 问题 List 集合
     */
    List<Question> selectByPaperIdAndType(Integer paperId, Integer typeId);

    /**
     * 通过题目类型 ID 和课程 ID 获取问题 List 集合
     * @param typeId   题目类型ID
     * @param courseId 课程ID
     * @return 问题集合
     */
    List<Question> listByTypeIdAndCourseId(Integer typeId, Integer courseId);

    /**
     * 导入题目
     * @param multipartFile multipartFile 对象
     */
    void importQuestion(MultipartFile multipartFile);

    /**
     * 通过题目名称查询题目列表
     * @param questionName 题目名称
     * @param courseId     课程ID
     * @param typeId       题目类型ID
     * @return 题目列表
     */
    List<Question> listByQuestionNameAndCourseIdAndTypeId(
            String questionName, Integer courseId, Integer typeId);

}
