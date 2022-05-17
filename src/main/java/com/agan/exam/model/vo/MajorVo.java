package com.agan.exam.model.vo;

import com.agan.exam.base.BaseEntity;
import com.agan.exam.model.Academy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorVo extends BaseEntity {

    private Integer id;

    /**
     * 专业名称
     */
    private String major;

    /**
     * 学院id
     */
    private Integer academyId;

    /**
     * 学院实体表
     */
    private Academy academy;
}
