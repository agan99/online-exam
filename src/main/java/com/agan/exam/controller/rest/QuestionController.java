package com.agan.exam.controller.rest;

import com.agan.exam.base.AjaxResponse;
import com.agan.exam.base.R;
import com.agan.exam.model.Question;
import com.agan.exam.model.dto.QueryQuestionDto;
import com.agan.exam.model.vo.QuestionVo;
import com.agan.exam.server.QuestionService;
import com.agan.exam.util.HttpUtil;
import com.agan.exam.util.PageUtil;
import com.agan.exam.util.SysConsts;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 题库 api
 */
@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Resource
    private QuestionService questionService;

    /**
     *  分页显示题库
     * @param page 分页
     * @param entity 查询关键字
     * @return 题目信息
     */
    @GetMapping("/listByPage")
    public AjaxResponse listPage(Page<Question> page, QueryQuestionDto entity) {
        return  AjaxResponse.success(this.questionService.listPage(page, entity));
    }

    /**
     * 根据id查询题目信息
     * @param id id
     * @return 题目信息
     */
    @GetMapping("/{id}")
    public AjaxResponse getOne(@PathVariable Integer id) {
        return AjaxResponse.success(this.questionService.selectVoById(id));
    }

    /**
     * 更新题目信息
     * @param question 题目数据
     * @return 成功信息
     */
    @PostMapping("/update")
    public AjaxResponse update(Question question){
        return AjaxResponse.success(this.questionService.updateById(question));
    }

    /**
     * 删除试题
     * @param id id
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    public AjaxResponse delete(@PathVariable Integer id){
        return AjaxResponse.success(this.questionService.removeById(id));
    }

    /**
     * 添加试题
     * @param question 题目信息
     * @return 成功信息
     */
    @PostMapping("/save")
    public AjaxResponse add(Question question) {
        // 获取教师id
        question.setTeacherId((int)HttpUtil.getAttribute(SysConsts.Session.TEACHER_ID));
        return AjaxResponse.success(this.questionService.save(question));
    }

    /**
     * 导入试题
     * @param multipartFile MultipartFile 对象
     * @return 导入题目结果 r
     */
    @PostMapping("/import")
    public R importQuestion(@RequestParam("file") MultipartFile multipartFile) {
        // 调用试题导入接口
        this.questionService.importQuestion(multipartFile);
        return R.success();
    }
}
