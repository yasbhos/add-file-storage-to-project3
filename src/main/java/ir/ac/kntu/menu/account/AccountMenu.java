package ir.ac.kntu.menu.account;

import ir.ac.kntu.db.AdminDB;
import ir.ac.kntu.db.UserDB;
import ir.ac.kntu.menu.Menu;
import ir.ac.kntu.model.User;
import ir.ac.kntu.util.Cipher;
import ir.ac.kntu.util.ScannerWrapper;

public class AccountMenu implements Menu {
    private final User user;

    private final AdminDB adminDB;

    private final UserDB userDB;

    public AccountMenu(User user,AdminDB adminDB, UserDB userDB) {
        this.user = user;
        this.userDB = userDB;
        this.adminDB = adminDB;
    }

    @Override
    public void menu() {
        AccountMenuOption accountMenuOption;
        do {
            accountMenuOption = ScannerWrapper.getInstance().readEnum(AccountMenuOption.values(), "ACCOUNT MENU");
            handleTheOption(accountMenuOption);
        } while (accountMenuOption != AccountMenuOption.BACK);
    }

    @Override
    public <T extends Enum<T>> void handleTheOption(T option) {
        switch ((AccountMenuOption) option) {
            case EDIT_FIRST_NAME -> editFirstName();
            case EDIT_USERNAME -> editUsername();
            case EDIT_PASSWORD -> editPassword();
            case EDIT_EMAIL -> editEmail();
            case EDIT_PHONE_NUMBER -> editPhoneNumber();
            case EDIT_NATIONAL_CODE -> editNationalCode();
            default -> {
            }
        }
    }

    private void editFirstName() {
        String firstName = ScannerWrapper.getInstance().readString("Enter new first name: ");
        user.setFirstName(firstName);
        System.out.println("Successfully changed");
    }

    private void editUsername() {
        String username = ScannerWrapper.getInstance().readString("Enter new username: ");
        if (adminDB.isUsernameUnique(username) || !userDB.isUsernameUnique(username)) {
            System.out.println("This username has already been used");
            return;
        }

        user.setUsername(username);
    }

    private void editPassword() {
        String oldPassword = ScannerWrapper.getInstance().readPassword("Enter old password: ");
        if (!user.getHashedPassword().equals(Cipher.sha256(oldPassword))) {
            System.out.println("Wrong password");
            return;
        }

        String password = ScannerWrapper.getInstance().readPassword("Enter new password: ");
        user.setPassword(password);
        System.out.println("Successfully changed");
    }

    private void editEmail() {
        String email = ScannerWrapper.getInstance().readString("Enter new email: ");
        user.setEmail(email);
        System.out.println("Successfully changed");
    }

    private void editPhoneNumber() {
        String phoneNumber = ScannerWrapper.getInstance().readString("Enter new phone number: ");
        user.setPhoneNumber(phoneNumber);
        System.out.println("Successfully changed");
    }

    private void editNationalCode() {
        String nationalCode = ScannerWrapper.getInstance().readString("Enter new national code: ");
        user.setNationalCode(nationalCode);
        System.out.println("Successfully changed");
    }
}
