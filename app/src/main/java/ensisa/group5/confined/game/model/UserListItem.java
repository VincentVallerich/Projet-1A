package ensisa.group5.confined.game.model;

public class UserListItem {
    private String pseudo;

    private int score;


    public UserListItem(String pseudo, int score)
    {
        this.pseudo = pseudo;
        this.score = score;
    }

    public String getPseudo() { return pseudo; }

    public int getScore()
    {
        return score;
    }



}
