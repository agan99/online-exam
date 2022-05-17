package com.agan.exam.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class QueryPaperDto {

    // 开始结束情况 1：开始 0：结束
    private Integer state;

    private String paperType;

    private Integer majorId;

    private Integer teacherId;

    private Integer courseId;

    private String paperName;

    private Integer academyId;

    private Integer level;

    private Integer gradeId;

}