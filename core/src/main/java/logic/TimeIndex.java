package logic;

import java.time.LocalDateTime;

public class TimeIndex {
    private static final LocalDateTime defaultTime = LocalDateTime.of(2000, 02, 24, 00, 00);

    public static int getIndex(LocalDateTime time) {
        int index = -1;
        System.out.println("метка 1");
        int days=time.getDayOfYear();
        System.out.println("метка 2");
        int year=time.getYear();
        while (year>2000){
            days+=getYearDays(year-1);
            year--;
        }
        index = (days - defaultTime.getDayOfYear()) * 8 + Math.round(time.getHour() / 3);
        System.out.println("метка 3");
        return index;
    }
    private static int getYearDays(int year){
        if (year%4==0) return 366;
        return 365;
    }
}