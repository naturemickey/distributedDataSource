package org.w01f.dds.layer1.id;

import java.util.UUID;

public class IDGenerator {

    private static final IDGenerator instance = new IDGenerator();

    private IDGenerator() {
    }

    private static IDGenerator getInstance() {
        return instance;
    }

    public static String takeID() {
        byte[] dbNo = IDConfig.getInstance().getOne();
        UUID uuid = UUID.randomUUID();
        byte[] data = new byte[26];
        data[0] = dbNo[0];
        data[1] = dbNo[1];
        write(data, 2, System.currentTimeMillis());
        write(data, 10, uuid.getMostSignificantBits());
        write(data, 18, uuid.getLeastSignificantBits());
        return IDCoder.encode(data);
    }

    private static void write(byte[] data, int start, long value) {
        data[start + 0] = (byte) (value >>> 56);
        data[start + 1] = (byte) (value >>> 48);
        data[start + 2] = (byte) (value >>> 40);
        data[start + 3] = (byte) (value >>> 32);
        data[start + 4] = (byte) (value >>> 24);
        data[start + 5] = (byte) (value >>> 16);
        data[start + 6] = (byte) (value >>> 8);
        data[start + 7] = (byte) (value >>> 0);
    }

    public static void main(String[] args) {
        new IDConfig("0-10");
        for (int i = 0;i < 10; ++i) {
            System.out.println(IDGenerator.takeID());
        }
    }
}
