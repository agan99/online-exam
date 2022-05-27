package com.agan.exam.server;

import com.agan.exam.model.Student;
import com.agan.exam.model.dto.ChangePassDto;
import com.agan.exam.model.dto.QueryStudentDto;
import com.agan.exam.model.vo.StudentVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface StudentService  extends IService<Student> {

    /**
     * 分页查询学生
     * @param page 分页信息
     * @return 学生信息
     */
    Map<String, Object> listPage(Page<Student> page, QueryStudentDto entity);

    /**
     * 通过 ID 查询学生详细信息
     * @param id 学生 ID
     * @return 学生信息
     */
    StudentVo selectVoById(Integer id);

    /**
     * 学生登录
     * @param stuNumber   学生学号
     * @param stuPassword 学生密码
     * @return 学生信息
     */
    StudentVo login(String stuNumber, String stuPassword);

    /**
     * 通过学号查询学生信息
     * @param stuNumber 学号
     * @return 学生信息
     */
    Student selectByStuNumber(String stuNumber);

    /**
     * 学生修改密码
     * @param dto 新的密码
     */
    void updatePassword(ChangePassDto dto);
}
