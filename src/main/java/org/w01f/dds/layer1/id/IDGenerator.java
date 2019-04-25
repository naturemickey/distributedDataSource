package org.w01f.dds.layer1.id;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class IDGenerator {

    private IDGenerator() {
    }

    public static String takeId() {
        return takeID(null);
    }

    public static String takeId(String parentId) {
        return takeID(IDCoder.decode(parentId));
    }

    private static String takeID(byte[] dbNo) {
        if (dbNo == null)
            dbNo = IDConfig.getInstance().getOne();
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

    public static void setId(Object object) {
        try {
            IdFields fields = getIdFields(object.getClass());
            byte[] dbNo = null;
            if (fields.pid != null) {
                dbNo = IDCoder.decode((String) fields.pid.get(object));
            }
            fields.id.set(object, takeID(dbNo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<Class<?>, IdFields> fieldsMap = new ConcurrentHashMap<>();

    private static class IdFields {
        Field id;
        Field pid;
    }

    private static IdFields getIdFields(Class<?> clazz) {
        return fieldsMap.computeIfAbsent(clazz, c -> {
            try {
                IdFields fields = new IdFields();
                for (Field field : c.getDeclaredFields()) {
                    if (field.getName().equals("id")) {
                        fields.id = field;
                        field.setAccessible(true);
                    } else if (field.getName().equals("parentId") || field.getName().equals("parent_id")) {
                        fields.pid = field;
                        field.setAccessible(true);
                    }
                }
                return fields;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static int getDbNo(String id) {
        byte[] bytes = IDCoder.decode(id);
        return ((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff);
    }

    public static void main(String[] args) throws Exception {
//        new IDConfig("65530-65535");
//        for (int i = 0; i < 10; ++i) {
//            String id = IDGenerator.takeID(null);
//            System.out.println(id + "\t" + getDbNo(id));
//        }
//
//
//        String id = IDGenerator.takeID(null);
//        for (int i = 0; i < 10; ++i) {
//            String id2 = IDGenerator.takeID(IDCoder.decode(id));
//            System.out.println(id2 + "\t" + getDbNo(id2));
//        }

        new IDConfig("0");

        int sum = 0;

        do {
            final String id1 = IDGenerator.takeId();

            TimeUnit.MICROSECONDS.sleep(1);

            final String id2 = IDGenerator.takeId();

            if (id2.compareTo(id1) < 0) {
                System.out.println(sum);
                throw new RuntimeException();
            }

            sum++;
        } while (true);
    }
}
