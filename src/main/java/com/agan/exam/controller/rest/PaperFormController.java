package com.agan.exam.controller.rest;

import com.agan.exam.base.R;
import com.agan.exam.model.PaperForm;
import com.agan.exam.server.PaperFormService;
import com.agan.exam.util.SysConsts;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/paperForm")
public class PaperFormController {

    @Resource
    private PaperFormService paperFormService;

    /**
     * 删除模版不跳转页面
     *
     * @param id 试卷模板ID
     * @return 当前试卷模板页面
     */
    @GetMapping("/delete/{id}")
    public R delPaperForm(@PathVariable Integer id) {
        // 调用试卷模板已出接口（通过ID删除）
        this.paperFormService.removeById(id);
        return R.success();
    }


    /**
     * 试卷题型模版
     *
     * @param paperForm 试卷题型
     * @return 试卷题型页面
     */
    @PostMapping("/save")
    public R addPaperForm(PaperForm paperForm) {
        // 设置模板类型
        paperForm.setType(SysConsts.PaperForm.INSERT);
        // 调用试卷模板新增接口
        this.paperFormService.save(paperForm);
        return R.successWithData(paperForm.getId());
    }
}
