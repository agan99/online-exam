package com.agan.exam.server.impl;


import com.agan.exam.dao.QuestionTypeDAO;
import com.agan.exam.model.QuestionType;
import com.agan.exam.server.QuestionTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionTypeServiceImpl extends ServiceImpl<QuestionTypeDAO, QuestionType> implements QuestionTypeService {
}
