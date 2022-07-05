package ir.ac.kntu.model.contest;

import ir.ac.kntu.db.UserDB;
import ir.ac.kntu.model.DateTime;
import ir.ac.kntu.model.GroupResponder;
import ir.ac.kntu.model.User;
import ir.ac.kntu.model.question.ChoiceOneQuestion;
import ir.ac.kntu.model.question.Question;
import ir.ac.kntu.model.question.ShortAnswerQuestion;
import ir.ac.kntu.util.DateTimeUtility;
import ir.ac.kntu.util.ExportAsHTML;
import ir.ac.kntu.util.ScannerWrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class SpecialContest extends Contest implements Serializable {
    private static final int MAXIMUM_PARTICIPANTS = 100;

    private int maximumGroupsCapacity;

    private final ArrayList<GroupResponder> responders;

    @java.io.Serial
    private static final long serialVersionUID = 14L;

    public SpecialContest(User ownerAdmin, String name, DateTime startDateTime, DateTime endDateTime,
                          ArrayList<Question> questions, int maximumGroupCapacity) {
        super(ownerAdmin, name, startDateTime, endDateTime, questions);
        this.maximumGroupsCapacity = maximumGroupCapacity;
        this.responders = new ArrayList<>();
    }

    public int getMaximumGroupsCapacity() {
        return maximumGroupsCapacity;
    }

    public void setMaximumGroupsCapacity(int maximumGroupsCapacity) {
        this.maximumGroupsCapacity = maximumGroupsCapacity;
    }

    @Override
    public boolean addParticipant(User target) {
        enum Option {
            NEW_GROUP,
            EXISTING_GROUP
        }

        System.out.println("Add to Existing group or New group?");
        Option option = ScannerWrapper.getInstance().readEnum(Option.values());
        return switch (option) {
            case NEW_GROUP -> {
                String name = ScannerWrapper.getInstance().readString("Enter group name: ");
                ArrayList<User> members = new ArrayList<>();
                members.add(target);
                yield this.addGroup(name, members);
            }
            case EXISTING_GROUP -> this.addParticipantToExistingGroup(target);
        };
    }

    private boolean addParticipantToExistingGroup(User participant) {
        for (GroupResponder responder : responders) {
            System.out.println("Name: " + responder.getName());
        }
        String name = ScannerWrapper.getInstance().readString("Enter group name: ");

        GroupResponder target = responders.stream().filter(groupResponder -> groupResponder.getName().equals(name)
        ).findFirst().orElse(null);
        if (target == null) {
            System.out.println("Invalid name");
            return false;
        }

        if (target.addMember(participant)) {
            System.out.println("Successfully added");
            return true;
        }

        return false;
    }

    private boolean addGroup(String name, ArrayList<User> members) {
        if (isCapacityFilled()) {
            System.out.println("The capacity is full");
            return false;
        }

        GroupResponder responder = new GroupResponder(name, members);
        if (responders.add(responder)) {
            System.out.println("Successfully added");
            return true;
        }

        return false;
    }

    public boolean isCapacityFilled() {
        return (responders.size() + 1) * maximumGroupsCapacity >= MAXIMUM_PARTICIPANTS;
    }

    @Override
    public boolean containsParticipant(User target) {
        for (GroupResponder responder : responders) {
            for (User member : responder.getMembers()) {
                if (member.equals(target)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void registerMarkToFinalSent() {
        System.out.println("Register mark to final sent of contest " + super.getName());
        for (Question question : super.getQuestions()) {
            question.registerMarkToFinalSent(super.getEndDateTime(), 100);
        }
    }

    @Override
    public void scoreBoard() {
        responders.sort(Collections.reverseOrder());
        System.out.println("Scoreboard for contest " + super.getName());
        System.out.println("------------------------------------------------------------");
        System.out.println("| Group name\tTotal Score\tAverage sent DateTime");
        responders.forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }

    @Override
    public void exportScoreBoard() {
        responders.sort(Collections.reverseOrder());
        ExportAsHTML.exportGroupResponders(responders, super.getName() + "-scoreboard.html");
    }

    @Override
    public void updateResponder(String username) {
        GroupResponder target = getTarget(username);
        if (target == null) {
            return;
        }

        double totalScore = 0;
        ArrayList<DateTime> sentDTs = new ArrayList<>();

        for (Question question : super.getQuestions()) {
            ArrayList<Question.Responder> responders = getTargetGroupAnswers(target, question);
            Collections.sort(responders);

            totalScore += responders.get(0).getFinalAnswer().getScoreWithDelay();
            sentDTs.add(responders.get(0).getFinalAnswer().getSentDateTime());
        }

        target.setTotalScore(totalScore);
        target.setAverageSentDT(DateTimeUtility.getAverageSentDateTimes(sentDTs));
    }

    private GroupResponder getTarget(String username) {
        for (GroupResponder responder : responders) {
            for (User user : responder.getMembers()) {
                if (user.getUsername().equals(username)) {
                    return responder;
                }
            }
        }

        return null;
    }

    private ArrayList<Question.Responder> getTargetGroupAnswers(GroupResponder target, Question question) {
        ArrayList<Question.Responder> responders = new ArrayList<>();
        for (Question.Responder responder : question.getResponders()) {
            for (User user : target.getMembers()) {
                if (user.getUsername().equals(responder.getUsername())) {
                    if (isAutomaticScoring()) {
                        automaticScoring(question, responder);
                    }

                    responders.add(responder);
                }
            }
        }

        return responders;
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

    @Override
    public void finalResult(UserDB userDB) {
        if (DateTimeUtility.now().compareTo(super.getEndDateTime()) <= 0) {
            System.out.println("The contest is not over yet");
            return;
        }
        if (responders.isEmpty()) {
            return;
        }

        responders.sort(Collections.reverseOrder());
        for (int i = 0; i < 10; i++) {
            responders.get(i).setTotalScore(responders.get(i).getTotalScore() + 25);
        }

        System.out.println("Scoreboard for contest " + super.getName());
        System.out.println("------------------------------------------------------------");
        System.out.println("| Group name\tTotal Score\tAverage sent DateTime");
        responders.forEach(System.out::println);
        System.out.println("------------------------------------------------------------");

        for (GroupResponder responder : responders) {
            ArrayList<User> targets = responder.getMembers();
            if (targets == null) {
                return;
            }

            for (User target : targets) {
                target.setRating(target.getRating() + responder.getTotalScore());
            }
        }
    }
}
