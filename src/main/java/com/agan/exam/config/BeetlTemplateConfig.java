package com.agan.exam.config;

import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Beetl Template 配置
 */
@Configuration
public class BeetlTemplateConfig {

  @Bean
  public BeetlGroupUtilConfiguration beetlConfig() {
    BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
    ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader();
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    Resource beetlResource = resolver.getResource("classpath:beetl.properties");
    beetlGroupUtilConfiguration.setConfigFileResource(beetlResource);
    beetlGroupUtilConfiguration.setResourceLoader(classpathResourceLoader);
    beetlGroupUtilConfiguration.init();
    return beetlGroupUtilConfiguration;
  }

  @Bean
  public BeetlSpringViewResolver beetlViewResolver(BeetlGroupUtilConfiguration beetlConfig) {
    BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
    beetlSpringViewResolver.setSuffix(".html");
    beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
    beetlSpringViewResolver.setOrder(0);
    beetlSpringViewResolver.setCache(true);
    beetlSpringViewResolver.setConfig(beetlConfig);
    return beetlSpringViewResolver;
  }
}
