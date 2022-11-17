package concepts;

import jade.content.Concept;

public class Tache implements Concept {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String Nom;
	
	private int jobID;	
	private Machine machine;
	
	public Tache() {
		
	}
	
	public Tache(int id, String nom, int jobID, Machine machine) {
		this.id = id;
		Nom = nom;
		this.jobID = jobID;
		this.machine = machine;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return Nom;
	}
	public void setNom(String nom) {
		Nom = nom;
	}
	public int getJobID() {
		return jobID;
	}
	public void setJobID(int jobID) {
		this.jobID = jobID;
	}
	public Machine getMachine() {
		return machine;
	}
	public void setMachine(Machine machine) {
		this.machine = machine;
	}
	
}
