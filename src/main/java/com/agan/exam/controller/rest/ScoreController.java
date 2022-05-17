package com.agan.exam.controller.rest;

import com.agan.exam.base.R;
import com.agan.exam.model.Score;
import com.agan.exam.model.vo.StudentVo;
import com.agan.exam.server.ScoreService;
import com.agan.exam.util.HttpUtil;
import com.agan.exam.util.SysConsts;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    @Resource
    private ScoreService scoreService;

    /**
     * 获取学生乘机分业数据
     *
     * @return 获取学生乘机分业数据
     */
    @GetMapping("/list")
    public Map<String, Object> pageScore(Page<Score> page) {

        return this.scoreService.pageByStuId(page, 1);
    }

    @PostMapping("/grade/chart/{paperId}/{gradeId}")
    public R gradeChart(@PathVariable Integer paperId, @PathVariable Integer gradeId) {
        return R.successWithData(scoreService.averageGradeScore(paperId, gradeId));
    }
}
