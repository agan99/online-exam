package com.agan.exam.controller.rest;

import com.agan.exam.base.AjaxResponse;
import com.agan.exam.server.QuestionTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/type")
public class QuestionTypeController {

    @Resource
    private QuestionTypeService questionTypeService;

    @GetMapping
    public AjaxResponse getQuestionTypeList(){
        return AjaxResponse.success(this.questionTypeService.list());
    }

}
