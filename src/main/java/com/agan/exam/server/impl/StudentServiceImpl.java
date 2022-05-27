package com.agan.exam.server.impl;

import com.agan.exam.dao.StudentDAO;
import com.agan.exam.model.Student;
import com.agan.exam.model.dto.ChangePassDto;
import com.agan.exam.model.dto.QueryQuestionDto;
import com.agan.exam.model.dto.QueryStudentDto;
import com.agan.exam.model.vo.StudentVo;
import com.agan.exam.server.StudentService;
import com.agan.exam.util.PageUtil;
import com.agan.exam.util.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl extends ServiceImpl<StudentDAO, Student> implements StudentService {

    private final StudentDAO studentDAO;

    /**
     * 分页查询学生
     * @param page 分页信息
     * @return 学生信息
     */
    @Override
    public Map<String, Object> listPage(Page<Student> page, QueryStudentDto entity) {
        return PageUtil.toPageList(this.studentDAO.pageVo(page, entity));
    }

    /**
     * 通过 ID 查询学生详细信息
     * @param id 学生 ID
     * @return 学生信息
     */
    @Override
    public StudentVo selectVoById(Integer id) {
        return this.studentDAO.selectVoById(id);
    }

    /**
     * 学生登录
     * @param stuNumber   学生学号
     * @param stuPassword 学生密码
     * @return 学生信息
     */
    @Override
    public StudentVo login(String stuNumber, String password) {
        // 调用通过学号查询学生查询接口
        Student student = this.selectByStuNumber(stuNumber);

        // 如果学生对象为空说明不存在该用户，抛出异常信息
        if (student == null) {
            throw new ServiceException("该学号不存在");
        }

        if (!password.equals(student.getPassword())) {
            throw new ServiceException("密码错误");
        }

        return this.selectVoById(student.getId());
    }

    /**
     * 通过学号查询学生信息
     * @param stuNumber 学号
     * @return 学生信息
     */
    @Override
    public Student selectByStuNumber(String stuNumber) {
        // 使用 QueryWrapper 构造通过学号查询条件
        LambdaQueryWrapper<Student> qw = new LambdaQueryWrapper<>();
        qw.eq(Student::getStuNumber, stuNumber);
        // 通过学号查询学生信息
        return studentDAO.selectOne(qw);
    }

    /**
     * 学生修改密码
     * @param dto 新的密码
     */
    @Override
    public void updatePassword(ChangePassDto dto) {
        // 通过 ID 查询学生信息
        Student student = studentDAO.selectById(dto.getId());
        if (student == null) {
            throw new ServiceException("用户身份异常");
        }

        if (!dto.getOldPassword().equals(student.getPassword())) {
            throw new ServiceException("原密码错误");
        }

        student.setPassword(dto.getPassword());
        // 执行密码修改
        studentDAO.updateById(student);
    }
}
