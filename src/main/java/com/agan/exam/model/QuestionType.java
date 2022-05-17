package com.agan.exam.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionType {

        @TableId
        private Integer id;

        /**
         * 题目类型
         */
        private String typeName;

        /**
         * 该类型题目的分数
         */
        private String score;

}
