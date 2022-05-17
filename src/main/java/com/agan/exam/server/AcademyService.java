package com.agan.exam.server;

import com.agan.exam.model.Academy;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface AcademyService extends IService<Academy> {

  /**
   * 通过学院名称查询学院
   * @param academyName 学院名称
   * @return 学院信息
   */
  Academy selectByName(String academyName);

}
