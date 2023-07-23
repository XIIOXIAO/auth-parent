package com.xiio.system.test.Thread.ThreadClass;

import org.junit.runner.RunWith;

/**
 * @description:
 * @author: xiio
 * @time: 2023/6/8 11:25
 */
public class MyRun implements Runnable {
    int ticket = 1;
    @Override
    public void run() {
        mymethod();
    }

    private synchronized void mymethod() {
        while (true){
            if (ticket<=100){
                System.out.println(Thread.currentThread().getName()+"正在售卖:"+ticket+"张票");
                ticket++;
            }else {
                break;
            }
        }
    }
}
