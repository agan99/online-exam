package com.agan.exam.controller.rest;

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

@RestController
@RequestMapping("/api/grade")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @GetMapping("/list")
    public Map<String, Object> pageList(Page<Grade> page, QueryGradeDto entity) {
        return this.gradeService.listPage(page, entity);
    }

    /**
     * 增加班级
     */
    @PostMapping("/save")
    public R saveGrades(@Valid ImportGradeDto entity) {
        // 调用新增接口
        this.gradeService.save(entity);
        return R.success();
    }

}
