/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.service;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ndegel
 */
@Repository
public class RedisService {

    private final static Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    RedisTemplate<String, Object> template;

    public void set(String key, Object value, Duration duration) {
        template.opsForValue().set(key, value, duration);
    }

    public void set(String key, Object value) {
        template.opsForValue().set(key, value);
    }

    public Object get(String key) {
        return template.opsForValue().get(key);
    }

    public Object getSet(String key, Object value) {
        logger.info("Fetching " + key);
        if (template.opsForValue().get(key) == null) {
            logger.info("Saving " + key);
            template.opsForValue().set(key, value);
        }
        return template.opsForValue().get(key);
    }

}
