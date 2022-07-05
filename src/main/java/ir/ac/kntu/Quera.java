package ir.ac.kntu;

import ir.ac.kntu.db.*;
import ir.ac.kntu.menu.login.LoginMenu;
import ir.ac.kntu.util.ScannerWrapper;

public class Quera {
    private LoginMenu loginMenu;

    public void start() {
        initialize();
        loginMenu.menu();
        ScannerWrapper.getInstance().close();
    }

    private void initialize() {
        AdminDB adminDB = new AdminDB();
        UserDB userDB = new UserDB();
        CourseDB courseDB = new CourseDB();
        ContestDB contestDB = new ContestDB();
        QuestionDB questionDB = new QuestionDB();

        loginMenu = new LoginMenu(adminDB, userDB, courseDB, contestDB, questionDB);
    }
}
