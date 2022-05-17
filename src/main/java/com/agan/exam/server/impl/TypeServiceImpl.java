package com.agan.exam.server.impl;

import com.agan.exam.dao.TypeDAO;
import com.agan.exam.model.Type;
import com.agan.exam.server.TypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TypeServiceImpl extends ServiceImpl<TypeDAO, Type> implements TypeService {

}