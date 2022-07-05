package ir.ac.kntu.util;

import ir.ac.kntu.model.course.Course;
import ir.ac.kntu.model.DateTime;
import ir.ac.kntu.model.User;
import ir.ac.kntu.model.course.Course.Status;

public class CourseUtility {
    public static Course readCourse(User owner, String massage) {
        System.out.println(massage);
        String name = ScannerWrapper.getInstance().readString("Enter name: ");
        String institute = ScannerWrapper.getInstance().readString("Enter institute: ");
        DateTime startDate = DateTimeUtility.readDate("Enter start date");
        Status status = ScannerWrapper.getInstance().readEnum(Status.values(), "COURSE STATUS");
        String password = null;
        if (status == Status.OPEN_PRIVATE) {
            password = ScannerWrapper.getInstance().readPassword("Enter password: ");
            if (password == null) {
                return null;
            }
        }
        String description = ScannerWrapper.getInstance().readString("Enter description: ");
        return new Course(owner, name, institute, startDate, status, password, description);
    }

}
