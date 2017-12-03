package chapter4;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LTN
 *
 * 一个示例来测试简易数据库连接池的工作情况，模拟客户端ConnectionRunner获取、使用、最后释放连接的过程。
 * 当它使用时连接将会增加获取到连接的数量，反之，将会增加未获取到连接的数量。
 *
 * 数据库连接池的设计也可以复用到其他的资源获取的场景，针对昂贵资源（比如数据库连接）的获取都应该加以超时限制。
 */
public class ConnectionPoolTest {
    static ConnectionPool pool = new ConnectionPool(10);
    // 保证所有  ConnectionRunner能够同时开始
    static CountDownLatch start = new CountDownLatch(1);
    // main 线程将会等待所有 ConnectionRunner  结束后才能继续执行
    static CountDownLatch end;

    public static void main(String[] args) throws Exception {
        // 线程数量，可以修改线程数量进行观察，大概有以下规律：（结果可能不尽相同）
        /*线程数   10  20  30
        *获取数   200 387 542
        *未获取数  0   13  58
        */
        int threadCount = 10;
        end = new CountDownLatch(threadCount);
        int count = 20;
        AtomicInteger got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new ConnetionRunner(count, got, notGot),"ConnectionRunnerThread");
            thread.start();
        }
        start.countDown();
        end.await();
        System.out.println("total invoke: " + (threadCount * count));
        System.out.println("got connection: " + got);
        System.out.println("not got connection " + notGot);
    }

    static class ConnetionRunner implements Runnable {
        int count;
        AtomicInteger got;
        AtomicInteger notGot;

        public ConnetionRunner(int count, AtomicInteger got, AtomicInteger notGot) {
            this.count = count;
            this.got = got;
            this.notGot = notGot;
        }

        public void run() {
            try {
                start.await();
            } catch (Exception ex) {
                System.out.println(ex);
            }
            while (count > 0) {
                try {
                    // 从线程池中获取连接，如果  1000ms  内无法获取到，将会返回null
                    // 分别统计连接获取的数量  got和未获取到的数量  notGot
                    Connection connection = pool.fetchConnection(1000);
                    if (connection != null) {
                        try {
                            connection.createStatement();
                            connection.commit();
                        } finally {
                            pool.releaseConnection(connection);
                            got.incrementAndGet();
                        }
                    } else {
                        notGot.incrementAndGet();
                    }
                } catch (Exception ex) {
                } finally {
                    count--;
                }
            }
            end.countDown();
        }
    }
}
