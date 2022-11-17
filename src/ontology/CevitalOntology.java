package ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

public class CevitalOntology extends BeanOntology {

	private static final long serialVersionUID = 1L;
	
	private static Ontology instance = new CevitalOntology("my_ontology");
	
	public static Ontology getInstance(){
		return instance;
	}
	
	//singleton pattern
	private CevitalOntology(String name) {
		super(name);
		
		try {			
			add("concepts");	
			//add("predicates");
		} catch (BeanOntologyException e) {
			e.printStackTrace();
		}
	}
	
	

}
