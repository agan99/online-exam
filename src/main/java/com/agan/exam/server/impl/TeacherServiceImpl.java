package com.agan.exam.server.impl;

import cn.hutool.core.util.StrUtil;
import com.agan.exam.dao.TeacherDAO;
import com.agan.exam.model.Teacher;
import com.agan.exam.model.dto.ChangePassDto;
import com.agan.exam.model.dto.QueryTeacherDto;
import com.agan.exam.server.TeacherService;
import com.agan.exam.util.PageUtil;
import com.agan.exam.util.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor

public class TeacherServiceImpl extends ServiceImpl<TeacherDAO, Teacher> implements TeacherService {

    private final TeacherDAO teacherDAO;

    /**
     * 分页查询教师信息
     * @param page   分页信息
     * @param entity 查询实体
     * @return 分页结果集
     */
    @Override
    public Map<String, Object> listPage(Page<Teacher> page, QueryTeacherDto entity) {
        LambdaQueryWrapper<Teacher> qw = new LambdaQueryWrapper<>();
        // 查询条件是否为空
        if (StrUtil.isNotBlank(entity.getKey())) {
            // 根据教师姓名或者工号来进行模糊查询
            qw.like(Teacher::getName, entity.getKey()).or().like(Teacher::getWorkNumber, entity.getKey());
        }
        // 接收查询结果
        Page<Teacher> pageInfo = this.teacherDAO.selectPage(page, qw);
        return PageUtil.toPage(pageInfo);
    }

    /**
     * 教师登录
     * @param teacherId       教师ID
     * @param teacherPassword 教师密码
     * @return 教师信息
     */
    @Override
    public Teacher login(String teaNumber, String password) {
        // 查询改用户名的教师信息
        Teacher teacher = this.selectByWorkNumber(teaNumber);
        // 判断对象的情况
        if (teacher == null) {
            throw new ServiceException("该职工号不存在，请重新输入");
        }
        // 比较密码是否相等
        if (!password.equals(teacher.getPassword())) {
            throw new ServiceException("密码错误");
        }
        return teacher;
    }

    /**
     * 通过工号查询教师信息
     * @param workNumber 工号
     * @return 教师信息
     */
    @Override
    public Teacher selectByWorkNumber(String workNumber) {
        // 查询改用户名的教师信息
        LambdaQueryWrapper<Teacher> qw = new LambdaQueryWrapper<>();
        qw.eq(Teacher::getWorkNumber, workNumber);
        return this.teacherDAO.selectOne(qw);
    }

    /**
     * 修改密码
     * @param dto 密码信息
     */
    @Override
    public void updatePassword(ChangePassDto dto) {
        // 查询该 ID 的教师信息
        Teacher teacher = this.getById(dto.getId());
        if (teacher == null) {
            throw new ServiceException("用户不存在");
        }

        // 验证密码
        if (!dto.getOldPassword().equals(teacher.getPassword())) {
            throw new ServiceException("原密码错误");
        }

        teacher.setPassword(dto.getPassword());
        this.updateById(teacher);
    }
}
