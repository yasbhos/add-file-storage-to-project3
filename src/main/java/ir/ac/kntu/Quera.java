package ir.ac.kntu;

import java.util.ArrayList;

import ir.ac.kntu.db.*;
import ir.ac.kntu.menu.login.LoginMenu;
import ir.ac.kntu.model.*;
import ir.ac.kntu.model.contest.Contest;
import ir.ac.kntu.model.contest.NormalContest;
import ir.ac.kntu.model.course.Course;
import ir.ac.kntu.model.question.ChoiceOneQuestion;
import ir.ac.kntu.model.question.Question;
import ir.ac.kntu.util.ScannerWrapper;

public class Quera {
    private LoginMenu loginMenu;

    public void start() {
        initialize();
        loginMenu.menu();
        ScannerWrapper.getInstance().close();
    }

    private void initialize() {
        AdminDB adminDB = new AdminDB(new ArrayList<>());
        UserDB userDB = new UserDB(new ArrayList<>());
        CourseDB courseDB = new CourseDB(new ArrayList<>());
        ContestDB contestDB = new ContestDB(new ArrayList<>());
        QuestionDB questionDB = new QuestionDB(new ArrayList<>());

        User admin = new User("Hossein", "funlife", "2126",
                "yasbolaghi.sharrahi@gmail.com", "09184878759", "5710111326");

        User user = new User("Mahdi", "react", "1379",
                "niati@gmail.com", "09334969659", "0710020650");

        DateTime startDateTime = new DateTime(2022, 6, 18, 0, 0, 0);
        Course course = new Course(user, "AP", "KNTU", startDateTime, Course.Status.OPEN_PRIVATE,
                "2126", "Advanced Programming with Java language");

        startDateTime = new DateTime(2022, 6, 30, 0, 0, 0);
        DateTime endDateTime = new DateTime(2022, 7, 1, 0, 0, 0);
        Contest normalContest = new NormalContest(admin, "CodeCup4", startDateTime, endDateTime, new ArrayList<>());

        Question question1 = new ChoiceOneQuestion("Test", 25,
                "Select the word that is not one of the OOP pillars:",
                Question.Type.CHOICE_ONE, Question.Level.EASY, "interface", "inheritance",
                "encapsulation", "polymorphism", "a");

        Question question2 = new ChoiceOneQuestion("Car", 100,
                "How many wheels do the car have?",
                Question.Type.CHOICE_ONE, Question.Level.VERY_HARD, "none", "1",
                "2", "4", "d");

        adminDB.addAdmin(admin);
        userDB.addUser(user);
        courseDB.addCourse(course);
        contestDB.addContest(normalContest);
        questionDB.addQuestion(question1);
        questionDB.addQuestion(question2);

        loginMenu = new LoginMenu(adminDB, userDB, courseDB, contestDB, questionDB);
    }
}
