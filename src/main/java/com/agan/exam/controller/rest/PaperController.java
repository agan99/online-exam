package com.agan.exam.controller.rest;

import com.agan.exam.base.AjaxResponse;
import com.agan.exam.base.R;
import com.agan.exam.model.Paper;
import com.agan.exam.model.Teacher;
import com.agan.exam.model.dto.QueryPaperDto;
import com.agan.exam.server.PaperService;
import com.agan.exam.util.HttpUtil;
import com.agan.exam.util.SysConsts;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/paper")
public class PaperController {

    private final PaperService paperService;

    /**
     * 分页查询试卷列表
     * @param page 分页信息
     * @param entity 查询关键字
     * @return 分页返回试卷列表
     */
    @GetMapping("/listByPage")
    public AjaxResponse pagePaper(Page<Paper> page, QueryPaperDto entity) {
        return AjaxResponse.success(this.paperService.pagePaper(page, entity));
    }

    /**
     * 获取单张试卷信息
     * @param id 试卷id
     * @return 试卷信息
     */
    @GetMapping("/{id}")
    public AjaxResponse getOne(@PathVariable Integer id) {
        return AjaxResponse.success(this.paperService.getById(id));
    }

    /**
     * 级联删除试卷、分数、答案记录
     * @param id 试卷ID
     * @return 试卷页面
     */
    @PostMapping("/delete/{id}")
    public AjaxResponse delPaper(@PathVariable Integer id) {
        return AjaxResponse.success(paperService.deletePaperById(id));
    }

    /**
     * 指派班级
     * @param paper 试卷信息
     * @return 成功提醒
     */
    @PostMapping("/update/gradeIds")
    public AjaxResponse updateGradeIds(Paper paper) {
        return AjaxResponse.success(this.paperService.updateGradeIds(paper));
    }

    /**
     * 智能组卷
     * @param paper 试卷信息
     * @param paperFormId 试卷模板ID
     * @param difficulty 难度
     * @return 组卷页面
     */
    @PostMapping("/save/random")
    public R add(@Valid Paper paper, Integer paperFormId, String difficulty) {
        // 设置试卷模板 ID
        paper.setPaperFormId(paperFormId);
        // 获取教师 ID
        Teacher teacher = (Teacher) HttpUtil.getAttribute(SysConsts.Session.TEACHER);
        // 设置出卷老师
        paper.setTeacherId(teacher.getId());
        // 设置试卷学院
        paper.setAcademyId(teacher.getAcademyId());
        // 调用根据难度来智能组卷接口
        paperService.randomNewPaper(paper, difficulty);
        return R.successWithData(paper.getId());
    }
}
