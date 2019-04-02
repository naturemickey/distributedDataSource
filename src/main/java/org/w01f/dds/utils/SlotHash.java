package org.w01f.dds.utils;

import com.google.common.hash.Hashing;

public class SlotHash {

    private static long[] slots = new long[1000];
    private static int x = Integer.MAX_VALUE / 1000;


    private static void addval(String key) {
        int hash = getHash(key);

        int i = hash / x;
        if (i > 999) i = 999;
        slots[i] += 1;

    }

    private static int getHash(String str) {

        return Math.abs(Hashing.murmur3_32().hashBytes(str.getBytes()).asInt());

    }

    public static void main(String[] args) {
        for (int i = 0; i < 99999999; i++) {
            addval("" + i);
            //addval(UUID.randomUUID().toString());
        }

        for (long slot : slots) {
            System.out.println(slot);
        }
    }
}
