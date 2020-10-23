public interface testInterface {
    public abstract void sayHello();
    String str = "hello";
    int num = 330;
    Integer integer = 30;
}

/**
 * 接口中方法默认是abstract 和 public 修饰的(也只能是这两个修饰符修饰) 定义的的变量默认时public 也只能是public  定义的变量必须初始化
 */
class testClass implements testInterface{
    @Override
    public void sayHello() {
        System.out.println(str);
    }
}
class main{
    public static void main(String[] args) {
        String[] a = new String[32];
        int num;
        String c[];
        String d[];
        String[][] b;
        a[0] = "hello world";
        System.out.println(a[0]);
        testParams();
    }
    public static void testParams(){
        int i ;
        Integer num;
    }
    public void testNotStaticMethod(){
        System.out.println("我擦，这里只是一个测试");
    }

    public void testMethodsTwo(){
        System.out.println("不知道为什么要写一个控制台打印");
        testNotStaticMethod();
    }


}