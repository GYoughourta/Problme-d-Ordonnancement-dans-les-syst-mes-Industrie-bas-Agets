package manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import com.alee.laf.rootpane.WebFrame;
import concepts.Job;
import concepts.Machine;
import concepts.Produit;
import concepts.User;
import conceptsDAO.DAO;
import conceptsDAO.JobDAO;
import conceptsDAO.MachineDAO;
import conceptsDAO.ProduitDAO;
import conceptsDAO.UserDAO;
import forms._ajouterMachineForm;
import forms._ajouterPersonForm;
import forms._ajouterProduitForm;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.util.Logger;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import job.AgentJob;
import machines.AR;
import machines.MachineGUI;
import ontology.ButtonStyle;
import ontology.Database;
import ontology.UIFont;


/**
 * @author GUENANA El-Rabia
 * 
 * Main UI for general Interface. 
 */
public class ManagerGUI extends WebFrame implements ActionListener, MouseListener {
	
	private static final long serialVersionUID = 1L;

	private AR machineAgent;
	private AM managerAgent;
	
	private ArrayList<AID> agentsMachinesAID;

	/*************************************************************************************************
	 *                              Declaration des variables globales
	 *************************************************************************************************/
	public JDesktopPane principalDesktopPane = new JDesktopPane();
	private Dimension tailleEcran;
	private int largeurEcran;
	private int hauteurEcran;
	
	/** variables de la méthode _setLoginPanel **/
	private JPanel loginViewPanel;
	private JTextField utilisateurText;
	private JPasswordField passeText;
	private ButtonStyle annulerButton;
	private ButtonStyle entreeButton;
	
	/** variables de la méthode _setPortailPanel **/
	private JLabel identifiantLabel;
	public ButtonStyle personnelsButton;	
	public ButtonStyle produitsButton;
	public ButtonStyle machinesButton;
	public ButtonStyle jobsButton;	

	public JPanel viewPersonnelsPanel = new JPanel();
	public JPanel viewProduitsPanel = new JPanel();
	public JPanel viewMachinesPanel = new JPanel();	
	public JPanel viewJobsPanel = new JPanel();
	
	/** variables de la méthode _setMenuBar **/
	private JMenu cevitalMenu;
	private JMenu developpeurMenu;
	private JMenuItem agentsMachinesMenuItem;
	private JMenuItem produitsMenuItem;
	private JMenuItem machinesMenuItem;
	private JMenuItem jobsMenuItem;
	private JMenuItem quitterMenuItem;
	private JMenuItem aideMenuItem;
	private JMenuItem aproposMenuItem;
	
	 /** variables de la méthode _setViewPersonnelsPanel **/
	private DefaultTableModel personnelsTableModel;
	private JTable personnelsTable;
	private JScrollPane personnelsTableScrollPane;
	private JButton ajouterPersonButton;
	private JButton editerPersonButton;
	private JButton supprimerPersonButton;
	
	 /** variables de la méthode _setViewProduitsPanel **/
	private DefaultTableModel produitsTableModel;
	private JTable produitsTable;
	private JScrollPane produitsTableScrollPane;
	private JButton ajouterProduitButton;
	private JButton editerProduitButton;
	private JButton supprimerProduitButton;
	
	 /** variables de la méthode _setViewMachinesPanel **/
	private DefaultTableModel machinesTableModel;
	private JTable machinesTable;
	private JScrollPane machinesTableScrollPane;
	private JButton ajouterMachineButton;
	private JButton editerMachineButton;
	private JButton supprimerMachineButton;
	
	 /** variables de la méthode _setViewJobsPanel **/
	private DefaultTableModel jobsTableModel;
	private JTable jobsTable;
	private JScrollPane jobsTableScrollPane;
	private JButton remonterJobButton;
	private JButton descenderJObButton;
	private JButton supprimerJobButton;
	private ButtonStyle lancerJobsButton;	


	/*************************************************************************************************
	 *                             Creation de l'application AgentCoordinateur 
	 *************************************************************************************************/
	public ManagerGUI() {		
		initComponents();
		showGui();
        
		//_getProduitsTable();
		//emptyForm();
	}
	
	public ManagerGUI(AM am) throws HeadlessException {
		
		super(am.getName());
		
		managerAgent = am;
		//agentsMachinesAID = arAID;
		
		initComponents();
		showGui();
	}
	
	/*************************************************************************************************
	 *                                Creation de la méthode initComponents 
	 *************************************************************************************************/
	private void initComponents() {
		
		tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
		largeurEcran = tailleEcran.width-200;
		hauteurEcran = tailleEcran.height-200;

		this.getContentPane().remove(principalDesktopPane);		
		_setLoginPanel();		
        this.setContentPane(principalDesktopPane);
	}
	
	/*************************************************************************************************
	 *                                Creation de la méthode showGui 
	 *************************************************************************************************                                
	 * Initialized the parameters of display of the frame and make 
	 * it visible at appropriate location with desired size.
	 *************************************************************************************************/
	private void showGui() {		       
		
		//this.setTitle("CEVITAL" + /*cevitalAgent.getLocalName() +*/ "-> " + "Gestion des produits");
		this.setResizable(false);		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("resources\\home-48.png"));
		
