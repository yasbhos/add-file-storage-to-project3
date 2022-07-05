package ir.ac.kntu.model.course;

import ir.ac.kntu.model.DateTime;
import ir.ac.kntu.model.Observer;
import ir.ac.kntu.model.SingleResponder;
import ir.ac.kntu.model.User;
import ir.ac.kntu.model.question.ChoiceOneQuestion;
import ir.ac.kntu.model.question.Question;
import ir.ac.kntu.model.question.ShortAnswerQuestion;
import ir.ac.kntu.util.DateTimeUtility;
import ir.ac.kntu.util.ScannerWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Assignment implements Observer {
    public enum Status {
        ACTIVE,
        INACTIVE
    }

    private String name;

    private String description;

    private DateTime startDateTime;

    private DateTime endDateTime;

    private DateTime delayDateTime;

    private int delayCoefficient;

    private Status assignmentStatus;

    private Status scoreBoardStatus;

    private final ArrayList<Question> questions;

    private final ArrayList<SingleResponder> responders;

    private boolean automaticScoring;

    public Assignment(String name, String description, DateTime startDateTime, DateTime endDateTime,
                      DateTime delayDateTime, int delayCoefficient, Status assignmentStatus, Status scoreBoardStatus) {
        this.name = name;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.delayDateTime = delayDateTime;
        this.delayCoefficient = delayCoefficient;
        this.assignmentStatus = assignmentStatus;
        this.scoreBoardStatus = scoreBoardStatus;
        this.questions = new ArrayList<>();
        this.responders = new ArrayList<>();
        this.automaticScoring = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public DateTime getDelayDateTime() {
        return delayDateTime;
    }

    public void setDelayDateTime(DateTime delayDateTime) {
        this.delayDateTime = delayDateTime;
    }

    public int getDelayCoefficient() {
        return delayCoefficient;
    }

    public void setDelayCoefficient(int delayCoefficient) {
        this.delayCoefficient = delayCoefficient;
    }

    public Status getAssignmentStatus() {
        return assignmentStatus;
    }

    public void setAssignmentStatus(Status assignmentStatus) {
        this.assignmentStatus = assignmentStatus;
    }

    public Status getScoreBoardStatus() {
        return scoreBoardStatus;
    }

    public void setScoreBoardStatus(Status scoreBoardStatus) {
        this.scoreBoardStatus = scoreBoardStatus;
    }

    public boolean isAutomaticScoring() {
        return automaticScoring;
    }

    public void setAutomaticScoring(boolean automaticScoring) {
        this.automaticScoring = automaticScoring;
    }

    public boolean addQuestion(Question question) {
        return questions.add(question);
    }

    public boolean removeQuestion(Question question) {
        return questions.remove(question);
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

    public void listFinalSentAnswers(User student) {
        for (Question question : questions) {
            System.out.println(question.getResponderByUsername(student.getUsername()).getFinalAnswer());
            System.out.println();
        }
    }

    public void scoreBoard() {
        responders.sort(Collections.reverseOrder());
        System.out.println("Scoreboard for assignment " + this.name);
        System.out.println("------------------------------------------------------------");
        System.out.println("| Student Username\tTotal Score\tAverage sent DateTime");
        responders.forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }

    @Override
    public void updateResponder(String username) {
        double totalScore = 0;
        ArrayList<DateTime> sentDTs = new ArrayList<>();

        for (Question question : questions) {
            Question.Responder responder = question.getResponderByUsername(username);
            if (responder == null) {
                continue;
            }
            if (isAutomaticScoring()) {
                automaticScoring(question, responder);
            }

            totalScore += responder.getFinalAnswer().getScoreWithDelay();
            sentDTs.add(responder.getFinalAnswer().getSentDateTime());
        }

        SingleResponder responder = new SingleResponder(username);
        responder.setTotalScore(totalScore);
        responder.setAverageSentDT(DateTimeUtility.getAverageSentDateTimes(sentDTs));
        responders.remove(responder);
        responders.add(responder);
    }

    private void automaticScoring(Question question, Question.Responder responder) {
        if (question instanceof ChoiceOneQuestion choiceOneQuestion) {
            if (choiceOneQuestion.getCorrectAnswer().equals(responder.getFinalAnswer().getAnswer())) {
                responder.getFinalAnswer().setScore(question.getScore());
                responder.getFinalAnswer().setScoreWithDelay(question.getScore());
            }
        } else if (question instanceof ShortAnswerQuestion shortAnswerQuestion) {
            if (shortAnswerQuestion.getCorrectAnswer().equals(responder.getFinalAnswer().getAnswer())) {
                responder.getFinalAnswer().setScore(question.getScore());
                responder.getFinalAnswer().setScoreWithDelay(question.getScore());
            }
        }
    }

    public void registerMarkToFinalSent() {
        System.out.println("Register mark to final sent of assignment " + this.name);
        for (Question question : questions) {
            question.registerMarkToFinalSent(endDateTime, delayCoefficient);
        }
    }

    @Override
    public String toString() {
        StringBuilder assignmentToString = new StringBuilder("Assignment{" +
                "name='" + name + '\'' +
                "\ndescription='" + description + '\'' +
                "\nstartDateTime=" + startDateTime +
                "\nendDateTime=" + endDateTime +
                "\ndelayCoefficient=" + delayCoefficient +
                "\ndelayDateTime=" + delayDateTime);

        for (Question question : questions) {
            assignmentToString.append("\n" + question.getName());
        }

        assignmentToString.append("\nassignmentStatus=").append(assignmentStatus).append(", scoreBoardStatus=").append(scoreBoardStatus).append("\n}");

        return assignmentToString.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assignment that)) {
            return false;
        }

        return Objects.equals(name, that.name) && Objects.equals(startDateTime, that.startDateTime) && Objects.equals(endDateTime, that.endDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startDateTime, endDateTime);
    }
}
