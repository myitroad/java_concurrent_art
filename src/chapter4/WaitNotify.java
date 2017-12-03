package chapter4;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by xdcao on 2017/5/4.
 *
 * Java线程之间通信的方式：等待/通知方式。
 * Wait类可看作消费者（等待条件改变），Notify类可看作生产者（改变条件）。
 */
public class WaitNotify {

    static boolean flag = true;
    static Object lock = new Object();

    static class Wait implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                while (flag) {
                    try {
                        System.out.println(Thread.currentThread() + " flag is true, wait @ " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
                System.out.println(Thread.currentThread() + " flag is false, running @ " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }
    }

    /**
     * 试试把 lock.notifyAll();一行屏蔽，那么Wait任务中的wait()将一直等待，而导致程序不会终止。
     * 这时可以使用超时的wait()方法
     */
    static class Notify implements Runnable {

        @Override
        public void run() {
            synchronized (lock) {
                System.out.println(Thread.currentThread() + " hold lock, notify @ " +
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));
                lock.notifyAll();
                flag = false;
                SleepUtils.second(5);
            }

            synchronized (lock) {
                System.out.println(Thread.currentThread() + " hold lock again, sleep @ " +
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));
                SleepUtils.second(5);
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread waitThread = new Thread(new Wait(), "WaitThread");
        waitThread.start();
        TimeUnit.SECONDS.sleep(1);
        Thread notifyThread = new Thread(new Notify(), "NotifyThread");
        notifyThread.start();
    }

}
