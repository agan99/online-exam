package com.agan.exam.controller;

import com.agan.exam.model.Paper;
import com.agan.exam.model.Question;
import com.agan.exam.model.Teacher;
import com.agan.exam.model.dto.PaperChartDto;
import com.agan.exam.model.dto.StuAnswerRecordDto;
import com.agan.exam.model.vo.GradeVo;
import com.agan.exam.server.*;
import com.agan.exam.util.HttpUtil;
import com.agan.exam.util.ServletUtil;
import com.agan.exam.util.SysConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherModuleController {

    private final QuestionSortService questionSortService;
    private final PaperService paperService;
    private final CourseService courseService;
    private final MajorService majorService;
    private final StuAnswerRecordService stuAnswerRecordService;
    private final QuestionService questionService;
    private final GradeService gradeService;

    /**
     * 教师账号退出
     * @return 重定向到登录页面
     */
    @GetMapping("/logout")
    public String logout() {
        HttpSession session = HttpUtil.getSession();
        // 移除session
        session.removeAttribute(SysConsts.Session.TEACHER_ID);
        session.removeAttribute(SysConsts.Session.TEACHER);
        // 重定向到登录页面
        return "redirect:/";
    }

    /**
     * 教师修改密码页面
     * @return 页面
     */
    @GetMapping("/update/pass")
    public String updatePass() {
        return ServletUtil.isAjax()
                ? "/teacher/self/update-pass#updateTable"
                : "/teacher/self/update-pass";
    }

    /**
     * 教师个人主页
     * @return 教师主页
     */
    @GetMapping("/index")
    public String index() {
        return "/teacher/main/index";
    }

    /**
     * 展示该教师所教课程
     * @return 课程信息
     */
    @GetMapping("/course")
    public String courseList() {
        return ServletUtil.isAjax() ? "/teacher/course/list#courseTable" : "/teacher/course/list";
    }

    /**
     * 试题页面
     * @return 试题集合页面
     */
    @GetMapping("/question")
    public String list() {
        return ServletUtil.isAjax() ? "/teacher/question/list#questionTable" : "/teacher/question/list";
    }

    /**
     * 试卷管理页面
     * @return 试卷管理页面
     */
    @GetMapping("/paper")
    public String pagePaper() {
        return ServletUtil.isAjax() ? "/teacher/paper/list#paperTable" : "/teacher/paper/list";
    }

    /**
     * 查询试卷的详细信息
     * @param id 试卷的ID
     * @param mv ModelAndView 对象
     * @return 此 ID 的试卷详细信息
     */
    @GetMapping("/paper/show/{id}")
    public ModelAndView show(@PathVariable Integer id, ModelAndView mv) {
        String[] names = {"qChoiceList", "qMulChoiceList", "qTofList", "qFillList", "qSaqList"};
        // 根据 试卷ID 获取试卷的详细信息
        Paper paper = paperService.getById(id);
        if (paper == null) {
            return mv;
        }
        // 设置基础 model 信息
        mv.addObject("paper", paper);
        mv.addObject("course", courseService.getById(paper.getCourseId()));
        mv.addObject("major", majorService.getById(paper.getMajorId()));
        // 获取五种题型的题目信息列表
        for (int i = 0; i < 5 ; i++) {
            List<Question> questions = questionService.selectByPaperIdAndType(id, i+1);
            mv.addObject(names[i], questions);
        }
        if (ServletUtil.isAjax()) {
            mv.setViewName("/teacher/paper/show#paperShowTable");
        } else {
            mv.setViewName("/teacher/paper/show");
        }
        return mv;
    }

    /**
     * 智能组卷页面
     * @param id 试卷模板 ID
     * @param mv ModelAndView 对象
     * @return 组卷页面
     */
    @GetMapping("/paper/save/{id}")
    public ModelAndView add(@PathVariable Integer id, ModelAndView mv) {
        // 封装 model 参数，试卷模板id
        mv.addObject("paperFormId", id);
        if (ServletUtil.isAjax()) {
            mv.setViewName("/teacher/paper/save-paper#savePaperTable");
        } else {
            mv.setViewName("/teacher/paper/save-paper");
        }
        return mv;
    }

    /**
     * 试卷复查
     * @param mv ModelAndView 对象
     * @return 试卷集合
     */
    @GetMapping("/reviewPaper")
    public ModelAndView reviewPaper(ModelAndView mv) {
        // 获取教师id
        int id = (int) HttpUtil.getAttribute(SysConsts.Session.TEACHER_ID);
        // 返回 Model 对象
        mv.addObject("paperList", paperService.listDoneByTeacherId(id));
        if (ServletUtil.isAjax()) {
            mv.setViewName("/teacher/review/list#reviewTable");
        } else {
            mv.setViewName("/teacher/review/list");
        }
        return mv;
    }

    /**
     * 复查某场考试的试卷
     * @param paperId 试卷ID
     * @param mv      ModelAndView 对象
     * @return 待复查试卷信息
     */
    @GetMapping("/reviewRes")
    public ModelAndView reviewPaper(Integer paperId, ModelAndView mv) {
        // 检测复查试卷是否存在
        mv.setViewName("redirect:/teacher/reviewPaper");
        Paper paper = this.paperService.getById(paperId);
        if (paper == null) {
            return mv;
        }
        // 答题记录传输对象 List 集合
        List<StuAnswerRecordDto> records = this.stuAnswerRecordService.listStuAnswerRecordDto(paperId);
        mv.addObject("stuAnswer", records);
        mv.addObject("paper", paper);
        mv.addObject("questionList", this.questionService.listByStuAnswerRecordDto(records.get(0)));
        if (ServletUtil.isAjax()) {
            mv.setViewName("/teacher/review/record-list#reviewListTable");
        } else {
            mv.setViewName("/teacher/review/record-list");
        }
        return mv;
    }

    @GetMapping("/chart")
    public ModelAndView chartPaper(ModelAndView mv) {
        // 获取教师id
        Teacher teacher = (Teacher) HttpUtil.getAttribute(SysConsts.Session.TEACHER);
        // 返回 Model 对象
        mv.addObject("paperList", paperService.listDoneByTeacherId(teacher.getId()));
        mv.addObject("majorList", majorService.listByAcademyId(teacher.getAcademyId()));
        if (ServletUtil.isAjax()) {
            mv.setViewName("/teacher/chart/list#chartTable");
        } else {
            mv.setViewName("/teacher/chart/list");
        }
        return mv;
    }
    /**
     * 某场考试的班级分析
     */
    @GetMapping("/chartRes")
    public ModelAndView chartPaper(@Valid PaperChartDto entity, ModelAndView mv) {
        // 检查试卷时候符合
        this.paperService
                .checkTestedByGradeId(entity.getPaperId(), entity.getLevel(), entity.getGradeId());
        Paper paper = this.paperService.getById(entity.getPaperId());
        Integer gradeId = entity.getGradeId();
        GradeVo gradeVo = gradeService.selectVoById(entity.getGradeId());
        mv.addObject("paper", paper);
        mv.addObject("gradeId", gradeId);
        mv.addObject("grade", gradeVo);
        if (ServletUtil.isAjax()) {
            mv.setViewName("/teacher/chart/chart-list#chartListTable");
        } else {
            mv.setViewName("/teacher/chart/chart-list");
        }
        return mv;
    }
}
