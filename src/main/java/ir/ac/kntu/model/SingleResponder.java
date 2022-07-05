package ir.ac.kntu.model;

import java.io.Serializable;
import java.util.Objects;

public class SingleResponder implements Comparable<SingleResponder>, Serializable {
    private final String username;

    private double totalScore;

    private DateTime averageSentDT;

    @java.io.Serial
    private static final long serialVersionUID = 100L;

    public SingleResponder(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public DateTime getAverageSentDT() {
        return averageSentDT;
    }

    public void setAverageSentDT(DateTime averageSentDT) {
        this.averageSentDT = averageSentDT;
    }

    @Override
    public int compareTo(SingleResponder o) {
        if (this.totalScore > o.totalScore) {
            return 1;
        }
        if (this.totalScore < o.totalScore) {
            return -1;
        }

        return this.averageSentDT.compareTo(o.averageSentDT);
    }

    @Override
    public String toString() {
        return username + " | " + totalScore + " | " + averageSentDT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SingleResponder responder)) {
            return false;
        }

        return Objects.equals(username, responder.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
