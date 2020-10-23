import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKProxy implements InvocationHandler {

    private Object target;

    public JDKProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("begin");
        Object result = method.invoke(target, args);
        System.out.println("end");
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T wrap(Object target){
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new JDKProxy(target)
                );
    }

    public static void main(String[] args) {
        ISubject iSubject = new ISubjectImpl();
        System.out.println("代理前");
        iSubject.sayHello();
        System.out.println("代理之后");
        iSubject = JDKProxy.wrap(iSubject);
        iSubject.sayHello();
    }
}

// 接口
interface ISubject{
    void sayHello();
}
// 实现接口
class ISubjectImpl implements ISubject{
    @Override
    public void sayHello() {
        System.out.println("这里是ISubject");
    }
}