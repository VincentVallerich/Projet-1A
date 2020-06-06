package ensisa.group5.confined.model;

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
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
