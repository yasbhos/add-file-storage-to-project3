package ir.ac.kntu.util;

import ir.ac.kntu.db.AdminDB;
import ir.ac.kntu.db.UserDB;
import ir.ac.kntu.model.User;

public class UserUtility {
    public static User readUser(String message, AdminDB adminDB, UserDB userDB) {
        System.out.println(message);
        String firstName = ScannerWrapper.getInstance().readString("Enter firstname: ");
        String username = ScannerWrapper.getInstance().readString("Enter username: ");
        if (!adminDB.isUsernameUnique(username) || !userDB.isUsernameUnique(username)) {
            System.out.println("This username has already been used");
            return null;
        }
        String passToHash = ScannerWrapper.getInstance().readPassword("Enter password: ");
        if (passToHash == null) {
            return null;
        }
        String email = ScannerWrapper.getInstance().readString("Enter email: ");
        String phoneNumber = ScannerWrapper.getInstance().readString("Enter phone number: ");
        String nationalCode = ScannerWrapper.getInstance().readString("Enter national code: ");
        return new User(firstName, username, passToHash, email, phoneNumber, nationalCode);
    }
}
