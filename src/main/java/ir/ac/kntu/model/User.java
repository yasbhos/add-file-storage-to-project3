package ir.ac.kntu.model;

import ir.ac.kntu.util.Cipher;

import java.util.Objects;

public class User {
    private boolean admin;

    private String firstName;

    private String username;

    private String hashedPassword;

    private String email;

    private String phoneNumber;

    private String nationalCode;

    private double rating;

    public User(String firstName, String username, String password, String email, String phoneNumber, String nationalCode) {
        this.admin = false;
        this.firstName = firstName;
        this.username = username;
        this.hashedPassword = Cipher.sha256(password);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nationalCode = nationalCode;
        this.rating = 0;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setPassword(String password) {
        this.hashedPassword = Cipher.sha256(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRank() {
        return (int) (rating / 50);
    }

    @Override
    public String toString() {
        return "User{" +
                "admin='" + admin + '\'' +
                ", firstName='" + firstName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", nationalCode='" + nationalCode + '\'' +
                ", rating=" + rating +
                ", ranking=" + getRank() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)) {
            return false;
        }

        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
