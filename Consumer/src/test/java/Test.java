import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid.toString().length());
        System.out.println(uuid.toString());
        String str = "06f46958-0f58-11eb-874e-00163e15d5ac";
        String str1 = str.replace("-", "");
        System.out.println(str1 + ":" + str1.length());
        System.out.println(str.length());
    }
}
