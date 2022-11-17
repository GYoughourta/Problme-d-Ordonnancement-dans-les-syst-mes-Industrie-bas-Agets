package manager;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.SwingWorker;

import concepts.Job;
import concepts.Machine;
import concepts.Produit;
import concepts.Tache;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import machines.AR;
import ontology.CevitalOntology;
import ontology.Database;

public class AM extends GuiAgent{
	
	private static final long serialVersionUID = 1L;
	
	//public static final int nouveauJobGuiEvent = 12;
	public static final int produitGuiEvent = 111;
	public static final int lancerJobsGuiEvent = 113;

	
	private AID jobAID = new AID("Job", AID.ISLOCALNAME);
	private AID machineAID = new AID("Machine", AID.ISLOCALNAME);
	
	private ArrayList<AR> agentsMachinesAID = new ArrayList<>();

	private Codec codec = new SLCodec();
	private Ontology ontology = CevitalOntology.getInstance();
	private ManagerGUI managerGUI;

	private ArrayList<Job> jobs = new ArrayList<>();
	private ArrayList<Job> jobcourant=new ArrayList<>();
	private Job nouveauJob = new Job();
	private int jobsindice=0;
	
	private Job courantJob = new Job();
	private int indiceJob = 0;
	
	private ArrayList<Machine> machines = new ArrayList<>();
	
	public ArrayList<Machine> getMachines() {
		return machines;
	}

	public void setMachines(ArrayList<Machine> machines) {
		this.machines = machines;
	}

	public Job getNouveauJob() {
		return nouveauJob;
	}

	public void setNouveauJob(Job job) {
		this.nouveauJob = job;
	}
	
	
	public Job getCourantJob() {
		return courantJob;
	}

	public void setCourantJob(Job courantJob) {
		this.courantJob = courantJob;
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
		sd.setType("Manager");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		
		try {  

			DFService.register(this, dfd );
		}
		catch (FIPAException fe) { 
			fe.getStackTrace();
		}
		//FindMachines();
		
		// Créer et afficher l'interface graphique
		managerGUI = new ManagerGUI(this);
		managerGUI.setTitle(getAID().getName() +  "-> CEVITAL : AGENT MANAGER");
		setMachines(sequenceMachineFlowShop());
		
		System.out.println("Agent-" + this.getAID().getName() + "-> est près.");
		//doWait(1000);
		
		// Comportement de l'agent lors d'un évenement
		addBehaviour(new RecevoirNouveauJob(this));
		
		/*ArrayList<Machine> listTemp = null;
		
		for (int i=1; i<3; i++){
			Machine machine = new Machine(i, "M"+i, 25*i);
			orders.add(machine);
		}*/
		//setMachines(listTemp);
		
		/*SequentialBehaviour sequentialBehaviour = new SequentialBehaviour();		
		sequentialBehaviour.addSubBehaviour(new RecevoirNouveauJob(this));		
		sequentialBehaviour.addSubBehaviour(new RecevoirLancerExecution(this));		
		addBehaviour(sequentialBehaviour);*/
		
		//addBehaviour(new RecevoirNouveauJob(this));
		//addBehaviour(new RecevoirLancerExecution(this));
		//LancerExecution();
	}
	
