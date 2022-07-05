package ir.ac.kntu.model.question;

import ir.ac.kntu.model.User;
import ir.ac.kntu.util.ScannerWrapper;

public class ChoiceOneQuestion extends Question {
    public class Options {
        private String a;

        private String b;

        private String c;

        private String d;

        public Options(String a, String b, String c, String d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        public String getA() {
            return a;
        }

        public String getB() {
            return b;
        }

        public String getC() {
            return c;
        }

        public String getD() {
            return d;
        }
    }

    private Options options;

    private String correctAnswer;

    public ChoiceOneQuestion(String name, double score, String description, Type type, Level level,
                             String a, String b, String c, String d, String correctAnswer) {
        super(name, score, description, type, level);
        this.options = new Options(a, b, c, d);
        this.correctAnswer = correctAnswer;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(String a, String b, String c, String d) {
        options.a = a;
        options.b = b;
        options.c = c;
        options.d = d;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public Question deepCopy() {
        ChoiceOneQuestion question = new ChoiceOneQuestion(super.getName(), super.getScore(), super.getDescription(),
                super.getType(), super.getLevel(), options.a, options.b, options.c, options.d, correctAnswer);
        question.setUploadDateTime(super.getUploadDateTime());

        return question;
    }

    @Override
    public Answer readAnswer(User user) {
        enum Option {
            A, B, C, D
        }

        System.out.println(super.getDescription());
        System.out.println(this.getOptions().getA());
        System.out.println(this.getOptions().getB());
        System.out.println(this.getOptions().getC());
        System.out.println(this.getOptions().getD());
        System.out.println();
        System.out.println("Enter correct option:");
        Option option = ScannerWrapper.getInstance().readEnum(Option.values());
        return switch (option) {
            case A -> new Answer(user.getUsername(), this, "a");
            case B -> new Answer(user.getUsername(), this, "b");
            case C -> new Answer(user.getUsername(), this, "c");
            case D -> new Answer(user.getUsername(), this, "d");
        };
    }

    @Override
    public String toString() {
        String questionToString = "Question{" +
                "name='" + super.getName() + '\'' +
                ", score=" + super.getScore() +
                "\ndescription='" + super.getDescription() + "'\n";

        questionToString += "Options:\n" +
                "a. " + options.getA() + "\n" +
                "b. " + options.getB() + "\n" +
                "c. " + options.getC() + "\n" +
                "d. " + options.getD() + "\n";

        questionToString += "type=" + super.getType() +
                ", level=" + super.getLevel() +
                "\nuploadDateTime=" + super.getUploadDateTime() +
                "\n}";

        return questionToString;
    }
}
