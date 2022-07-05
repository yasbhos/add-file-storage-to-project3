package ir.ac.kntu.menu.main.user;

import ir.ac.kntu.db.*;
import ir.ac.kntu.menu.Menu;
import ir.ac.kntu.menu.account.AccountMenu;
import ir.ac.kntu.menu.contest.user.UserContestMenu;
import ir.ac.kntu.menu.course.user.lecturer.LecturerCourseMenu;
import ir.ac.kntu.menu.course.user.student.StudentCourseMenu;
import ir.ac.kntu.menu.course.user.ta.TACourseMenu;
import ir.ac.kntu.menu.main.user.UserMainMenuOption.*;
import ir.ac.kntu.menu.question.user.UserQuestionMenu;
import ir.ac.kntu.model.*;
import ir.ac.kntu.model.contest.Contest;
import ir.ac.kntu.model.course.Course;
import ir.ac.kntu.model.question.Question;
import ir.ac.kntu.util.*;

public class UserMainMenu implements Menu {
    private final User currentUser;

    private final AdminDB adminDB;

    private final UserDB userDB;

    private final CourseDB courseDB;

    private final ContestDB contestDB;

    private final QuestionDB questionDB;

    public UserMainMenu(User currentUser, AdminDB adminDB, UserDB userDB, CourseDB courseDB, ContestDB contestDB, QuestionDB questionDB) {
        this.currentUser = currentUser;
        this.adminDB = adminDB;
        this.userDB = userDB;
        this.courseDB = courseDB;
        this.contestDB = contestDB;
        this.questionDB = questionDB;
    }

    @Override
    public void menu() {
        UserMainMenuOption userMainMenuOption;
        do {
            userMainMenuOption = ScannerWrapper.getInstance().readEnum(UserMainMenuOption.values(), "USER MAIN MENU");
            handleTheOption(userMainMenuOption);
        } while (userMainMenuOption != UserMainMenuOption.LOGOUT);
    }

    @Override
    public <T extends Enum<T>> void handleTheOption(T option) {
        switch ((UserMainMenuOption) option) {
            case COURSES -> coursesSubMenu();
            case CONTESTS -> contestsSubMenu();
            case QUESTIONS -> questionsSubMenu();
            case ACCOUNT -> account();
            default -> {
            }
        }
    }

    private void coursesSubMenu() {
        CoursesSubMenu coursesSubMenu;
        do {
            coursesSubMenu = ScannerWrapper.getInstance().readEnum(CoursesSubMenu.values());
            handleCoursesSubMenuOption(coursesSubMenu);
        } while (coursesSubMenu != CoursesSubMenu.BACK);
    }

    private void handleCoursesSubMenuOption(CoursesSubMenu option) {
        switch (option) {
            case ADD_COURSE -> addCourse();
            case LIST_OF_COURSES -> listOfCourses();
            case REGISTER_TO_COURSE -> registerToCourse();
            default -> {
            }
        }
    }

    private void addCourse() {
        Course course = CourseUtility.readCourse(currentUser, "Enter course attributes");
        if (course == null) {
            return;
        }

        courseDB.addCourse(course);
        System.out.println("Successfully added");
    }

    private void listOfCourses() {
        Course course = courseDB.getCourse();
        if (course == null) {
            return;
        }

        System.out.println(course);
        if (course.isStudent(currentUser)) {
            StudentCourseMenu studentCourseMenu = new StudentCourseMenu(currentUser, course);
            studentCourseMenu.menu();
            return;
        }

        if (course.isTA(currentUser)) {
            TACourseMenu taCourseMenu = new TACourseMenu(currentUser, course, userDB, questionDB);
            taCourseMenu.menu();
            return;
        }

        if (course.isLecturer(currentUser)) {
            LecturerCourseMenu lecturerCourseMenu = new LecturerCourseMenu(currentUser, course, userDB, questionDB);
            lecturerCourseMenu.menu();
        }
    }

    private void registerToCourse() {
        Course course = courseDB.getCourse();
        if (!checkCourseGuards(course)) {
            return;
        }

        if (!course.addStudent(currentUser)) {
            System.out.println("Already registered");
            return;
        }
        System.out.println("Successfully registered");
    }

    private boolean checkCourseGuards(Course course) {
        if (course == null) {
            return false;
        }

        if (course.getStatus() == Course.Status.CLOSE) {
            System.out.println("This course is close");
            return false;
        }

        if (course.getStatus() == Course.Status.OPEN_PUBLIC) {
            return true;
        }

        String password = ScannerWrapper.getInstance().readPassword("Enter course password: ");

        return course.getHashedPassword().equals(Cipher.sha256(password));
    }

    private void contestsSubMenu() {
        ContestsSubMenu contestsSubMenu;
        do {
            contestsSubMenu = ScannerWrapper.getInstance().readEnum(ContestsSubMenu.values());
            handleContestsSubMenuOption(contestsSubMenu);
        } while (contestsSubMenu != ContestsSubMenu.BACK);
    }

    private void handleContestsSubMenuOption(ContestsSubMenu option) {
        switch (option) {
            case LIST_OF_CONTESTS -> listOfContests();
            case REGISTER_TO_CONTEST -> registerToContest();
            default -> {
            }
        }
    }

    private void listOfContests() {
        Contest contest = contestDB.getContestForUser(currentUser);
        if (contest == null) {
            return;
        }

        System.out.println(contest);
        System.out.println();
        if (contest.containsParticipant(currentUser)) {
            UserContestMenu userContestMenu = new UserContestMenu(currentUser, contest);
            userContestMenu.menu();
        }
    }

    private void registerToContest() {
        Contest contest = contestDB.getContestForUser(currentUser);
        if (!checkContestGuards(contest)) {
            return;
        }
        if (contest.containsParticipant(currentUser)) {
            System.out.println("You have been registered to contest before");
            return;
        }

        contest.addParticipant(currentUser);
        System.out.println("Successfully registered");
    }

    private boolean checkContestGuards(Contest contest) {
        if (contest == null) {
            return false;
        }
        if (DateTimeUtility.now().compareTo(contest.getStartDateTime()) < 0) {
            System.out.println("The registration deadline has passed");
            return false;
        }

        return true;
    }

    private void questionsSubMenu() {
        QuestionsSubMenu questionsSubMenu;
        do {
            questionsSubMenu = ScannerWrapper.getInstance().readEnum(QuestionsSubMenu.values());
            handleQuestionsSubMenuOption(questionsSubMenu);
        } while (questionsSubMenu != QuestionsSubMenu.BACK);
    }

    private void handleQuestionsSubMenuOption(QuestionsSubMenu option) {
        switch (option) {
            case ADD_QUESTION -> addQuestionToBank();
            case LIST_OF_QUESTIONS -> listOfQuestions();
            default -> {
            }
        }
    }

    private void addQuestionToBank() {
        Question question = QuestionUtility.readQuestion("Enter question attributes");
        if (question == null) {
            return;
        }

        questionDB.addQuestion(question);
        System.out.println("Successfully added");
    }

    private void listOfQuestions() {
        Question question = questionDB.getQuestion();
        if (question == null) {
            return;
        }

        System.out.println(question);
        UserQuestionMenu userQuestionMenu = new UserQuestionMenu(currentUser, question);
        userQuestionMenu.menu();
    }

    private void account() {
        AccountMenu accountMenu = new AccountMenu(currentUser, adminDB, userDB);
        accountMenu.menu();
    }
}
