package logic;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Vasiliy Bobkov on 20.11.2016.
 */
public abstract class CsvConverter {
    public static String of(Map<LocalDateTime, Double> dataMap) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<LocalDateTime, Double> entry : dataMap.entrySet()) {
            builder
                    .append(dateFormat.format(entry.getKey()))
                    .append(";")
                    .append(entry.getValue())
                    .append("\r\n");
        }
        return builder.toString();
    }

    public static Map<LocalDateTime, Double> from(File file) {
        Map<LocalDateTime, Double> map = new TreeMap<>();
        String csvData = MyFileReader.read(file);
        String[] rows = csvData.split("\r\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        for (int i = 1; i < rows.length; i++) {
            String[] columns = rows[i].split(";");
            map.put(LocalDateTime.parse(columns[0], formatter), Double.parseDouble(columns[1]));
        }
        return map;
    }
}
