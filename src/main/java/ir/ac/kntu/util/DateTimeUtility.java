package ir.ac.kntu.util;

import ir.ac.kntu.model.DateTime;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class DateTimeUtility {
    public static DateTime now() {
        LocalDateTime ldt = LocalDateTime.now();

        return new DateTime(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond());
    }

    public static DateTime readDate(String message) {
        System.out.println(message);
        int year = ScannerWrapper.getInstance().readInt("Year: ");
        int month = ScannerWrapper.getInstance().readInt("Month: ");
        int day = ScannerWrapper.getInstance().readInt("Day: ");

        return new DateTime(year, month, day, 0, 0, 0);
    }

    public static DateTime readDateTime(String message) {
        System.out.println(message);
        int year = ScannerWrapper.getInstance().readInt("Year: ");
        int month = ScannerWrapper.getInstance().readInt("Month: ");
        int day = ScannerWrapper.getInstance().readInt("Day: ");
        int hour = ScannerWrapper.getInstance().readInt("Hour: ");
        int minute = ScannerWrapper.getInstance().readInt("Minute: ");
        int second = ScannerWrapper.getInstance().readInt("Second: ");

        return new DateTime(year, month, day, hour, minute, second);
    }

    public static boolean isLeapYear(int year) {
        double a = 0.025;
        double b = 266;
        double leapDays0, leapDays1;
        int frac0, frac1;
        if (year > 0) {
            leapDays0 = ((year + 38) % 2820) * 0.24219 + a;  //0.24219 ~ extra days of one year
            leapDays1 = ((year + 39) % 2820) * 0.24219 + a;  //38 days is the difference of epoch to
            //2820-year cycle
        } else if (year < 0) {
            leapDays0 = ((year + 39) % 2820) * 0.24219 + a;
            leapDays1 = ((year + 40) % 2820) * 0.24219 + a;
        } else {
            return false;
        }

        frac0 = (int) ((leapDays0 - (int) (leapDays0)) * 1000);
        frac1 = (int) ((leapDays1 - (int) (leapDays1)) * 1000);

        return frac0 <= b && frac1 > b;
    }

    public static DateTime getAverageSentDateTimes(ArrayList<DateTime> dateTimes) {
        long seconds = 0;
        for (DateTime dateTime : dateTimes) {
            LocalDateTime localDateTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(),
                    dateTime.getDay(), dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
            seconds += localDateTime.toEpochSecond(ZoneOffset.UTC);
        }

        seconds /= dateTimes.size();
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);

        return new DateTime(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth(),
                localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond());
    }
}
