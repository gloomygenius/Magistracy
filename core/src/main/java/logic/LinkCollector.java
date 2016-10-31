package logic;

import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public String getLink(String propertyName) throws Exception {
        loadProperty(propertyName);
        checkInputData(propertyName);
        StringBuilder linkBuilder = new StringBuilder("http://")
                .append(prop.getProperty("dataSource")).append(".sci.gsfc.nasa.gov/dods/")
                .append(prop.getProperty("dataSet")).append(".ascii?")
                .append(prop.getProperty("parameter"))
                .append("[").append(generateTimeIndex(timeStart)).append(":")
                .append(generateTimeIndex(timeEnd)).append("]").append("[")
                .append(genLatIndex(latitude)).append("]").append("[")
                .append(genLonIndex(longitude)).append("]");

        return linkBuilder.toString();
    }

    private void checkInputData(String propName) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime timeStart = LocalDateTime.parse(prop.getProperty("defaultTime"), formatter);
        boolean error=false;
        StringBuffer errorMassage = new StringBuffer();
        if ((this.timeStart.isBefore(timeStart)) || (timeEnd.isAfter(LocalDateTime.now()))
                ||this.timeStart.isAfter(timeEnd)) {
            errorMassage.append(propName + ": Неправильная дата\r\n");
            error=true;
        }
        if (latitude < Double.parseDouble(prop.getProperty("minLatitude")) ||
                latitude > Double.parseDouble(prop.getProperty("maxLatitude"))) {
            errorMassage.append(propName + ": Неправильная широта\r\n");
            error=true;
        }
        if (longitude < Double.parseDouble(prop.getProperty("minLongitude")) ||
                longitude > Double.parseDouble(prop.getProperty("maxLongitude"))) {
            errorMassage.append(propName + ": Неправильная долгота");
            error=true;
        }
        if (error) throw new Exception(errorMassage.toString());
    }

    private void loadProperty(String propertyName) {
        try (InputStream stream = getClass().getResourceAsStream("/gui/" + propertyName + ".properties")) {
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