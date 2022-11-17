package forms;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import concepts.Produit;
import conceptsDAO.DAO;
import conceptsDAO.ProduitDAO;
import ontology.ButtonStyle;
import ontology.Database;
import ontology.UIFont;


public class _ajouterProduitForm extends JDialog implements ActionListener {
	/*************************************************************************************************
	 * Declaration of variables.
	 *************************************************************************************************/
	private JDesktopPane principalDesktopPane;
	private JTextField descriptionText;
	private JTextField prixText;
	private JTextField tache1Text;
	private JTextField tache2Text;
	private ButtonStyle ajouterButton;
	private ButtonStyle annulerButton;
	
	public Produit produit = new Produit();
			
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
				new _ajouterProduitForm();
			}
		});
	}
	
	/*************************************************************************************************
	 * Create the application.
	 *************************************************************************************************/
	
	public _ajouterProduitForm() {		
		display();
	}
	
	public Produit getProduit() {		
		return this.produit;
	}
	public void setProduit(Produit produit) {		
		this.produit = produit;
	}
	
	public void display() {
		
		pack();
		this.setTitle("Produits..");
    	this.setIconImage(Toolkit.getDefaultToolkit().getImage("resources\\dribbble-shop-icon.png"));    
		//this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
		this.setSize(new Dimension(500, 300));		
		this.setResizable(false);
		this.setLocationRelativeTo(null);	
		principalDesktopPane = new JDesktopPane();
		
		JLabel addNewUserLabel = new JLabel();
		addNewUserLabel.setText("Ajouter un nouveau produit :");
		//addNewUserLabel.setForeground(Color.WHITE);
		addNewUserLabel.setFont(new Font("Tahoma", 1, 14));
		addNewUserLabel.setBounds(10, 10, 340, 25);
    	principalDesktopPane.add(addNewUserLabel, JLayeredPane.DEFAULT_LAYER);
    	
    	JLabel descriptionLabel = new JLabel();
    	//userFirstnameLabel.setFont(new Font("Tahoma", 0, 12));
    	//userFirstnameLabel.setForeground(new Color(255, 255, 255));
    	descriptionLabel.setText("DESCRIPTION* :");
    	descriptionLabel.setBounds(40, 50, 150, 30);
        principalDesktopPane.add(descriptionLabel, JLayeredPane.DEFAULT_LAYER);
        
        descriptionText = new JTextField();
        descriptionText.setBounds(150, 50, 300, 30);
        principalDesktopPane.add(descriptionText, JLayeredPane.DEFAULT_LAYER);
        
        JLabel prixLabel = new JLabel();
        //userLastnameLabel.setFont(new Font("Tahoma", 0, 12));
        //userLastnameLabel.setForeground(new Color(255, 255, 255));
        prixLabel.setText("PRIX :");
        prixLabel.setBounds(40, 90, 150, 30);
        principalDesktopPane.add(prixLabel, JLayeredPane.DEFAULT_LAYER);
        
        prixText = new JTextField();
        prixText.setBounds(150, 90, 300, 30);
        principalDesktopPane.add(prixText, JLayeredPane.DEFAULT_LAYER);
        
        JLabel champsObligatoireLabel = new JLabel();
        champsObligatoireLabel.setText("(*) : Champs Obligatoire");
        champsObligatoireLabel.setForeground(Color.RED);
        champsObligatoireLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 10));
        champsObligatoireLabel.setBounds(40, 120, 340, 20);
        principalDesktopPane.add(champsObligatoireLabel, JLayeredPane.DEFAULT_LAYER);
        
        annulerButton = new ButtonStyle();    	
        //userCancelButton.setForeground(new Color(255, 255, 255));
        annulerButton.setIcon(new ImageIcon("resources/close.png"));
        annulerButton.setText("Annuler");
        //userCancelButton.setFont(new Font("Tahoma", 1, 12));
        annulerButton.setBounds(100, 170, 130, 45);
        annulerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        principalDesktopPane.add(annulerButton, JLayeredPane.DEFAULT_LAYER);
        annulerButton.addActionListener(this);
       
        ajouterButton = new ButtonStyle();
        //userAddButton.setForeground(new Color(255, 255, 255));
        ajouterButton.setIcon(new ImageIcon("resources/User-Interface-Save-As-icon.png")); 
        ajouterButton.setText("Ajouter");
        //userAddButton.setFont(new Font("Tahoma", 1, 12));
        ajouterButton.setBounds(270, 170, 130, 45);
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
			if (!descriptionText.getText().equals("") && !prixText.getText().equals("")) {
				if(Produit.findProduit(descriptionText.getText()) == null) {
					
					produit.setDescription(descriptionText.getText());
					produit.setPrix(Double.parseDouble(prixText.getText()));
					
					DAO<Produit> produitrDao = new ProduitDAO(Database.getInstance());
					if (produitrDao.insert(produit) != 0){
						JOptionPane.showMessageDialog(this, "Produit insirer avec succès.. !", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
						this.dispose();
					}
					else 
						JOptionPane.showMessageDialog(this, "Erreur d'insirtion.. !", "ERROR", JOptionPane.ERROR_MESSAGE);
				}else {
					JOptionPane.showMessageDialog(this, "Produit existe déjà.. !", "WARNING", JOptionPane.WARNING_MESSAGE);
				}
			}else {
				JOptionPane.showMessageDialog(this, "Nom et prix de produit require", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (arg.getSource() == annulerButton) {
    		descriptionText.setText("");
    		prixText.setText("");
		}
		
	}

}
