package ir.ac.kntu.model.question;

import ir.ac.kntu.model.DateTime;
import ir.ac.kntu.model.Observer;
import ir.ac.kntu.model.User;
import ir.ac.kntu.util.DateTimeUtility;
import ir.ac.kntu.util.IdGenerator;
import ir.ac.kntu.util.ScannerWrapper;

import java.util.ArrayList;
import java.util.Objects;

public class Question {
    public class Responder implements Comparable<Responder> {
        private final String username;

        private final ArrayList<Answer> sentAnswers;

        public Responder(String username) {
            this.username = username;
            this.sentAnswers = new ArrayList<>();
        }

        public String getUsername() {
            return username;
        }

        public void addAnswer(Answer answer) {
            sentAnswers.add(answer);
        }

        public Answer getFinalAnswer() {
            if (sentAnswers.isEmpty()) {
                return null;
            }

            return sentAnswers.get(sentAnswers.size() - 1);
        }

        @Override
        public int compareTo(Responder o) {
            if (this.getFinalAnswer().getScoreWithDelay() > o.getFinalAnswer().getScoreWithDelay()) {
                return 1;
            }
            if (this.getFinalAnswer().getScoreWithDelay() < o.getFinalAnswer().getScoreWithDelay()) {
                return -1;
            }

            return this.getFinalAnswer().getSentDateTime().compareTo(o.getFinalAnswer().getSentDateTime());
        }
    }

    public enum Type {
        CHOICE_ONE,
        SHORT_ANSWER,
        LONG_ANSWER,
        FILL_IN_THE_BLANK
    }

    public enum Level {
        EASY,
        MEDIUM,
        HARD,
        VERY_HARD
    }

    private Observer observer;

    private final String id;

    private String name;

    private double score;

    private String description;

    private Type type;

    private Level level;

    private DateTime uploadDateTime;

    private final ArrayList<Responder> responders;

    public Question(String name, double score, String description, Type type, Level level) {
        this.id = IdGenerator.createID();
        this.name = name;
        this.score = score;
        this.description = description;
        this.type = type;
        this.level = level;
        this.uploadDateTime = DateTimeUtility.now();
        this.responders = new ArrayList<>();
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public DateTime getUploadDateTime() {
        return uploadDateTime;
    }

    public void setUploadDateTime(DateTime uploadDateTime) {
        this.uploadDateTime = uploadDateTime;
    }

    public Responder getResponderByUsername(String username) {
        return responders.stream().filter(responder -> responder.getUsername().equals(username)).findFirst().orElse(null);
    }

    public ArrayList<Responder> getResponders() {
        return new ArrayList<>(responders);
    }

    public void addAnswer(Answer answer) {
        Responder target = responders.stream().filter(responder -> responder.getUsername().equals(answer.getSenderUsername()
        )).findFirst().orElse(null);

        if (target == null) {
            Responder responder = new Responder(answer.getSenderUsername());
            responder.addAnswer(answer);
            responders.add(responder);
            observer.updateResponder(responder.getUsername());
        } else {
            target.addAnswer(answer);
            observer.updateResponder(target.getUsername());
        }
    }

    public Answer readAnswer(User user) {
        System.out.println(description);
        System.out.println();
        String answer = ScannerWrapper.getInstance().readString("Enter correct answer:");

        return new Answer(user.getUsername(), this, answer);
    }

    public void listSentAnswers(User user) {
        Responder target = responders.stream().filter(responder -> responder.getUsername().equals(user.getUsername())
        ).findFirst().orElse(null);
        if (target == null) {
            return;
        }

        for (Answer answer : target.sentAnswers) {
            System.out.println(answer);
            System.out.println();
        }
    }

    public void registerMarkToFinalSent(DateTime endDateTime, int delayCoefficient) {
        System.out.println("Register mark to final sent answers of question:");
        System.out.println(this);
        System.out.println();
        System.out.println("start-------------------------------------------------------");
        for (Responder responder : responders) {
            registerMarkToResponder(endDateTime, delayCoefficient, responder);
            observer.updateResponder(responder.getUsername());
        }
        System.out.println("done--------------------------------------------------------");
    }

    private void registerMarkToResponder(DateTime endDateTime, int delayCoefficient, Responder responder) {
        System.out.println("Sender username: " + responder.username);
        System.out.println(responder.getFinalAnswer());
        double mark = ScannerWrapper.getInstance().readDouble("Enter mark: ");
        responder.getFinalAnswer().setScore(mark);

        if (responder.getFinalAnswer().getSentDateTime().compareTo(endDateTime) <= 0) {
            responder.getFinalAnswer().setScoreWithDelay(mark);
        } else {
            double markWithDelay = mark * (delayCoefficient / 100.0);
            responder.getFinalAnswer().setScoreWithDelay(markWithDelay);
        }
    }

    public Question deepCopy() {
        Question question = new Question(name, score, description, type, level);
        question.setUploadDateTime(uploadDateTime);

        return question;
    }

    @Override
    public String toString() {
        return "Question{" +
                "name='" + name + '\'' +
                ", score=" + score +
                "\ndescription='" + description + '\'' +
                "\ntype=" + type +
                ", level=" + level +
                "\nuploadDateTime=" + uploadDateTime +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question question)) {
            return false;
        }
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