		//this.setSize(new Dimension(largeurEcran, hauteurEcran));
		this.setMinimumSize(new Dimension(largeurEcran, hauteurEcran));
		//this.setMinimumSize(new Dimension(700, 600));
		//this.setPreferredSize(new java.awt.Dimension(880, 680));
		
		//setLocation(centerX - getWidth()/2, centerY - getHeight()/2);
		this.setLocationRelativeTo(null);
		pack();
		super.setVisible(true);
	}
	/**
	 * Main void to launch application
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		UIFont.loadFont();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ManagerGUI();
			}
		});
	}
	
	/*************************************************************************************************
	 *                                Creation de la méthode _setLoginPanel
	 *************************************************************************************************/
	private void _setLoginPanel() {
		
		/** this.setTitle("CEVITAL-> " + pagent.getName() +  ": Connexion"); **/	
		/*largeurEcran = tailleEcran.width-400;
		hauteurEcran = tailleEcran.height-300;
		this.setMinimumSize(new Dimension(largeurEcran, hauteurEcran));*/
		
		principalDesktopPane.removeAll();		
		principalDesktopPane.setBackground(Color.WHITE);
		
		loginViewPanel = new JPanel();	
		utilisateurText = new JTextField();
		passeText = new JPasswordField();
		annulerButton = new ButtonStyle();
		entreeButton = new ButtonStyle();
		
		loginViewPanel.removeAll();
		loginViewPanel.setLayout(null);
		//loginViewPanel.setBackground(Color.LIGHT_GRAY);
		loginViewPanel.setBounds(380, 120, 400, 300);
		
		JLabel titreConnexionLabel = new JLabel();
		titreConnexionLabel.setText("Authentification");
		titreConnexionLabel.setFont(new Font("Times New Roman", 1, 36));
		//titreConnexionLabel.setForeground(java.awt.Color.white);
		//titreConnexionLabel.setFont(new Font("Sans Serif", Font.PLAIN, 36));
		//TitreLabel.setFont(new Font("Tahoma", 1, 20));
		titreConnexionLabel.setBounds(80, 30, 250, 40);
		loginViewPanel.add(titreConnexionLabel);
		
		JLabel utilisateurLabel = new JLabel();
		//UserLabel.setFont(new Font("Tahoma", 0, 12));
        utilisateurLabel.setText("Utilisateur :");
        utilisateurLabel.setBounds(20, 120, 100, 30);
		loginViewPanel.add(utilisateurLabel);		
		
		utilisateurText.setBounds(120, 120, 260, 30);
        loginViewPanel.add(utilisateurText);
        
        JLabel passeLabel = new JLabel();
        passeLabel.setText("Mot de passe :");
        //PassLabel.setFont(new Font("Tahoma", 0, 12));
        passeLabel.setBounds(20, 165, 100, 30);
        loginViewPanel.add(passeLabel);
        
        passeText.setBounds(120, 165, 260, 30);
        loginViewPanel.add(passeText);
         
        entreeButton.setText("Entrée");
        //ButtonEntree.setFont(new Font("Tahoma", 1, 12));
        entreeButton.setIcon(new ImageIcon("resources/Users-Find-User-icon.png"));
        //entreeButton.setBackground(new Color(255, 255, 255));
        entreeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
        entreeButton.setBounds(220, 220, 130, 50);              
        loginViewPanel.add(entreeButton); 
        entreeButton.addActionListener(this);
        
        annulerButton.setText("Annuler");
        //ButtonAnnuler.setFont(new Font("Tahoma", 1, 12));
        annulerButton.setIcon(new ImageIcon("resources/back32.png"));
        annulerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  
        annulerButton.setBounds(50, 220, 130, 50);            
        loginViewPanel.add(annulerButton);
        annulerButton.addActionListener(this);
        
        principalDesktopPane.add(loginViewPanel);
        principalDesktopPane.repaint();
        revalidate();
        //return ViewPanel;
	}
	
	/*************************************************************************************************
	 *                                Creation de la méthode _setLoginPanel
	 *************************************************************************************************/	
	private void _setPortailPanel(User user) {
			
		/** this.setTitle("CEVITAL-> " + pagent.getName() +  ": Portail"); **/ 
		/*largeurEcran = tailleEcran.width-200;
		hauteurEcran = tailleEcran.height-100;
		this.setMinimumSize(new Dimension(largeurEcran, hauteurEcran));*/
		
		principalDesktopPane.removeAll();
		principalDesktopPane.setBackground(Color.WHITE);
		
		identifiantLabel = new JLabel();
		identifiantLabel.setText("Bienvenue " + user.getUsername());
		identifiantLabel.setFont(new Font("Arial Narrow", Font.BOLD, 14));
		identifiantLabel.setForeground(Color.BLUE);
		identifiantLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		identifiantLabel.setBounds(largeurEcran-(10+user.getUsername().length())*8-5, 5, (10+user.getUsername().length())*8, 20);
		principalDesktopPane.add(identifiantLabel, JLayeredPane.DEFAULT_LAYER);
		identifiantLabel.addMouseListener(this);
	    
		JLabel TitreLabel = new JLabel();		
		TitreLabel.setText("GESTION DU LA PRODUCTION DANS L'ATELIER CEVITAL");
		TitreLabel.setFont(new Font("Times New Roman", 1, 18));
		TitreLabel.setBounds(350, 40, 550, 25);
		//TitreLabel.setBounds((largeur/2)-(TitreLabel.getText().length()*15/2), 40, TitreLabel.getText().length()*15, 25);
		principalDesktopPane.add(TitreLabel, JLayeredPane.DEFAULT_LAYER);
		
		personnelsButton = new ButtonStyle();
		personnelsButton.setText("PERSONNELS");
        personnelsButton.setIcon(new ImageIcon("resources/utilisateur-50.png"));        
        personnelsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        personnelsButton.setBounds(10, 100, 180, 65);
		//ButtonAdministration.setEnabled(false);               
        principalDesktopPane.add(personnelsButton, JLayeredPane.DEFAULT_LAYER);
        personnelsButton.addActionListener(this); 
        
        produitsButton = new ButtonStyle();
        produitsButton.setText("  PRODUITS");
        produitsButton.setIcon(new ImageIcon("resources/bdd-50.png"));
        produitsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        produitsButton.setBounds(10, 170, 180, 65);    
        principalDesktopPane.add(produitsButton, JLayeredPane.DEFAULT_LAYER);
        produitsButton.addActionListener(this);
    	
        machinesButton = new ButtonStyle();
        machinesButton.setText("  MACHINES");
        machinesButton.setIcon(new ImageIcon("resources/nas-50.png"));
        machinesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        machinesButton.setBounds(10, 240, 180, 65);     
        principalDesktopPane.add(machinesButton, JLayeredPane.DEFAULT_LAYER);
        machinesButton.addActionListener(this);
        
        jobsButton = new ButtonStyle();
        jobsButton.setText(" LISTE JOBS");
        jobsButton.setIcon(new ImageIcon("resources/audit-50.png"));
        jobsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jobsButton.setBounds(10, 310, 180, 65);     
        principalDesktopPane.add(jobsButton, JLayeredPane.DEFAULT_LAYER);
        jobsButton.addActionListener(this);
        
		JLabel logoCevitalLabel = new JLabel();
		logoCevitalLabel.setIcon(new ImageIcon("resources/cevitalBanner_300x100.png"));
		logoCevitalLabel.setBounds(0 /*largeurEcran-330*/, hauteurEcran - 150, logoCevitalLabel.getIcon().getIconWidth(), logoCevitalLabel.getIcon().getIconHeight());
		principalDesktopPane.add(logoCevitalLabel, JLayeredPane.DEFAULT_LAYER);
        
        principalDesktopPane.repaint();	
        revalidate();	
	}
	
	/*************************************************************************************************
	 *                        Creation de la méthode _setMenuBar
	 *************************************************************************************************/
	private JMenuBar _setMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		cevitalMenu = new JMenu("Action");
		developpeurMenu = new JMenu("?");
		
		agentsMachinesMenuItem = new JMenuItem("Démarrer les agents machines");
		
		//personnelsMenuItem = new JMenuItem("Gestions du Personnels");
		produitsMenuItem = new JMenuItem("Gestion des Produits");
		machinesMenuItem = new JMenuItem("Gestion des Machines");
		jobsMenuItem = new JMenuItem("Gestion des Commandes");
		quitterMenuItem = new JMenuItem("Quitter");
		aideMenuItem = new JMenuItem("Aide");
		aproposMenuItem = new JMenuItem("A propos");
		
		//Menu Personnels
		agentsMachinesMenuItem.addActionListener(this);
		agentsMachinesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
		cevitalMenu.add(agentsMachinesMenuItem);
		
		//Menu Qutter
		quitterMenuItem.addActionListener(this);
		cevitalMenu.addSeparator();
		quitterMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
		cevitalMenu.add(quitterMenuItem);
		
		//Menu Aide
		aideMenuItem.addActionListener(this);
		aideMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Event.CTRL_MASK));
		developpeurMenu.add(aideMenuItem);
		
		//Menu À propos
		aproposMenuItem.addActionListener(this);
		developpeurMenu.addSeparator();		
		aproposMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
		developpeurMenu.add(aproposMenuItem);
		
		menuBar.add(cevitalMenu);
		menuBar.add(developpeurMenu);
		return menuBar;	
	}
	
	/*************************************************************************************************
	 *             Creation de la méthode _setViewPersonnelsPanel
	 *************************************************************************************************/
	private void _setViewPersonnelsPanel(){
		
		viewPersonnelsPanel.removeAll();
		viewPersonnelsPanel.setLayout(null);
		viewPersonnelsPanel.setBounds(200, 100, largeurEcran-210, hauteurEcran-285);
		
		JLabel listUsersLabel = new JLabel();
		listUsersLabel.setText("Table du personnels : ");
		listUsersLabel.setIcon(new ImageIcon("resources/645160.png"));
		//listUsersLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		listUsersLabel.setBounds(10, 5, 200, 60);
		viewPersonnelsPanel.add(listUsersLabel);
		
		personnelsTableModel = new DefaultTableModel();			
		//String query = "SELECT USER_ID, USER_FIRSTNAME, USER_LASTNAME, USER_FUNCTION, USER_USERNAME FROM USERS";
		//Database bdd = new Database(query);
		//defaultTableModel.setDataVector(Database.columnNames, Database.data);	
		String query = "SELECT USER_ID, USER_FIRSTNAME, USER_LASTNAME, USER_FUNCTION, USER_USERNAME FROM USERS";
		personnelsTableModel.setDataVector(Database.getTableDatas(query), Database.getTableHeaders(query));
		personnelsTable = new JTable(personnelsTableModel);
				
        personnelsTableScrollPane = new JScrollPane(personnelsTable);
        personnelsTableScrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	//jScrollPane.setFont(new Font("Tahoma", Font.PLAIN, 11));
    	//jScrollPane.setBounds(10, 65, 880, 200);
    	//jScrollPane.setBounds(200, 100, largeur-260, hauteur-135);
    	personnelsTableScrollPane.setBounds(10, 65, 880, 200);
    	viewPersonnelsPanel.add(personnelsTableScrollPane); 
	
		JLabel tableSizeLabel = new JLabel();
	    tableSizeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
	    tableSizeLabel.setBounds(210, 5, 106, 60);
	    //tableSize.setText(Integer.parseInt(String.valueOf(n)) + " user");
	    tableSizeLabel.setText(personnelsTable.getRowCount()+"");    	
	    viewPersonnelsPanel.add(tableSizeLabel);
	    	
	    ajouterPersonButton = new JButton();	    
	    ajouterPersonButton.setContentAreaFilled(false);	    
	    //ButtonAddElement.setForeground(new Color(255, 255, 255));
	    ajouterPersonButton.setIcon(new ImageIcon("resources/Users-Add-User-icon.png")); 
	    //ButtonAddUser.setText("");
	    //ButtonAddElement.setContentAreaFilled(false);
	    //ButtonAddElement.setFont(new Font("Tahoma", 1, 12));	    
	    ajouterPersonButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));	 
	    ajouterPersonButton.setBounds(900, 75, 48, 48);
	    viewPersonnelsPanel.add(ajouterPersonButton);
	    ajouterPersonButton.addActionListener(this);
	    
	    editerPersonButton = new JButton();
	    editerPersonButton.setContentAreaFilled(false);
	    //ButtonEditElement.setForeground(new Color(255, 255, 255));
	    editerPersonButton.setIcon(new ImageIcon("resources/Users-Edit-User-icon.png")); 
	    //ButtonAddUser.setText("");
	    //ButtonEditElement.setFont(new Font("Tahoma", 1, 12));	    
	    editerPersonButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    editerPersonButton.setBounds(900, 140, 48, 48);		    
	    viewPersonnelsPanel.add(editerPersonButton);
	    editerPersonButton.addActionListener(this); 
	    
	    supprimerPersonButton = new JButton();	
	    supprimerPersonButton.setContentAreaFilled(false);
	    supprimerPersonButton.setIcon(new ImageIcon("resources/Users-Remove-User-icon.png"));	
	    //ButtonDeleteElement.setText("");
	    supprimerPersonButton.setBounds(900, 205, 48, 48);
	    supprimerPersonButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
	    viewPersonnelsPanel.add(supprimerPersonButton);
	    supprimerPersonButton.addActionListener(this);
	    
	    principalDesktopPane.add(viewPersonnelsPanel);
	    principalDesktopPane.repaint();
    	revalidate();
	}
	
	/*************************************************************************************************
	 *             Creation de la méthode _setViewProduitsPanel
	 *************************************************************************************************/
	private void _setViewProduitsPanel(){
		
		viewProduitsPanel.removeAll();
		viewProduitsPanel.setLayout(null);
		viewProduitsPanel.setBounds(200, 100, largeurEcran-210, hauteurEcran-285);
		
		JLabel listUsersLabel = new JLabel();
		listUsersLabel.setText("Table des produits : ");
		listUsersLabel.setIcon(new ImageIcon("resources/645160.png"));
		//listUsersLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		listUsersLabel.setBounds(10, 5, 200, 60);
		viewProduitsPanel.add(listUsersLabel);
		
		produitsTableModel = new DefaultTableModel();			
		//String query = "SELECT USER_ID, USER_FIRSTNAME, USER_LASTNAME, USER_FUNCTION, USER_USERNAME FROM USERS";
		//Database bdd = new Database(query);
		//defaultTableModel.setDataVector(Database.columnNames, Database.data);	
		String query = "SELECT PRODUIT_ID, PRODUIT_NOM, PRODUIT_PRIX FROM PRODUITS";
		produitsTableModel.setDataVector(Database.getTableDatas(query), Database.getTableHeaders(query));
		produitsTable = new JTable(produitsTableModel);
			
        produitsTableScrollPane = new JScrollPane(produitsTable);
        produitsTableScrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	//jScrollPane.setFont(new Font("Tahoma", Font.PLAIN, 11));
    	//jScrollPane.setBounds(10, 65, 880, 200);
    	//jScrollPane.setBounds(200, 100, largeur-260, hauteur-135);
        produitsTableScrollPane.setBounds(10, 65, 880, 200);
    	viewProduitsPanel.add(produitsTableScrollPane); 
	
		JLabel tableSizeLabel = new JLabel();
	    tableSizeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
	    tableSizeLabel.setBounds(210, 5, 106, 60);
	    //tableSize.setText(Integer.parseInt(String.valueOf(n)) + " user");
	    tableSizeLabel.setText(produitsTable.getRowCount()+"");    	
	    viewProduitsPanel.add(tableSizeLabel);
	    	
	    ajouterProduitButton = new JButton();	    
	    ajouterProduitButton.setContentAreaFilled(false);	    
	    //ButtonAddElement.setForeground(new Color(255, 255, 255));
	    ajouterProduitButton.setIcon(new ImageIcon("resources/Users-Add-User-icon.png")); 
	    //ButtonAddUser.setText("");
	    //ButtonAddElement.setContentAreaFilled(false);
	    //ButtonAddElement.setFont(new Font("Tahoma", 1, 12));	    
	    ajouterProduitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));	 
	    ajouterProduitButton.setBounds(900, 75, 48, 48);
	    viewProduitsPanel.add(ajouterProduitButton);
	    ajouterProduitButton.addActionListener(this);
	    
	    editerProduitButton = new JButton();
	    editerProduitButton.setContentAreaFilled(false);
	    //ButtonEditElement.setForeground(new Color(255, 255, 255));
	    editerProduitButton.setIcon(new ImageIcon("resources/Users-Edit-User-icon.png")); 
	    //ButtonAddUser.setText("");
	    //ButtonEditElement.setFont(new Font("Tahoma", 1, 12));	    
	    editerProduitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    editerProduitButton.setBounds(900, 140, 48, 48);		    
	    viewProduitsPanel.add(editerProduitButton);
	    editerProduitButton.addActionListener(this); 
	    
	    supprimerProduitButton = new JButton();	
	    supprimerProduitButton.setContentAreaFilled(false);
	    supprimerProduitButton.setIcon(new ImageIcon("resources/Users-Remove-User-icon.png"));	
	    //ButtonDeleteElement.setText("");
	    supprimerProduitButton.setBounds(900, 205, 48, 48);
	    supprimerProduitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
	    viewProduitsPanel.add(supprimerProduitButton);
	    supprimerProduitButton.addActionListener(this);
	    
		principalDesktopPane.add(viewProduitsPanel);
	    principalDesktopPane.repaint();
    	revalidate();
	}
	
	/*************************************************************************************************
	 *             Creation de la méthode _setViewProduitsPanel
	 *************************************************************************************************/
	private void _setViewMachinesPanel(){
		
		viewMachinesPanel.removeAll();
		viewMachinesPanel.setLayout(null);
		viewMachinesPanel.setBounds(200, 100, largeurEcran-210, hauteurEcran-285);
		
		JLabel listUsersLabel = new JLabel();
		listUsersLabel.setText("Table des machines : ");
		listUsersLabel.setIcon(new ImageIcon("resources/645160.png"));
		//listUsersLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		listUsersLabel.setBounds(10, 5, 200, 60);
		viewMachinesPanel.add(listUsersLabel);
		
		machinesTableModel = new DefaultTableModel();			
		//String query = "SELECT USER_ID, USER_FIRSTNAME, USER_LASTNAME, USER_FUNCTION, USER_USERNAME FROM USERS";
		//Database bdd = new Database(query);
		//defaultTableModel.setDataVector(Database.columnNames, Database.data);	
		String query = "SELECT MACHINE_ID, MACHINE_NOM, MACHINE_DUREE FROM MACHINES";
		machinesTableModel.setDataVector(Database.getTableDatas(query), Database.getTableHeaders(query));
		machinesTable = new JTable(machinesTableModel);
				
        machinesTableScrollPane = new JScrollPane(machinesTable);
        machinesTableScrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	//jScrollPane.setFont(new Font("Tahoma", Font.PLAIN, 11));
    	//jScrollPane.setBounds(10, 65, 880, 200);
    	//jScrollPane.setBounds(200, 100, largeur-260, hauteur-135);
        machinesTableScrollPane.setBounds(10, 65, 880, 200);
        viewMachinesPanel.add(machinesTableScrollPane); 
	
		JLabel tableSizeLabel = new JLabel();
	    tableSizeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
	    tableSizeLabel.setBounds(210, 5, 106, 60);
	    //tableSize.setText(Integer.parseInt(String.valueOf(n)) + " user");
	    tableSizeLabel.setText(machinesTable.getRowCount()+"");    	
	    viewMachinesPanel.add(tableSizeLabel);
	    	
	    ajouterMachineButton = new JButton();	    
	    ajouterMachineButton.setContentAreaFilled(false);	    
	    //ButtonAddElement.setForeground(new Color(255, 255, 255));
	    ajouterMachineButton.setIcon(new ImageIcon("resources/Users-Add-User-icon.png")); 
	    //ButtonAddUser.setText("");
	    //ButtonAddElement.setContentAreaFilled(false);
	    //ButtonAddElement.setFont(new Font("Tahoma", 1, 12));	    
	    ajouterMachineButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));	 
	    ajouterMachineButton.setBounds(900, 75, 48, 48);
	    viewMachinesPanel.add(ajouterMachineButton);
	    ajouterMachineButton.addActionListener(this);
	    
	    editerMachineButton = new JButton();
	    editerMachineButton.setContentAreaFilled(false);
	    //ButtonEditElement.setForeground(new Color(255, 255, 255));
	    editerMachineButton.setIcon(new ImageIcon("resources/Users-Edit-User-icon.png")); 
	    //ButtonAddUser.setText("");
	    //ButtonEditElement.setFont(new Font("Tahoma", 1, 12));	    
	    editerMachineButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    editerMachineButton.setBounds(900, 140, 48, 48);		    
	    viewMachinesPanel.add(editerMachineButton);
	    editerMachineButton.addActionListener(this); 
	    
	    supprimerMachineButton = new JButton();	
	    supprimerMachineButton.setContentAreaFilled(false);
	    supprimerMachineButton.setIcon(new ImageIcon("resources/Users-Remove-User-icon.png"));	
	    //ButtonDeleteElement.setText("");
	    supprimerMachineButton.setBounds(900, 205, 48, 48);
	    supprimerMachineButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
	    viewMachinesPanel.add(supprimerMachineButton);
	    supprimerMachineButton.addActionListener(this);
	    
		principalDesktopPane.add(viewMachinesPanel);
	    principalDesktopPane.repaint();
    	revalidate();
	}
	
	/*************************************************************************************************
	 *             Creation de la méthode _setViewJobsPanel
	 *************************************************************************************************/
	public void _setViewJobsPanel(){
		
		viewJobsPanel.removeAll();
		viewJobsPanel.setLayout(null);
		viewJobsPanel.setBounds(200, 100, largeurEcran-210, hauteurEcran-285);
		
		JLabel listUsersLabel = new JLabel();
		listUsersLabel.setText("Table des jobs : ");
		listUsersLabel.setIcon(new ImageIcon("resources/645160.png"));
		//listUsersLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		listUsersLabel.setBounds(10, 5, 200, 60);
		viewJobsPanel.add(listUsersLabel);
				
		jobsTableModel = new DefaultTableModel();			
		//String query = "SELECT USER_ID, USER_FIRSTNAME, USER_LASTNAME, USER_FUNCTION, USER_USERNAME FROM USERS";
		//Database bdd = new Database(query);
		//defaultTableModel.setDataVector(Database.columnNames, Database.data);	
		String query = "SELECT JOB_ID, JOB_PRODUIT_ID, JOB_QUANTITE, JOB_COUT, JOB_ETAT FROM JOBS";
		jobsTableModel.setDataVector(Database.getTableDatas(query), Database.getTableHeaders(query));
		jobsTable = new JTable(jobsTableModel);
				
        jobsTableScrollPane = new JScrollPane(jobsTable);
        jobsTableScrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	//jScrollPane.setFont(new Font("Tahoma", Font.PLAIN, 11));
    	//jScrollPane.setBounds(10, 65, 880, 200);
    	//jScrollPane.setBounds(200, 100, largeur-260, hauteur-135);
    	jobsTableScrollPane.setBounds(10, 65, 880, 200);
    	viewJobsPanel.add(jobsTableScrollPane); 
    	
		JLabel tableSizeLabel = new JLabel();
	    tableSizeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
	    tableSizeLabel.setBounds(210, 5, 106, 60);
	    //tableSize.setText(Integer.parseInt(String.valueOf(n)) + " user");
	    tableSizeLabel.setText(jobsTable.getRowCount()+"");    	
	    viewJobsPanel.add(tableSizeLabel);	    
   
	    supprimerJobButton = new JButton();	    
	    supprimerJobButton.setContentAreaFilled(false);	    
	    supprimerJobButton.setIcon(new ImageIcon("resources/Users-Remove-User-icon.png"));	    
	    supprimerJobButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));	 
	    supprimerJobButton.setBounds(900, 75, 48, 48);
	    viewJobsPanel.add(supprimerJobButton);
	    supprimerJobButton.addActionListener(this);
	    
    	//lllllllllllllllllll
    	lancerJobsButton = new ButtonStyle();
    	lancerJobsButton.setText("  LANCER");
    	lancerJobsButton.setIcon(new ImageIcon("resources/jouer-50.png"));
    	lancerJobsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	lancerJobsButton.setBounds(largeurEcran-190, hauteurEcran-175, 180, 65);  
    	//lancerJobsButton.setVisible(false);
        principalDesktopPane.add(lancerJobsButton, JLayeredPane.DEFAULT_LAYER);
        lancerJobsButton.addActionListener(this);
        //principalDesktopPane.add(lancerJobsButton); 
	    		    
		principalDesktopPane.add(viewJobsPanel);
	    principalDesktopPane.repaint();
    	revalidate();
	}
	
	public void ajouterJob(Job job){
		DAO<Job> jobDao = new JobDAO(Database.getInstance());
		try {
			jobDao.insert(job);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	
	/*************************************************************************************************
	 *                                 Liste des evenements lors d'execution
	 *************************************************************************************************/

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == identifiantLabel) {	        
	        if (JOptionPane.showConfirmDialog(this, "Êtes vous sûr de vouloir quitter", "ATTENTION", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
	        	this.dispose();
	        	new ManagerGUI();	        	
	        }
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	@SuppressWarnings({ "deprecation", "static-access" })
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		if (arg0.getSource() == entreeButton) {
			if((utilisateurText.getText().equals("")) || (passeText.equals(""))) {
				JOptionPane.showMessageDialog(null, "Un ou plusieurs champs sont vides !", "WARNING", JOptionPane.WARNING_MESSAGE);
			}else {
				if(User.findUser(utilisateurText.getText(), passeText.getText()) != null) {						
					JOptionPane.showMessageDialog(null, "Authentification réussite !", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
					this.setJMenuBar(_setMenuBar());
					_setPortailPanel(User.findUser(utilisateurText.getText(), passeText.getText()));
				}else {
					JOptionPane.showMessageDialog(null, "Utilisateur introuvable !", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		if (arg0.getSource() == annulerButton) {
    		utilisateurText.setText("");
    		passeText.setText("");
		}
		
		if (arg0.getSource() == agentsMachinesMenuItem) {
			
			/*Runtime myRuntime = Runtime.instance();
        	Profile profile = new ProfileImpl(false);
			profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");			
			//int nombreMachine = 1;			
			try {
				AgentContainer newContainer = myRuntime.createAgentContainer(profile);
				newContainer.start();				
				Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet result = state.executeQuery("SELECT * FROM MACHINES");
				while (result.next()) {
					//Machine machine = new Machine(result.getInt("MACHINE_ID"), result.getString("MACHINE_NOM"), result.getLong("MACHINE_DUREE"));
					AgentController agentAR = newContainer.createNewAgent(result.getString("MACHINE_NOM"), AR.class.getName(), null);
					agentAR.start();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ControllerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/        	
		}
		
		if (arg0.getSource() == quitterMenuItem) {			
	        if (JOptionPane.showConfirmDialog(this, "Etes vous sûr de vouloir quitter", "ATTENTION", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
	        	System.exit(0);	        	
	        }
		}
		
		if (arg0.getSource() == aproposMenuItem) {
			JOptionPane jop = new JOptionPane();
			ImageIcon img = new ImageIcon("resources/perso.png");
			String mess = "Projet Réalisé Par : \n    GUENANA Jighou \n";
			mess += "    Etudiant Master 2 en Informatique \n";
			mess += "\n    0667....";
			mess += "\n    jigo@gmail.dz";
			jop.showMessageDialog(null, mess, "À propos", JOptionPane.INFORMATION_MESSAGE, img);
		}
		
		if (arg0.getSource() == personnelsButton) {	
			new SwingWorker<Void, Void>() {
	            @Override
	            protected Void doInBackground() throws Exception {
	            	personnelsButton.setEnabled(false);
	            	produitsButton.setEnabled(true);
	            	machinesButton.setEnabled(true);
	            	jobsButton.setEnabled(true);
	            	principalDesktopPane.remove(viewPersonnelsPanel);
	            	principalDesktopPane.remove(viewProduitsPanel);
	            	principalDesktopPane.remove(viewMachinesPanel);
	            	principalDesktopPane.remove(viewJobsPanel);
	            	_setViewPersonnelsPanel();
	            	//lancerJobsButton.setVisible(false);
	                return null;
	            }
	        }.execute();
		}		
		if (arg0.getSource() == ajouterPersonButton) {
			_ajouterPersonForm nPersonForm = new _ajouterPersonForm();
			nPersonForm.addWindowListener(new WindowAdapter(){
		        @Override
		        public void windowClosed(WindowEvent e){
		        	principalDesktopPane.remove(viewPersonnelsPanel);
		        	_setViewPersonnelsPanel();
		        }
		   });
		}    	
		if (arg0.getSource() == supprimerPersonButton) {
			try {
				System.out.println(personnelsTable.getSelectedRowCount());
				if (personnelsTable.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(this, "Sélectionnez l'élement à supprimer !", "WARNING", JOptionPane.WARNING_MESSAGE);
				}else {
					if (JOptionPane.showConfirmDialog(this, "Êtes vous sûr de vouloir supprimer cet utilisateur ?", "CONFIRMATION", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						int id = (int) personnelsTable.getModel().getValueAt(personnelsTable.getSelectedRow(), 0);
						DAO<User> userDao = new UserDAO(Database.getInstance());
						userDao.delete(id);
					}else {
						return;
					}
					/*DefaultTableModel dm = (DefaultTableModel)userJTable.getModel();
					 dm.fireTableDataChanged();*/
					principalDesktopPane.remove(viewPersonnelsPanel);
					_setViewPersonnelsPanel();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (arg0.getSource() == produitsButton) {	
			new SwingWorker<Void, Void>() {
	            @Override
	            protected Void doInBackground() throws Exception {
	            	personnelsButton.setEnabled(true);
	            	produitsButton.setEnabled(false);
	            	machinesButton.setEnabled(true);
	            	jobsButton.setEnabled(true);
	            	principalDesktopPane.remove(viewPersonnelsPanel);
	            	principalDesktopPane.remove(viewProduitsPanel);
	            	principalDesktopPane.remove(viewMachinesPanel);
	            	principalDesktopPane.remove(viewJobsPanel);
	            	_setViewProduitsPanel();
	            	//lancerJobsButton.setVisible(false);
	                return null;
	            }
	        }.execute();
		}		
		if (arg0.getSource() == ajouterProduitButton) {
			_ajouterProduitForm nProduitForm = new _ajouterProduitForm();
			nProduitForm.addWindowListener(new WindowAdapter(){
		        @Override
		        public void windowClosed(WindowEvent e){		        	
		        	try {
						GuiEvent ge = new GuiEvent(this, AM.produitGuiEvent);
						ge.addParameter((Produit) nProduitForm.getProduit());					//param 0
						managerAgent.postGuiEvent(ge);
					} catch (Exception ex) {
						ex.getStackTrace();
					}
		        	principalDesktopPane.remove(viewProduitsPanel);
		        	_setViewProduitsPanel();
		        }
		   });
		}    	
		if (arg0.getSource() == supprimerProduitButton) {
			try {
				System.out.println(produitsTable.getSelectedRowCount());
				if (produitsTable.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(this, "Sélectionnez l'élement à supprimer !", "WARNING", JOptionPane.WARNING_MESSAGE);
				}else {
					if (JOptionPane.showConfirmDialog(this, "Êtes vous sûr de vouloir supprimer cet produit ?", "CONFIRMATION", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						int id = (int) produitsTable.getModel().getValueAt(produitsTable.getSelectedRow(), 0);
						DAO<Produit> produitDao = new ProduitDAO(Database.getInstance());						
						try {
							GuiEvent ge = new GuiEvent(this, AM.produitGuiEvent);
							ge.addParameter((Produit) produitDao.find(id));
							managerAgent.postGuiEvent(ge);
							ge.addParameter(null);
						} catch (Exception ex) {
							ex.getStackTrace();
						}
						produitDao.delete(id);
					}else {
						return;
					}
					principalDesktopPane.remove(viewProduitsPanel);
					_setViewProduitsPanel();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (arg0.getSource() == machinesButton) {	
			new SwingWorker<Void, Void>() {
	            @Override
	            protected Void doInBackground() throws Exception {
	            	personnelsButton.setEnabled(true);
	            	produitsButton.setEnabled(true);
	            	machinesButton.setEnabled(false);
	            	jobsButton.setEnabled(true);	            	
	            	principalDesktopPane.remove(viewPersonnelsPanel);
	            	principalDesktopPane.remove(viewProduitsPanel);
	            	principalDesktopPane.remove(viewMachinesPanel);
	            	principalDesktopPane.remove(viewJobsPanel);
	            	_setViewMachinesPanel();
	            	//lancerJobsButton.setVisible(false);
	                return null;
	            }
	        }.execute();
		}
		if (arg0.getSource() == ajouterMachineButton) {
			_ajouterMachineForm nMachineForm = new _ajouterMachineForm();
			nMachineForm.addWindowListener(new WindowAdapter(){
		        @Override
		        public void windowClosed(WindowEvent e){
		        	principalDesktopPane.remove(viewMachinesPanel);
		        	_setViewMachinesPanel();	        	
		    
		        }
		   });
		}    	
		if (arg0.getSource() == supprimerMachineButton) {
			try {
				System.out.println(machinesTable.getSelectedRowCount());
				if (machinesTable.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(this, "Sélectionnez l'élement à supprimer !", "WARNING", JOptionPane.WARNING_MESSAGE);
				}else {
					if (JOptionPane.showConfirmDialog(this, "Êtes vous sûr de vouloir supprimer cette machine ?", "CONFIRMATION", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						int id = (int) machinesTable.getModel().getValueAt(machinesTable.getSelectedRow(), 0);
						DAO<Machine> machineDao = new MachineDAO(Database.getInstance());
						machineDao.delete(id);
					}else {
						return;
					}
					/*DefaultTableModel dm = (DefaultTableModel)userJTable.getModel();
					 dm.fireTableDataChanged();*/
					principalDesktopPane.remove(viewMachinesPanel);
					_setViewMachinesPanel();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (arg0.getSource() == jobsButton) {	
			new SwingWorker<Void, Void>() {
	            @Override
	            protected Void doInBackground() throws Exception {
	            	personnelsButton.setEnabled(true);
	            	produitsButton.setEnabled(true);
	            	machinesButton.setEnabled(true);
	            	jobsButton.setEnabled(false);	            	
	            	principalDesktopPane.remove(viewPersonnelsPanel);
	            	principalDesktopPane.remove(viewProduitsPanel);
	            	principalDesktopPane.remove(viewMachinesPanel);
	            	principalDesktopPane.remove(viewJobsPanel);
	            	_setViewJobsPanel();
	            	//lancerJobsButton.setVisible(true);
	                return null;
	            }
	        }.execute();
		}
		if (arg0.getSource() == supprimerJobButton) {
			try {
				System.out.println(jobsTable.getSelectedRowCount());
				if (jobsTable.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(this, "Sélectionnez l'élement à supprimer !", "WARNING", JOptionPane.WARNING_MESSAGE);
				}else {
					if (JOptionPane.showConfirmDialog(this, "Êtes vous sûr de vouloir supprimer ce job ?", "CONFIRMATION", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						int id = (int) jobsTable.getModel().getValueAt(jobsTable.getSelectedRow(), 0);
						DAO<Job> jobDao = new JobDAO(Database.getInstance());	
						jobDao.delete(id);
					}else {
						return;
					}
					principalDesktopPane.remove(viewJobsPanel);
					_setViewJobsPanel();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (arg0.getSource() == lancerJobsButton) {	
			try {
				GuiEvent ge = new GuiEvent(this, AM.lancerJobsGuiEvent);
				//ge.addParameter((int) jobsTable.getModel().getValueAt(jobsTable.getSelectedRow(), 0));	//param 0
				managerAgent.postGuiEvent(ge);
			} catch (Exception ex) {
				ex.getStackTrace();
			}
		} 
			
	
	
	}

}
