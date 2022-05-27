package com.agan.exam.controller.rest;

import com.agan.exam.base.AjaxResponse;
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

/**
 * 课程管理 api
 */
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    /**
     * 分页显示课程信息
     * @param page 分页信息
     * @param entity 查询关键字
     * @return 课程信息
     */
    @GetMapping("/listByPage")
    public AjaxResponse pageCourse(Page<Course> page, QueryCourseDto entity) {
        return AjaxResponse.success(courseService.listPage(page, entity));
    }

    /**
     * 根据id查看课程信息
     * @param id 课程ID
     * @return 课程信息
     */
    @GetMapping("/{id}")
    public AjaxResponse getCourse(@PathVariable Integer id) {
        return AjaxResponse.success(this.courseService.getById(id));
    }

    /**
     * 显示所有课程
     * @return 课程信息
     */
    @GetMapping("/lists")
    public AjaxResponse listCourse() {
        List<Course> courses = this.courseService.list();
        return AjaxResponse.success(PageUtil.toPageList(courses, courses.size()));
    }

    /**
     * 添加课程
     * @return 成功信息
     */
    @PostMapping("/save")
    public AjaxResponse saveCourse(@Valid Course course) {
        course.setTeacherIds("1");
        return AjaxResponse.success(this.courseService.save(course));
    }

    /**
     * 删除课程
     * @param id 课程ID
     * @return 删除成功信息
     */
    @PostMapping("/delete/{id}")
    public AjaxResponse deleteCourse(@PathVariable Integer id) {
        return AjaxResponse.success(this.courseService.removeById(id));
    }

    /**
     * 更新课程
     * @return 成功信息
     */
    @PostMapping("/update")
    public AjaxResponse updateCourse(Course course) {
        return AjaxResponse.success(this.courseService.update(course));
    }

    /**
     * 根据 教师id 查询所教课程信息
     * @param teacherId 教师id
     * @return 课程信息
     */
    @GetMapping("/teacher/{teacherId}")
    public AjaxResponse listByTeacherId(@PathVariable Integer teacherId) {
        return AjaxResponse.success(this.courseService.listByTeacherId(teacherId));
    }
}