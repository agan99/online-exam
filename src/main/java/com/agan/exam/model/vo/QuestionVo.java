package com.agan.exam.model.vo;

import com.agan.exam.model.Course;
import com.agan.exam.model.Question;
import com.agan.exam.model.Teacher;
import com.agan.exam.model.Type;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionVo extends Question {

  private Course course;

  private Type type;

  private Teacher teacher;
}