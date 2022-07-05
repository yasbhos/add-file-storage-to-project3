package ir.ac.kntu.menu.course.user.student;

import ir.ac.kntu.menu.Menu;
import ir.ac.kntu.model.course.Assignment;
import ir.ac.kntu.model.course.Course;
import ir.ac.kntu.model.User;
import ir.ac.kntu.util.ScannerWrapper;

public class StudentCourseMenu implements Menu {
    private User student;

    private Course course;

    public StudentCourseMenu(User student, Course course) {
        this.student = student;
        this.course = course;
    }

    @Override
    public void menu() {
        StudentCourseMenuOption studentCourseMenuOption;
        do {
            studentCourseMenuOption = ScannerWrapper.getInstance().readEnum(StudentCourseMenuOption.values(),
                    "STUDENT COURSE MENU");
            handleTheOption(studentCourseMenuOption);
        } while (studentCourseMenuOption != StudentCourseMenuOption.BACK);
    }

    @Override
    public <T extends Enum<T>> void handleTheOption(T option) {
        switch ((StudentCourseMenuOption) option) {
            case LIST_OF_ASSIGNMENTS -> listOfAssignments();
            default -> {
            }
        }
    }

    private void listOfAssignments() {
        Assignment assignment = course.searchAssignment();
        if (assignment == null) {
            return;
        }

        StudentAssignmentMenu studentAssignmentMenu = new StudentAssignmentMenu(student, assignment);
        studentAssignmentMenu.menu();
    }
}
