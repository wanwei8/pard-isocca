package com.pard.common;

import com.google.common.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by wawe on 17/5/27.
 */
@Configuration
public class CacheConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final int DEFAULT_MAXSIZE = 50000;
    public static final int DEFAULT_TTL = 600;

    @Autowired
    CacheBuilder<Object, Object> cacheBuilder;

    @Bean
    public CacheBuilder<Object, Object> cacheBuilder() {
        RemovalListener<Object, Object> asyncRemovalListener = RemovalListeners.asynchronous(addRemovalListener(),
                Executors.newSingleThreadExecutor());
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
                .maximumSize(DEFAULT_MAXSIZE)
                .expireAfterWrite(DEFAULT_TTL, TimeUnit.SECONDS)
                .removalListener(asyncRemovalListener);
        return cacheBuilder;
    }

    @Bean
    @Primary
    public CacheManager getCacheManager() {
        GuavaCacheManager cacheManager = new GuavaCacheManager();
        cacheManager.setCacheBuilder(cacheBuilder);
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

    private RemovalListener<Object, Object> addRemovalListener() {
        return new RemovalListener<Object, Object>() {
            @Override
            public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                /*String tips = String.format("key=%s,value=%s,reason=%s", removalNotification.getKey(),
                        removalNotification.getValue(),
                        removalNotification.getCause());
                logger.debug(tips);*/

                /*if (RemovalCause.EXPLICIT != removalNotification.getCause()){
                    try{
                        String key = (String) removalNotification.getKey();
                        String val = (String) removalNotification.getValue();
                    }catch (Exception e){
                        logger.error("Cache RemovalListener Exception [{}]",e.getMessage(), e);
                    }
                }*/
            }
        };
    }
}
