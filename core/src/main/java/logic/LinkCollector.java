package logic;

import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Setter
public class LinkCollector {
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private double latitude;
    private double longitude;
    private Properties prop = new Properties();

    public String getLink(String propertyName) {
        loadProperty(propertyName);
        StringBuilder linkBuilder;
        linkBuilder = new StringBuilder("http://")
                .append(prop.getProperty("dataSource")).append(".sci.gsfc.nasa.gov/dods/")
                .append(prop.getProperty("dataSet")).append(".ascii?")
                .append(prop.getProperty("parameter"))
                .append("[").append(generateTimeIndex(timeStart)).append(":")
                .append(generateTimeIndex(timeEnd)).append("]").append("[")
                .append(genLatIndex(latitude)).append("]").append("[")
                .append(genLonIndex(longitude)).append("]");

        return linkBuilder.toString();
    }

    private void loadProperty(String propertyName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(propertyName + ".properties").getFile());
        try (FileInputStream stream = new FileInputStream(file)) {
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int generateTimeIndex(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime defaultTime = LocalDateTime.parse(prop.getProperty("defaultTime"), formatter);
        int days = time.getDayOfYear();
        int year = time.getYear();
        while (year > defaultTime.getYear()) {
            days += getYearDays(year - 1);
            year--;
        }
        double timeResolution = Double.parseDouble(prop.getProperty("timeResolution"));
        int dayMultiplier = (int) Math.round(1 / timeResolution);
        int hourMultiplier = (int) Math.round(24 * timeResolution);
        return (days - defaultTime.getDayOfYear()) * dayMultiplier + Math.round(time.getHour() / hourMultiplier);
    }

    private int getYearDays(int year) {
        if (year % 4 == 0) return 366;
        return 365;
    }

    private int genLatIndex(double lat) {
        double minLat = Double.parseDouble(prop.getProperty("minLatitude"));
        double degree = Double.parseDouble(prop.getProperty("degreeLat"));
        return (int) Math.round((lat - minLat) / degree);
    }

    private int genLonIndex(double lon) {
        double minLon = Double.parseDouble(prop.getProperty("minLongitude"));
        double degree = Double.parseDouble(prop.getProperty("degreeLon"));
        return (int) Math.round((lon - minLon) / degree);
    }
}