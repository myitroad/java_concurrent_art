#第四章 Java并发编程基础

##一、线程简介

使用多线程的原因：

    1.更多的处理器核心：一个线程在一个时刻只能运行在一个处理器核心上
    2.更快的响应时间
    3.更好的编程模型
    
线程优先级：

操作系统基本采用时分的形式调度运行的线程，操作系统会分出一个个时间片，线程会分配到若干时间片，
当线程的时间片用完了就会发生线程调度，并等待着下次分配，线程分配到的时间片多少就决定了线程使用
处理器资源的多少。

线程的状态：6种，在给定的时刻只能处于一种状态

    NEW：初始状态，线程被构建，但还没有调用start方法
    RUNNABLE：运行状态，java线程将就绪和运行两种状态统称为运行状态
    BLOCKED：阻塞状态，表明线程阻塞于锁
    WAITING：等待状态，等待其他线程的通知或中断
    TIME_WAITING：超时等待状态，可以在指定的时间自行返回
    TERMINATED：终止状态，表示当前线程已经执行完毕
    
Daemon线程（守护线程）

当一个java虚拟机中不存在非守护线程时，虚拟机将会退出。

ps. 在构建Daemon线程时，不能依靠finally块中的内容来确保执行关闭或清理资源的逻辑

##二、启动和终止线程

一个新构造的线程对象是由其父线程来进行空间分配的，而子线程的各种属性继承自父线程，同时还会分配一个唯一的ID。

启动线程：start方法：当前线程同步告知虚拟机：只要线程规划器空闲，应立即启动调用start方法的线程

中断：

可以理解为线程的一个标识符属性，它标识一个运行中的线程是否被其他线程进行了中断操作。
线程通过isInterrupted（）方法判断是否被中断，也可以调用静态方法Thread.interrupted()
对当前的线程中断标识位进行复位。

    从java的API可以看出，许多声明抛出InterruptedException的方法在抛出这个异常前，java虚拟机会
    先将该线程的中断标志位清除，然后再抛异常。此时调用isInterrupted返回false
    
##三、线程间通信（重点）

###1、volatile和synchronized关键字

    volatile:告知程序任何对该变量的访问均需要从共享内存中获取，而对他的改变必须同步刷新回主内存，
    他能保证所有线程对变量访问的可见性。
    synchronized：确保多个线程在同一个时刻，只能有一个线程处于方法或同步块中，
    保证了线程对变量访问的可见性和排他性。
    
关于synchronized：本质是对一个对象的监视器（monitor）的获取，而这个获取过程是排他的，也就是同一时刻
只能有一个线程获取到有synchronized所保护对象的监视器。任意一个对象都拥有自己的监视器。

    任意线程对Object的访问，首先要获得Object的监视器。如果获取失败，线程进入同步队列，
    线程状态变为BLOCKED。当访问Object的前驱（获得了锁的线程）释放了锁，则该释放操作
    唤醒阻塞在同步队列中的线程，使其重新尝试对监视器的获取。
    
###2、等待/通知机制（生产者-消费者模型）

等待/通知机制，是指一个线程A调用了对象O的wait方法进入等待状态，而另一个线程B调用了对象O的notify
或notifyall方法，线程A收到通知后从对象O的wait方法返回，进而执行后续操作。

注意： 

（1）使用wait、notify、notifyAll时需要先对调用对象加锁

（2）调用wait方法后，线程状态由RUNNING变为WAITING，并将当前线程防止到对象的等待队列

（3）notify或notifyAll方法调用后，等待线程依旧不会从wait返回，需要调用notify或
notifyAll的线程释放锁之后，等待线程才有机会从wait返回

（4）notify方法将等待队列中的等待线程从等待队列中移到同步队列中，被移动的线程从WAITING变为BLOCKED

（5）从wait方法返回的前提是获得了调用对象的锁

    P101页的图
    
###3、等待/通知经典范式

    等待方：1.获取对象的锁
           2.如果条件不满足，那么调用对象的wait方法，被通知后仍要检查条件
           3.条件满足则执行对应的逻辑
           
            伪代码：
                   synchronized(对象){
                        while(条件不满足){
                            对象.wait();
                        }
                        对应的处理逻辑
                   }
    
    通知方：1.获得对象的锁
            2.改变条件
           3.通知所有等待在对象上的线程
           
           伪代码：
                   synchronized(对象){
                        改变条件
                        对象.notify();
                   }
    
###4、管道输入输出流

PipedOutputStream、PipedInputStream(字节数据)

PipedReader、PipedWriter(字符)

对于piped类型的流，使用时必须先调用connect方法进行绑定，否则会抛出异常

###5、Thread.join()

含义：当前线程等待thread线程终止之后才从thread.join返回。
另外还有两个join(long millis)具备超时特性的方法（如果线程在给定的时间内没有终止，那么将会从该超时方法返回）。

###6、ThreadLocal

##四、应用实例


