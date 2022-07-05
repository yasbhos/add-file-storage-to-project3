package ir.ac.kntu.menu.course.user.lecturer;

import ir.ac.kntu.db.QuestionDB;
import ir.ac.kntu.menu.Menu;
import ir.ac.kntu.menu.question.admin.AdminQuestionMenu;
import ir.ac.kntu.model.course.Assignment;
import ir.ac.kntu.model.question.Question;
import ir.ac.kntu.util.QuestionUtility;
import ir.ac.kntu.util.ScannerWrapper;

public class LecturerAssignmentMenu implements Menu {
    private final Assignment assignment;

    private final QuestionDB questionDB;

    public LecturerAssignmentMenu(Assignment assignment, QuestionDB questionDB) {
        this.assignment = assignment;
        this.questionDB = questionDB;
    }

    @Override
    public void menu() {
        LecturerAssignmentMenuOption lecturerAssignmentMenuOption;
        do {
            lecturerAssignmentMenuOption = ScannerWrapper.getInstance().readEnum(LecturerAssignmentMenuOption.values(),
                    "ASSIGNMENT MENU");
            handleTheOption(lecturerAssignmentMenuOption);
        } while (lecturerAssignmentMenuOption != LecturerAssignmentMenuOption.BACK);
    }

    @Override
    public <T extends Enum<T>> void handleTheOption(T option) {
        switch ((LecturerAssignmentMenuOption) option) {
            case SCOREBOARD -> assignment.scoreBoard();
            case ADD_QUESTION -> addQuestion();
            case REMOVE_QUESTION -> removeQuestion();
            case LIST_OF_QUESTIONS -> listOfQuestions();
            case LIST_OF_ANSWERS_BY_EMAIL -> listOfAnswersByEmail();
            case REGISTER_MARK_TO_FINAL_SENT -> assignment.registerMarkToFinalSent();
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

                question.setObserver(assignment);
                assignment.addQuestion(question);
                System.out.println("Successfully added");
            }
            case ADD_EXISTING_QUESTION -> {
                Question question = questionDB.getQuestion().deepCopy();
                if (question == null) {
                    return;
                }

                question.setObserver(assignment);
                assignment.addQuestion(question);
                System.out.println("Successfully added");
            }
            default -> {
            }
        }
    }

    private void removeQuestion() {
        Question question = assignment.searchQuestion();
        if (question == null) {
            return;
        }

        assignment.removeQuestion(question);
        System.out.println("Successfully removed");
    }

    private void listOfQuestions() {
        Question question = assignment.searchQuestion();
        if (question == null) {
            return;
        }

        System.out.println(question);
        System.out.println();
        AdminQuestionMenu adminQuestionMenu = new AdminQuestionMenu(question);
        adminQuestionMenu.menu();
    }

    private void listOfAnswersByEmail() {
        //TODO: implement this method
    }
}
