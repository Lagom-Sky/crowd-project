import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class testString {
    public static void main(String[] args) {
        TreeSet<String> set = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return -1;
            }
        });
        set.add("aa");
        set.add("bafsddddddddddddddddddd");
        set.add("bagfdgfddddddsarfewrwe");
        set.add("d");
        set.add("c");
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
