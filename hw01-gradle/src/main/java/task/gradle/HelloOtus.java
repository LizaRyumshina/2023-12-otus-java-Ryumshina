package task.gradle;

import com.google.common.base.Joiner;

public class HelloOtus {
    public static void main(String[] args) {
        Joiner joiner = Joiner.on(", ").skipNulls();

        String result = joiner.join("First", "Second", null, "Third");

        System.out.println(result);
    }
}
