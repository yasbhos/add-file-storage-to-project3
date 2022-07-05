package ir.ac.kntu.util;

import ir.ac.kntu.model.*;
import ir.ac.kntu.model.contest.Contest;
import ir.ac.kntu.model.contest.NormalContest;
import ir.ac.kntu.model.contest.PrivateContest;
import ir.ac.kntu.model.contest.SpecialContest;

import java.util.ArrayList;

public class ContestUtility {
    public enum ContestType {
        NORMAL,
        PRIVATE,
        SPECIAL
    }

    public static Contest readContest(User ownerAdmin, String message) {
        System.out.println(message);
        String name = ScannerWrapper.getInstance().readString("Enter name: ");
        DateTime startDateTime = DateTimeUtility.readDateTime("Enter start date and time");
        DateTime endDateTime = DateTimeUtility.readDateTime("Enter end date and time");
        ContestType contestType = ScannerWrapper.getInstance().readEnum(ContestType.values(), "CONTEST TYPE");
        return switch (contestType) {
            case NORMAL -> new NormalContest(ownerAdmin, name, startDateTime, endDateTime, new ArrayList<>());
            case PRIVATE -> new PrivateContest(ownerAdmin, name, startDateTime, endDateTime, new ArrayList<>());
            case SPECIAL -> {
                int maxGroupCapacity = ScannerWrapper.getInstance().readInt("Enter maximum group capacity: ");
                yield new SpecialContest(ownerAdmin, name, startDateTime, endDateTime, new ArrayList<>(), maxGroupCapacity);
            }
        };
    }
}
