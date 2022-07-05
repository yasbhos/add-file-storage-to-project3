package ir.ac.kntu.menu.guest;

import ir.ac.kntu.db.ContestDB;
import ir.ac.kntu.db.QuestionDB;
import ir.ac.kntu.menu.Menu;
import ir.ac.kntu.model.contest.Contest;
import ir.ac.kntu.model.question.Question;
import ir.ac.kntu.util.ScannerWrapper;

public class GuestMenu implements Menu {
    private ContestDB contestDB;

    private QuestionDB questionDB;

    public GuestMenu(ContestDB contestDB, QuestionDB questionDB) {
        this.contestDB = contestDB;
        this.questionDB = questionDB;
    }

    @Override
    public void menu() {
        GuestMenuOption guestMenuOption;
        do {
            guestMenuOption = ScannerWrapper.getInstance().readEnum(GuestMenuOption.values(), "GUEST MENU");
            handleTheOption(guestMenuOption);
        } while (guestMenuOption != GuestMenuOption.BACK);
    }

    @Override
    public <T extends Enum<T>> void handleTheOption(T option) {
        switch ((GuestMenuOption) option) {
            case LIST_OF_QUESTIONS -> listOfQuestions();
            case LIST_OF_CONTESTS -> listOfContests();
            default -> {
            }
        }
    }

    private void listOfQuestions() {
        System.out.println("Bank of questions");
        Question question = questionDB.getQuestion();
        if (question == null) {
            return;
        }

        System.out.println(question);
    }

    private void listOfContests() {
        System.out.println("List of finished contests");
        Contest contest = contestDB.getContestForGuest();
        if (contest == null) {
            return;
        }

        System.out.println(contest);
    }

}
