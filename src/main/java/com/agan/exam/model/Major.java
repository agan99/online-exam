package com.agan.exam.model;

import com.agan.exam.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Major extends BaseEntity {

    @TableId
    private Integer id;

    /**
     * 专业名称
     */
    private String major;

    /**
     * 学院id
     */
    private Integer academyId;
}
