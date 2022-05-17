package com.agan.exam.controller.rest;

import com.agan.exam.base.R;
import com.agan.exam.model.Admin;
import com.agan.exam.model.dto.ChangePassDto;
import com.agan.exam.model.dto.LoginDto;
import com.agan.exam.server.AdminService;
import com.agan.exam.util.HttpUtil;
import com.agan.exam.util.SysConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 管理员登录接口
     * @param entity 账号密码
     * @return 管理员主页
     */
    @PostMapping("/login")
    public R login(@Valid LoginDto entity) {
        // 查询数据库，比对账户是否存在
        Admin admin = this.adminService.login(entity.getUsername(), entity.getPassword());

        // 如果账户存在，将用户信息保存到 session 中
        HttpSession session = HttpUtil.getSession();
        session.setAttribute(SysConsts.Session.ADMIN, admin);
        session.setAttribute(SysConsts.Session.ROLE_ID, admin.getRoleId());
        // 重定向到管理员主页
        return R.successWithData(admin.getId());
    }

    /**
     * 提交修改密码信息
     * @param dto 新旧密码信息
     * @return 回调信息
     */
    @PostMapping("/update/pass")
    public R updatePass(@Valid ChangePassDto dto) {
        // 调用密码修改接口
        this.adminService.updatePassword(dto);
        // 获取 session 对象后，移除 session 信息
        HttpSession session = HttpUtil.getSession();
        session.removeAttribute(SysConsts.Session.TEACHER_ID);
        session.removeAttribute(SysConsts.Session.ROLE_ID);
        session.removeAttribute(SysConsts.Session.TEACHER);
        // 重定向登录页面
        return R.success();
    }
}
