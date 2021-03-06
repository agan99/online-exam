package com.agan.exam.server.impl;

import com.agan.exam.dao.AcademyDAO;
import com.agan.exam.model.Academy;
import com.agan.exam.server.AcademyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AcademyServiceImpl extends ServiceImpl<AcademyDAO, Academy>implements AcademyService {

}
