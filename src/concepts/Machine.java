package concepts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import jade.content.Concept;
import ontology.Database;

public class Machine implements Concept {
	
	private static final long serialVersionUID = 1L;
	
	private int Id;
	private String Nom;
	private int Duree; // en seconde ou en minute dans notre cas c'est le temps quand met l'agent endormi!
	
	
	public Machine() {
		
	}
	
	public Machine(int id, String nom, int duree) {
		Id = id;
		Nom = nom;
		Duree = duree;
	}
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getNom() {
		return Nom;
	}
	public void setNom(String nom) {
		Nom = nom;
	}
	public int getDuree() {
		return Duree;
	}
	public void setDuree(int duree) {
		Duree = duree;
	}
	
	/*************************************************************************************************
	 * Fin User by username and password application
	 *************************************************************************************************/
	public static Machine findMachine(String nom) {
		try {
				Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				String query = "SELECT * FROM MACHINES WHERE MACHINE_NOM = '"+ nom +"'";
				ResultSet result = state.executeQuery(query);
				if(result.first())
					return new Machine(result.getInt("MACHINE_ID"), nom, result.getInt("MACHINE_DUREE"));
				result.close();
				state.close();	
		} catch (SQLException e) {
				e.printStackTrace();
		}
		return null;
	}

}
