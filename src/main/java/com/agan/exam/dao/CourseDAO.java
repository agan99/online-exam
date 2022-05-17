package com.agan.exam.dao;

import com.agan.exam.model.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseDAO extends BaseMapper<Course> {

  @Select("select * from course where FIND_IN_SET(#{teacherId} ,teacher_ids)")
  List<Course> listByTeacherId(@Param("teacherId") Integer teacherId);
}
