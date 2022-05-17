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
public class Student {
    @TableId
    private Integer id;
    /**
     * 学生姓名
     */
    private String name;
    /**
     * 学生密码
     */
    private String password;
    /**
     * 学号
     */
    private String stuNumber;
    /**
     * 性别
     */
    private String sex;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 专业id
     */
    private Integer majorId;

    /**
     * 年级
     */
    private Integer level;

    /**
     * 学院id
     */
    private Integer gradeId;;
    
}
