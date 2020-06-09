/*package ensisa.group5.confined.game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class ScoreBoard {
	public static void main(String[] args) {
			
		ArrayList<Utilisateur> listPlayer = readPlayersFromCSV("rawScore.csv"); 
		
		Collections.sort(listPlayer);
		for (Utilisateur u : listPlayer) 
		{ 
			System.out.print("Utilisateur : "+u.getId()+" Score : "+u.getScore()+"\n");}	
	}
	private static ArrayList<Utilisateur> readPlayersFromCSV(String fileName) { 
		
		BufferedReader br = null;
		ArrayList<Utilisateur> listPlayer = new ArrayList<>(); 
		
		
		try  { 
			br = new BufferedReader(new FileReader(fileName));
			String line=br.readLine();
			
			while (line !=null) {
				String[] attributes = line.split(","); 
				Utilisateur u = createPlayer(attributes);
				listPlayer.add(u);
				
				line = br.readLine();
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(br != null) {
				try {
					br.close();
				}catch(IOException e) {
					e.printStackTrace();
					
				}}}
		return listPlayer;}
		

	private static Utilisateur createPlayer(String[] data) {
		String id = data[0];
		int score =Integer.parseInt(data[1]);
		return new Utilisateur(id,score);
	}
}
*/