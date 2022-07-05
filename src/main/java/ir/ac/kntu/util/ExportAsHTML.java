package ir.ac.kntu.util;

import ir.ac.kntu.model.GroupResponder;
import ir.ac.kntu.model.SingleResponder;
import ir.ac.kntu.model.question.Answer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ExportAsHTML {
    public static void exportAnswers(ArrayList<Answer> answers, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println("<!DOCTYPE html>");
            printWriter.println("<html>");
            printWriter.println("<head>");
            printWriter.println("<title>Answers</title>");
            printWriter.println("</head>");
            printWriter.println("<body>");
            printWriter.println("<table>");

            printWriter.println("<tr>");
            printWriter.println("<th>| Sender Username |</th>");
            printWriter.println("<th>| Sent Date&Time |</th>");
            printWriter.println("<th>| Question Name&Id |</th>");
            printWriter.println("<th>| Description |</th>");
            printWriter.println("<th>| Score |</th>");
            printWriter.println("<th>| Score With Delay |</th>");
            printWriter.println("</tr>");

            for (Answer answer : answers) {
                saveAnswerInfo(printWriter, answer);
            }

            printWriter.println("</table>");
            printWriter.println("</body>");
            printWriter.println("</html>");
        } catch (Exception e) {
            System.out.println("(ExportAsHTML::exportAnswers): An error occurred while trying to save answer.");
        }
    }

    private static void saveAnswerInfo(PrintWriter printWriter, Answer answer) {
        printWriter.println("<tr>");
        printWriter.println("<th>" + answer.getSenderUsername() + "</th>");
        printWriter.println("<th>" + answer.getSentDateTime() + "</th>");
        printWriter.println("<th>" + answer.getQuestion().getName() +
                "-" + answer.getQuestion().getId() + "</th>");
        printWriter.println("<th>" + answer.getAnswer() + "</th>");
        printWriter.println("<th>" + answer.getScore() + "</th>");
        printWriter.println("<th>" + answer.getScoreWithDelay() + "</th>");
        printWriter.println("</tr>");
    }

    public static void exportSingleResponders(ArrayList<SingleResponder> responders, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println("<!DOCTYPE html>");
            printWriter.println("<html>");
            printWriter.println("<head>");
            printWriter.println("<title>Scoreboard</title>");
            printWriter.println("</head>");
            printWriter.println("<body>");
            printWriter.println("<table>");

            printWriter.println("<tr>");
            printWriter.println("<th>| Username |</th>");
            printWriter.println("<th>| Total Score |</th>");
            printWriter.println("<th>| Average Sent Date&Time |</th>");
            printWriter.println("</tr>");

            for (SingleResponder responder : responders) {
                saveSingleResponderInfo(printWriter, responder);
            }

            printWriter.println("</table>");
            printWriter.println("</body>");
            printWriter.println("</html>");
        } catch (Exception e) {
            System.out.println("(ExportAsHTML::exportSingleResponders): An error occurred while trying to save responder.");
        }
    }

    private static void saveSingleResponderInfo(PrintWriter printWriter, SingleResponder responder) {
        printWriter.println("<tr>");
        printWriter.println("<th>" + responder.getUsername() + "</th>");
        printWriter.println("<th>" + responder.getTotalScore() + "</th>");
        printWriter.println("<th>" + responder.getAverageSentDT() + "</th>");
        printWriter.println("</tr>");
    }

    public static void exportGroupResponders(ArrayList<GroupResponder> responders, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println("<!DOCTYPE html>");
            printWriter.println("<html>");
            printWriter.println("<head>");
            printWriter.println("<title>Scoreboard</title>");
            printWriter.println("</head>");
            printWriter.println("<body>");
            printWriter.println("<table>");

            printWriter.println("<tr>");
            printWriter.println("<th>| Group Name |</th>");
            printWriter.println("<th>| Total Score |</th>");
            printWriter.println("<th>| Average Sent Date&Time |</th>");
            printWriter.println("</tr>");

            for (GroupResponder responder : responders) {
                saveGroupResponderInfo(printWriter, responder);
            }

            printWriter.println("</table>");
            printWriter.println("</body>");
            printWriter.println("</html>");
        } catch (Exception e) {
            System.out.println("(ExportAsHTML::exportGroupResponders): An error occurred while trying to save responder.");
        }
    }

    private static void saveGroupResponderInfo(PrintWriter printWriter, GroupResponder responder) {
        printWriter.println("<tr>");
        printWriter.println("<th>" + responder.getName() + "</th>");
        printWriter.println("<th>" + responder.getTotalScore() + "</th>");
        printWriter.println("<th>" + responder.getAverageSentDT() + "</th>");
        printWriter.println("</tr>");
    }
}
