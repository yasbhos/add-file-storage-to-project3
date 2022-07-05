package ir.ac.kntu.menu.main.admin;

public enum AdminMainMenuOption {
    USERS,
    COURSES,
    CONTESTS,
    QUESTIONS,
    ACCOUNT,
    LOGOUT;

    public enum UsersSubMenu {
        LIST_OF_USERS,
        ADD_NEW_ADMIN,
        BACK
    }

    public enum CoursesSubMenu {
        ADD_COURSE,
        REMOVE_COURSE,
        LIST_OF_COURSES,
        BACK
    }

    public enum ContestsSubMenu {
        ADD_CONTEST,
        REMOVE_CONTEST,
        LIST_OF_CONTESTS,
        BACK
    }

    public enum QuestionsSubMenu {
        ADD_QUESTION,
        REMOVE_QUESTION,
        LIST_OF_QUESTIONS,
        BACK
    }
}
