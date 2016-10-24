package controller;

import gui.DownloadFrame;
import logic.LinkCollector;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class LinkAssamblerController {

    public static String getLink(DownloadFrame frame) {
        LinkCollector link = new LinkCollector();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime timeStart = LocalDateTime.parse(frame.getTimeStartField().getText(), formatter);
        link.setTimeStart(timeStart);

        LocalDateTime timeEnd = LocalDateTime.parse(frame.getTimeEndField().getText(), formatter);
        link.setTimeEnd(timeEnd);

        // парсим и записываем в LinkAssembler

        double longitude = Double.parseDouble(frame.getLongitudeField().getText());
        link.setLongitude(longitude);
        double latitude = Double.parseDouble(frame.getLatitudeField().getText());
        link.setLatitude(latitude);

        for (Map.Entry<String, Boolean> entry : frame.getCheckBoxItems().entrySet()) {
            if (entry.getValue()==true) return link.getLink(entry.getKey());
        }

        return null;
    }
}
