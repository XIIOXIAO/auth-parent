package com.xiio.system.test.Thread;

import com.xiio.process.service.ThreadService;
import com.xiio.system.config.MybatisPlusConfig;
import com.xiio.system.test.Thread.ThreadClass.MyRun;
import com.xiio.system.test.Thread.ThreadClass.MyThread;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: xiio
 * @time: 2023/6/7 19:54
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ThreadTest {
    @Autowired
    private ThreadService threadService;

    @Test
    public void test1(){
        threadService.updateInfo();
        System.out.println("用户更新完成咯");
    }

    @Test
    public void test2(){
        MyThread t1 = new MyThread();
        MyThread t2 = new MyThread();
        MyThread t3 = new MyThread();

        t1.setName("窗口1");
        t2.setName("窗口2");
        t3.setName("窗口3");

        t1.start();
        t2.start();
        t3.start();
    }
    @Test
    public void test3(){
        MyRun myRun = new MyRun();

        Thread t1 = new Thread(myRun);
        Thread t2 = new Thread(myRun);
        Thread t3 = new Thread(myRun);

        t1.setName("窗口1");
        t2.setName("窗口2");
        t3.setName("窗口3");

        t1.start();
        t2.start();
        t3.start();
    }
}
