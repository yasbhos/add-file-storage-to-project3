package ir.ac.kntu.menu.login;

import ir.ac.kntu.db.*;
import ir.ac.kntu.menu.Menu;
import ir.ac.kntu.menu.main.admin.AdminMainMenu;
import ir.ac.kntu.menu.guest.GuestMenu;
import ir.ac.kntu.menu.main.user.UserMainMenu;
import ir.ac.kntu.model.User;
import ir.ac.kntu.util.ScannerWrapper;
import ir.ac.kntu.util.UserUtility;

public class LoginMenu implements Menu {
    private final AdminDB adminDB;

    private final UserDB userDB;

    private final CourseDB courseDB;

    private final ContestDB contestDB;

    private final QuestionDB questionDB;

    public LoginMenu(AdminDB adminDB, UserDB userDB, CourseDB courseDB, ContestDB contestDB, QuestionDB questionDB) {
        this.adminDB = adminDB;
        this.userDB = userDB;
        this.courseDB = courseDB;
        this.contestDB = contestDB;
        this.questionDB = questionDB;
    }

    @Override
    public void menu() {
        LoginMenuOption option;
        do {
            option = ScannerWrapper.getInstance().readEnum(LoginMenuOption.values(), "LOGIN MENU");
            handleTheOption(option);
        } while (option != LoginMenuOption.EXIT);
    }

    @Override
    public <T extends Enum<T>> void handleTheOption(T option) {
        switch ((LoginMenuOption) option) {
            case SIGN_IN -> signIn();
            case SIGN_UP -> signUp();
            case CONTINUE_AS_GUEST -> continueAsGuest();
            case EXIT -> exit();
            default -> {
            }
        }
    }

    private void signIn() {
        String username = ScannerWrapper.getInstance().readString("Enter username: ");
        String password = ScannerWrapper.getInstance().readPassword("Enter password: ");
        User currentAdmin = adminDB.getAdminByUsernameAndPassword(username, password);
        if (currentAdmin != null) {
            System.out.println("Successfully signed in");
            AdminMainMenu adminMainMenu = new AdminMainMenu(currentAdmin, adminDB, userDB, courseDB, contestDB, questionDB);
            adminMainMenu.menu();
            return;
        }

        User currentUser = userDB.getUserByUsernameAndPassword(username, password);
        if (currentUser != null) {
            System.out.println("Successfully signed in");
            UserMainMenu userMainMenu = new UserMainMenu(currentUser, adminDB, userDB, courseDB, contestDB, questionDB);
            userMainMenu.menu();
            return;
        }

        System.out.println("Invalid username or password");
    }

    private void signUp() {
        User user = UserUtility.readUser("Enter your attributes", adminDB, userDB);
        if (user == null) {
            return;
        }

        userDB.addUser(user);
        System.out.println("Successfully signed up");
    }

    private void continueAsGuest() {
        GuestMenu guestMenu = new GuestMenu(contestDB, questionDB);
        guestMenu.menu();
    }

    private void exit() {
        adminDB.saveAdminsInfo();
        userDB.saveUsersInfo();
        courseDB.saveCoursesInfo();
        contestDB.saveContestsInfo();
        questionDB.saveQuestionsInfo();

        System.exit(0);
    }
}
