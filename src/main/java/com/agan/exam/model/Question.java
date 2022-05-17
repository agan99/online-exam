package com.agan.exam.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 问题实体
 */
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Question {

    @TableId
    private Integer id;

    /**
     * 题目名称
     */
    private String questionName;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 题目类型Id
     */
    private Integer typeId;

    /**
     * 课程Id
     */
    private Integer courseId;

    /**
     * 题目难度，容易、中等、困难
     */
    private String difficulty;

    /**
     * 题目解析
     */
    private String remark;
    /**
     * 教师Id
     */
    private Integer teacherId;

    /**
     * 题型名称
     */
    @JsonIgnore
    @TableField(exist = false)
    private String typeName;

}
