package ir.ac.kntu.model.question;

import ir.ac.kntu.model.DateTime;
import ir.ac.kntu.util.DateTimeUtility;

public class Answer {
    private final String senderUsername;

    private final DateTime sentDateTime;

    private final Question question;

    private final String answer;

    private double score;

    private double scoreWithDelay;

    public Answer(String senderUsername, Question question, String answer) {
        this.senderUsername = senderUsername;
        this.sentDateTime = DateTimeUtility.now();
        this.question = question;
        this.answer = answer;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public DateTime getSentDateTime() {
        return sentDateTime;
    }

    public Question getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScoreWithDelay() {
        return scoreWithDelay;
    }

    public void setScoreWithDelay(double scoreWithDelay) {
        this.scoreWithDelay = scoreWithDelay;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "senderUsername='" + senderUsername + '\'' +
                ", sentDateTime=" + sentDateTime +
                ", question=" + question.getName() +
                "\nanswer='" + answer + '\'' +
                ", score=" + score +
                ", scoreWithDelay=" + scoreWithDelay +
                "}";
    }
}
