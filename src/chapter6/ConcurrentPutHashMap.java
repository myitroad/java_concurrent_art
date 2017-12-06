package chapter6;

import chapter4.Profiler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 并发 put	--> 运行时很快程序就结束了，并没有发生死循环，可能是 JDK 版本问题，JDK1.8 可能不会死循环。 @2015-12-25 21:25:50
 *
 * @author tengfei.fangtf
 * @version $Id: Snippet.java, v 0.1 2015-7-31 下午 11:53:55 tengfei.fangtf Exp $
 */
public class ConcurrentPutHashMap {
    public static void main(String[] args) throws InterruptedException {
        final Map<String, String> map = new ConcurrentHashMap<String, String>(2);
        Profiler.begin();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            map.put(UUID.randomUUID().toString(), "");
                        }
                    }, "ftf" + i).start();
                }
            }
        }, "ftf");

        t.start();
        t.join();
        System.out.println("Consume: "+Profiler.end());
    }
}