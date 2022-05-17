package com.agan.exam.model.dto;

import com.agan.exam.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class QueryStudentDto extends BaseEntity {

    // 查询关键字
    private String key;

    private Integer academyId;
}
