//public class ProxyTest {
//    public static void main(String[] args) {
//        ISubject iSubject= new ISubjectImpl();
//        System.out.println("代理前");
//        iSubject.sayHello();
//        System.out.println("我是分割线——————————————————————————————");
//        System.out.println("代理后");
//        iSubject =  proxySubject.initObject(iSubject);
//        iSubject.sayHello();
//    }
//}
//// 接口
//interface ISubject{
//    void sayHello();
//}
//// 实现接口
// class ISubjectImpl implements ISubject{
//    @Override
//    public void sayHello() {
//        System.out.println("这里是ISubject");
//    }
//}
//// 包含实现类的代理类
//class proxySubject implements ISubject {
//    ISubject iSubject;
//
//    public proxySubject(ISubject iSubject){
//        this.iSubject = iSubject;
//    }
//
//    @Override
//    public void sayHello() {
//        System.out.println("加强前操作");
//        iSubject.sayHello();
//        System.out.println("加强后操作");
//    }
//    // 初始化代理对象的方法
//    public static proxySubject initObject(ISubject iSubject){
//        return new proxySubject(iSubject);
//    }
//}