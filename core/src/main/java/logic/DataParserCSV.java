package logic;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParserCSV {
    private File file;
    private HashMap<LocalDateTime, Double> data = new HashMap<>();
    @Getter
    private String parameter;
    @Getter
    private LocalDateTime timeStart = null;

    public DataParserCSV() {
    }

    public DataParserCSV(File file) {
        this.file = file;
    }

    public String parseAndConvertData(String text) {
        Pattern timePattern = Pattern.compile("time, \\[\\d+\\]\\D*(\\d+.\\d+)");
        Matcher matcher = timePattern.matcher(text);
        StringBuilder builder = new StringBuilder();
        if (matcher.find()) {
            timeStart = getTimeFromDouble(Double.parseDouble(matcher.group(1)));
        }
        LocalDateTime time;
        Pattern pattern = Pattern.compile("\\[(\\d+)\\]\\[\\d\\], (-*\\d.\\d+E*.\\d*)");
        matcher = pattern.matcher(text);
        double value;
        builder.append("Time;Value\r\n");
        while (matcher.find()) {
            assert timeStart != null;
            time = timeStart.plusHours(3 * Long.parseLong(matcher.group(1)));
            value = Double.parseDouble(matcher.group(2));
            System.out.println(time.toString() + " " + value);
            builder
                    .append(formatDate(time.getDayOfMonth(), 2)).append(".")
                    .append(formatDate(time.getMonthValue(), 2)).append(".")
                    .append(time.getYear())
                    .append(" ")
                    .append(formatDate(time.getHour(), 2)).append(":")
                    .append(formatDate(time.getMinute(), 2)).append(";")
                    .append(value)
                    .append("\r\n");
            data.put(time, value);
        }

        Pattern paramPattern;
        paramPattern = Pattern.compile("(\\w+)");
        matcher = paramPattern.matcher(text);
        if (matcher.find()) parameter = matcher.group(1);
        return builder.toString();
    }

    private String formatDate(int number, int digits) {
        String stringNum = String.valueOf(number);
        while (stringNum.length() < digits) {
            stringNum = "0" + stringNum;
        }
        return stringNum;
    }

    public static HashMap<LocalDateTime, Double> parseFromCSV(File file) {
        HashMap<LocalDateTime, Double> map = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buf = new byte[fis.available()];
            fis.read(buf);
            String[] rows = new String(buf).trim().split("\r\n");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            for (int i=1; i < rows.length; i++) {
                String[] columns = rows[i].split(";");
                map.put(LocalDateTime.parse(columns[0], formatter), Double.parseDouble(columns[1]));
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private LocalDateTime getTimeFromDouble(double index) {
        LocalDateTime date = LocalDateTime.of(0000, 12, 30, 0, 0);
        date = date.plusDays((long) index);
        return date;
    }
}