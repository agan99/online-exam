package com.agan.exam.server.impl;

import com.agan.exam.dao.AdminDAO;
import com.agan.exam.model.Admin;
import com.agan.exam.model.dto.ChangePassDto;
import com.agan.exam.server.AdminService;

import com.agan.exam.util.RsaCipherUtil;
import com.agan.exam.util.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * 管理员业务实现
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends ServiceImpl<AdminDAO, Admin> implements AdminService {

  private final AdminDAO adminDAO;

  /**
   * 管理员登录
   * @param number   管理员账号
   * @param password 管理员密码
   * @return 管理员信息
   */
  @Override
  public Admin login(String number, String password) {
    // 调用通用账号查询信息的方法
    Admin admin = this.selectByNumber(number);
    // 判断管理员对象是否为空对象
    if (admin == null) {
      throw new ServiceException("管理员帐号不存在");
    }
    if (!password.equals(admin.getPassword())) {
      throw new ServiceException("密码错误");
    }
    // 更新登陆时间
    this.adminDAO.updateById(Admin.builder().id(admin.getId()).lastLoginTime(new Date()).build());
    return admin;
  }

  /**
   * 通过管理员账号查询管理员信息
   * @param number 管理员账号
   * @return 管理员信息
   */
  @Override
  public Admin selectByNumber(String number) {
    // 通过学号获取学生信息
    LambdaQueryWrapper<Admin> qw = new LambdaQueryWrapper<>();
    qw.eq(Admin::getNumber, number);
    return this.adminDAO.selectOne(qw);
  }

  /**
   * 修改管理员密码
   * @param dto 密码信息
   */
  @Override
  public void updatePassword(ChangePassDto dto) {
    // 获取该管理员的信息
    Admin admin = this.adminDAO.selectById(dto.getId());
    // 比较旧密码是否输入正确
    if (!dto.getOldPassword().equals(admin.getPassword())) {
      throw new ServiceException("旧密码输入错误");
    } else {
      // 旧密码输入正确，进行更新
      Admin build = Admin.builder()
              .id(dto.getId())
              .password(dto.getPassword()).build();
      this.adminDAO.updateById(build);
    }
  }

}
