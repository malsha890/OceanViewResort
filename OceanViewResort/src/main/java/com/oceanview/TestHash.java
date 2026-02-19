package com.oceanview;

import com.oceanview.util.PasswordUtil;

public class TestHash {

    public static void main(String[] args) {

        String hash = PasswordUtil.hashPassword("admin123");

        System.out.println("Generated Hash:");
        System.out.println(hash);
    }
}
