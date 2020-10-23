import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TestThread {
    public static void main(String[] args) {
        // 方式一但是不推荐
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hello world";
            }
        });
//        ExecutorService executorService1 =  new ThreadPoolExecutor();

        // 方式二ali手册推荐使用的方法
//        ThreadPoolExecutor();
    }

}
