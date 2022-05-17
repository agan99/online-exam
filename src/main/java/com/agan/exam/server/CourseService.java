package com.agan.exam.server;

import com.agan.exam.model.Course;
import com.agan.exam.model.dto.QueryCourseDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface CourseService extends IService<Course> {

    /**
     * 分页查询课程信息
     * @param page   分页数据
     * @param entity 查询数据
     * @return 分页结果集
     */
    Map<String, Object> listPage(Page<Course> page, QueryCourseDto entity);

    /**
     * 查询课程 ID 集合
     * @param teacherId 教师 ID
     * @return 教师所有课程的 ID
     */
    List<Integer> listIdByTeacherId(Integer teacherId);

    /**
     * 查询一个老师所教的所有课程
     * @param teacherId 教师ID
     * @return 课程集合
     */
    List<Course> listByTeacherId(Integer teacherId);


    /**
     * 更新课程
     *
     * @param course 课程信息
     * @return boolean
     */
    boolean update(Course course);
}
