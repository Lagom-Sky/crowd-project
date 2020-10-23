import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class TestStringToByteArray {
    public static void main(String[] args) {
        String str1 = "abcc";
        String str2 = "a大家好";
        byte[] bytes = str2.getBytes();
        System.out.println("数组的长度为" + bytes.length);
        System.out.println(bytes[0] + "_______" + bytes[1]);

        Set<String> set = new TreeSet<String>();
        Comparator comparator =  String.CASE_INSENSITIVE_ORDER;
        int i = comparator.compare(str1, str2);
        System.out.println(i + "_______________");
        set.add(str1);
        set.add(str2);
        System.out.println(set.iterator().next());
        System.out.println();
        System.out.println(str1.compareTo(str2));
        byte[] byteArray = str2.getBytes();
        for (byte b : byteArray) {
            System.out.println(b);
        }
        System.out.println();
    }
}
