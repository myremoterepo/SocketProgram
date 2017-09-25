package com.iqiyi.fzf.socketprogram.socket.unicast;

import java.util.concurrent.TimeUnit;

/**
 * Created by fzf on 17-9-22.
 */

public class ThreadExample implements Runnable {

    private String greeting;

    public ThreadExample(String greeting) {
        this.greeting = greeting;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(Thread.currentThread().getName() + " ： " + greeting);

            try {
                TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
