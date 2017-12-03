package chapter4;

/**
 * @author LTN
 * Created @2017-12-03
 * <p>
 * synchronized同步块和同步方法的实现机制。
 * 进入编译后的目录，javap -v Synchronized.class
 * 部分结果如下：
 * <pre>
 *     {@code
 * public static void main(java.lang.String[]);
 * descriptor: ([Ljava/lang/String;)V
 * flags: ACC_PUBLIC, ACC_STATIC
 * Code:
 * stack=2, locals=3, args_size=1
 * 0: ldc           #2                  // class chapter4/Synchronized
 * 2: dup
 * 3: astore_1
 * 4: monitorenter     //同步块使用监视器
 * 5: aload_1
 * 6: monitorexit
 * 7: goto          15
 * ...
 *
 * public static synchronized void m();
 * descriptor: ()V
 * flags: ACC_PUBLIC, ACC_STATIC, ACC_SYNCHRONIZED     //同步方法使用ACC_SYNCHRONIZED
 * ...
 * }
 * </pre>
 */
public class Synchronized {
    public static void main(String[] args) {
        // 对 Synchronized Class 对象进行加锁
        synchronized (Synchronized.class) {
        }
        //  静态同步方法，对Synchronized Class 对象进行加锁
        m();
    }

    public static synchronized void m() {
    }
}
