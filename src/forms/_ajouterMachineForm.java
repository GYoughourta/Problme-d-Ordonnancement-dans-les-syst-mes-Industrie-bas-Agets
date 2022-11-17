package forms;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import concepts.Machine;
import conceptsDAO.DAO;
import conceptsDAO.MachineDAO;
import manager.ManagerGUI;
import ontology.ButtonStyle;
import ontology.UIFont;
import ontology.Database;

public class _ajouterMachineForm extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	/*************************************************************************************************
	 * Declaration of variables.
	 *************************************************************************************************/
	private JDesktopPane principalDesktopPane;
	private JTextField nomText;
	private JTextField dureeText;
	private ButtonStyle ajouterButton;
	private ButtonStyle annulerButton;
	
	public Machine machine = new Machine();
	public Machine[] machines = null;
	public ManagerGUI managerUI;
			
	/*************************************************************************************************
	 * Launch the application.
	 * 
	 * Main void to launch application
	 * 
	 * @param args the command line arguments
	 *************************************************************************************************/
	public static void main(String[] args) {
		UIFont.loadFont();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new _ajouterMachineForm();
			}
		});
	}
	
	/*************************************************************************************************
	 * Create the application.
	 *************************************************************************************************/
	
	public _ajouterMachineForm() {		
		display();
	}
	
	public _ajouterMachineForm(Machine[] machines) {
		//super(machines);
		this.machines = machines;
		display();
	}
	
	public _ajouterMachineForm(ManagerGUI parent1) {
		super(parent1);
		this.managerUI = parent1;
		display();
	}
	
	/*public _ajouterMachineForm(ManagerUI parent) {
		super(parent);
		display();
	}*/
	
	public Machine getMachine() {		
		return this.machine;
	}
	public void setMachine(Machine machine) {		
		this.machine = machine;
	}
	
	public void display() {
		
		pack();
		this.setTitle("Gestion Des Machines..");
    	this.setIconImage(Toolkit.getDefaultToolkit().getImage("resources\\machine-48.png"));    
		//this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
		this.setSize(new Dimension(500, 300));		
		this.setResizable(false);
		this.setLocationRelativeTo(null);	
		principalDesktopPane = new JDesktopPane();
		
		JLabel addNewUserLabel = new JLabel();
		addNewUserLabel.setText("Ajouter une nouvelle machine :");
		//addNewUserLabel.setForeground(Color.WHITE);
		addNewUserLabel.setFont(new Font("Tahoma", 1, 14));
		addNewUserLabel.setBounds(10, 10, 340, 25);
    	principalDesktopPane.add(addNewUserLabel, JLayeredPane.DEFAULT_LAYER);
    	
    	JLabel descriptionLabel = new JLabel();
    	//userFirstnameLabel.setFont(new Font("Tahoma", 0, 12));
    	//userFirstnameLabel.setForeground(new Color(255, 255, 255));
    	descriptionLabel.setText("NOM* :");
    	descriptionLabel.setBounds(40, 50, 150, 30);
        principalDesktopPane.add(descriptionLabel, JLayeredPane.DEFAULT_LAYER);
        
        nomText = new JTextField();
        nomText.setBounds(150, 50, 300, 30);
        principalDesktopPane.add(nomText, JLayeredPane.DEFAULT_LAYER);
        
        JLabel prixLabel = new JLabel();
        //userLastnameLabel.setFont(new Font("Tahoma", 0, 12));
        //userLastnameLabel.setForeground(new Color(255, 255, 255));
        prixLabel.setText("DUREE* :");
        prixLabel.setBounds(40, 90, 150, 30);
        principalDesktopPane.add(prixLabel, JLayeredPane.DEFAULT_LAYER);
        
        dureeText = new JTextField();
        dureeText.setBounds(150, 90, 300, 30);
        principalDesktopPane.add(dureeText, JLayeredPane.DEFAULT_LAYER);
           
        JLabel champsObligatoireLabel = new JLabel();
        champsObligatoireLabel.setText("(*) : Champs Obligatoire");
        champsObligatoireLabel.setForeground(Color.RED);
        champsObligatoireLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 10));
        champsObligatoireLabel.setBounds(40, 150, 340, 20);
        principalDesktopPane.add(champsObligatoireLabel, JLayeredPane.DEFAULT_LAYER);
        
        annulerButton = new ButtonStyle();    	
        //userCancelButton.setForeground(new Color(255, 255, 255));
        annulerButton.setIcon(new ImageIcon("resources/close.png"));
        annulerButton.setText("Annuler");
        //userCancelButton.setFont(new Font("Tahoma", 1, 12));
        annulerButton.setBounds(100, 190, 130, 45);
        annulerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        principalDesktopPane.add(annulerButton, JLayeredPane.DEFAULT_LAYER);
        annulerButton.addActionListener(this);
       
        ajouterButton = new ButtonStyle();
        //userAddButton.setForeground(new Color(255, 255, 255));
        ajouterButton.setIcon(new ImageIcon("resources/User-Interface-Save-As-icon.png")); 
        ajouterButton.setText("Ajouter");
        //userAddButton.setFont(new Font("Tahoma", 1, 12));
        ajouterButton.setBounds(270, 190, 130, 45);
        ajouterButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        principalDesktopPane.add(ajouterButton, JLayeredPane.DEFAULT_LAYER);
        ajouterButton.addActionListener(this);	      
        
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        principalDesktopPane.repaint();
        this.setContentPane(principalDesktopPane);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg) {
		// TODO Auto-generated method stub
		
		if (arg.getSource() == ajouterButton) {
			if (!nomText.getText().equals("") && !dureeText.getText().equals("")) {
				if(Machine.findMachine(nomText.getText()) == null) {
					
					machine.setNom(nomText.getText());
					machine.setDuree(Integer.parseInt(dureeText.getText()));
		             
					//managerUI.plan.add(machine);
					//machine.setAgentMachine("Agent-Machine#" + machine.getOperation());
					
					DAO<Machine> machineDao = new MachineDAO(Database.getInstance());
					if (machineDao.insert(machine) != 0){
						setMachine(Machine.findMachine(machine.getNom()));
						JOptionPane.showMessageDialog(this, "Machine insirer avec succès.. !", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
						this.dispose();
					}
					else 
						JOptionPane.showMessageDialog(this, "Erreur d'insirtion.. !", "ERROR", JOptionPane.ERROR_MESSAGE);
				}else {
					JOptionPane.showMessageDialog(this, "Machine existe déjà.. !", "WARNING", JOptionPane.WARNING_MESSAGE);
				}
			}else {
				JOptionPane.showMessageDialog(this, "Type et durée d'opération require", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (arg.getSource() == annulerButton) {
    		nomText.setText("");
    		dureeText.setText("");
		}
		
	}

}
