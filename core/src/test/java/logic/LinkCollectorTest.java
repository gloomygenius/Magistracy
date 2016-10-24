package logic;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class LinkCollectorTest {
    LinkCollector link = new LinkCollector();

    @Test
    public void setTimeTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime timer = LocalDateTime.parse("24.02.2000 00:00", formatter);
        LocalDateTime timer1 = LocalDateTime.parse("25.02.2000 21:00", formatter);
        link.setTimeStart(timer);
        link.setTimeEnd(timer1);
        link.setLatitude(-59.5);
        link.setLongitude(179.5);
        System.out.println(link.getLink("solar"));
    }
}