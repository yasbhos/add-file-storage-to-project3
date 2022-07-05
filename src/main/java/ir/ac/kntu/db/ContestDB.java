package ir.ac.kntu.db;

import ir.ac.kntu.model.contest.Contest;
import ir.ac.kntu.model.contest.NormalContest;
import ir.ac.kntu.model.contest.PrivateContest;
import ir.ac.kntu.model.User;
import ir.ac.kntu.model.contest.SpecialContest;
import ir.ac.kntu.util.DateTimeUtility;
import ir.ac.kntu.util.ScannerWrapper;

import java.io.*;
import java.util.ArrayList;

public class ContestDB {
    private ArrayList<Contest> contests;

    public ContestDB() {
        loadUContestsInfo();
    }

    public boolean addContest(Contest contest) {
        if (contests.add(contest)) {
            saveContestsInfo();
            return true;
        }
        return false;
    }

    public boolean removeContest(Contest contest) {
        if (contests.remove(contest)) {
            saveContestsInfo();
            return true;
        }
        return false;
    }

    public boolean containsContest(Contest contest) {
        return contests.contains(contest);
    }

    public Contest getContestForAdmin() {
        for (Contest contest : contests) {
            System.out.println("Id: " + contest.getId() + ", Name: " + contest.getName());
        }

        System.out.println();
        String name = ScannerWrapper.getInstance().readString("Enter contest name: ");
        for (Contest contest : contests) {
            if (contest.getName().equals(name)) {
                System.out.println("Id: " + contest.getId() + ", Name: " + contest.getName());
            }
        }
        String id = ScannerWrapper.getInstance().readString("Enter contest Id: ");
        Contest contest = getContestById(id);
        if (contest == null) {
            System.out.println("Invalid Id");
            return null;
        }

        return contest;
    }

    public Contest getContestForUser(User currentUser) {
        for (Contest contest : contests) {
            if (checkContestGuards(currentUser, contest)) {
                System.out.println("Id: " + contest.getId() + ", Name: " + contest.getName());
            }
        }

        System.out.println();
        String name = ScannerWrapper.getInstance().readString("Enter contest name: ");
        for (Contest contest : contests) {
            if (!checkContestGuards(currentUser, contest)) {
                continue;
            }

            if (contest.getName().equals(name)) {
                System.out.println("Id: " + contest.getId() + ", Name: " + contest.getName());
            }
        }
        String id = ScannerWrapper.getInstance().readString("Enter contest Id: ");
        Contest contest = getContestById(id);
        if (contest == null) {
            System.out.println("Invalid Id");
            return null;
        }

        return contest;
    }

    private boolean checkContestGuards(User currentUser, Contest contest) {
        if (contest instanceof NormalContest normalContest && !normalContest.isCapacityFilled()) {
            return true;
        }
        if (contest instanceof PrivateContest privateContest && !privateContest.isCapacityFilled()
                && !privateContest.canParticipant(currentUser)) {
            return true;
        }
        return contest instanceof SpecialContest specialContest && !specialContest.isCapacityFilled();
    }

    public Contest getContestForGuest() {
        for (Contest contest : contests) {
            if (contest.getEndDateTime().compareTo(DateTimeUtility.now()) < 0) {
                System.out.println("Id: " + contest.getId() + ", Name: " + contest.getName());
            }
        }

        System.out.println();
        String name = ScannerWrapper.getInstance().readString("Enter contest name: ");
        for (Contest contest : contests) {
            if (contest.getEndDateTime().compareTo(DateTimeUtility.now()) >= 0) {
                continue;
            }
            if (!contest.getName().equals(name)) {
                continue;
            }

            System.out.println("Id: " + contest.getId() + ", Name: " + contest.getName());
        }

        String id = ScannerWrapper.getInstance().readString("Enter contest Id: ");
        Contest contest = getContestById(id);
        if (contest == null) {
            System.out.println("Invalid Id");
            return null;
        }

        return contest;
    }

    public Contest getContestById(String id) {
        return contests.stream().filter(contest -> contest.getId().equals(id)).findFirst().orElse(null);
    }

    private void loadUContestsInfo() {
        ArrayList<Contest> contests = new ArrayList<>();
        File file = new File("contests.info");
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
            while (true) {
                try {
                    Contest contest = (Contest) inputStream.readObject();
                    contests.add(contest);
                } catch (EOFException e) {
                    break;
                } catch (Exception e) {
                    System.out.println("Problem with some of the records in the contests data file");
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("No previous data for contests has been saved.");
        }

        this.contests = contests;
    }

    public void saveContestsInfo() {
        File file = new File("contests.info");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
            for (Contest contest : contests) {
                try {
                    outputStream.writeObject(contest);
                } catch (IOException e) {
                    System.out.println("(ContestsDB::saveContestsInfo): An error occurred while trying to save info");
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("(ContestsDB::saveContestsInfo): An error occurred while trying to save info");
            System.out.println(e.getMessage());
        }
    }
}
