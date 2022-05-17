package com.agan.exam.controller.rest;

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

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Resource
    private QuestionService questionService;

    /**
     *  显示题库
     * @param page
     * @param entity
     * @return
     */
    @GetMapping("/list")
    public Map<String, Object> listPage(Page<Question> page, QueryQuestionDto entity) {
        return  this.questionService.listPage(page, entity);
    }

    @GetMapping("/{id}")
    public QuestionVo getOne(@PathVariable Integer id) {
        return this.questionService.selectVoById(id);
    }

    /**
     * 添加试题
     * @param question 题目信息
     * @return 成功信息
     */
    @PostMapping("/save")
    public R add(Question question) {
        // 获取教师id
        question.setTeacherId((int)HttpUtil.getAttribute(SysConsts.Session.TEACHER_ID));
        // 调用试题新增接口
        this.questionService.save(question);
        return R.success();
    }

    /**
     * 删除试题
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public R delete(@PathVariable Integer id){
        // 通过 id 移除试题
        this.questionService.removeById(id);
        return R.success();
    }

    /**
     * 修改试题
     * @param question
     * @return
     */
    @PostMapping("/update")
    public R update(Question question){
        // 修改试题
        this.questionService.updateById(question);
        return R.success();
    }

}
