package ir.ac.kntu.menu.course.user.lecturer;

import ir.ac.kntu.db.QuestionDB;
import ir.ac.kntu.db.UserDB;
import ir.ac.kntu.menu.Menu;
import ir.ac.kntu.model.course.Assignment;
import ir.ac.kntu.model.course.Course;
import ir.ac.kntu.model.DateTime;
import ir.ac.kntu.model.User;
import ir.ac.kntu.util.AssignmentUtility;
import ir.ac.kntu.util.Cipher;
import ir.ac.kntu.util.DateTimeUtility;
import ir.ac.kntu.util.ScannerWrapper;

public class LecturerCourseMenu implements Menu {
    private User lecturer;

    private Course course;

    private UserDB userDB;

    private QuestionDB questionDB;

    public LecturerCourseMenu(User lecturer, Course course, UserDB userDB, QuestionDB questionDB) {
        this.lecturer = lecturer;
        this.course = course;
        this.userDB = userDB;
        this.questionDB = questionDB;
    }

    @Override
    public void menu() {
        LecturerCourseMenuOption lecturerCourseMenuOption;
        do {
            lecturerCourseMenuOption = ScannerWrapper.getInstance().readEnum(LecturerCourseMenuOption.values(),
                    "LECTURER COURSE MENU");
            handleTheOption(lecturerCourseMenuOption);
        } while (lecturerCourseMenuOption != LecturerCourseMenuOption.BACK);
    }

    @Override
    public <T extends Enum<T>> void handleTheOption(T option) {
        switch ((LecturerCourseMenuOption) option) {
            case REGISTER_STUDENT_TO_COURSE -> registerStudentToCourse();
            case ADD_ASSIGNMENT -> addAssignment();
            case LIST_OF_ASSIGNMENTS -> listOfAssignments();
            case EDIT_NAME -> editName();
            case EDIT_INSTITUTE -> editInstitute();
            case EDIT_START_DATETIME -> editStartDateTime();
            case EDIT_STATUS -> editStatus();
            case EDIT_PASSWORD -> editPassword();
            case EDIT_DESCRIPTION -> editDescription();
            default -> {
            }
        }
    }

    private void registerStudentToCourse() {
        User student = userDB.getUser();
        if (student == null) {
            return;
        }

        course.addStudent(student);
        System.out.println("Successfully added");
    }

    private void addAssignment() {
        Assignment assignment = AssignmentUtility.readAssignment("Enter assignment attributes");
        if (assignment == null) {
            return;
        }

        course.addAssignment(assignment);
        System.out.println("Successfully added");
    }

    private void listOfAssignments() {
        Assignment assignment = course.searchAssignment();
        if (assignment == null) {
            return;
        }

        System.out.println(assignment);
        LecturerAssignmentMenu lecturerAssignmentMenu = new LecturerAssignmentMenu(assignment, questionDB);
        lecturerAssignmentMenu.menu();
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

    private void editStartDateTime() {
        DateTime dateTime = DateTimeUtility.readDateTime("Enter new date-time: ");
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
}
