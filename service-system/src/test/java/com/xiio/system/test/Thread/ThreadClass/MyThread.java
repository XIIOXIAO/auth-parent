package com.xiio.system.test.Thread.ThreadClass;

/**
 * @description:
 * @author: xiio
 * @time: 2023/6/8 10:43
 */
public class MyThread extends Thread{
    //多线程共享同一个变量
    static int ticket = 1;

    public void sendTicket(){
        while (true){
            synchronized (MyThread.class){
                if (ticket<=100){
                    System.out.println(getName()+"正在售卖:"+ticket+"张票");
                    ticket++;
                }else {
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        sendTicket();
    }
}
