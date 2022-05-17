package com.agan.exam.model;

import com.agan.exam.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 学院表实体类
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class Academy extends BaseEntity {

  /**
   * 学院id
   */
  @TableId
  private Integer id;

  /**
   * 学院名称
   */
  private String name;

}
