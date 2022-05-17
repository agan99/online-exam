package com.agan.exam.controller.rest;

import com.agan.exam.base.R;
import com.agan.exam.model.Academy;
import com.agan.exam.server.AcademyService;
import com.agan.exam.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/academy")
public class AcademyController {

    @Resource
    private AcademyService academyService;

    /**
     * 获得学院集合
     * @return 学院集合
     */
    @GetMapping()
    public List<Academy> list(){
        return this.academyService.list();
    }

    /**
     * 分页获取学院列表
     * @param page 分页
     * @return 学院列表
     */
    @GetMapping("/list")
    public Map<String, Object> ListPageAcademy(Page<Academy> page) {
        return PageUtil.toPage(this.academyService.page(page));
    }

    /**
     * 获取单个学院id
     * @param id id
     * @return 返回学院信息
     */
    @GetMapping("/{id}")
    public Academy getOneAcademy(@PathVariable Integer id){
        return this.academyService.getById(id);
    }

    /**
     * 新增学院信息
     * @return 返回成功信息
     */
    @PostMapping ("/save")
    public R saveAcademy(@Valid Academy academy){
        this.academyService.save(academy);
        return R.success();
    }

    /**
     * 删除学院
     * @param id 学院id
     * @return 返回成功信息
     */
    @PostMapping("/delete/{id}")
    public R deleteAcademy(@PathVariable Integer id){
        this.academyService.removeById(id);
        return R.success();
    }

    /**
     * 更新学院信息
     * @param academy 学院信息
     * @return 返回成功信息
     */
    @PostMapping("/update")
    public R updateAcademy(@Valid Academy academy){
        this.academyService.updateById(academy);
        return R.success();
    }

}
