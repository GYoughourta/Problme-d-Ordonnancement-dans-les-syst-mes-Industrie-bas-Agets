package ontology;

import java.sql.*;
import java.util.Vector;

import javax.swing.JOptionPane;

public class Database {
	
	/**
	* URL de connection
	*/
	private static String url = "jdbc:derby:BDD/BDDCevital";
	/**
	* Nom du user
	*/
	private static String user = "user";
	/**
	* Mot de passe du user
	*/
	private static String passwd = "password";
	/**
	* Objet Connection
	*/
	private static Connection connect;

	public Database() {
		
	}
	
	/**
	* Méthode qui va retourner notre instance
	* et la créer si elle n'existe pas...
	* @return
	*/
	public static Connection getInstance(){
		if(connect == null){
			try {
				connect = DriverManager.getConnection(url, user, passwd);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Echèc du démarrage de la base de données !", "ERREUR DE CONNEXION !", JOptionPane.ERROR_MESSAGE);
			}
		}
		return connect;
	}
	
	public static Connection setClosInstance(){
		if(connect != null)
			try {
				connect.close();	           
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Echèc de déconnecter la base de données !", "ERREUR DE DECONNEXION !", JOptionPane.ERROR_MESSAGE);
			}
		return connect;
	}
	
	/*************************************************************************************************
	 * Create the Methodes
	 *************************************************************************************************/	
	public static Vector<Vector<Object>> getTableDatas(String query) {
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		try {
			Statement statement = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = statement.executeQuery(query);
			ResultSetMetaData metaData = result.getMetaData();
			// Data of the table
			while (result.next()) {
				Vector<Object> vector = new Vector<Object>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					vector.add(result.getObject(i));
				}
				data.add(vector);
			}
			result.close();
			statement.close(); 
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	
	public static Vector<String> getTableHeaders(String query){
		Vector<String> header = new Vector<String>();
		try {
			Statement statement = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = statement.executeQuery(query);
			ResultSetMetaData metaData = result.getMetaData();
			// Names of columns
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				header.add(metaData.getColumnName(i));
			}
			result.close();
			statement.close(); 
		} catch (Exception e) {
			System.err.println(e);
		}
		return header;
	}
	
	/*************************************************************************************************
	 * Launch the application
	 *************************************************************************************************/
	public static void main(String[] args) {
	
		new Database();
	}
}
