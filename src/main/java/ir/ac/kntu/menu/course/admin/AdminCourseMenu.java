package ir.ac.kntu.menu.course.admin;

import ir.ac.kntu.db.AdminDB;
import ir.ac.kntu.db.QuestionDB;
import ir.ac.kntu.db.UserDB;
import ir.ac.kntu.menu.Menu;
import ir.ac.kntu.menu.account.AccountMenu;
import ir.ac.kntu.model.course.Assignment;
import ir.ac.kntu.model.course.Course;
import ir.ac.kntu.model.DateTime;
import ir.ac.kntu.model.User;
import ir.ac.kntu.model.question.Question;
import ir.ac.kntu.util.*;

public class AdminCourseMenu implements Menu {
    private final User currentAdmin;

    private final Course course;

    private final AdminDB adminDB;

    private final UserDB userDB;

    private final QuestionDB questionDB;


    public AdminCourseMenu(User currentAdmin, Course course, AdminDB adminDB, UserDB userDB, QuestionDB questionDB) {
        this.currentAdmin = currentAdmin;
        this.course = course;
        this.adminDB = adminDB;
        this.userDB = userDB;
        this.questionDB = questionDB;
    }

    @Override
    public void menu() {
        AdminCourseMenuOption adminCourseMenuOption;
        do {
            adminCourseMenuOption = ScannerWrapper.getInstance().readEnum(AdminCourseMenuOption.values(),
                    "ADMIN COURSE MENU");
            handleTheOption(adminCourseMenuOption);
        } while (adminCourseMenuOption != AdminCourseMenuOption.BACK);
    }

    @Override
    public <T extends Enum<T>> void handleTheOption(T option) {
        switch ((AdminCourseMenuOption) option) {
            case EDIT_NAME -> editName();
            case EDIT_INSTITUTE -> editInstitute();
            case EDIT_LECTURER -> editLecturer();
            case EDIT_START_DATETIME -> editStartDateTime();
            case EDIT_STATUS -> editStatus();
            case EDIT_PASSWORD -> editPassword();
            case EDIT_DESCRIPTION -> editDescription();
            case EDIT_LIST_OF_STUDENTS -> editListOfStudents();
            case EDIT_LIST_OF_ASSIGNMENTS -> editListOfAssignments();
            case EDIT_LIST_OF_TEACHER_ASSISTANTS -> editListOfTAs();
            default -> {
            }
        }
    }

    private void editName() {
        String name = ScannerWrapper.getInstance().readString("Enter new name: ");
        course.setName(name);
        System.out.println("Successfully changed");
    }

    private void editInstitute() {
        String institute = ScannerWrapper.getInstance().readString("Enter new institute: ");
        course.setInstitute(institute);
        System.out.println("Successfully changed");
    }

    private void editLecturer() {
        User user = userDB.getUser();
        if (user == null) {
            return;
        }

        course.setLecturer(user);
        System.out.println("Successfully changed");
    }

    private void editStartDateTime() {
        DateTime dateTime = DateTimeUtility.readDateTime("Enter new date and time");
        course.setStartDate(dateTime);
        System.out.println("Successfully changed");
    }

    private void editStatus() {
        System.out.println("Enter new status: ");
        Course.Status status = ScannerWrapper.getInstance().readEnum(Course.Status.values());
        course.setStatus(status);
        System.out.println("Successfully changed");
    }

    private void editPassword() {
        String oldPassword = ScannerWrapper.getInstance().readPassword("Enter old password: ");
        if (!course.getHashedPassword().equals(Cipher.sha256(oldPassword))) {
            System.out.println("Wrong password");
            return;
        }

        String password = ScannerWrapper.getInstance().readPassword("Enter new password: ");
        course.setPassword(password);
        System.out.println("Successfully changed");
    }

    private void editDescription() {
        String description = ScannerWrapper.getInstance().readString("Enter new description: ");
        course.setDescription(description);
        System.out.println("Successfully changed");
    }

