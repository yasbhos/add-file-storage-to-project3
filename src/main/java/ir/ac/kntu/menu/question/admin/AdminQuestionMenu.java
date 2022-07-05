package ir.ac.kntu.menu.question.admin;

import ir.ac.kntu.menu.Menu;
import ir.ac.kntu.model.question.ChoiceOneQuestion;
import ir.ac.kntu.model.question.Question;
import ir.ac.kntu.model.question.ShortAnswerQuestion;
import ir.ac.kntu.util.ScannerWrapper;

public class AdminQuestionMenu implements Menu {
    private Question question;

    public AdminQuestionMenu(Question question) {
        this.question = question;
    }

    @Override
    public void menu() {
        AdminQuestionMenuOption adminQuestionMenuOption;
        do {
            adminQuestionMenuOption = ScannerWrapper.getInstance().readEnum(AdminQuestionMenuOption.values());
            handleTheOption(adminQuestionMenuOption);
        } while (adminQuestionMenuOption != AdminQuestionMenuOption.BACK);
    }

    @Override
    public <T extends Enum<T>> void handleTheOption(T option) {
        switch ((AdminQuestionMenuOption) option) {
            case EDIT_NAME -> editName();
            case EDIT_SCORE -> editScore();
            case EDIT_DESCRIPTION -> editDescription();
            case EDIT_TYPE -> editType();
            case EDIT_LEVEL -> editLevel();
            case EDIT_CORRECT_ANSWER -> editCorrectAnswer();
            case EDIT_QUESTION_OPTIONS -> editQuestionOptions();
            default -> {
            }
        }
    }

    private void editName() {
        String name = ScannerWrapper.getInstance().readString("Enter new name: ");
        question.setName(name);
        System.out.println("Successfully changed");
    }

    private void editScore() {
        double score = ScannerWrapper.getInstance().readDouble("Enter new score: ");
        question.setScore(score);
        System.out.println("Successfully changed");
    }

    private void editDescription() {
        String description = ScannerWrapper.getInstance().readString("Enter new description: ");
        question.setDescription(description);
        System.out.println("Successfully changed");
    }

    private void editType() {
        System.out.println("Enter new type");
        Question.Type type = ScannerWrapper.getInstance().readEnum(Question.Type.values());
        question.setType(type);
        System.out.println("Successfully changed");
    }

    private void editLevel() {
        System.out.println("Enter new level");
        Question.Level level = ScannerWrapper.getInstance().readEnum(Question.Level.values());
        question.setLevel(level);
        System.out.println("Successfully changed");
    }

    private void editCorrectAnswer() {
        if (question instanceof ShortAnswerQuestion shortAnswerQuestion) {
            String answer = ScannerWrapper.getInstance().readString("Enter new correct answer: ");
            shortAnswerQuestion.setCorrectAnswer(answer);
        } else if (question instanceof ChoiceOneQuestion choiceOneQuestion) {
            String answer = ScannerWrapper.getInstance().readString("Enter new correct answer: ");
            choiceOneQuestion.setCorrectAnswer(answer);
        } else {
            System.out.println("This option is not enabled for this question");
        }
    }

    private void editQuestionOptions() {
        if (question instanceof ChoiceOneQuestion choiceOneQuestion) {
            System.out.println("Previous options:");
            System.out.println("a: " + choiceOneQuestion.getOptions().getA());
            System.out.println("b: " + choiceOneQuestion.getOptions().getA());
            System.out.println("c: " + choiceOneQuestion.getOptions().getA());
            System.out.println("d: " + choiceOneQuestion.getOptions().getA());

            System.out.println("Enter new options:");
            String a = ScannerWrapper.getInstance().readString("a: ");
            String b = ScannerWrapper.getInstance().readString("b: ");
            String c = ScannerWrapper.getInstance().readString("c: ");
            String d = ScannerWrapper.getInstance().readString("d: ");
            choiceOneQuestion.setOptions(a, b, c, d);
        } else {
            System.out.println("This option is enabled just for the Choice One Questions");
        }
    }
}
