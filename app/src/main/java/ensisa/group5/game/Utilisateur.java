package utilisat;

public class Utilisateur implements Comparable<Utilisateur>{
	private String id; 
	private int score;
	
	public Utilisateur(String id, int score) {
		super();
		this.id = id;
		this.score = score;
	}
		
	public void setId(String pseudo) {
		this.id = pseudo;
	}
	
	public void setScore(int point) {
		this.score = point;
	}
	
	public String getId() {
		return this.id;
	}
	
	public int getScore() {
		return this.score;}
		
	public int compareTo(Utilisateur u){
	       return this.score > u.score ? 1 : this.score < u.score ? -1 : 0;
	}
}

	
