package com.agan.exam.model.dto;

import com.agan.exam.base.BaseEntity;
import com.agan.exam.model.Score;
import com.agan.exam.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 学生主观题答题记录数据传输bean
 *
 * @author chachae
 * @since 2020/2/23 11:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class StuAnswerRecordDto extends BaseEntity {

  private Student student;
  private Score score;
  private List<StudentAnswerDto> records;
}
