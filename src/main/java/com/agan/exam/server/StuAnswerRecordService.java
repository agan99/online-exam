package com.agan.exam.server;

import com.agan.exam.model.StuAnswerRecord;
import com.agan.exam.model.dto.StuAnswerRecordDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface StuAnswerRecordService extends IService<StuAnswerRecord> {
    /**
     * 根据试卷ID查找复查试题记录
     *
     * @param paperId 试卷ID
     * @return 答题信息
     */
    List<StuAnswerRecord> selectByPaperId(Integer paperId);

    /**
     * 通过试卷ID查询学生答题记录集合传输对象
     *
     * @param paperId 试卷ID
     * @return 集合对象
     */
    List<StuAnswerRecordDto> listStuAnswerRecordDto(Integer paperId);
}
