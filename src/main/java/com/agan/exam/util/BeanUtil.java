package com.agan.exam.util;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.springframework.cglib.beans.BeanCopier;

/**
 * Cglib 实体拷贝工具类
 *
 * @author chachae
 * @date 2019/10/3 11:26
 */
public class BeanUtil {

  private BeanUtil() {
  }

  private static final Map<String, BeanCopier> MAP = Maps.newHashMap();

  /**
   * 对象复制
   *
   * @param obj   被复制对象，为空会抛出异常
   * @param clazz 复制类型
   * @return T
   */
  public static <T> T copyObject(Object obj, Class<T> clazz) {
    T obj2;
    try {
      obj2 = clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new ServiceException("被复制的对象为空");
    }
    String name = getClassName(obj.getClass(), clazz);
    BeanCopier beanCopier;
    if (MAP.containsKey(name)) {
      beanCopier = MAP.get(name);
    } else {
      beanCopier = BeanCopier.create(obj.getClass(), clazz, false);
      MAP.put(name, beanCopier);
    }
    beanCopier.copy(obj, obj2, null);
    return obj2;
  }

  /**
   * 复制队列
   *
   * @param list 被复制队列
   * @param <T>  复制类型
   * @return T
   */
  public static <T> List<T> copyList(List<?> list, Class<T> clazz) {
    if (CollUtil.isEmpty(list)) {
      throw new ServiceException("被复制的队列为空");
    }
    List<T> resultList = Lists.newLinkedList();
    for (Object obj1 : list) {
      T t = copyObject(obj1, clazz);
      resultList.add(t);
    }
    return resultList;
  }

  /**
   * 获取类命
   *
   * @param class1 obj1
   * @param class2 obj2
   * @return 类名
   */
  private static String getClassName(Class<?> class1, Class<?> class2) {
    return class1.getName() + class2.getName();
  }
}
