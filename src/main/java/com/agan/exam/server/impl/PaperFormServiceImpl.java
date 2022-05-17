package com.agan.exam.server.impl;

import com.agan.exam.dao.PaperFormDAO;
import com.agan.exam.model.PaperForm;
import com.agan.exam.server.PaperFormService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaperFormServiceImpl extends ServiceImpl<PaperFormDAO, PaperForm> implements PaperFormService {

}
