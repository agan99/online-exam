package com.agan.exam.model.dto;

import com.agan.exam.base.BaseEntity;
import com.agan.exam.model.StuAnswerRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 评分信息实体类
 *
 * @author chachae
 * @since 2020/2/6 21:46
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class MarkInfoDto extends BaseEntity {

  private Integer score;

  private List<String> wrongIds;

  private List<StuAnswerRecord> stuAnswerRecord;
}
