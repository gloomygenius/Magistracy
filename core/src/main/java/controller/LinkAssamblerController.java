package controller;

import gui.DownloadFrame;
import logic.LinkCollector;

import javax.swing.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class LinkAssamblerController {

    public static String getLink(DownloadFrame frame) {
        LinkCollector link = new LinkCollector();
        Map<String, JTextField> textFieldMap = frame.getTextFieldMap();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            LocalDateTime timeStart = LocalDateTime.parse(textFieldMap.get("Диапазон времени 1").getText(), formatter);
            link.setTimeStart(timeStart);

            LocalDateTime timeEnd = LocalDateTime.parse(textFieldMap.get("Диапазон времени 2").getText(), formatter);
            link.setTimeEnd(timeEnd);

            // парсим и записываем в LinkAssembler

            double longitude = Double.parseDouble(textFieldMap.get("Долгота 1").getText());
            link.setLongitude(longitude);
            double latitude = Double.parseDouble(textFieldMap.get("Широта 1").getText());
            link.setLatitude(latitude);

            for (Map.Entry<String, Boolean> entry : frame.getCheckBoxItems().entrySet()) {
                if (entry.getValue()) return link.getLink(entry.getKey());
            }
        } catch (Exception e) {
            return "Ошибка! "+e.getLocalizedMessage();
        }
        return "";
    }
}