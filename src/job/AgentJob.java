package job;

import java.awt.Cursor;
import java.io.IOException;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.CevitalOntology;
import ontology.Database;
import concepts.Job;
import concepts.Produit;
import conceptsDAO.DAO;
import conceptsDAO.ProduitDAO;

public class AgentJob extends GuiAgent{
	
	private static final long serialVersionUID = 1L;
	
	/** code shared with the gui to quit the agent */
	public static final int EXIT = 0;
	
	private Codec codec = new SLCodec();
	private Ontology ontology = CevitalOntology.getInstance();
	
	private JobGUI jobGUI;
	public static final int nouveauJobGuiEvent = 112;
	
	private AID managerAID = new AID("Manager", AID.ISLOCALNAME);

	@Override
	protected void setup() {
		
		// Enregistrements de la langue et de l'ontologie:
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
						
        /* Registering with DF*/
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID()); 
		ServiceDescription sd  = new ServiceDescription();
		sd.setType("Manager");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {  
			DFService.register(this, dfd );
		}
		catch (FIPAException fe) { 
			fe.getStackTrace();
		}
		
	    // Créer et afficher l'interface graphique
		jobGUI = new JobGUI(this);
		jobGUI.setTitle(getAID().getName() +  "-> CEVITAL : AGANT JOB");
		//pgui.setVisible(true);
		
		System.out.println("Agent-" + this.getAID().getName() + "-> est près.");
		//doWait(1000);
		
		// Comportement de l'agent lors d'un évenement
	    addBehaviour(new RecevoirProduitEvent(this));		

	}
		
	//@Override
	/*protected void takeDown() {
		super.takeDown();
		try {
			DFService.deregister(this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if(jobGUI != null) {
			//managerGUI.clean();
			jobGUI.dispose();
		}
	}*/
	
	/**
	 * Recevoir et traiter le nouveau produit envoyé par l'agents Manager.
	 */	
	class RecevoirProduitEvent extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;
		
		RecevoirProduitEvent(AgentJob aThis){
			super(aThis);
		}
			
		@Override
		public void action() {
			try {
				MessageTemplate msgTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),										   					  	 
																  MessageTemplate.MatchConversationId("produitEvent:AM->AJ"));
				ACLMessage aclMessage = receive(msgTemplate);
					
				if(aclMessage != null) {		
					
					/*if(!aclMessage.getContentObject().equals("SupprissionProduit"))
					//else{
						Produit produit = (Produit) aclMessage.getContentObject();							
						AID sender = aclMessage.getSender();
						System.out.println(getAID().getName() + "-> Produit reçu, Sender:" + sender.getLocalName() + ", Produit:" + produit.getDescription() + ".");								
					//}*/
					new SwingWorker<Void, Void>() {
						@Override
						protected Void doInBackground() throws Exception {						   	
						   	jobGUI.getContentPane().remove(jobGUI.produitsTableScrollPane);
						  	jobGUI.produitsTableModel = new DefaultTableModel();			
							String query = "SELECT PRODUIT_ID, PRODUIT_NOM, PRODUIT_PRIX FROM PRODUITS";
							jobGUI.produitsTableModel.setDataVector(Database.getTableDatas(query), Database.getTableHeaders(query));
							jobGUI.produitsTable = new JTable(jobGUI.produitsTableModel);					        			
							jobGUI.produitsTableScrollPane = new JScrollPane(jobGUI.produitsTable);
							jobGUI.produitsTableScrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
							jobGUI.produitsTableScrollPane.setBounds(5, 120, 685, 150);
							jobGUI.getContentPane().add(jobGUI.produitsTableScrollPane); 
							return null;
						}
					}.execute();
				}else {
					block();
				}
					
			}catch (Exception e) {
				e.getStackTrace();
			}
		}
	}
	

	@Override
	protected void onGuiEvent(GuiEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getType() == AgentJob.EXIT) {
			doDelete();
		}
		
		if(ev.getType() == nouveauJobGuiEvent) { 
			// Créez la commande et traiter  les messages à envoyez à CommandeAgent.
			Job job = new Job();			
			Produit produit = new Produit();
			DAO<Produit> produitDao = new ProduitDAO(Database.getInstance());	
			produit = produitDao.find((int) ev.getParameter(0));	
			job.setNom("Job@" + produit.getDescription());			
			job.setProduit(produit);					
			job.setQuantite((int) ev.getParameter(1));					
			job.setCout(produit.getPrix() * job.getQuantite());
			job.setDueDate((long) System.currentTimeMillis());				
				
			// Prepare le message de la commande.
			ACLMessage msg = new ACLMessage(ACLMessage.AGREE);	
			msg.setLanguage(codec.getName());
			msg.setOntology(ontology.getName());
			msg.setConversationId("Noveau-Job:AJ->AM");			
			try {
				msg.setContentObject(job);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.getStackTrace();
				System.out.println(getAID().getName() + "-> Problème d'envoyer le job!");
			}
			msg.addReceiver(managerAID);
			send(msg);
			
			System.out.println("Agent-: " +getAID().getName() + "-> nouveau job, Job:[ID="+job.getId() + ", Nom="+ job.getNom() + ", Quantité="+job.getQuantite() +"].");
		}
	}
	
}