package test;

import com.google.gson.internal.bind.util.ISO8601Utils;

import java.util.UUID;

public class TestUUID {
    public static void main(String[] args) {
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
    }
}