	protected ArrayList<Machine> sequenceMachineFlowShop() {
		machines  = new ArrayList<>();
		//DAO<Machine> machineDao = new MachineDAO(Database.getInstance());
		try {
			Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = state.executeQuery("SELECT * FROM MACHINES");
			while (result.next()) {
				Machine machine = new Machine(result.getInt("MACHINE_ID"), result.getString("MACHINE_NOM"), result.getInt("MACHINE_DUREE"));
				machines.add(machine);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
		}

		return machines;
	}
		
	/*@Override
	protected void takeDown() {
		super.takeDown();
		try {
			DFService.deregister(this);
		}
		catch (Exception e) {
			
		}
		if(managerGUI != null) {
			//managerGUI.clean();
			managerGUI.dispose();
		}
	}*/
	
	
	class RecevoirNouveauJob extends CyclicBehaviour {
		
		private static final long serialVersionUID = 1L;
		
		RecevoirNouveauJob(AM aThis){
			super(aThis);
		}

		public void action() {

			MessageTemplate msgTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.AGREE),										   					  	 
															  MessageTemplate.MatchConversationId("Noveau-Job:AJ->AM"));
			ACLMessage msg = receive(msgTemplate);

			if (msg != null) {
				
				try {
					setNouveauJob((Job) msg.getContentObject());
					
					AID sender = msg.getSender();
					new SwingWorker<Void, Void>() {
			            @Override
			            protected Void doInBackground() throws Exception {
			            	managerGUI.personnelsButton.setEnabled(true);
			            	managerGUI.produitsButton.setEnabled(true);
			            	managerGUI.machinesButton.setEnabled(true);
			            	managerGUI.jobsButton.setEnabled(false);					            	
			            	managerGUI.principalDesktopPane.remove(managerGUI.viewPersonnelsPanel);					     
			            	managerGUI.principalDesktopPane.remove(managerGUI.viewProduitsPanel);					  
			            	managerGUI.principalDesktopPane.remove(managerGUI.viewMachinesPanel);					            
			            	managerGUI.principalDesktopPane.remove(managerGUI.viewJobsPanel);
			            	managerGUI.ajouterJob(getNouveauJob());			      
			            			
			            	setNouveauJob(Job.findJob(getNouveauJob().getNom()));
			            	managerGUI._setViewJobsPanel();
			                //return null;
							return null;
			            }
			        }.execute();
			        
			        //doWait(2000);
					
					getNouveauJob().setMachines(getMachines());
					jobs.add(getNouveauJob());
					
					ACLMessage msgConfirmation = new ACLMessage(ACLMessage.INFORM);
					msgConfirmation.setLanguage(codec.getName());
					msgConfirmation.setOntology(ontology.getName());
					msgConfirmation.setConversationId("job-reçu:AM->AJ");
					msgConfirmation.setContent("confirmer");
			        msgConfirmation.addReceiver(sender); 
			        send(msgConfirmation);
						
					System.out.println("Agent-:" + getAID().getName() + "-> Job reçu, [Job:ID=" + getNouveauJob().getId()+ "].");

				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.getStackTrace();
				}
				
			}else {
				//System.out.println("Agent-Negociateur:" + getAID().getName() + "-> j'attend un message");
				block();
			}
		}
	}
	
	class RecevoirTerminerJob extends CyclicBehaviour {
		
		private static final long serialVersionUID = 1L;
			
			RecevoirTerminerJob(AM aThis){
				super(aThis);
			}

			public void action() {

				MessageTemplate msgTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM_IF),										   					  	 
																  MessageTemplate.MatchConversationId("Terminer-Job:AR->AM"));
				ACLMessage msg = receive(msgTemplate);

				if (msg != null) {				
				
						//setNouveauJob((Job) msg.getContentObject());
						if(indiceJob < jobs.size()) {
							
							setCourantJob(jobs.get(indiceJob));
							//AID sender = msg.getSender();
										        
					        //doWait(2000);
							
					      //l'agent Manager envoie la première tache du job  
							ACLMessage msgJobTerminer = new ACLMessage(ACLMessage.REQUEST);
							msgJobTerminer.setLanguage(codec.getName());
							msgJobTerminer.setOntology(ontology.getName());
							msgJobTerminer.setConversationId("Execute-Job:AM->AR");
					        try {                	            
						          msgJobTerminer.setContentObject(getCourantJob());
					        } catch (IOException e) {
								// TODO Auto-generated catch block
								e.getStackTrace();
							}
					        msgJobTerminer.addReceiver(new AID(jobs.get(0).getMachines().get(0).getNom(), AID.ISLOCALNAME)); 
						    send(msgJobTerminer);
							System.out.println(getAID().getName() + "-> envoyer job" + getCourantJob().getNom());
							
							indiceJob++;
						}else {
							indiceJob = 0;
						}
					
				}else {
					//System.out.println("Agent-Negociateur:" + getAID().getName() + "-> j'attend un message");
					block();
				}
			}
	}
	
	/**
	 * Envoyer un événement managerGUI: creation de nouveau produit et l'envoyer à l'agent Job.
	 * Envoyer un événement managerGUI: envoyer l'excution du premier job.
	 */
	@Override
	protected void onGuiEvent(GuiEvent ev) {
		
		if(ev.getType() == produitGuiEvent) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setLanguage(codec.getName());
			msg.setOntology(ontology.getName());
			msg.setConversationId("produitEvent:AM->AJ");	
	        try {                	            
		          msg.setContentObject((Produit) ev.getParameter(0));
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.getStackTrace();
			}
	        msg.addReceiver(new AID("Job", AID.ISLOCALNAME)); 
		    send(msg);
			System.out.println(getAID().getName() + "-> mise à jour sur la liste des produits.");
			
			System.out.println(ev.getType());
			System.out.println(lancerJobsGuiEvent);
		}
		
		if(ev.getType() == lancerJobsGuiEvent) {	
			
			//System.out.println(ev.getType());
			
			// creation et lancement des des agents machines
			try{				
				Runtime myRuntime = Runtime.instance();
	        	Profile profile = new ProfileImpl(false);
				profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
		
				AgentContainer newContainer = myRuntime.createAgentContainer(profile);
				newContainer.start();
				
				for(Machine machine : getMachines()){
					AgentController agentAR = newContainer.createNewAgent(machine.getNom(), AR.class.getName(), null);
					agentAR.start();
				}
			} catch (ControllerException e) {
				// TODO Auto-generated catch block
				e.getStackTrace();
			}
			
			//doWait(1000);
			
			//l'agent Manager envoie la première tache du job  
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setLanguage(codec.getName());
			msg.setOntology(ontology.getName());
			msg.setConversationId("Execute-Job:AM->AR");
	        try {                	            
		          msg.setContentObject(jobs.get(0));
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.getStackTrace();
			}
	        msg.addReceiver(new AID(jobs.get(0).getMachines().get(0).getNom(), AID.ISLOCALNAME)); 
		    send(msg);
			System.out.println(getAID().getName() + "-> envoyer job" +jobs.get(0).getNom());
		}
		
	}
		

}
