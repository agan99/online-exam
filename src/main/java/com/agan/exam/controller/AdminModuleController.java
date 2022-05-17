package com.agan.exam.controller;

import com.agan.exam.util.HttpUtil;
import com.agan.exam.util.ServletUtil;
import com.agan.exam.util.SysConsts;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminModuleController {

    @GetMapping("/index")
    public String index(){
        return "/admin/main/index";
    }

    /**
     * 管理员主界面
     * @return 管理员主界面
     */
    @GetMapping("/home")
    public String home(){
        return ServletUtil.isAjax()? "/admin/self/home#homeTable" : "/admin/self/home";
    }

    /**
     * 教师列表
     * @return 教师管理界面
     */
    @GetMapping("/teacher")
    public String teacherList(){
        return ServletUtil.isAjax() ? "/admin/teacher/list#teacherTable" : "/admin/teacher/list";
    }

    /**
     * 学院列表
     * @return 学院管理界面
     */
    @GetMapping("/academy")
    public String listAcademy(){
        return ServletUtil.isAjax() ? "/admin/academy/list#academyTable" : "/admin/academy/list";
    }

    /**
     * 专业列表
     * @return 专业管理界面
     */
    @GetMapping("/major")
    public String listMajor(){
        return ServletUtil.isAjax() ? "/admin/major/list#majorTable" : "/admin/major/list";
    }

    /**
     * 专业列表
     * @return 专业管理界面
     */
    @GetMapping("/student")
    public String listStudent(){
        return ServletUtil.isAjax() ? "/admin/student/list#studentTable" : "/admin/student/list";
    }

    /**
     * 班级主界面
     * @return 管班级页面
     */
    @GetMapping("/grade")
    public String grade() {
        return ServletUtil.isAjax() ? "/admin/grade/list#gradeTable" : "/admin/grade/list";
    }
    /**
     * 试卷主界面
     * @return 试卷页面
     */
    @GetMapping("/paper")
    public String paper() {
        return ServletUtil.isAjax() ? "/admin/paper/list#paperTable" : "/admin/paper/list";
    }

    /**
     * 展示所教课程
     *
     * @return 课程信息
     */
    @GetMapping("/course")
    public String courseList() {
        return ServletUtil.isAjax() ? "/admin/course/list#courseTable" : "/admin/course/list";
    }

    /**
     * 管理员退出
     * @return 管理员登录页面
     */
    @GetMapping("/logout")
    public String logout() {
        // 获取session 对象
        HttpSession session = HttpUtil.getSession();
        // 删除当前用户的session
        session.removeAttribute(SysConsts.Session.ADMIN);
        session.removeAttribute(SysConsts.Session.ROLE_ID);
        return "redirect:/";
    }

    /**
     * 修改密码
     * @return 修改密码界面
     */
    @GetMapping("/update/pass")
    public String toChangPass() {
        return ServletUtil.isAjax() ? "/admin/self/update-pass#updateTable" : "/admin/self/update-pass";
    }
}
