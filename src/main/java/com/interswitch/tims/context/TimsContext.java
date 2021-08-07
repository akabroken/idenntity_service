/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.context;

import com.interswitch.tims.service.RestService;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author ndegel
 */
@Configuration
public class TimsContext implements WebMvcConfigurer {

    @Resource
    public Environment environment;

    @Autowired
    RestService restService;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(restService).addPathPatterns("/**");
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(environment.getRequiredProperty("redis.host"), Integer.valueOf(environment.getRequiredProperty("redis.port"))));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        RedisSerializer<String> stringSerializer = new StringRedisSerializer();

        template.setKeySerializer(stringSerializer);
        //       template.setValueSerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);

        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean(name = "fuseDataSource")
    public DataSource fuseSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl(environment.getRequiredProperty("fuse.datasource.url"));
        dataSource.setUsername(environment.getRequiredProperty("fuse.datasource.username"));
        dataSource.setPassword(environment.getRequiredProperty("fuse.datasource.password"));
        return dataSource;
    }

    @Bean(name = "timsDataSource")
    public DataSource timsSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl(environment.getRequiredProperty("tims.datasource.url"));
        dataSource.setUsername(environment.getRequiredProperty("tims.datasource.username"));
        dataSource.setPassword(environment.getRequiredProperty("tims.datasource.password"));
        return dataSource;
    }
}
