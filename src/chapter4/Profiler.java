package chapter4;

import java.util.concurrent.TimeUnit;

/**
 * Created by xdcao on 2017/5/4.
 *
 * 线程之间的通信：ThreadLocal。
 * 可以通过set(T)方法来设置一个值，在当前线程下再通过get()方法获取到原先设置的值。
 *
 * Profiler可以被复用在方法调用耗时统计的功能上，在方法的入口前执行begin()方法，在方法调用后执行end()方法。
 * 好处是两个方法的调用不用在一个方法或者类中。
 */
public class Profiler {

    private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    public static final void begin() {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    public static final Long end() {
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }


    public static void main(String[] args) throws InterruptedException {
        Profiler.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost " + Profiler.end());
    }

}
