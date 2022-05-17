package com.agan.exam.controller.rest;

import com.agan.exam.base.R;
import com.agan.exam.model.Course;
import com.agan.exam.model.dto.QueryCourseDto;
import com.agan.exam.server.CourseService;
import com.agan.exam.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    @GetMapping("/list")
    public Map<String, Object> pageCourse(Page<Course> page, QueryCourseDto entity) {
        return this.courseService.listPage(page, entity);
    }

    @GetMapping("/lists")
    public Map<String, Object>listsCourse() {
        List<Course> courses = this.courseService.list();

        // 统计集合总数
        int total = courses.size();
        return PageUtil.toPage(courses, total);
    }

    /**
     * 查询一个老师所教的所有课程
     * @param teacherId
     * @return
     */
    @GetMapping("/teacher/{teacherId}")
    public List<Course> listByTeacherId(@PathVariable Integer teacherId) {
        return this.courseService.listByTeacherId(teacherId);
    }

    /**
     * 查看课程
     *
     * @param id 课程ID
     * @return 删除成功信息
     */
    @GetMapping("/{id}")
    public Course getCourse(@PathVariable Integer id) {
        return this.courseService.getById(id);
    }

    /**
     * 删除课程
     * @param id 课程ID
     * @return 删除成功信息
     */
    @PostMapping("/delete/{id}")
    public R delCourse(@PathVariable Integer id) {
        // 调用课程删除接口
        this.courseService.removeById(id);
        return R.success();
    }

    /**
     * 添加课程
     *
     * @return 成功信息
     */
    @PostMapping("/save")
    public R newCourse(@Valid Course course) {
        this.courseService.save(course);
        return R.success();
    }

    /**
     * 更新课程
     *
     * @return 成功信息
     */
    @PostMapping("/update")
    public R updateCourse(Course course) {
        this.courseService.update(course);
        return R.success();
    }
}