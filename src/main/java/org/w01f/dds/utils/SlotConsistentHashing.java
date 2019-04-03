package org.w01f.dds.utils;

import com.google.common.hash.Hashing;

public class SlotConsistentHashing {

    private static final int slotcount = 2048;
    private static final long[] slots = new long[slotcount];
    private static final long intrange = Integer.MAX_VALUE + 0L - Integer.MIN_VALUE;
    private static final int x = (int) (intrange / slotcount);
    private static final long n = x * slotcount;

    private static void addval(String key) {
        long hash = getHash(key);
        int i = (int) ((hash - Integer.MIN_VALUE) / x);
        if (i == slotcount) i = (int) (hash - Integer.MIN_VALUE - n);
        slots[i] += 1;
    }

    private static int getHash(String str) {
        return Hashing.murmur3_32().hashBytes(str.getBytes()).asInt();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1024 * 1024 * 128; i++) {
            addval("" + i);
            //addval(UUID.randomUUID().toString());
        }

        for (long slot : slots) {
            System.out.println(slot);
        }
    }
}
