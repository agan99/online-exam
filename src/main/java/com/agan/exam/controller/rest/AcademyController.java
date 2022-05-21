package com.agan.exam.controller.rest;

import com.agan.exam.base.AjaxResponse;
import com.agan.exam.model.Academy;
import com.agan.exam.server.AcademyService;
import com.agan.exam.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 学院管理 api
 */
@RestController
@RequestMapping("/api/academy")
public class AcademyController {

    @Resource
    private AcademyService academyService;

    /**
     * Rest 获取学院集合
     *
     * @return 学院集合
     */
    @GetMapping
    public AjaxResponse list() {
        return AjaxResponse.success(this.academyService.list());
    }

    /**
     * 分页获取学院列表
     * @param page 分页
     * @return 学院列表
     */
    @GetMapping("/listByPage")
    public AjaxResponse ListPageAcademy(Page<Academy> page) {
        return AjaxResponse.success(PageUtil.toPageList(this.academyService.page(page)));
    }

    /**
     * 获取单个学院id
     * @param id id
     * @return 返回学院信息
     */
    @GetMapping("/{id}")
    public AjaxResponse getByAcademy(@PathVariable Integer id){
        return AjaxResponse.success(this.academyService.getById(id));
    }

    /**
     * 新增学院信息
     * @return 返回成功信息
     */
    @PostMapping("/save")
    public AjaxResponse saveAcademy(@Valid Academy academy){
        return AjaxResponse.success(this.academyService.save(academy));
    }

    /**
     * 更新学院信息
     * @param academy 学院信息
     * @return 返回成功信息
     */
    @PostMapping("/update")
    public AjaxResponse updateAcademy(@Valid Academy academy){
        System.out.println(academy.toString());
        return AjaxResponse.success(this.academyService.updateById(academy));
    }

    /**
     * 删除学院
     * @param id 学院id
     * @return 返回成功信息
     */
    @PostMapping("/delete/{id}")
    public AjaxResponse deleteAcademy(@PathVariable Integer id){
        return AjaxResponse.success(this.academyService.removeById(id));
    }
}
