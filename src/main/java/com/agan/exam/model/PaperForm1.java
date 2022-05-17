package com.agan.exam.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 试卷模板信息实体
 */
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaperForm1 {

    @TableId
    private Integer id;

    /**
     * 单选题数目
     */
    private Integer qChoiceNum;

    /**
     * 单选题分数
     */
    private Integer qChoiceScore;

    /**
     * 多选题数目
     */
    private Integer qMulChoiceNum;

    /**
     * 多选题分数
     */
    private Integer qMulChoiceScore;

    /**
     * 填空题数目
     */
    private Integer qFillNum;

    /**
     * 填空题分数
     */
    private Integer qFillScore;

    /**
     * 简答题数目
     */
    private Integer qSaqNum;

    /**
     * 简答题分数
     */
    private Integer qSaqScore;
}

