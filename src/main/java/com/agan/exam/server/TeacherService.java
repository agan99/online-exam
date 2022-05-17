package com.agan.exam.server;

import com.agan.exam.model.Teacher;
import com.agan.exam.model.dto.ChangePassDto;
import com.agan.exam.model.dto.QueryTeacherDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface TeacherService extends IService<Teacher> {

    /**
     * 分页查询教师信息
     * @param page   分页信息
     * @param entity 查询实体
     * @return 分页结果集
     */
    Map<String, Object> listPage(Page<Teacher> page, QueryTeacherDto entity);

    /**
     * 教师登录
     * @param teacherId       教师ID
     * @param teacherPassword 教师密码
     * @return 教师信息
     */
    Teacher login(String teacherId, String teacherPassword);

    /**
     * 通过工号查询教师信息
     * @param workNumber 工号
     * @return 教师信息
     */
    Teacher selectByWorkNumber(String workNumber);

    /**
     * 修改密码
     * @param dto 密码信息
     */
    void updatePassword(ChangePassDto dto);
}
