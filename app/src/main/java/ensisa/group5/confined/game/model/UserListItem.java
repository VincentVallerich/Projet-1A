package ensisa.group5.confined.game.model;

public class UserListItem {
    private String pseudo;

    private int score;
    private String img;


    public UserListItem(String pseudo, int score,String img)
    {
        this.pseudo = pseudo;
        this.score = score;
        this.img = img;
    }

    public String getPseudo() { return pseudo; }

    public int getScore()
    {
        return score;
    }
    public String getImg()
    {
        return img;
    }

}
