package chapter6;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Created by liutingna on 2017/12/8.
 *
 * @author liutingna
 * Fork/Join框架小示例-计算连续任意大个数之和
 * 代码来源：http://blog.csdn.net/imuduo/article/details/27114067
 */
public class ForkJoinAdder extends RecursiveTask<BigInteger> {
    private final static BigInteger threhold = new BigInteger("1000");
    private final static BigInteger TWO = new BigInteger("2");
    private BigInteger begin, end;

    public ForkJoinAdder(BigInteger begin, BigInteger end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected BigInteger compute() {
        // TODO Auto-generated method stub
        if (end.subtract(begin).compareTo(threhold) > 0) {
            ForkJoinTask<BigInteger> t1=new ForkJoinAdder(begin, begin.add(end.subtract(begin).divide(TWO).add(end.subtract(begin).remainder(TWO)))).fork();
            ForkJoinTask<BigInteger> t2=new ForkJoinAdder(begin.add(end.subtract(begin).divide(TWO).add(end.subtract(begin).remainder(TWO))).add(BigInteger.ONE), end).fork();
            return t1.join().add(t2.join());
        } else {
            BigInteger s = new BigInteger("0");
            BigInteger b=begin;
            while (b.compareTo(end) <= 0) {
                s=s.add(b);
                b = b.add(new BigInteger("1"));
            }
            return s;
        }
    }
}