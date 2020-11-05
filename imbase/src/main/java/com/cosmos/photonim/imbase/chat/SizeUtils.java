package com.cosmos.photonim.imbase.chat;

public class SizeUtils {
    public static String getSize(long size) {
        int k = (int) (size / 1024);
        int m = (int) (size / (1024 * 1024));
        if (m > 0) {
            return String.format("%dMB", m);
        } else if (k > 0) {
            return String.format("%dKB", k);
        } else {
            return String.format("%dB", size);
        }
    }
}
