package com.agan.exam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 增加班级实体类
 */

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class ImportGradeDto {

  @NotNull(message = "年级{required}")
  private Integer level;

  @NotNull(message = "专业{required}")
  private Integer majorId;

  @NotBlank(message = "班级{required}")
  private String gradeNumbers;

}