    private void editListOfStudents() {
        enum Option {
            ADD_STUDENT,
            REMOVE_STUDENT,
            EDIT_STUDENT_ATTRIBUTES,
            BACK
        }

        Option option = ScannerWrapper.getInstance().readEnum(Option.values());
        switch (option) {
            case ADD_STUDENT -> addStudent();
            case REMOVE_STUDENT -> removeStudent();
            case EDIT_STUDENT_ATTRIBUTES -> editStudentAttributes();
            default -> {
            }
        }
    }

    private void addStudent() {
        User student = userDB.getUser();
        if (student == null) {
            return;
        }

        course.addStudent(student);
        System.out.println("Successfully added");
    }

    private void removeStudent() {
        User student = course.searchStudent();
        if (student == null) {
            return;
        }

        System.out.println(student);
        System.out.println();

        course.removeStudent(student);
        System.out.println("Successfully removed");
    }

    private void editStudentAttributes() {
        User student = course.searchStudent();
        if (student == null) {
            return;
        }

        System.out.println(student);
        System.out.println();

        AccountMenu accountMenu = new AccountMenu(student, adminDB, userDB);
        accountMenu.menu();
    }

    private void editListOfAssignments() {
        enum Option {
            ADD_ASSIGNMENT,
            REMOVED_ASSIGNMENT,
            EDIT_ASSIGNMENT_ATTRIBUTES,
            ADD_QUESTION_TO_ASSIGNMENT,
            BACK
        }

        Option option = ScannerWrapper.getInstance().readEnum(Option.values());
        switch (option) {
            case ADD_ASSIGNMENT -> addAssignment();
            case REMOVED_ASSIGNMENT -> removeAssignment();
            case EDIT_ASSIGNMENT_ATTRIBUTES -> editAssignmentAttributes();
            default -> {
            }
        }
    }

    private void addAssignment() {
        Assignment assignment = AssignmentUtility.readAssignment("Enter assignment attributes");
        if (assignment == null) {
            return;
        }

        course.addAssignment(assignment);
        System.out.println("Successfully added");
    }

    private void removeAssignment() {
        Assignment assignment = course.searchAssignment();
        if (assignment == null) {
            return;
        }

        System.out.println(assignment);
        System.out.println();

        course.removeAssignment(assignment);
        System.out.println("Successfully removed");
    }

    private void editAssignmentAttributes() {
        Assignment assignment = course.searchAssignment();
        if (assignment == null) {
            return;
        }

        System.out.println(assignment);
        System.out.println();

        //TODO: implement this options for both admin and lecturer
        enum Option {
            EDIT_NAME,
            EDIT_DESCRIPTION,
            EDIT_START_DATETIME,
            EDIT_END_DATETIME,
            EDIT_DELAY_DATETIME,
            EDIT_DELAY_COEFFICIENT,
            EDIT_ASSIGNMENT_STATUS,
            EDIT_SCOREBOARD_STATUS,
            ADD_QUESTION
        }

        Option option = ScannerWrapper.getInstance().readEnum(Option.values());
        switch (option) {
            case ADD_QUESTION -> addQuestion(assignment);
            default -> {
            }
        }
    }

    private void addQuestion(Assignment assignment) {
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

    private void editListOfTAs() {
        enum Option {
            ADD_TA,
            REMOVED_TA,
            EDIT_TA_ATTRIBUTES,
            BACK
        }

        Option option = ScannerWrapper.getInstance().readEnum(Option.values());
        switch (option) {
            case ADD_TA -> addTA();
            case REMOVED_TA -> removeTA();
            case EDIT_TA_ATTRIBUTES -> editTAAttributes();
            default -> {
            }
        }
    }

    private void addTA() {
        User ta = userDB.getUser();
        if (ta == null) {
            return;
        }

        course.addTA(ta);
        System.out.println("Successfully added");
    }

    private void removeTA() {
        User ta = course.searchTA();
        if (ta == null) {
            return;
        }

        course.removeTA(ta);
        System.out.println("Successfully removed");
    }

    private void editTAAttributes() {
        User ta = course.searchTA();
        if (ta == null) {
            return;
        }

        System.out.println(ta);
        System.out.println();
        AccountMenu accountMenu = new AccountMenu(ta, adminDB, userDB);
        accountMenu.menu();
    }
}
