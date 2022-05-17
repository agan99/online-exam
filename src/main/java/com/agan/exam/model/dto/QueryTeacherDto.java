package com.agan.exam.model.dto;

import com.agan.exam.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class QueryTeacherDto extends BaseEntity {

  private String key;

  private Integer academyId;

}
