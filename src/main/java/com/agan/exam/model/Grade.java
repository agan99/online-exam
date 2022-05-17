package com.agan.exam.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Grade {

    @TableId
    private Integer id;
    /**
     * 班级年级
     */
    private Integer level;
    /**
     * 所属专业id
     */
    private Integer majorId;
    /**
     * 班级编号
     */
    private Integer gradeNumber;
}
