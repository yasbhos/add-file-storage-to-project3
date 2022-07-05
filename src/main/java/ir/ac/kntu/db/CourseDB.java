package ir.ac.kntu.db;

import ir.ac.kntu.model.course.Course;
import ir.ac.kntu.util.ScannerWrapper;

import java.util.ArrayList;

public class CourseDB {
    private final ArrayList<Course> courses;

    public CourseDB(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public boolean addCourse(Course course) {
        return courses.add(course);
    }

    public boolean removeCourse(Course course) {
        return courses.remove(course);
    }

    public boolean containsCourse(Course course) {
        return courses.contains(course);
    }

    public Course getCourse() {
        enum SearchBy {
            NAME,
            LECTURER,
            INSTITUTE
        }

        SearchBy searchBy = ScannerWrapper.getInstance().readEnum(SearchBy.values(), "SEARCH BY");

        return switch (searchBy) {
            case NAME -> searchCourseByName();
            case LECTURER -> searchCourseByLecturer();
            case INSTITUTE -> searchCourseByInstitute();
        };
    }

    public Course searchCourseByName() {
        String name = ScannerWrapper.getInstance().readString("Enter course name: ");

        for (Course course : courses) {
            if (course.getName().equals(name)) {
                System.out.println("Id: " + course.getId() +
                        ", name: " + course.getName() +
                        ", lecturer: " + course.getLecturer() +
                        ", institute: " + course.getInstitute());
            }
        }
        String id = ScannerWrapper.getInstance().readString("Enter course Id: ");
        Course course = getCourseByID(id);
        if (course == null) {
            System.out.println("Invalid Id");
        }

        return course;
    }


    public Course searchCourseByLecturer() {
        String lecturer = ScannerWrapper.getInstance().readString("Enter course lecturer name: ");

        for (Course course : courses) {
            if (course.getLecturer().getFirstName().equals(lecturer)) {
                System.out.println(course);
            }
        }
        String id = ScannerWrapper.getInstance().readString("Enter course Id: ");
        Course course = getCourseByID(id);
        if (course == null) {
            System.out.println("Invalid Id");
        }

        return course;
    }

    public Course searchCourseByInstitute() {
        String institute = ScannerWrapper.getInstance().readString("Enter course institute: ");

        for (Course course : courses) {
            if (course.getInstitute().equals(institute)) {
                System.out.println("Id: " + course.getId() +
                        ", name: " + course.getName() +
                        ", lecturer: " + course.getLecturer() +
                        ", institute: " + course.getInstitute());
            }
        }
        String id = ScannerWrapper.getInstance().readString("Enter course Id: ");
        Course course = getCourseByID(id);
        if (course == null) {
            System.out.println("Invalid Id");
        }

        return course;
    }

    public Course getCourseByID(String id) {
        return courses.stream().filter(course -> course.getId().equals(id)).findFirst().orElse(null);
    }
}
