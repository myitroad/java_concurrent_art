package chapter5;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 10-15
 * 一个测试，来观察公平和非公平锁在获取锁时的区别
 */
public class FairAndUnfairTest {

    private static Lock fairLock = new ReentrantLock2(true);
    private static Lock unfairLock = new ReentrantLock2(false);
    private static CountDownLatch start;

    /**
     * 公平锁一个运行效果
     * Lock by [0], Waiting by [1, 2, 3, 4]
     * Lock by [1], Waiting by [2, 3, 4, 0]
     * Lock by [2], Waiting by [3, 4, 0, 1]
     * Lock by [3], Waiting by [4, 0, 1, 2]
     * Lock by [4], Waiting by [0, 1, 2, 3]
     * Lock by [0], Waiting by [1, 2, 3, 4]
     * Lock by [1], Waiting by [2, 3, 4]
     * Lock by [2], Waiting by [3, 4]
     * Lock by [3], Waiting by [4]
     * Lock by [4], Waiting by []
     */
    @Test
    public void fair() {
        testLock(fairLock);
    }

    /**
     * 非公平锁一个运行效果
     * Lock by [0], Waiting by [2, 1, 3, 4]
     * Lock by [0], Waiting by [2, 1, 3, 4]
     * Lock by [2], Waiting by [1, 3, 4]
     * Lock by [2], Waiting by [1, 3, 4]
     * Lock by [1], Waiting by [3, 4]
     * Lock by [1], Waiting by [3, 4]
     * Lock by [3], Waiting by [4]
     * Lock by [3], Waiting by [4]
     * Lock by [4], Waiting by []
     * Lock by [4], Waiting by []
     */
    @Test
    public void unfair() {
        testLock(unfairLock);
    }

    private void testLock(Lock lock) {
        start = new CountDownLatch(1);

        for (int i = 0; i < 5; i++) {
            Thread thread = new Job(lock);
            thread.setName("" + i);
            thread.start();
        }

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println(e);
        }

        start.countDown();
    }

    private static class Job extends Thread {

        private Lock lock;

        public Job(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            try {
                start.await();
            } catch (InterruptedException e) {
                System.out.println(e);
            }

            for (int i = 0; i < 2; i++) {
                lock.lock();
                try {
                    System.out.println("Lock by [" + getName() + "], Waiting by " + ((ReentrantLock2) lock).getQueuedThreads());
                } finally {
                    lock.unlock();
                }
            }
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    private static class ReentrantLock2 extends ReentrantLock {

        public ReentrantLock2(boolean fair) {
            super(fair);
        }

        @Override
        public Collection<Thread> getQueuedThreads() {
            List<Thread> arrayList = new ArrayList<Thread>(super.getQueuedThreads());
            Collections.reverse(arrayList);
            return arrayList;
        }
    }
}