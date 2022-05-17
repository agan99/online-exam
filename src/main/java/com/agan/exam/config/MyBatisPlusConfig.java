package com.agan.exam.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;

/**
 * MyBatisPlus 相关配置
 */
@Configuration
public class MyBatisPlusConfig {

  /**
   * 新的分页插件，一缓和二缓遵循mybatis的规则,
   * 需要设置 MybatisConfiguration #useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
   * @return 分页插件对象
   */
  @Bean
  public MybatisPlusInterceptor paginationInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    // 把想用的分页插件添加到集合中
    List<InnerInterceptor> interceptors = new ArrayList<>();
    // 攻击 SQL 阻断解析器、加入解析链，作用！阻止恶意的全表更新删除
    interceptors.add(new BlockAttackInnerInterceptor());
    //自动分页
    interceptors.add(new PaginationInnerInterceptor());
    interceptor.setInterceptors(interceptors);
    return interceptor;
  }

}
