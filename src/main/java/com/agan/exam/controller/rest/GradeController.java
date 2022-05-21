package com.agan.exam.controller.rest;

import com.agan.exam.base.AjaxResponse;
import com.agan.exam.base.R;
import com.agan.exam.model.Grade;
import com.agan.exam.model.dto.ImportGradeDto;
import com.agan.exam.model.dto.QueryGradeDto;
import com.agan.exam.server.GradeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * 班级管理 api
 */
@RestController
@RequestMapping("/api/grade")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    /**
     * 分页显示 班级信息
     * @param page 分页信息
     * @param entity 查询关键字
     * @return 班级信息
     */
    @GetMapping("/listByPage")
    public AjaxResponse pageList(Page<Grade> page, QueryGradeDto entity) {
        return AjaxResponse.success(this.gradeService.listPage(page, entity));
    }

    /**
     * 增加班级
     */
    @PostMapping("/save")
    public AjaxResponse saveGrades(@Valid ImportGradeDto entity) {
        return AjaxResponse.success(this.gradeService.save(entity));
    }

}
