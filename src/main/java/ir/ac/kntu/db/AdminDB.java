package ir.ac.kntu.db;

import ir.ac.kntu.model.User;
import ir.ac.kntu.util.Cipher;

import java.util.ArrayList;

public class AdminDB {
    private final ArrayList<User> admins;

    public AdminDB(ArrayList<User> admins) {
        this.admins = admins;
    }

    public boolean addAdmin(User admin) {
        return admins.add(admin);
    }

    public User getAdminByUsername(String username) {
        return admins.stream().filter(admin -> admin.getUsername().equals(username)).findFirst().orElse(null);
    }

    public User getAdminByUsernameAndPassword(String username, String password) {
        return admins.stream().filter(Admin ->
                Admin.getUsername().equals(username) && Admin.getHashedPassword().equals(Cipher.sha256(password))
        ).findFirst().orElse(null);
    }

    public boolean isUsernameUnique(String username) {
        return getAdminByUsername(username) == null;
    }
}
