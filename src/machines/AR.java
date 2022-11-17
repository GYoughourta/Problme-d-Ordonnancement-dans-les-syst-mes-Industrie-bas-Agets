package machines;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import concepts.Job;
import concepts.Machine;
import concepts.Produit;
import concepts.Tache;
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
import jade.lang.acl.UnreadableException;
import manager.AM;
import manager.ManagerGUI;
import ontology.CevitalOntology;
import ontology.Database;

public class AR extends  GuiAgent{
	
	private static final long serialVersionUID = 1L;


	public static final int EXIT = 0;

	private Codec codec = new SLCodec();
	private Ontology ontology = CevitalOntology.getInstance();
	
	private MachineGUI machineGUI; 			// l'interface graphique du l'agent machine
	private Boolean etatMachine = false; 	// l'état de la machine
	private Job job;						// le job à excuter
	
	public void setJob(Job job) {
		this.job = job;
	}
	public Job getJob() {
		return this.job;
	}
	
	public void setEtatMachine(Boolean etatMachine) {
		this.etatMachine = etatMachine;
	}
	public Boolean getEtatMachine() {
		return this.etatMachine;
	}
	
	@Override
	protected void setup() {
		
		// Enregistrements de la langue et de l'ontologie:
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		
		/* Registering with DF*/
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID()); 
		ServiceDescription sd  = new ServiceDescription();
		sd.setType("Machine");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {  
			DFService.register(this, dfd );
		}
		catch (FIPAException fe) { 
			fe.getStackTrace();
		}
		
		// Créer et afficher l'interface graphique
		machineGUI = new MachineGUI(this);
		machineGUI.setTitle(getAID().getName() +  "-> CEVITAL : AGENT MACHINE");
    	machineGUI.setVisible(true);
    	
		System.out.println("Agent-" + this.getAID().getName() + "-> est près.");
		doWait(1000);
		
