package ir.ac.kntu.db;

import ir.ac.kntu.model.User;
import ir.ac.kntu.util.Cipher;
import ir.ac.kntu.util.ScannerWrapper;

import java.io.*;
import java.util.ArrayList;

public class UserDB {
    private ArrayList<User> users;

    public UserDB() {
        loadUsersInfo();
    }

    public boolean addUser(User user) {
        if (users.add(user)) {
            saveUsersInfo();
            return true;
        }
        return false;
    }

    public boolean removeUser(User user) {
        if (users.remove(user)) {
            saveUsersInfo();
            return true;
        }
        return false;
    }

    public User getUser() {
        for (User user : users) {
            System.out.println("Username: " + user.getUsername() + ", FirstName: " + user.getFirstName());
        }
        String username = ScannerWrapper.getInstance().readString("Enter username: ");
        User user = getUserByUsername(username);
        if (user == null) {
            System.out.println("Invalid username");
            return null;
        }

        return user;
    }

    public User getUserByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return users.stream().filter(user ->
                user.getUsername().equals(username) && user.getHashedPassword().equals(Cipher.sha256(password))
        ).findFirst().orElse(null);
    }

    public User getUserByNationalCodeOrEmail() {
        enum Option {
            SEARCH_BY_NATIONAL_CODE,
            SEARCH_BY_EMAIL
        }

        Option option = ScannerWrapper.getInstance().readEnum(Option.values());
        return switch (option) {
            case SEARCH_BY_NATIONAL_CODE -> searchUserByNationalCode();
            case SEARCH_BY_EMAIL -> searchUserByEmail();
        };
    }

    private User searchUserByNationalCode() {
        String nationalCode = ScannerWrapper.getInstance().readString("Enter national code: ");
        User target = users.stream().filter(user -> user.getNationalCode().equals(nationalCode)).findFirst().orElse(null);
        if (target == null) {
            System.out.println("User not found");
        }

        return target;
    }

    private User searchUserByEmail() {
        String email = ScannerWrapper.getInstance().readString("Enter email: ");
        User target = users.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
        if (target == null) {
            System.out.println("User not found");
        }

        return target;
    }

    public boolean isUsernameUnique(String username) {
        return getUserByUsername(username) == null;
    }

    private void loadUsersInfo() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File("users.info");
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
            while (true) {
                try {
                    User user = (User) inputStream.readObject();
                    users.add(user);
                } catch (EOFException e) {
                    break;
                } catch (Exception e) {
                    System.out.println("Problem with some of the records in the users data file");
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("No previous data for users has been saved.");
        }

        this.users = users;
    }

    public void saveUsersInfo() {
        File file = new File("users.info");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
            for (User user : users) {
                try {
                    outputStream.writeObject(user);
                } catch (IOException e) {
                    System.out.println("(UserDB::saveUsersInfo): An error occurred while trying to save info");
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("(UserDB::saveUsersInfo): An error occurred while trying to save info");
            System.out.println(e.getMessage());
        }
    }
}
