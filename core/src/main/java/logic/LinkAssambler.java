package logic;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LinkAssambler {
    private double latStart;
    private double latEnd;
    private double logStart;
    private double logEnd;
    private Date timeStart;
    private Date timeEnd;


    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        try {
            this.timeStart = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(timeStart);
        }catch (Exception e) {e.printStackTrace();}
    }
}
