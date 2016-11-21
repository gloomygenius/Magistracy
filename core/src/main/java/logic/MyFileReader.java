package logic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.stream.Stream;

/**
 * Created by Vasiliy Bobkov on 20.11.2016.
 */
@SuppressWarnings("WeakerAccess")
public abstract class MyFileReader {

    public static String read(File file, Charset charset){
        StringBuilder builder = new StringBuilder();
        try (Stream<String> stream = Files.lines(file.toPath(), charset)) {
            stream.forEach(s -> builder.append(s).append("\r\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
    public static String read(File file) {
        return read(file,Charset.defaultCharset());
    }
}
