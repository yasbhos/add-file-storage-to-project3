package ir.ac.kntu.util;

import ir.ac.kntu.model.question.ChoiceOneQuestion;
import ir.ac.kntu.model.question.Question;
import ir.ac.kntu.model.question.Question.Type;
import ir.ac.kntu.model.question.Question.Level;
import ir.ac.kntu.model.question.ShortAnswerQuestion;

public class QuestionUtility {
    public static Question readQuestion(String message) {
        //TODO: make this function clear and more readable
        System.out.println(message);
        String name = ScannerWrapper.getInstance().readString("Enter question name: ");
        double score = ScannerWrapper.getInstance().readDouble("Enter question score: ");
        String description = ScannerWrapper.getInstance().readString("Enter question description: ");
        Type type = ScannerWrapper.getInstance().readEnum(Type.values());
        Level level = ScannerWrapper.getInstance().readEnum(Level.values());
        return switch (type) {
            case CHOICE_ONE -> getQuestion(name, score, description, type, level);
            case SHORT_ANSWER -> {
                String correctAnswer = ScannerWrapper.getInstance().readString("Enter correct answer: ");
                yield new ShortAnswerQuestion(name, score, description, type, level, correctAnswer);
            }
            case LONG_ANSWER, FILL_IN_THE_BLANK -> new Question(name, score, description, type, level);
        };
    }

    private static Question getQuestion(String name, double score, String description, Type type, Level level) {
        System.out.println("Enter Question options");
        String a = ScannerWrapper.getInstance().readString("Enter the text of the option A: ");
        String b = ScannerWrapper.getInstance().readString("Enter the text of the option B: ");
        String c = ScannerWrapper.getInstance().readString("Enter the text of the option C: ");
        String d = ScannerWrapper.getInstance().readString("Enter the text of the option D: ");
        enum Option {
            A, B, C, D
        }

        Option option = ScannerWrapper.getInstance().readEnum(Option.values(), "", "Enter correct option");

        return switch (option) {
            case A -> new ChoiceOneQuestion(name, score, description, type, level, a, b, c, d, "A");
            case B -> new ChoiceOneQuestion(name, score, description, type, level, a, b, c, d, "B");
            case C -> new ChoiceOneQuestion(name, score, description, type, level, a, b, c, d, "C");
            case D -> new ChoiceOneQuestion(name, score, description, type, level, a, b, c, d, "D");
        };
    }
}
