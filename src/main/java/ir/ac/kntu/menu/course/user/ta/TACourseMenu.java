package ir.ac.kntu.menu.course.user.ta;

import ir.ac.kntu.db.QuestionDB;
import ir.ac.kntu.db.UserDB;
import ir.ac.kntu.menu.Menu;
import ir.ac.kntu.model.course.Assignment;
import ir.ac.kntu.model.course.Course;
import ir.ac.kntu.model.User;
import ir.ac.kntu.model.question.Question;
import ir.ac.kntu.util.QuestionUtility;
import ir.ac.kntu.util.ScannerWrapper;

/**
 * Represents Teacher Assistant class
 */
public class TACourseMenu implements Menu {
    private final User ta;

    private final Course course;

    private final UserDB userDB;

    private final QuestionDB questionDB;


    public TACourseMenu(User ta, Course course, UserDB userDB, QuestionDB questionDB) {
        this.ta = ta;
        this.course = course;
        this.userDB = userDB;
        this.questionDB = questionDB;
    }

    @Override
    public void menu() {
        TACourseMenuOption taCourseMenuOption;
        do {
            taCourseMenuOption = ScannerWrapper.getInstance().readEnum(TACourseMenuOption.values(),
                    "TA COURSE MENU");
            handleTheOption(taCourseMenuOption);
        } while (taCourseMenuOption != TACourseMenuOption.BACK);
    }

    @Override
    public <T extends Enum<T>> void handleTheOption(T option) {
        switch ((TACourseMenuOption) option) {
            case ADD_QUESTION_TO_ASSIGNMENT -> addQuestionToAssignment();
            case SET_LECTURER -> setLecturer();
            default -> {
            }
        }
    }

    private void addQuestionToAssignment() {
        Assignment assignment = course.searchAssignment();
        if (assignment == null) {
            return;
        }

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

    private void setLecturer() {
        User lecturer = userDB.getUser();
        if (lecturer == null) {
            return;
        }

        course.setLecturer(lecturer);
        System.out.println("Successfully set");
    }
}
