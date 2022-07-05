package ir.ac.kntu.menu.contest.admin;

import ir.ac.kntu.db.QuestionDB;
import ir.ac.kntu.db.UserDB;
import ir.ac.kntu.model.contest.NormalContest;
import ir.ac.kntu.util.ExportAsHTML;
import ir.ac.kntu.menu.Menu;
import ir.ac.kntu.menu.question.admin.AdminQuestionMenu;
import ir.ac.kntu.model.*;
import ir.ac.kntu.model.contest.Contest;
import ir.ac.kntu.model.contest.PrivateContest;
import ir.ac.kntu.model.question.Answer;
import ir.ac.kntu.model.question.Question;
import ir.ac.kntu.util.DateTimeUtility;
import ir.ac.kntu.util.QuestionUtility;
import ir.ac.kntu.util.ScannerWrapper;

import java.util.ArrayList;

public class AdminContestMenu implements Menu {
    private final User currentAdmin;

    private final Contest contest;

    private final UserDB userDB;

    private final QuestionDB questionDB;

    public AdminContestMenu(User currentAdmin, Contest contest, UserDB userDB, QuestionDB questionDB) {
        this.currentAdmin = currentAdmin;
        this.contest = contest;
        this.userDB = userDB;
        this.questionDB = questionDB;
    }

    @Override
    public void menu() {
        AdminContestMenuOption adminContestSubMenuOption;
        do {
            adminContestSubMenuOption = ScannerWrapper.getInstance().readEnum(AdminContestMenuOption.values());
            handleTheOption(adminContestSubMenuOption);
        } while (adminContestSubMenuOption != AdminContestMenuOption.BACK);
    }

    @Override
    public <T extends Enum<T>> void handleTheOption(T option) {
        switch ((AdminContestMenuOption) option) {
            case ADD_QUESTION -> addQuestion();
            case REMOVE_QUESTION -> removeQuestion();
            case LIST_OF_QUESTIONS -> listOfQuestions();
            case ADD_USER_TO_CONTEST -> addUserToContest();
            case INVITE_USER_TO_CONTEST -> inviteUserToContest();
            case REGISTER_MARK_TO_FINAL_SENT -> contest.registerMarkToFinalSent();
            case EXPORT_FINAL_ANSWERS_AS_HTML_DOC -> exportFinalAnswerAsHTML();
            case EXPORT_SCOREBOARD_RESULT_AS_HTML_DOC -> contest.exportScoreBoard();
            case SCOREBOARD -> contest.scoreBoard();
            case FINAL_RESULTS -> contest.finalResult(userDB);
            case EDIT_NAME -> editName();
            case EDIT_START_DATETIME -> editStartDateTime();
            case EDIT_END_DATETIME -> editEndDateTime();
            case EDIT_AUTOMATIC_SCORING -> editAutomaticScoring();
            default -> {
            }
        }
    }

    private void addQuestion() {
        enum Option {
            ADD_NEW_QUESTION,
            ADD_EXISTING_QUESTION
        }

        Option option = ScannerWrapper.getInstance().readEnum(Option.values());
        switch (option) {
            case ADD_NEW_QUESTION -> {
                Question question = QuestionUtility.readQuestion("Enter question attributes: ");
                if (question == null) {
                    return;
                }

                question.setObserver(contest);
                contest.addQuestion(question);
                System.out.println("Successfully added");
            }
            case ADD_EXISTING_QUESTION -> {
                Question question = questionDB.getQuestion().deepCopy();
                if (question == null) {
                    return;
                }

                question.setObserver(contest);
                contest.addQuestion(question);
                System.out.println("Successfully added");
            }
            default -> {
            }
        }
    }

    private void removeQuestion() {
        Question question = contest.searchQuestion();
        if (question == null) {
            return;
        }

        contest.removeQuestion(question);
        System.out.println("Successfully removed");
    }

    private void listOfQuestions() {
        Question question = contest.searchQuestion();
        if (question == null) {
            return;
        }

        System.out.println(question);
        System.out.println();
        AdminQuestionMenu adminQuestionMenu = new AdminQuestionMenu(question);
        adminQuestionMenu.menu();
    }

    private void addUserToContest() {
        User target = userDB.getUser();
        if (target == null) {
            return;
        }
        if (contest.containsParticipant(target)) {
            System.out.println("This user has been registered to contest before");
            return;
        }

        contest.addParticipant(target);
    }

    private void inviteUserToContest() {
        if (contest instanceof PrivateContest privateContest) {
            User user = userDB.getUserByNationalCodeOrEmail();
            if (user == null) {
                return;
            }

            privateContest.addWhosCanParticipant(user);
            System.out.println("Successfully invited");
            return;
        }

        System.out.println("This contest is not private");
    }

    private void exportFinalAnswerAsHTML() {
        ArrayList<Answer> answers = new ArrayList<>();
        for (Question question : contest.getQuestions()) {
            for (Question.Responder responder : question.getResponders()) {
                answers.add(responder.getFinalAnswer());
            }
        }

        ExportAsHTML.exportAnswers(answers, "exported-answers.html");
        System.out.println("Successfully exported");
    }

    private void editName() {
        System.out.println("Previous name: " + contest.getName());
        String name = ScannerWrapper.getInstance().readString("Enter new name: ");
        contest.setName(name);
        System.out.println("Successfully changed");
    }

    public void editStartDateTime() {
        DateTime dateTime = DateTimeUtility.readDateTime("Enter new start date and time: ");
        if (dateTime == null) {
            return;
        }

        contest.setStartDateTime(dateTime);
        System.out.println("Successfully changed");
    }

    public void editEndDateTime() {
        DateTime dateTime = DateTimeUtility.readDateTime("Enter new end date and time: ");
        if (dateTime == null) {
            return;
        }

        contest.setEndDateTime(dateTime);
        System.out.println("Successfully changed");
    }

    public void editAutomaticScoring() {
        enum Status {
            AUTOMATIC_SCORING_TRUE,
            AUTOMATIC_SCORING_FALSE
        }

        Status status = ScannerWrapper.getInstance().readEnum(Status.values());
        switch (status) {
            case AUTOMATIC_SCORING_TRUE -> contest.setAutomaticScoring(true);
            case AUTOMATIC_SCORING_FALSE -> contest.setAutomaticScoring(false);
            default -> {
            }
        }
    }
}
