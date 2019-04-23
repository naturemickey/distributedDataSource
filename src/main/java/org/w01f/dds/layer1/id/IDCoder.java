package org.w01f.dds.layer1.id;

import java.util.*;

public class IDCoder {

    private IDCoder() {
        // empty.
    }

    private static final char[] TO_BASE64 = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '=', 'A', 'B',
            'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '_', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    private static final int[] FROM_BASE64 = new int[128];

    static {
        Arrays.fill(FROM_BASE64, -1);
        for (int i = 0, len = TO_BASE64.length; i < len; i++) {
            FROM_BASE64[TO_BASE64[i]] = i;
        }
    }

    private static class CharStringBuilder {
        char[] chars;
        int idx = 0;

        CharStringBuilder(int capacity) {
            chars = new char[capacity];
        }

        void append(char c) {
            chars[idx++] = c;
        }

        public String toString() {
            return new String(chars, 0, idx);
        }
    }

    public static String encode(byte[] data) {
        CharStringBuilder sb = new CharStringBuilder(data.length * 8 / 6 + 1);
        int idxFrom = 0;
        int preSave = -1;


        for (int i = 0, len = data.length; i < len; ++i) {
            byte b = data[i];

            switch (idxFrom) {
                case 0:
                    sb.append(TO_BASE64[(b >> 2) & 0b0011_1111]);
                    preSave = (b & 0b0011) << 4;

                    if (i < len - 1) {
                        b = data[i + 1];
                        sb.append(TO_BASE64[preSave | ((b >> 4) & 0b1111)]);

                        preSave = -1;
                        idxFrom = 4;
                    }
                    break;
                case 2:
                    sb.append(TO_BASE64[b & 0b0011_1111]);
                    idxFrom = 0;
                    break;
                case 4:
                    preSave = (b & 0b1111) << 2;
                    if (i < len - 1) {
                        b = data[i + 1];
                        sb.append(TO_BASE64[preSave | ((b >> 6) & 0b0011)]);

                        preSave = -1;
                        idxFrom = 2;
                    }
                    break;
                default:
                    throw new RuntimeException("impossible.");
            }
        }

        if (preSave >= 0) {
            sb.append(TO_BASE64[preSave]);
        }
        return sb.toString();
    }

    public static byte[] decode(String s) {
        final char[] chars = s.toCharArray();

        byte[] bytes = new byte[chars.length * 6 / 8];
        int idx = 0;

        int need = 8;
        int preSave = -1;

        for (int i = 0, len = chars.length; i < len; i++) {

            int v = FROM_BASE64[chars[i]]; // v's lenth is 6 bit.
            switch (need) {
                case 4: {
                    bytes[idx++] = (byte) ((preSave << 4) | (v >> 2));
                    preSave = v & 0b0011;
                    need = 6;

                    break;
                }
                case 6: {
                    bytes[idx++] = (byte) ((preSave << 6) | v);
                    need = 8;

                    break;
                }
                case 8: { // 如果是合法的数据，就不会越界。
//                    if (i < len - 1) {
                    int n = (v << 2);
                    i += 1;
                    v = FROM_BASE64[chars[i]];
                    bytes[idx++] = (byte) (n | (v >> 4));
                    preSave = v & 0b1111;
                    need = 4;
//                    }
                    break;
                }
                default:
                    throw new RuntimeException("impossible.");
            }
        }

        return bytes;
    }

    public static void main(String[] args) {
        byte[] bytes = {51, -57, 57, -99, 101, -100, 50, 57, -45, 51, 102, -55, 101, -45, 52, 97, 50, 56, 45, 56, 51, 49, 50, 45, 55, 50, 100, 49, 52, 101, 48, 99, 57, 99, 55};
//        byte[] bytes = {51};

        final String ec = encode(bytes);
        // System.out.println(ec);


        System.out.println("51, -57, 57, -99, 101, -100, 50, 57, -45, 51, 102, -55, 101, -45, 52, 97, 50, 56, 45, 56, 51, 49, 50, 45, 55, 50, 100, 49, 52, 101, 48, 99, 57, 99, 55");
        for (byte b : decode(ec)) {
            System.out.print(b + ", ");
        }

        List<byte[]> list = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 1000000; i++) {
//            list.add(UUID.randomUUID().toString().getBytes());
            list.add((r.nextDouble() + "").getBytes());
        }

        System.out.println();

        final long start = System.currentTimeMillis();
        for (byte[] bs : list) {
//            final String encode = encode(bs);
//            final byte[] decode = decode(encode);
//
//            if (!Arrays.equals(bs, decode)) {
//                throw new RuntimeException();
//            }
//
            decode(encode(bs));
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
