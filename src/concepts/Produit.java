package concepts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;


import jade.content.Concept;
import ontology.Database;

//CTRL + SHIFT + O pour générer les imports
public class Produit implements Concept {
	private static final long serialVersionUID = 1L;
	
	private int Id = 0;
	private String Description = "";
	private double Prix;
	//private Machine[] listMachines;
	//private Vector<Machine> listMachines;
	
	public Produit(int id, String description, double prix) {
		this.Id = id;
		this.Description = description;
		this.Prix = prix;
		//this.listMachines = listMachines;
	}
	
	public Produit(){}
	
	public int getId() {
		return Id;
	}
	
	public void setId(int id) {
		this.Id = id;
	}
	
	public String getDescription() {
		return Description;
	}
	
	public void setDescription(String description) {
		this.Description = description;
	}
	
	public double getPrix() {
		return Prix;
	}
	
	public void setPrix(double prix) {
		this.Prix = prix;
	}
		
	public boolean equals(Produit produit){
		return this.getId() == produit.getId();
	}
		
/*	public Vector<Machine> getListMachines() {
		return listMachines;
	}

	public void setListMachines(Vector<Machine> listMachines) {
		this.listMachines = listMachines;
	}*/

	/*************************************************************************************************
	 * Fin User by username and password application
	 *************************************************************************************************/
	public static Produit findProduit(String description) {
		try {
				Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				String query = "SELECT * FROM PRODUITS WHERE PRODUIT_NOM = '"+ description +"'";
				ResultSet result = state.executeQuery(query);
				if(result.first())
					return new Produit(result.getInt("PRODUIT_ID"), description, result.getDouble("PRODUIT_PRIX"));
				result.close();
				state.close();	
		} catch (SQLException e) {
				e.printStackTrace();
		}
		return null;
	}

}
