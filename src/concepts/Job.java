package concepts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import jade.content.Concept;
import ontology.Database;

public class Job implements Concept {
	private static final long serialVersionUID = 1L;
	
	private int Id;
	private String Nom;
	private Produit Produit;
	private int Produit_Id;

	private int Quantite;
	private double Cout;
	private long DueDate;
	
	private ArrayList<Machine> Machines;
	private ArrayList<Tache> Taches;
	private int TacheCourante;
	
	private int Duree; 
	private String Etat;
	
	public Job() {
	}

	public Job(int id, String nom, int quantite) {
		this.Id = id;
		this.Nom = nom;
		this.Quantite = quantite;
	}
	
	public Job(int id, String nom, int produit_id, int quantite, String etat) {
		super();
		Id = id;
		Nom = nom;
		Produit_Id = produit_id;
		Quantite = quantite;
		Etat = etat;
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
	
	public int getProduit_Id() {
		return Produit_Id;
	}

	public void setProduit_Id(int produit_Id) {
		Produit_Id = produit_Id;
	}

	public int getQuantite() {
		return Quantite;
	}
	public void setQuantite(int quantite) {
		Quantite = quantite;
	}
	
	public ArrayList<Tache> getTaches() {
		return Taches;
	}

	public void setTaches(ArrayList<Tache> taches) {
		Taches = taches;
	}
	
	public double getCout() {
		return Cout;
	}

	public void setCout(double cout) {
		Cout = cout;
	}

	public long getDueDate() {
		return DueDate;
	}

	public void setDueDate(long dueDate) {
		DueDate = dueDate;
	}

	public Produit getProduit() {
		return Produit;
	}

	public void setProduit(Produit produit) {
		Produit = produit;
	}
	

	public int getTacheCourante() {
		return TacheCourante;
	}

	public void setTacheCourante(int tacheCourante) {
		TacheCourante = tacheCourante;
	}
	
	

	public ArrayList<Machine> getMachines() {
		return Machines;
	}

	public void setMachines(ArrayList<Machine> machines) {
		Machines = machines;
	}

	public int getDuree() {
		return Duree;
	}

	public void setDuree(int duree) {
		Duree = duree;
	}

	public String getEtat() {
		return Etat;
	}

	public void setEtat(String etat) {
		Etat = etat;
	}

	/*************************************************************************************************
	 * Fin User by username and password application
	 *************************************************************************************************/
	public static Job findJob(String nom) {
		try {
				Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				String query = "SELECT * FROM JOBS WHERE JOB_NOM = '"+ nom +"'";
				ResultSet result = state.executeQuery(query);
				if(result.first())
					return new Job(result.getInt("JOB_ID"), nom, result.getInt("JOB_QUANTITE"));
				result.close();
				state.close();	
		} catch (SQLException e) {
				e.printStackTrace();
		}
		return null;
	}
	
}
