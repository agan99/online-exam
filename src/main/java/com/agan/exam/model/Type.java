package com.agan.exam.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Type {

  @TableId
  private Integer id;

  /**
   * 题目类型
   */
  private String typeName;

  /**
   * 各个类型题目的分数
   */
  private String score;

  /**
   * 该类型题目说明
   */
  private String remark;
}