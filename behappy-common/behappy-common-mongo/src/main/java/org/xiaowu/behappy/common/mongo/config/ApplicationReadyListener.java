package org.xiaowu.behappy.common.mongo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;

/**
 * @author xiaowu
 * @desc: 此类若不加，那么插入的一行会默认添加一个_class字段来存储实体类类型
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationReadyListener implements ApplicationListener<ContextRefreshedEvent> {

  private final MongoTemplate oneMongoTemplate;
  
  private static final String TYPEKEY = "_class";

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    MongoConverter converter = oneMongoTemplate.getConverter();
    if (converter.getTypeMapper().isTypeKey(TYPEKEY)) {
      ((MappingMongoConverter) converter).setTypeMapper(new DefaultMongoTypeMapper(null));
    }
  }
}
