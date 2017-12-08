package chapter6;

import chapter4.Profiler;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by liutingna on 2017/12/8.
 *
 * @author liutingna
 * 对比使用ForkJoin框架和使用for循环累加的执行速度。在累加1000000000情况下，框架比直接for循环要慢很多。
 */
public class ForkJoinAdderApp {
    public static void main(String[] args) throws Exception {
        Profiler.begin();
        //框架执行
        ForkJoinPool pool = new ForkJoinPool();
        BigInteger b = new BigInteger("0");
        BigInteger e = new BigInteger("1000000000");
        ForkJoinAdder t = new ForkJoinAdder(b, e);
        pool.execute(t);
        System.out.println(t.get());

        //累加执行
        long sum=0;
        for (int i = 0; i <= 1000000000; i++) {
            sum += i;
        }
        System.out.println(sum);
        System.out.println(Profiler.end());
    }

}