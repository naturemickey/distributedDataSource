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
                    dsNoList.add(i);
                }
            } else {
                dsNoList.add(Integer.parseInt(s));
            }
        }

        index = new AtomicInteger((int) (System.currentTimeMillis() % dsNoList.size()));

        IDConfig.config = this;
    }

    public byte[] getOne() {
        int idx = index.incrementAndGet();
        if (idx < 0) {
            index.set(0); // TODO : maybe set concurrently.
        }
        Integer res = dsNoList.get(idx % dsNoList.size());
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((res >> 8) & 0xff);
        bytes[1] = (byte) (res & 0xff);
        return bytes;
    }

    public static IDConfig getInstance() {
        return IDConfig.config;
    }

    public static void main(String[] args) {
        new IDConfig("0-10,11,12,20-30");
        for (int i = 0; i < 100; ++i) {
            byte[] bytes = IDConfig.getInstance().getOne();
            System.out.println(bytes[0] + ", " + bytes[1]);
        }
    }
}
