package ir.ac.kntu.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class GroupResponder implements Comparable<GroupResponder>, Serializable {
    private String name;

    private final ArrayList<User> members;

    private double totalScore;

    private DateTime averageSentDT;

    @java.io.Serial
    private static final long serialVersionUID = 120L;

    public GroupResponder(String name, ArrayList<User> members) {
        this.name = name;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean addMember(User member) {
        return members.add(member);
    }

    public ArrayList<User> getMembers() {
        return new ArrayList<>(members);
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
    public int compareTo(GroupResponder o) {
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
        return name + " | " + totalScore + " | " + averageSentDT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupResponder group)) {
            return false;
        }

        return Objects.equals(name, group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
