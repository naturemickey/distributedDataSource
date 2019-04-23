package org.w01f.dds;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Test {

    static int sum = 0;

    public static void main(String[] args) throws Exception{


        Files.walk(Paths.get("src/main")).forEach(p -> {
            final File file = p.toFile();
            if (!file.isDirectory() && file.getName().endsWith(".java")) {
                try {
                    final List<String> lines = Files.readAllLines(p);
                    sum += lines.size();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        System.out.println(sum);
    }
}
