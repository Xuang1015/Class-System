package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Iostream {
    public static String input(String path) throws IOException {
        File file = new File(path).getAbsoluteFile();
        try(FileInputStream fileInputStream = new FileInputStream(file)){
            int len = fileInputStream.available();
            byte[] buf = new byte[len];
            fileInputStream.read(buf);
            return new String(buf);
        }
    }
    public static void output(String path,String content) throws IOException {
        File file = new File(path).getAbsoluteFile();
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(content.getBytes());
        }
    }
}
