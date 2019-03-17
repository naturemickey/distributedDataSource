package org.w01f.dds.layer1.id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IDConfig {

    private static IDConfig config;

    private List<Integer> dsNoList = new ArrayList<>();
    private AtomicInteger index;

    public IDConfig(String config) {
        String[] ss = config.split(",");
        for (String s : ss) {
            if (s.contains("-")) {
                String[] sss = s.split("-");
                if (sss.length > 2) {
                    throw new RuntimeException();
                }
                int from = Integer.parseInt(sss[0]);
                int to = Integer.parseInt(sss[1]);
                for (int i = from; i <= to; ++i) {
                    dsNoList.add(checkRange(i));
                }
            } else {
                dsNoList.add(checkRange(Integer.parseInt(s)));
            }
        }

        index = new AtomicInteger((int) (System.nanoTime() % dsNoList.size()));

        IDConfig.config = this;
    }

    private int checkRange(int n) {
        if (n < 0 || n > 65535)
            throw new RuntimeException();
        return n;
    }

    public byte[] getOne() {
        int idx = index.incrementAndGet();
        if (idx < 0) {
            idx = (int) (System.nanoTime() % dsNoList.size());
            index.set(idx); // TODO : maybe set concurrently.
        }
        Integer res = dsNoList.get(idx % dsNoList.size());
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((res & 0xff00) >> 8);
        bytes[1] = (byte) (res & 0xff);
        return bytes;
    }

    public static IDConfig getInstance() {
        return IDConfig.config;
    }

    public static void main(String[] args) {
        new IDConfig("65535");
        for (int i = 0; i < 100; ++i) {
            byte[] bytes = IDConfig.getInstance().getOne();
            System.out.println(bytes[0] + ", " + bytes[1]);
        }
    }
}
