package com.agan.exam.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Teacher {
    @TableId
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 工号
     */
    private String workNumber;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 职位
     */
    private String job;

    /**
     * 性别
     */
    private String sex;

    /**
     * 所属学院
     */
    private Integer academyId;

    /**
     * 学院信息
     */
    @TableField(exist = false)
    @JsonIgnore
    private Academy academy;
}
