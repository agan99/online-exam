package com.agan.exam.server;

import com.agan.exam.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

/**
 * 试题返回 随机 / 顺序设置
 *
 * @author chachae
 * @since 2020/2/25 21:16
 */
@Component
@RequiredArgsConstructor
public class QuestionSortService {

  private final QuestionService questionService;

  /**
   * 设置题目的 model 对象信息
   * @param mv       model 对象
   * @param paperId  试卷id
   * @param isRandom 是否进行题目随机排序
   */
  public void setQuestionModel(ModelAndView mv, Integer paperId, boolean isRandom) {

    String[] names = {"qChoiceList", "qMulChoiceList", "qTofList", "qFillList", "qSaqList",
        "qProgramList"};
    for (int i = 0; i < names.length; i++) {
      List<Question> questions = questionService.selectByPaperIdAndType(paperId, i + 1);
      if (isRandom) {
        Collections.shuffle(questions);
      }
      mv.addObject(names[i], questions);
    }

  }
}
