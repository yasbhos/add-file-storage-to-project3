package ir.ac.kntu.db;

import ir.ac.kntu.model.User;
import ir.ac.kntu.model.question.Question;
import ir.ac.kntu.util.ScannerWrapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class QuestionDB {
    private ArrayList<Question> questions;

    public QuestionDB() {
        loadQuestionsInfo();
    }

    public boolean addQuestion(Question question) {
        if (questions.add(question)) {
            saveQuestionsInfo();
            return true;
        }
        return false;
    }

    public boolean removeQuestion(Question question) {
        if (questions.remove(question)) {
            saveQuestionsInfo();
            return true;
        }
        return false;
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

    private void loadQuestionsInfo() {
        ArrayList<Question> questions = new ArrayList<>();
        File file = new File("questions.info");
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
            while (true) {
                try {
                    Question question = (Question) inputStream.readObject();
                    questions.add(question);
                } catch (EOFException e) {
                    break;
                } catch (Exception e) {
                    System.out.println("Problem with some of the records in the questions data file");
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("No previous data for questions has been saved.");
        }

        this.questions = questions;
    }

    public void saveQuestionsInfo() {
        File file = new File("questions.info");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
            for (Question question : questions) {
                try {
                    outputStream.writeObject(question);
                } catch (IOException e) {
                    System.out.println("(QuestionDB::saveQuestionsInfo): An error occurred while trying to save info");
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("(QuestionDB::saveQuestionsInfo): An error occurred while trying to save info");
            System.out.println(e.getMessage());
        }
    }
}
