package job;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import com.alee.laf.rootpane.WebFrame;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.gui.GuiEvent;
import jade.util.Logger;
import ontology.Database;
import ontology.UIFont;

/**
 * @author GUENANA El-Rabia
 * 
 * Main UI for general Interface. 
 */
public class JobGUI extends WebFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	
	@SuppressWarnings("unused")
	private AgentJob jobAgent;
	
	private JPanel titreFramePanel;
	private JLabel titreFrameLabel;
	
	private JComboBox<String> rechercheComboBox;
	private JFormattedTextField rechercheTextField;
	private JButton rechercheButton;
	
	DefaultTableModel produitsTableModel;
	JTable produitsTable;
	JScrollPane produitsTableScrollPane;
	
	private JLabel quantiteLabel;
	private JFormattedTextField quantiteTextFiled;
	private JButton envoyerButton;
	
	private JLabel affichageLabel;
	private JTextArea affichageTextArea;
	private JScrollPane affichageScrollPane;
	
	private JButton actualiserButton;
	private JButton quitterButton;
	
	// Constructeur: créer l'interface graphique sans agent.
	public JobGUI(){	
		// Ajouter les widgets au JFrame
		initComponents();
		envoyerButton.setEnabled(false);
		affichageTextArea.setEditable(false);		
		showGui();
	}
	
	// Constructeur: créer l'interface graphique.
	public JobGUI(AgentJob aj) {
		this.jobAgent = aj;		
		// Ajouter les widgets au JFrame
		initComponents();		
		showGui();
		
		affichageTextArea.setEditable(false);	
	}
	
	// Ajouter les widgets
	private void initComponents() {
		
		getContentPane().setLayout(null);
		
		titreFramePanel = new JPanel();
		titreFrameLabel = new JLabel();
		rechercheComboBox = new JComboBox<>();
		rechercheTextField = new JFormattedTextField();
		rechercheButton = new JButton();

		produitsTableScrollPane = new JScrollPane();
		envoyerButton = new JButton(); 
		quantiteLabel = new JLabel();
		quantiteTextFiled = new JFormattedTextField();
		affichageLabel = new JLabel();
		affichageTextArea = new JTextArea();
		affichageScrollPane = new JScrollPane();
		actualiserButton = new JButton();
		quitterButton = new JButton();
	        
	    //titreFramePanel.setBackground(new Color(121, 121, 149));
	    titreFramePanel.setBounds(0, 15, 700, 50);        
	        
	    titreFrameLabel.setFont(new Font("Sans Serif", Font.PLAIN, 28)); // NOI18N	
	    //titreFrameLabel.setForeground(new Color(255, 255, 255));
	    titreFrameLabel.setText("Créer un nouveau Job");
	    titreFramePanel.add(titreFrameLabel); 
	    getContentPane().add(titreFramePanel);
	        
	    //rechercheComboBox.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
	    rechercheComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "  ID Produit", "  Description", "  Prix"}));        
	    rechercheComboBox.setBounds(700-135-205-110, 80, 130, 30);
	    getContentPane().add(rechercheComboBox);
	        
	    //rechercheTextField.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N        
	    rechercheTextField.setBounds(700-205-110, 80, 200, 30);
	    getContentPane().add(rechercheTextField);
      
	    rechercheButton.setText("Rechercher");        
	    //jButton6.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
	    //rechercheButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/magnify.png"))); // NOI18N
	    rechercheButton.setBounds(700-110, 80, 100, 30);
	    getContentPane().add(rechercheButton);
	     
        //loadData("SELECT USER_ID, USER_FIRSTNAME, USER_LASTNAME, USER_FUNCTION, USER_USERNAME FROM USERS");
		produitsTableModel = new DefaultTableModel();			
		String query = "SELECT PRODUIT_ID, PRODUIT_NOM, PRODUIT_PRIX FROM PRODUITS";
		produitsTableModel.setDataVector(Database.getTableDatas(query), Database.getTableHeaders(query));
		produitsTable = new JTable(produitsTableModel);
				
        produitsTableScrollPane = new JScrollPane(produitsTable);
        produitsTableScrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        produitsTableScrollPane.setBounds(5, 120, 685, 150);
        getContentPane().add(produitsTableScrollPane); 

	    //label_prix_proposer.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        quantiteLabel.setText("Quantité :");        
	    quantiteLabel.setBounds(10, 280, 70, 30);
	    getContentPane().add(quantiteLabel);
	       
	    quantiteTextFiled.setBounds(75, 280, 100, 30);
	    getContentPane().add(quantiteTextFiled);
	      
	    //envoyerButton.setIcon(new ImageIcon(getClass().getResource("/icons/yes.png"))); // NOI18N
	    envoyerButton.setText("Envoyer");
	    //envoyerButton.setSelected(false);
	    //envoyerButton.setEnabled(false);
	    envoyerButton.setBounds(180, 280, 100, 30);
	    envoyerButton.addActionListener(this);
	    getContentPane().add(envoyerButton);
	        
	        affichageLabel.setText("Le procéssus de la production :");
	        affichageLabel.setFont(new Font("Sans Serif", Font.PLAIN, 18)); // NOI18N	       
	        affichageLabel.setBounds(10, 320, 300, 30);
	        getContentPane().add(affichageLabel);
	       
	        //affichageTextArea.setColumns(20);
	        //affichageTextArea.setRows(10);
	        
	        affichageScrollPane.setViewportView(affichageTextArea);
	        affichageScrollPane.setBounds(5, 350, 685, 150);
	        getContentPane().add(affichageScrollPane);
	        
	        ImageIcon imgBanner = new ImageIcon("resources/cevitalBanner_300x100.png");
			JLabel logoLabel = new JLabel("", imgBanner, JLabel.CENTER);
			logoLabel.setBounds(10, 500, 300, 70);
			getContentPane().add(logoLabel, FlowLayout.LEFT);
			
	        actualiserButton.setText("Actualiser");
	        actualiserButton.setFont(new Font("Sans Serif", Font.PLAIN, 18));
	        actualiserButton.setIcon(new ImageIcon("resources/refresh.png"));
	        actualiserButton.setSelected(true);
	        actualiserButton.setBounds(700-140-140, 520, 130, 40);	       
	        getContentPane().add(actualiserButton);
	        actualiserButton.addActionListener(this);
	        
	        quitterButton.setText("Quitter");
	        quitterButton.setFont(new Font("Sans Serif", Font.PLAIN, 18)); // NOI18N
	        quitterButton.setIcon(new ImageIcon("resources/power_off.png")); // NOI18N
	        quitterButton.setSelected(true);
	        quitterButton.setBounds(700-140, 520, 130, 40);	        
	        getContentPane().add(quitterButton);
	        quitterButton.addActionListener(this);
	}
	
	
	public void _setAfficheTextArea(String msg, boolean show){
		if(show == true){
			affichageTextArea.append(msg + "\n");
		} else{
			affichageTextArea.setText(msg);
		}
	}
	
	
	/**
	 * Initialized the parameters of display of the frame and make 
	 * it visible at appropriate location with desired size
	 */
	private void showGui() {		       
		//this.setTitle("CLIENT -> " + "Home");
		this.setResizable(false);		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("resources\\user-48.png"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() - 500;
		int centerY = (int)screenSize.getHeight() - 200;
		//this.setMinimumSize(new Dimension(centerX, centerY));
		this.setMinimumSize(new Dimension(700, 600));
		//this.setPreferredSize(new Dimension(700, 570));
		//setLocation(centerX - getWidth()/2, centerY - getHeight()/2);
		this.setLocationRelativeTo(null);
		pack();
		super.setVisible(true);
	}
	
	
	/**
	 * Main void to launch application
	 * 
	 */
	public static void main(String[] args) {
		UIFont.loadFont();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new JobGUI();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getSource() == actualiserButton) {
			//jButton6ActionPerformed(evt);
		}
		
		if (e.getSource() == envoyerButton) {
			//bouton_négocierActionPerformed(evt);
			try {
				GuiEvent ge = new GuiEvent(this, AgentJob.nouveauJobGuiEvent);
				ge.addParameter((int) produitsTable.getModel().getValueAt(produitsTable.getSelectedRow(), 0));	//param 0
				ge.addParameter((Integer) Integer.parseInt(quantiteTextFiled.getText()));					//param 1
				jobAgent.postGuiEvent(ge);
				JOptionPane.showMessageDialog(this, "Job envoyée avec succès.. !", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Sélectionner un produit et saisir une quantité correcte!", "WARNING", JOptionPane.WARNING_MESSAGE);
			}
			
			/*if (produitsTable.getSelectedRow() < 0 || !quantiteTextFiled.getText().equals("")){
				JOptionPane.showMessageDialog(this, "Sélectionnez un produit !", "WARNING", JOptionPane.WARNING_MESSAGE);
			}else {
				GuiEvent ge = new GuiEvent(this, CA.nouvelleCommandeGuiEvent);
				ge.addParameter((int) produitsTable.getModel().getValueAt(produitsTable.getSelectedRow(), 0));	//param 0
				ge.addParameter((Integer) Integer.parseInt(quantiteTextFiled.getText()));					//param 1
				ca.postGuiEvent(ge);
			}*/
		}
		
		if (e.getSource() == actualiserButton) {
			//bouton_actualiserActionPerformed(evt);
		}
		
		if (e.getSource() == quitterButton) {
			//bouton_actualiser1ActionPerformed(evt);
		}
	}
	
	

}