package com.agan.exam.controller.rest;

import com.agan.exam.base.AjaxResponse;
import com.agan.exam.base.R;
import com.agan.exam.model.Student;
import com.agan.exam.model.dto.ChangePassDto;
import com.agan.exam.model.dto.LoginDto;
import com.agan.exam.model.dto.QueryStudentDto;
import com.agan.exam.model.vo.StudentVo;
import com.agan.exam.server.StudentService;
import com.agan.exam.util.HttpUtil;
import com.agan.exam.util.SysConsts;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

/**
 * 学生管理 api
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    /**
     * 学生登录 验证学号和密码
     * @param entity 账号密码
     * @return 主界面
     */
    @PostMapping("/login")
    public R login(@Valid LoginDto entity) {
        // 执行登录接口
        StudentVo student = studentService.login(entity.getUsername(), entity.getPassword());
        // 设置 Session 信息
        HttpSession session = HttpUtil.getSession();
        session.setAttribute(SysConsts.Session.ROLE_ID, student.getRoleId());
        session.setAttribute(SysConsts.Session.STUDENT_ID, student.getId());
        session.setAttribute(SysConsts.Session.STUDENT, student);
        // 重定向到学生主界面
        return R.successWithData(student.getId());
    }

    /**
     * 密码修改
     * @param dto 密码信息
     * @return 重定向到登录界面
     */
    @PostMapping("/update/password")
    public R updatePassword(@Valid ChangePassDto dto) {
        // 通过获取 Session 对象
        HttpSession session = HttpUtil.getSession();
        // 调用密码修改接口
        this.studentService.updatePassword(dto);
        // 移除学生 session 信息
        session.removeAttribute(SysConsts.Session.ROLE_ID);
        session.removeAttribute(SysConsts.Session.STUDENT_ID);
        session.removeAttribute(SysConsts.Session.STUDENT);
        return R.success();
    }

    /**
     * 分页返回学生信息
     * @param page 分页信息
     * @param key 查询关键字
     * @return 学生信息
     */
    @GetMapping("/listByPage")
    public AjaxResponse listPage(Page<Student> page, QueryStudentDto key){
        return AjaxResponse.success(this.studentService.listPage(page, key));
    }

    /**
     * 获取单个学生信息
     * @param id 学生id
     * @return 学生信息
     */
    @GetMapping("/{id}")
    public AjaxResponse getOneStudent(@PathVariable Integer id){
        return AjaxResponse.success(this.studentService.selectVoById(id));
    }

    /**
     * 增加学生
     * @param student 学生信息
     * @return 成功消息
     */
    @PostMapping("/save")
    public AjaxResponse saveStudent(@Valid Student student){
        return AjaxResponse.success(this.studentService.save(student));
    }

    /**
     * 删除学生
     * @param id 学生ID
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    public AjaxResponse deleteStudent(@PathVariable Integer id) {
        return AjaxResponse.success(this.studentService.removeById(id));
    }

    /**
     * 更新学生
     * @param student 学生信息
     * @return 成功信息
     */
    @PostMapping("/update")
    public AjaxResponse updateStudent(Student student) {
        return AjaxResponse.success(this.studentService.updateById(student));
    }
}
