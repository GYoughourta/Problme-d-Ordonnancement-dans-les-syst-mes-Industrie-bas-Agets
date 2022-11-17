package test2;

import javax.swing.SwingUtilities;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import job.AgentJob;
import manager.AM;
import ontology.UIFont;

public class test2 {
	AgentContainer ac=null;
	public AgentContainer getAc() {
		return ac;
	}

	public void setAc(AgentContainer ac) {
		this.ac = ac;
	}
	public static void main(String[] args) {
		
		UIFont.loadFont();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
			try {		              
		        	 Runtime myRuntime = Runtime.instance();
		        	 Profile myProfile = new ProfileImpl();		       
						
				
					ContainerController myContainer = myRuntime.createMainContainer(myProfile);
					AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
					rma.start();
							
					Profile profile = new ProfileImpl(false);
					profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
					// Creer deux identique clients.
					AgentContainer newContainer = myRuntime.createAgentContainer(profile);
					newContainer.start();
                    String v_snifer;

                     AgentController agentAM = newContainer.createNewAgent("Job", AgentJob.class.getName(), null);              
                     agentAM.start();
		             v_snifer="Job;";
                     
		             AgentController agentAN = newContainer.createNewAgent("Manager", AM.class.getName(), null);
		             agentAN.start();
		             v_snifer+="Manager";		             

		             newContainer.createNewAgent("sniffer", "jade.tools.sniffer.Sniffer", new Object[]{v_snifer}).start();
		             
		             /*Machine machine1 = new Machine(1, "M1", 20000);
		             Machine machine2 = new Machine(2, "M2", 50000);
		             
		             AgentController agentAR1 = newContainer.createNewAgent("Machine-"+machine1.getOperation(), AR.class.getName(), null);            
		             AgentController agentAR2 = newContainer.createNewAgent("Machine-"+machine2.getOperation(), AR.class.getName(), null);
		             
		             String v_snifer;
		             v_snifer="Manager;";
		             v_snifer+="Negociateur;";
		             v_snifer+="Machine";

		             newContainer.createNewAgent("sniffer", "jade.tools.sniffer.Sniffer", new Object[]{v_snifer}).start();*
		             
		              
		             agentAM.start();
		             agentAN.start();
		             agentAR1.start();
		             agentAR2.start();*/

		          } catch (Exception e) {
		              e.printStackTrace();
		      }

			}
		});
	}
}
