package ir.ac.kntu.model;

import ir.ac.kntu.util.DateTimeUtility;

import java.io.Serializable;

public class DateTime implements Comparable<DateTime>, Serializable {
    private int year;

    private int month;

    private int day;

    private int hour;

    private int minute;

    private int second;

    @java.io.Serial
    private static final long serialVersionUID = 179L;

    public DateTime(int year, int month, int day, int hour, int minute, int second) {
        checkAndSetDateTime(year, month, day, hour, minute, second);
    }

    public DateTime(DateTime dateTime) {
        this.year = dateTime.year;
        this.month = dateTime.month;
        this.day = dateTime.day;
        this.hour = dateTime.hour;
        this.minute = dateTime.minute;
        this.second = dateTime.second;
    }

    private void checkAndSetDateTime(int year, int month, int day, int hour, int minute, int second) {
        if (checkInputs(year, month, day, hour, minute, second)) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        } else {
            this.year = 0;
            this.month = 0;
            this.day = 0;
            this.hour = 0;
            this.minute = 0;
            this.second = 0;
        }
    }

    private boolean checkInputs(int year, int month, int day, int hour, int minute, int second) {
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || day > 31) {
            return false;
        }
        if (month > 6 && day == 31) {
            return false;
        }
        if (month == 12 && day == 30 && !DateTimeUtility.isLeapYear(year)) {
            return false;
        }
        if (hour < 0 || hour > 23) {
            return false;
        }
        if (minute < 0 || minute > 59) {
            return false;
        }
        if (second < 0 || second > 59) {
            return false;
        }
        return true;
    }

    public void setYear(int year) {
        checkAndSetDateTime(year, this.month, this.day, this.hour, this.minute, this.second);
    }

    public int getYear() {
        return year;
    }

    public void setMonth(int month) {
        checkAndSetDateTime(this.year, month, this.day, this.hour, this.minute, this.second);
    }

    public int getMonth() {
        return month;
    }

    public void setDay(int day) {
        checkAndSetDateTime(this.year, this.month, day, this.hour, this.minute, this.second);
    }

    public int getDay() {
        return day;
    }

    public void setHour(int hour) {
        checkAndSetDateTime(this.year, this.month, this.day, hour, this.minute, this.second);
    }

    public int getHour() {
        return hour;
    }

    public void setMinute(int minute) {
        checkAndSetDateTime(this.year, this.month, this.day, this.hour, minute, this.second);
    }

    public int getMinute() {
        return minute;
    }

    public void setSecond(int second) {
        checkAndSetDateTime(this.year, this.month, this.day, this.hour, this.minute, second);
    }

    public int getSecond() {
        return second;
    }

    @Override
    public int compareTo(DateTime other) {
        if (this.year > other.year) {
            return 1;
        } else if (this.year < other.year) {
            return -1;
        }
        if (this.month > other.month) {
            return 1;
        } else if (this.month < other.month) {
            return -1;
        }
        if (this.day > other.day) {
            return 1;
        } else if (this.day < other.day) {
            return -1;
        }
        if (this.hour > other.hour) {
            return 1;
        } else if (this.hour < other.hour) {
            return -1;
        }
        if (this.minute > other.minute) {
            return 1;
        } else if (this.minute < other.minute) {
            return -1;
        }
        if (this.second > other.second) {
            return 1;
        } else if (this.second < other.second) {
            return -1;
        }

        return 0;
    }

    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + day;
        result = prime * result + hour;
        result = prime * result + minute;
        result = prime * result + month;
        result = prime * result + second;
        result = prime * result + year;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DateTime other = (DateTime) obj;
        if (day != other.day) {
            return false;
        }
        if (hour != other.hour) {
            return false;
        }
        if (minute != other.minute) {
            return false;
        }
        if (month != other.month) {
            return false;
        }
        if (second != other.second) {
            return false;
        }
        if (year != other.year) {
            return false;
        }

        return true;
    }

}