package com.agan.exam.model.dto;

import com.agan.exam.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false)
@Data
public class QueryQuestionDto extends BaseEntity {

    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 题型id
     */
    private Integer typeId;

    /**
     * 题目名字
     */
    private String questionName;
}
