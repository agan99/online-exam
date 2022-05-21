package com.agan.exam.server.impl;

import cn.hutool.core.util.StrUtil;
import com.agan.exam.dao.AcademyDAO;
import com.agan.exam.dao.CourseDAO;
import com.agan.exam.dao.TeacherDAO;
import com.agan.exam.model.Course;
import com.agan.exam.model.Teacher;
import com.agan.exam.model.dto.QueryCourseDto;
import com.agan.exam.server.CourseService;
import com.agan.exam.util.PageUtil;
import com.agan.exam.util.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends ServiceImpl<CourseDAO, Course> implements CourseService {

    private final CourseDAO courseDAO;
    private final TeacherDAO teacherDAO;
    private final AcademyDAO academyDAO;

    /**
     * 分页查询课程信息
     * @param page   分页数据
     * @param entity 查询数据
     * @return 分页结果集
     */
    @Override
    public Map<String, Object> listPage(Page<Course> page, QueryCourseDto entity) {
        if (entity.getTeacherId() != null) {
            List<Course> courses = this.courseDAO.listByTeacherId(entity.getTeacherId());
            // 过滤所属学院信息
            if (entity.getAcademyId() != null) {
                courses = courses.stream()
                        .filter(course -> course.getAcademyId().equals(entity.getAcademyId()))
                        .collect(Collectors.toList());
            }
            // 课程名称模糊搜索
            if (StrUtil.isNotBlank(entity.getKey())) {
                courses = courses.stream()
                        .filter(course -> course.getCourseName().contains(entity.getKey()))
                        .collect(Collectors.toList());
            }
            // 统计集合总数
            int total = courses.size();
            // List 分页
            courses = PageUtil.toPage(page.getCurrent(), page.getSize(), courses);
            return PageUtil.toPageList(courses, total);
        } else {
            LambdaQueryWrapper<Course> qw = new LambdaQueryWrapper<>();
            if (entity.getAcademyId() != null) {
                qw.eq(Course::getAcademyId, entity.getAcademyId());
            }
            if (StrUtil.isNotBlank(entity.getKey())) {
                qw.like(Course::getCourseName, entity.getKey());
            }
            Page<Course> pageInfo = this.courseDAO.selectPage(page, qw);
            return PageUtil.toPageList(pageInfo);
        }
    }


    @Override
    public List<Integer> listIdByTeacherId(Integer teacherId) {
        List<Course> courses = this.courseDAO.listByTeacherId(teacherId);
        // 将课程集合转为 ID 集合
        return Lists.transform(courses, Course::getId);
    }

    /**
     * 查询一个老师所教的所有课程
     * @param teacherId 教师ID
     * @return 课程集合
     */
    @Override
    public List<Course> listByTeacherId(Integer teacherId) {
        List<Course> courses = this.courseDAO.listByTeacherId(teacherId);
        // 为每一门课程设置教师信息
        for (Course course : courses) {
            // 切割教师id列表
            String tIds = course.getTeacherIds();
            String[] strIds = StrUtil.splitToArray(tIds, ',');
            StringBuilder names = new StringBuilder();
            StringBuilder workNumbers = new StringBuilder();
            for (String id : strIds) {
                Teacher res = this.teacherDAO.selectById(id);
                names.append(res.getName()).append(',');
                workNumbers.append(res.getWorkNumber()).append(',');
            }
            String b = names.toString();
            String c = workNumbers.toString();
            if (workNumbers.length() > 0) {
                b = b.substring(0, b.length() - 1);
                c = c.substring(0, b.length() - 1);
            }
            course.setNames(b);
            course.setWorkNumbers(c);
        }
        return courses;
    }

    /**
     * 更新课程
     *
     * @param course 课程信息
     * @return boolean
     */
    @Override
    public boolean update(Course course) {
        baseMapper.updateById(toSaveOrUpdate(course));
        return true;
    }

    private Course toSaveOrUpdate(Course course) {
        if (course.getNames() == null || course.getWorkNumbers() == null) {
            throw new ServiceException("教师信息不能为空");
        }

        String[] workNumbers = StrUtil.splitToArray(course.getWorkNumbers(), ',');
        String[] names = StrUtil.splitToArray(course.getNames(), ',');
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < workNumbers.length; i++) {
            LambdaQueryWrapper<Teacher> qw = new LambdaQueryWrapper<>();
            qw.eq(Teacher::getWorkNumber, workNumbers[i]).eq(Teacher::getName, names[i]);
            Teacher result = teacherDAO.selectOne(qw);
            if (result == null) {
                throw new ServiceException("教师信息有误，请确认后再试");
            } else {
                ids.append(result.getId()).append(',');
            }
        }
        String resIds = ids.toString();
        resIds = resIds.substring(0, resIds.length() - 1);
        course.setTeacherIds(resIds);
        return course;
    }


    @Override
    public Course getById(Serializable id) {
        Course course = baseMapper.selectById(id);
        // 为课程设置教师信息
        String teacherIds = course.getTeacherIds();
        String[] strIds = StrUtil.splitToArray(teacherIds, ',');
        StringBuilder workNumberSb = new StringBuilder();
        StringBuilder nameSb = new StringBuilder();
        for (String strId : strIds) {
            Teacher teacher = this.teacherDAO.selectById(strId);
            workNumberSb.append(teacher.getWorkNumber()).append(',');
            nameSb.append(teacher.getName()).append(',');
        }
        String res1 = workNumberSb.toString();
        res1 = res1.substring(0, res1.length() - 1);
        String res2 = nameSb.toString();
        res2 = res2.substring(0, res2.length() - 1);
        course.setWorkNumbers(res1);
        course.setNames(res2);
        // 设置学院信息
        course.setAcademy(this.academyDAO.selectById(course.getAcademyId()));
        return course;
    }
}
