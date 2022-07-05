package ir.ac.kntu.db;

import ir.ac.kntu.model.question.Question;
import ir.ac.kntu.util.ScannerWrapper;

import java.util.ArrayList;
import java.util.Comparator;

public class QuestionDB {
    private final ArrayList<Question> questions;

    public QuestionDB(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public boolean addQuestion(Question question) {
        return questions.add(question);
    }

    public boolean removeQuestion(Question question) {
        return questions.remove(question);
    }

    public boolean containsQuestion(Question question) {
        return questions.contains(question);
    }

    public Question getQuestion() {
        sortQuestions();

        for (Question question : questions) {
            System.out.println("Id: " + question.getId() +
                    ", name: " + question.getName() +
                    ", score: " + question.getScore() +
                    ", type: " + question.getType() +
                    ", level: " + question.getLevel());
        }
        String id = ScannerWrapper.getInstance().readString("Enter question id: ");
        Question question = getQuestionById(id);
        if (question == null) {
            System.out.println("Invalid Id");
        }

        return question;
    }

    private void sortQuestions() {
        enum SortBy {
            UPLOAD_TIME,
            DIFFICULTY,
            LIST
        }

        SortBy sortBy = ScannerWrapper.getInstance().readEnum(SortBy.values(), "SORTING OPTION");
        switch (sortBy) {
            case UPLOAD_TIME, LIST -> questions.sort(Comparator.comparing(Question::getUploadDateTime));
            case DIFFICULTY -> questions.sort(Comparator.comparing(Question::getLevel));
            default -> {
            }
        }
    }

    public Question getQuestionById(String id) {
        return questions.stream().filter(question -> question.getId().equals(id)).findFirst().orElse(null);
    }
}
