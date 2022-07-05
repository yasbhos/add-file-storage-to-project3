package ir.ac.kntu.util;

import java.io.Console;
import java.util.Scanner;

public class ScannerWrapper {
    private static ScannerWrapper instance = new ScannerWrapper();

    private Console console;

    private Scanner scanner;

    private ScannerWrapper() {
        console = System.console();
        scanner = new Scanner(System.in);
    }

    public static ScannerWrapper getInstance() {
        return instance;
    }

    public int nextInt() {
        String nextInt = scanner.next();
        scanner.nextLine();

        return Integer.parseInt(nextInt);
    }

    public int readInt(String message) {
        System.out.print(message);

        return nextInt();
    }

    public double readDouble(String message) {
        System.out.print(message);
        String nextDouble = scanner.next();
        scanner.nextLine();

        return Double.parseDouble(nextDouble);
    }

    public String readString(String message) {
        System.out.print(message);

        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }

    public String readPassword(String message) {
        if (console == null) {
            System.out.println("No console available");
            return null;
        }
        System.out.print(message);

        return String.valueOf(console.readPassword());
    }

    public <T extends Enum<T>> T readEnum(T[] options) {
        showMenu(options);

        return scanOption(options);
    }

    public <T extends Enum<T>> T readEnum(T[] options, String menuName) {
        showMenu(options, menuName);

        return scanOption(options);
    }

    public <T extends Enum<T>> T readEnum(T[] options, String menuName, String message) {
        System.out.println(message);
        showMenu(options, menuName);

        return scanOption(options);
    }

    private <T extends Enum<T>> T scanOption(T[] options) {
        int userInput = nextInt();
        userInput--;
        if (userInput >= 0 && userInput < options.length) {
            System.out.println();
            return options[userInput];
        }
        System.out.print("Please enter a valid number: ");

        return scanOption(options);
    }

    private <T extends Enum<T>> void showMenu(T[] options) {
        System.out.println("******************************");
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%2d. %s\n", i + 1, options[i].name());
        }
        System.out.println("******************************");
        System.out.print("Please enter your choice: ");
    }

    private <T extends Enum<T>> void showMenu(T[] options, String menuName) {
        System.out.println("*************** " + menuName + " ***************");
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%2d. %s\n", i + 1, options[i].name());
        }
        System.out.println("*************** " + menuName + " ***************");
        System.out.print("Please enter your choice: ");
    }
}
