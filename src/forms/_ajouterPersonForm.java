package forms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import concepts.User;
import conceptsDAO.DAO;
import conceptsDAO.UserDAO;
import ontology.ButtonStyle;
import ontology.Database;
import ontology.UIFont;

import java.awt.Cursor;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class _ajouterPersonForm extends JDialog {
	
	/*************************************************************************************************
	 * Declaration of variables.
	 *************************************************************************************************/
	private JDesktopPane DesktopPrincipale = new JDesktopPane();

			
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
				new _ajouterPersonForm();
			}
		});
	}
	
	/*************************************************************************************************
	 * Create the application.
	 *************************************************************************************************/
	public JDesktopPane MydesktopPane;

	/*public ajouterPersonForm(ManagerUI parent) {
		super(parent);
		display();
	}*/
	
	public _ajouterPersonForm() {		
		display();
	}
	
	public void display() {
		//Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
		//int largeurEcran = tailleEcran.width-600;
		//int hauteurEcran = tailleEcran.height-300;
		
		pack();
		this.setTitle("Utilisateurs..");
    	this.setIconImage(Toolkit.getDefaultToolkit().getImage("images\\male_user.png"));    
		//this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
		this.setSize(new Dimension(500, 400));		
		this.setResizable(false);
		this.setLocationRelativeTo(null);	
		//setPanelLogin();
		
		JLabel addNewUserLabel = new JLabel();
		addNewUserLabel.setText("Ajouter un nouveau utilisateur :");
		//addNewUserLabel.setForeground(Color.WHITE);
		addNewUserLabel.setFont(new Font("Tahoma", 1, 14));
		addNewUserLabel.setBounds(10, 10, 340, 25);
    	DesktopPrincipale.add(addNewUserLabel, JLayeredPane.DEFAULT_LAYER);
    	
    	JLabel userFirstnameLabel = new JLabel();
    	//userFirstnameLabel.setFont(new Font("Tahoma", 0, 12));
    	//userFirstnameLabel.setForeground(new Color(255, 255, 255));
    	userFirstnameLabel.setText("NOM :");
    	userFirstnameLabel.setBounds(50, 50, 100, 30);
        DesktopPrincipale.add(userFirstnameLabel, JLayeredPane.DEFAULT_LAYER);
        
        final JTextField userFirstnameText = new JTextField();
        userFirstnameText.setBounds(150, 50, 300, 30);
        DesktopPrincipale.add(userFirstnameText, JLayeredPane.DEFAULT_LAYER);
        
        JLabel userLastnameLabel = new JLabel();
        //userLastnameLabel.setFont(new Font("Tahoma", 0, 12));
        //userLastnameLabel.setForeground(new Color(255, 255, 255));
        userLastnameLabel.setText("PRENOM :");
        userLastnameLabel.setBounds(50, 90, 100, 30);
        DesktopPrincipale.add(userLastnameLabel, JLayeredPane.DEFAULT_LAYER);
        
        final JTextField userLastnameText = new JTextField();
        userLastnameText.setBounds(150, 90, 300, 30);
        DesktopPrincipale.add(userLastnameText, JLayeredPane.DEFAULT_LAYER);
        
        JLabel userFunctionLabel = new JLabel();  
        //userFunctionLabel.setFont(new Font("Tahoma", 0, 12));
        userFunctionLabel.setText("FONCTION :");
        //userFunctionLabel.setForeground(new Color(255, 255, 255));
        userFunctionLabel.setBounds(50, 130, 100, 30);
        DesktopPrincipale.add(userFunctionLabel, JLayeredPane.DEFAULT_LAYER);
        
        final JTextField userFunctionText = new JTextField();
        userFunctionText.setBounds(150, 130, 300, 30);
        DesktopPrincipale.add(userFunctionText, JLayeredPane.DEFAULT_LAYER);
        
        JLabel userLoginLabel = new JLabel();
        //userLoginLabel.setFont(new Font("Tahoma", 0, 12));
        //userLoginLabel.setForeground(new Color(255, 255, 255));
        userLoginLabel.setText("USERNAME* :");
        userLoginLabel.setBounds(50, 170, 100, 30);
        DesktopPrincipale.add(userLoginLabel, JLayeredPane.DEFAULT_LAYER);
        
        final JTextField userLoginText = new JTextField();
        userLoginText.setBounds(150, 170, 300, 30);
        DesktopPrincipale.add(userLoginText, JLayeredPane.DEFAULT_LAYER);
        
        JLabel userPassLabel = new JLabel();
        //userPassLabel.setFont(new Font("Tahoma", 0, 12)); 
        //userPassLabel.setForeground(new Color(255, 255, 255));
        userPassLabel.setText("PASSWORD* :");
        userPassLabel.setBounds(50, 210, 100, 30);
        DesktopPrincipale.add(userPassLabel, JLayeredPane.DEFAULT_LAYER);
        
        final JPasswordField userPassText = new JPasswordField();
        userPassText.setBounds(150, 210, 300, 30);
        DesktopPrincipale.add(userPassText, JLayeredPane.DEFAULT_LAYER);
           
        JLabel ChampsObligatoireLabel = new JLabel();
        ChampsObligatoireLabel.setText("(*) : Champs Obligatoire");
        ChampsObligatoireLabel.setForeground(Color.RED);
        ChampsObligatoireLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 10));
        ChampsObligatoireLabel.setBounds(50, 250, 340, 20);
        DesktopPrincipale.add(ChampsObligatoireLabel, JLayeredPane.DEFAULT_LAYER);
        
        ButtonStyle userCancelButton = new ButtonStyle();    	
        //userCancelButton.setForeground(new Color(255, 255, 255));
        userCancelButton.setIcon(new ImageIcon("resources/close.png"));
        userCancelButton.setText("Annuler");
        //userCancelButton.setFont(new Font("Tahoma", 1, 12));
        userCancelButton.setBounds(100, 290, 130, 45);
        userCancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        DesktopPrincipale.add(userCancelButton, JLayeredPane.DEFAULT_LAYER);
        userCancelButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		userFirstnameText.setText("");
        		userLastnameText.setText("");
        		userLoginText.setText("");
        		userPassText.setText("");        		
        		userFunctionText.setText("");
        	}
        });
       
        ButtonStyle userAddButton = new ButtonStyle();
        //userAddButton.setForeground(new Color(255, 255, 255));
        userAddButton.setIcon(new ImageIcon("resources/User-Interface-Save-As-icon.png")); 
        userAddButton.setText("Ajouter");
        //userAddButton.setFont(new Font("Tahoma", 1, 12));
        userAddButton.setBounds(270, 290, 130, 45);
        userAddButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        DesktopPrincipale.add(userAddButton, JLayeredPane.DEFAULT_LAYER);
        userAddButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				setNewUser(userFirstnameText.getText(), userLastnameText.getText(), userFunctionText.getText(), userLoginText.getText(),userPassText.getText());
        	}
        });	      
        
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        DesktopPrincipale.repaint();
        this.setContentPane(DesktopPrincipale);
		this.setVisible(true);
	}
		
	/*************************************************************************************************
	 * Create set New User application
	 *************************************************************************************************/
	public void setNewUser(String nom, String prenom, String fonction, String login, String password) {
		
		if (!login.equals("") && !password.equals("")) {
			if(User.findUser(login, password) == null) {
				User user = new User();
				user.setFirstname(nom);
				user.setLastname(prenom);
				user.setFunction(fonction);
				user.setUsername(login);
				user.setPassword(password);	
				
				DAO<User> UserDao = new UserDAO(Database.getInstance());
				if (UserDao.insert(user) != 0){
					JOptionPane.showMessageDialog(this, "Utilisateur insirer avec succès.. !", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
					/*MydesktopPane.remove(MyAdministrationPanel);
					MyAdministrationPanel = new TablePanel(MydesktopPane, MyAdministrationPanel, "SELECT USER_ID, USER_FIRSTNAME, USER_LASTNAME, USER_FUNCTION, USER_USERNAME FROM USERS", "utilisateurs");
					MydesktopPane.add(MyAdministrationPanel);*/
					this.dispose();
				}
				else 
					JOptionPane.showMessageDialog(this, "Erreur d'insirtion.. !", "ERROR", JOptionPane.ERROR_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(this, "Utilisateur existe déjà.. !", "WARNING", JOptionPane.WARNING_MESSAGE);
			}
		}else {
			JOptionPane.showMessageDialog(this, "Nom d'utilisateur et mot de passe require", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
			
	

}
