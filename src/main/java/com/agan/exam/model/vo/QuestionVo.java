package com.agan.exam.model.vo;

import com.agan.exam.model.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionVo extends Question {

  private Course course;

  private QuestionType questionType;

  private Teacher teacher;
}