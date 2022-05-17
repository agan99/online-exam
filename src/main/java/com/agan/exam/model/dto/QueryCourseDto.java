package com.agan.exam.model.dto;

import com.agan.exam.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class QueryCourseDto extends BaseEntity {

  private Integer teacherId;

  private Integer academyId;

  private String key;

}