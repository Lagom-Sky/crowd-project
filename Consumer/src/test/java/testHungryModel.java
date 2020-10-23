public class testHungryModel {
}

class Singleton {
    // 匿名内部类
    private Singleton() {
    }

    // 静态变量常量模式实现饿汉模式
    private static Singleton singleton = new Singleton();

    private static Singleton singleton2;

    private static volatile Singleton singleton3; //双重验证

    static { // 静态代码块方式
        singleton2 = new Singleton();
    }

    public static Singleton getInstance() {
        return singleton;
    }

    // 懒汉式 加入同步
    public static synchronized Singleton getInstance2() { // 这种方式解决了线程安全问题但是没有解决同步效率低
        if (singleton == null) {
            return new Singleton(); // 没有new且需要时再去初始化
        } else {
            return singleton; // 如果已经存在对象就直接返回
        }
    }
    // 双重验证
    public static Singleton getInstance3(){ //两次验证实列是不是为null
        if (singleton3 == null) {
            synchronized (Singleton.class) {
                if (singleton3 == null) {
                    singleton3 = new Singleton();
                }
            }
            return singleton3; // 判断之后返回
        }
        else {
            return singleton3; // 不为null直接返回
        }
    }
}