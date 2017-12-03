package chapter4;

import java.util.concurrent.TimeUnit;

/**
 * Created by xdcao on 2017/5/4.
 *
 * 线程之间通信的方式：Thread.join
 * 创建了10个线程，编号0~9，每个线程调用前一个线程的join()方法。
 * 也就是线程0结束了，线程1才能从join()方法中返回，而线程0需要等待main线程结束。
 */
public class Join {

    static class Domino implements Runnable {

        private Thread thread;

        private Domino(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println(Thread.currentThread().getName() + " terminate.");
        }
    }

    public static void main(String[] args) throws Exception {
        Thread previous = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Domino(previous), String.valueOf(i));
            thread.start();
            previous = thread;
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(Thread.currentThread().getName() + " terminate.");
    }

}
