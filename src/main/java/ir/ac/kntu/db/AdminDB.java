package ir.ac.kntu.db;

import ir.ac.kntu.model.User;
import ir.ac.kntu.util.Cipher;

import java.io.*;
import java.util.ArrayList;

public class AdminDB {
    private ArrayList<User> admins;

    public AdminDB() {
        loadAdminsInfo();
    }

    public boolean addAdmin(User admin) {
        if (admins.add(admin)) {
            saveAdminsInfo();
            return true;
        }
        return false;
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

    private void loadAdminsInfo() {
        ArrayList<User> admins = new ArrayList<>();
        File file = new File("admins.info");
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
            while (true) {
                try {
                    User admin = (User) inputStream.readObject();
                    admins.add(admin);
                } catch (EOFException e) {
                    break;
                } catch (Exception e) {
                    System.out.println("Problem with some of the records in the admins data file");
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("No previous data for admins has been saved.");
        }

        this.admins = admins;
    }

    public void saveAdminsInfo() {
        File file = new File("admins.info");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
            for (User admin : admins) {
                try {
                    outputStream.writeObject(admin);
                } catch (IOException e) {
                    System.out.println("(AdminDB::saveAdminsInfo): An error occurred while trying to save info");
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("(AdminDB::saveAdminsInfo): An error occurred while trying to save info");
            System.out.println(e.getMessage());
        }
    }
}
