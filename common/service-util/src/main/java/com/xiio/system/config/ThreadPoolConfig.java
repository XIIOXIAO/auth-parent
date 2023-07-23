package com.xiio.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @description:
 * @author: xiio
 * @time: 2023/6/8 9:32
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {
    @Bean("MyTaskExecutor")
    public Executor asyncServiceExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置核心线程数
        executor.setCorePoolSize(5);
        //设置最大线程数
        executor.setMaxPoolSize(20);
        //配置队列大小
        executor.setQueueCapacity(Integer.MAX_VALUE);
        //设置线程活跃时间(秒)
        executor.setKeepAliveSeconds(60);
        //设置默认线程名称
        executor.setThreadNamePrefix("XIIOXIAO 测试线程");
        //等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //执行初始化
        executor.initialize();

        return executor;
    }
}