package ensisa.group5.confined.model;

import java.util.UUID;

public class User {
    private int id;
    private String mail;
    private String name;
    private int points;
    private boolean isMaster;

    public User() {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.name = "EmptyName";
        this.mail = "UndefinedMail";
        this.points = 0;
        this.isMaster = false;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.points = score;
    }

    public int getScore() {
        return points;
    }
}
