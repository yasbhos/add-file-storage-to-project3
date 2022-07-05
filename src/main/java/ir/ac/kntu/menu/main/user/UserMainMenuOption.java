package ir.ac.kntu.menu.main.user;

public enum UserMainMenuOption {
    COURSES,
    CONTESTS,
    QUESTIONS,
    ACCOUNT,
    LOGOUT;

    public enum CoursesSubMenu {
        ADD_COURSE,
        LIST_OF_COURSES,
        REGISTER_TO_COURSE,
        BACK
    }

    public enum ContestsSubMenu {
        LIST_OF_CONTESTS,
        REGISTER_TO_CONTEST,
        BACK
    }

    public enum QuestionsSubMenu {
        ADD_QUESTION,
        LIST_OF_QUESTIONS,
        BACK
    }
}
