package com.agan.exam.controller.rest;

import com.agan.exam.base.AjaxResponse;
import com.agan.exam.base.R;
import com.agan.exam.model.Teacher;
import com.agan.exam.model.dto.ChangePassDto;
import com.agan.exam.model.dto.LoginDto;
import com.agan.exam.model.dto.QueryTeacherDto;
import com.agan.exam.server.TeacherService;
import com.agan.exam.util.HttpUtil;
import com.agan.exam.util.SysConsts;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

/**
 * 教师管理 api
 */
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Resource
    private TeacherService teacherService;

    /**
     * 教师登录
     * @param entity 账号密码
     * @return 教师主页
     */
    @PostMapping("/login")
    public R login(@Valid LoginDto entity) {
        // 执行登录
        Teacher teacher = teacherService.login(entity.getUsername(), entity.getPassword());
        // 设置 session 信息
        HttpSession session = HttpUtil.getSession();
        session.setAttribute(SysConsts.Session.ROLE_ID, teacher.getRoleId());
        session.setAttribute(SysConsts.Session.TEACHER_ID, teacher.getId());
        session.setAttribute(SysConsts.Session.TEACHER, teacher);
        // 重定向到教师主页
        return R.successWithData(teacher.getId());
    }

    /**
     * 教师账号退出
     * @return 重定向到登录页面
     */
    @GetMapping("/logout")
    public String logout() {
        HttpSession session = HttpUtil.getSession();
        // 移除session
        session.removeAttribute(SysConsts.Session.TEACHER_ID);
        session.removeAttribute(SysConsts.Session.TEACHER);
        // 重定向到登录页面
        return "redirect:/";
    }

    /**
     * 修改密码
     * @param dto 密码信息
     * @return 重定向登录页面
     */
    @PostMapping("/update/pass")
    public R updatePassword(ChangePassDto dto) {
        // 获取 session 对象
        HttpSession session = HttpUtil.getSession();
        // 调用密码修改接口
        teacherService.updatePassword(dto);
        // 移除 session 信息
        session.removeAttribute(SysConsts.Session.ROLE_ID);
        session.removeAttribute(SysConsts.Session.TEACHER_ID);
        session.removeAttribute(SysConsts.Session.TEACHER);
        return R.success();
    }

    /**
     * 分页获取教师信息
     * @param page 分页结果集
     * @param entity 查询实体
     * @return 分页信息结果集
     */
    @GetMapping("/listByPage")
    public AjaxResponse page(Page<Teacher> page, QueryTeacherDto entity) {
        return AjaxResponse.success(this.teacherService.listPage(page, entity));
    }

    /**
     * 增加教师
     * @param teacher 教师信息
     * @return 成功信息
     */
    @PostMapping("/save")
    public AjaxResponse saveTeacher(@Valid Teacher teacher) {
        return AjaxResponse.success(this.teacherService.save(teacher));
    }

    /**
     * 根据id获取教师信息
     * @param id 教师id
     * @return 教师信息
     */
    @GetMapping("/{id}")
    public AjaxResponse getOne(@PathVariable Integer id){
        return AjaxResponse.success(this.teacherService.getById(id));
    }

    /**
     * 更新教师信息
     * @param teacher 教师信息
     * @return 成功信息
     */
    @PostMapping("/update")
    public AjaxResponse updateTeacher(Teacher teacher) {
        return AjaxResponse.success(this.teacherService.updateById(teacher));
    }

    /**
     * 删除教师
     * @param id 教师ID
     * @return 回调信息
     */
    @PostMapping("/delete/{id}")
    public AjaxResponse deleteTeacher(@PathVariable Integer id) {
        return AjaxResponse.success(this.teacherService.removeById(id));
    }

}
