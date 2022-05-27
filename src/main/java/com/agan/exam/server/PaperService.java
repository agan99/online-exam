package com.agan.exam.server;

import com.agan.exam.model.Paper;
import com.agan.exam.model.dto.QueryPaperDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface PaperService extends IService<Paper> {

  /**
   * 教师批改试卷
   * @param stuId   学生ID
   * @param paperId 试卷ID
   * @param request request 对象.
   */
  void markPaper(Integer stuId, Integer paperId, HttpServletRequest request);

  /**
   * 分页查询试卷信息
   * @param page   分页信息
   * @param entity 模糊搜索条件
   * @return 分页结果集
   */
  Map<String, Object> pagePaper(Page<Paper> page, QueryPaperDto entity);

  /**
   * 删除试卷
   * @param id 试卷ID
   */
  int deletePaperById(Integer id);

  /**
   * 通过试卷模板 ID 查询试卷数量
   * @param paperFormId 试卷模板ID
   * @return 数量
   */
  Long countPaperByPaperFormId(Integer paperFormId);

  /**
   * 智能组卷
   * @param difficulty 难度
   * @param paper      试卷信息
   */
  void randomNewPaper(Paper paper, String difficulty);

  /**
   * 更新试卷指派班级
   * @param paper 试卷信息
   */
  int updateGradeIds(Paper paper);

  /**
   * 根据老师Id查找已结束试卷
   * @param teacherId 教师ID
   * @return 该教师所属已完成的试卷
   */
  List<Paper> listDoneByTeacherId(Integer teacherId);

  /**
   * 检测是否有被改版机考过
   * @param paperId 试卷id
   * @param gradeId 班级表编号
   * @param level   年级
   */
  void checkTestedByGradeId(Integer paperId, Integer level, Integer gradeId);
}
