import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String rangeBytes = "123-";
        String[] split = rangeBytes.split("-");
        System.out.println(Arrays.toString(split));
    }
}
