package ir.ac.kntu.db;

import ir.ac.kntu.model.User;
import ir.ac.kntu.util.Cipher;
import ir.ac.kntu.util.ScannerWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class UserDB {
    private final ArrayList<User> users;

    public UserDB(ArrayList<User> users) {
        this.users = users;
    }

    public boolean addUser(User user) {
        return users.add(user);
    }

    public boolean removeUser(User user) {
        return users.remove(user);
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
}
