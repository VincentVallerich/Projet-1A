package ensisa.group5.confined.model;

<<<<<<< HEAD
public class User {
    private int nickname;
    private String name;
    private int score;

    public int getNickname() {
        return nickname;
    }

    public String getName() {
        return name;
    }

    public void setNickname(int nickname) {
        this.nickname = nickname;
=======
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
>>>>>>> 3a6a8cf233ff0632a54d94d2336003832ca7881a
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
<<<<<<< HEAD
        this.score = score;
    }

    public int getScore() {
        return score;
=======
        this.points = score;
    }

    public int getScore() {
        return points;
>>>>>>> 3a6a8cf233ff0632a54d94d2336003832ca7881a
    }
}
