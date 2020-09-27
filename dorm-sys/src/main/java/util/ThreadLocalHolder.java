package util;

public class ThreadLocalHolder {
    public static final ThreadLocal<Integer> COUNT = new ThreadLocal<>();

    public static ThreadLocal<Integer> get() {
        return COUNT;
    }
}
