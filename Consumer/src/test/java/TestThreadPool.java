import springfox.documentation.annotations.Cacheable;

import java.util.concurrent.*;

public class TestThreadPool implements Runnable {

    public String name;

    public TestThreadPool(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name + Thread.currentThread().getName());
        try{
            Thread.sleep(1000);
        }catch ( InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 用Executors可以创建的线程线程池的种类
        Executors.newSingleThreadExecutor(); // 单线程
        Executors.newFixedThreadPool(3); // 固定大小的线程池
        Executors.newCachedThreadPool(); // 带有缓冲的线程池
        Executors.newScheduledThreadPool(12); // 优先级队列为基础的延时队列
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(3); //  有界队列
        BlockingQueue<Runnable> workQueue1 = new LinkedBlockingDeque<>(); // 无界的等待队列 最大容量为integer.Max值，不是真正的无界队列
        /**
         * 一个不缓存任务的阻塞队列，生产者放入一个任务必须等到消费者取出这个任务。也就是说新任务进来时，不会缓存，而是直接被调度执行该任务，
         * 如果没有可用线程，则创建新线程，如果线程数量达到maxPoolSize，则执行拒绝策略。
         */
        BlockingQueue<Runnable> workQueue2 =  new SynchronousQueue<>();; // 不带缓存的
        BlockingQueue<Runnable> workQueue3 =  new PriorityBlockingQueue<>();; // 优先级队列 无界

        /**
         * 四种线程池的拒绝策略
         * ①CallerRunsPolicy // 调用主线程执行
         * 该策略下，在调用者线程中直接执行被拒绝任务的run方法，除非线程池已经shutdown，则直接抛弃任务。
         *
         * ②AbortPolicy
         * 该策略下，直接丢弃任务，并抛出RejectedExecutionException异常。
         *
         * ③DiscardPolicy  // 直接丢弃
         * 该策略下，直接丢弃任务，什么都不做。
         *
         * ④DiscardOldestPolicy
         * 该策略下，抛弃进入队列最早的那个任务，然后尝试把这次拒绝的任务放入队列
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1, // 核心线程数
                2, //最大线程数
                1L, // 线程没有任务时多久会自动地死亡
                TimeUnit.SECONDS, // 时间的单位
                workQueue // 等待队列
        );
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 调用主线程执行任务
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); // 抛出异常，拒绝执行
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy()); // 直接丢弃
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy()); // 直接最老的线程丢弃
        threadPoolExecutor.execute(new TestThreadPool("任务一"));
        threadPoolExecutor.execute(new TestThreadPool("任务二"));
        System.out.println("线程池的大小" + threadPoolExecutor.getPoolSize());
        threadPoolExecutor.execute(new TestThreadPool("任务三"));
        threadPoolExecutor.execute(new TestThreadPool("任务四"));
        threadPoolExecutor.execute(new TestThreadPool("任务五"));
        threadPoolExecutor.execute(new TestThreadPool("任务六")); // 线程的数量等于最大线程数并且等待队列已经满了，不能再增加了，就会拒绝加入新的任务;
        threadPoolExecutor.execute(new TestThreadPool("任务七")); // 线程的数量等于最大线程数并且等待队列已经满了，不能再增加了，就会拒绝加入新的任务;
        threadPoolExecutor.execute(new TestThreadPool("任务八")); // 线程的数量等于最大线程数并且等待队列已经满了，不能再增加了，就会拒绝加入新的任务;
        threadPoolExecutor.execute(new TestThreadPool("任务九")); // 线程的数量等于最大线程数并且等待队列已经满了，不能再增加了，就会拒绝加入新的任务;
        threadPoolExecutor.execute(new TestThreadPool("任务十")); // 线程的数量等于最大线程数并且等待队列已经满了，不能再增加了，就会拒绝加入新的任务;
        threadPoolExecutor.shutdown();
        System.out.println("hello world");
        System.out.println("这里是新的分支");
    }
}
