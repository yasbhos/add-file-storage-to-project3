package ir.ac.kntu.db;

import ir.ac.kntu.model.User;
import ir.ac.kntu.model.course.Course;
import ir.ac.kntu.util.ScannerWrapper;

import java.io.*;
import java.util.ArrayList;

public class CourseDB {
    private ArrayList<Course> courses;

    public CourseDB() {
        loadCoursesInfo();
    }

    public boolean addCourse(Course course) {
        if (courses.add(course)) {
            saveCoursesInfo();
            return true;
        }
        return false;
    }

    public boolean removeCourse(Course course) {
        if (courses.remove(course)) {
            saveCoursesInfo();
            return true;
        }
        return false;
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

    private void loadCoursesInfo() {
        ArrayList<Course> courses = new ArrayList<>();
        File file = new File("courses.info");
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
            while (true) {
                try {
                    Course course = (Course) inputStream.readObject();
                    courses.add(course);
                } catch (EOFException e) {
                    break;
                } catch (Exception e) {
                    System.out.println("Problem with some of the records in the courses data file");
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("No previous data for courses has been saved.");
        }

        this.courses = courses;
    }

    public void saveCoursesInfo() {
        File file = new File("courses.info");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
            for (Course course : courses) {
                try {
                    outputStream.writeObject(course);
                } catch (IOException e) {
                    System.out.println("(CourseDB::saveCoursesInfo): An error occurred while trying to save info");
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("(CourseDB::saveCoursesInfo): An error occurred while trying to save info");
            System.out.println(e.getMessage());
        }
    }
}
