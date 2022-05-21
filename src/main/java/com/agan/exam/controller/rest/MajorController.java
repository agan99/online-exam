package com.agan.exam.controller.rest;

import com.agan.exam.base.AjaxResponse;
import com.agan.exam.base.R;
import com.agan.exam.model.Major;
import com.agan.exam.server.MajorService;
import com.agan.exam.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 专业管理功能 api
 */
@RestController
@RequestMapping("/api/major")
public class MajorController {

    @Resource
    private MajorService majorService;

    /**
     * 分页查询专业信息
     * @param page 分页信息
     * @param major 模糊查询条件
     * @return 专业集合信息
     */
    @GetMapping("/listByPage")
    public AjaxResponse listByPageMajor(Page<Major> page, Major major){
        return AjaxResponse.success(this.majorService.listByPageMajor(page, major));
    }

    /**
     * 添加专业
     * @param major 专业信息
     * @return 成功信息
     */
    @PostMapping("/save")
    public AjaxResponse saveMajor(@Valid Major major) {
        return AjaxResponse.success(this.majorService.save(major));
    }

    /**
     * 删除专业信息
     * @param id 专业id
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    public AjaxResponse deleteMajor(@PathVariable Integer id){
        return AjaxResponse.success(this.majorService.removeById(id));
    }

    /**
     * 获取单个专业信息
     * @param id 专业id
     * @return 专业信息
     */
    @GetMapping("/{id}")
    public AjaxResponse getOneMajor(@PathVariable Integer id){
        return AjaxResponse.success(this.majorService.getById(id));
    }

    /**
     * 更新专业信息
     * @param major 专业信息
     * @return 成功信息
     */
    @ResponseBody
    @PostMapping("/update")
    public AjaxResponse updateMajor(@Valid Major major) {
        return AjaxResponse.success(this.majorService.updateById(major));
    }

    /**
     * 根据学院 ID 搜索所属的专业集合信息
     * @param academyId 学院id
     * @return 专业集合信息
     */
    @GetMapping("/academy/{academyId}")
    public AjaxResponse listMajor(@PathVariable Integer academyId){
        return AjaxResponse.success(this.majorService.listByAcademyId(academyId));
    }
}
