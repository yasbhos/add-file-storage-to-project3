package ir.ac.kntu.model.contest;

import ir.ac.kntu.db.UserDB;
import ir.ac.kntu.model.DateTime;
import ir.ac.kntu.model.Observer;
import ir.ac.kntu.model.User;
import ir.ac.kntu.model.question.Question;
import ir.ac.kntu.util.IdGenerator;
import ir.ac.kntu.util.ScannerWrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Contest implements Observer, Serializable {
    private final User ownerAdmin;

    private final String id;

    private String name;

    private DateTime startDateTime;

    private DateTime endDateTime;

    private final ArrayList<Question> questions;

    private boolean automaticScoring;

    @java.io.Serial
    private static final long serialVersionUID = 385L;

    public Contest(User ownerAdmin, String name, DateTime startDateTime,
                   DateTime endDateTime, ArrayList<Question> questions) {
        this.ownerAdmin = ownerAdmin;
        this.id = IdGenerator.createID();
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.questions = questions;
        this.automaticScoring = true;
    }

    public User getOwnerAdmin() {
        return ownerAdmin;
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

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(DateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public boolean addQuestion(Question question) {
        return questions.add(question);
    }

    public boolean removeQuestion(Question question) {
        return questions.remove(question);
    }

    public boolean isAutomaticScoring() {
        return automaticScoring;
    }

    public void setAutomaticScoring(boolean automaticScoring) {
        this.automaticScoring = automaticScoring;
    }

    public ArrayList<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    public Question searchQuestion() {
        for (Question question : questions) {
            System.out.println("ID: " + question.getId() +
                    ", Name: " + question.getName() +
                    ", Level: " + question.getLevel() +
                    ", Type: " + question.getType() +
                    ", Score: " + question.getScore());
        }

        String id = ScannerWrapper.getInstance().readString("Enter question ID: ");
        for (Question question : questions) {
            if (question.getId().equals(id)) {
                return question;
            }
        }

        return null;
    }

    public boolean addParticipant(User target) {
        return true;
    }

    public boolean containsParticipant(User target) {
        return true;
    }

    public void registerMarkToFinalSent() {
    }

    public void scoreBoard() {
    }

    public void exportScoreBoard() {
    }

    public void finalResult(UserDB userDB) {
    }

    @Override
    public void updateResponder(String username) {
    }

    @Override
    public String toString() {
        StringBuilder contestToString = new StringBuilder("Contest{" +
                "name='" + name + '\'' +
                "\nstartDateTime=" + startDateTime +
                "\nendDateTime=" + endDateTime);

        contestToString.append("\nQuestions\n");
        for (Question question : questions) {
            contestToString.append(question.getName()).append("\n");
        }

        contestToString.append("}");

        return contestToString.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contest contest)) {
            return false;
        }
        return Objects.equals(id, contest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