		// Comportement de l'agent lors d'un évenement
		addBehaviour(new RecevoirJobExecution(this));
		//addBehaviour(new RecevoirTacheExecuter(this));
				
	}
	/*@Override
	protected void takeDown() {
		super.takeDown();
		try {
			DFService.deregister(this);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(machineGUI != null) {
			//machineGUI.clean();
			machineGUI.dispose();
		}
		System.out.println(getAID().getName() + "-> est deregister du service");
	}*/
	
	
	/**
	 * @author JIGHOU
	 * 
	 * la classe montre le comportement à implémenter par l'agent machine lors de la reception du job 
	 * ou lorsque un évenement c'est produit
	 */
	class RecevoirJobExecution extends CyclicBehaviour {
		
		private static final long serialVersionUID = 1L;
		
		RecevoirJobExecution(AR aThis){
			super(aThis);
		}

		public void action() {

			MessageTemplate msgTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),										   					  	 
															  MessageTemplate.MatchConversationId("Execute-Job:AM->AR"));
			ACLMessage msg = receive(msgTemplate);

			if (msg != null) {
				
				try {
					
					/** recupération du job à excuter à partir le message envoyé par Manager ou transferé par agent Machine **/
					setJob((Job) msg.getContentObject());
					AID sender = msg.getSender();
					
			        /** excution de la tache du job sur cette machine et actualiser le GUI du l'agent machine **/
					new SwingWorker<Void, Void>() {
			            @Override
			            protected Void doInBackground() throws Exception {
			            	machineGUI.machineProcessing();
			            	machineGUI.currentStatutPanel.setJob(getJob().getNom());
			            	machineGUI.currentStatutPanel.setTache("sur "+getJob().getMachines().get(getJob().getTacheCourante()).getNom());
			            	machineGUI.currentStatutPanel.setDuree(getJob().getMachines().get(getJob().getTacheCourante()).getDuree());			            	
							return null;
			            }
			        }.execute();
			        setEtatMachine(false);
			        //for (int q=0; q<getJob().getQuantite(); q++){
					try {
				    	  TimeUnit.SECONDS.sleep(getJob().getMachines().get(getJob().getTacheCourante()).getDuree());
				     } catch (InterruptedException e) {
				    	  // TODO Auto-generated catch block 
				    	  e.printStackTrace();
				     }
					System.out.println(getAID().getName() + "-> occupée, [Job="+getJob().getId() + " Quantité traité="+1 +"]");
			        
					/** s'il y a un suivant dans la liste des machines séquentielles et qu'il soit libre
					 *  je vais transferer le job pour achever l'excution de la tache suivante. 
					 */
					if(getJob().getTacheCourante() < getJob().getMachines().size()-1){
						
						System.out.println("tache courante = "+getJob().getTacheCourante());
						System.out.println("size "+ getJob().getMachines().size());
										
						ACLMessage msgAchever = new ACLMessage(ACLMessage.REQUEST);
						msgAchever.setLanguage(codec.getName());
						msgAchever.setOntology(ontology.getName());
						msgAchever.setConversationId("Execute-Job:AM->AR");
						try {
							//passer à la tache courante a la tache suivante
							getJob().setTacheCourante(getJob().getTacheCourante()+1);
							setJob(getJob());
							msgAchever.setContentObject(getJob());
						}catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						AID receiver = new AID(getJob().getMachines().get(getJob().getTacheCourante()).getNom(), AID.ISLOCALNAME);
				        msgAchever.addReceiver(receiver); 
				        send(msgAchever);
						System.out.println(getAID().getName() + "-> achevé à l'agent = " + receiver.getLocalName());
					
					}else { /** sinon j'envoie un message au sender pour lui informer que j'ai terminer la tache **/ 	
						
						/** j'envoie un message au Manager pour lui informer que j'ai terminer le Job **/ 
						ACLMessage msgJob = new ACLMessage(ACLMessage.INFORM_IF);
						msgJob.setLanguage(codec.getName());
						msgJob.setOntology(ontology.getName());
						msgJob.setConversationId("Terminer-Job:AR->AM");
						msgJob.setContent("terminer-job");
				        msgJob.addReceiver(new AID("Manager", AID.ISLOCALNAME)); 
				        send(msgJob);    
				        System.out.println(getAID().getName() + "-> job terminer.");
						new SwingWorker<Void, Void>() {
				            @Override
				            protected Void doInBackground() throws Exception {
				            	//machineGUI.machineDisponible();
				            	//machineGUI.currentStatutPanel.reset();			            	
								return null;
				            }
				        }.execute();
				        
				        // j'atend d'autre tache!
						//block();
					}
					
					/** j'envoie un message au sender pour lui informer que j'ai terminer la tache **/ 
					ACLMessage msgTache = new ACLMessage(ACLMessage.INFORM_IF);
					msgTache.setLanguage(codec.getName());
					msgTache.setOntology(ontology.getName());
					msgTache.setConversationId("Terminer-Tache:AR->AR");
					msgTache.setContent("terminer-tache");
			        msgTache.addReceiver(sender); 
			        send(msgTache);    
			        System.out.println(getAID().getName() + "-> terminer à " + sender.getLocalName());
					new SwingWorker<Void, Void>() {
			            @Override
			            protected Void doInBackground() throws Exception {
			            	machineGUI.machineDisponible();
			            	machineGUI.currentStatutPanel.reset();			            	
							return null;
			            }
			        }.execute();
					
					//block();
		
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.getStackTrace();
				}
				
			}else {
				//System.out.println("Agent-Machine:" + getAID().getName() + "-> j'attend un message");
				block();
			}
		}
	}
			
	@Override
	protected void onGuiEvent(GuiEvent ev) {
		// TODO Auto-generated method stub
		/*if(ev.getType() == lancerjobsGuiEvent) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setLanguage(codec.getName());
			msg.setOntology(ontology.getName());
			msg.setConversationId("produitEvent:AN->AM");	
	        try {                	            
		          msg.setContentObject((Produit) ev.getParameter(0));
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(getAID().getName() + "-> Problème d'envoyer le produit!");
			}
	        msg.addReceiver(new AID("Manager", AID.ISLOCALNAME)); 
		    send(msg);
			System.out.println("Agent-Negociateur:" + getAID().getName() + "-> mise à jour sur la liste des produits.");
		}*/
	   
		
	}

}

