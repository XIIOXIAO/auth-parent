package com.xiio.process.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: xiio
 * @time: 2023/6/7 19:52
 */
@Component
public class ThreadService {

    @Async("MyTaskExecutor")
    public void updateInfo(){

        try {
            Thread.sleep(5000);
            System.out.println("更新完成了......");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
