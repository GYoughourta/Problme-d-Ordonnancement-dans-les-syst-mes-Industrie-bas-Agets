package conceptsDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import concepts.Produit;
import ontology.Database;



//CTRL + SHIFT + O pour générer les imports
public class ProduitDAO extends DAO<Produit> {
	
	public ProduitDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	public int insert(Produit produit) {
		// TODO Auto-generated method stub
		try {
				Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				String query = "INSERT INTO PRODUITS (PRODUIT_NOM, PRODUIT_PRIX) VALUES ('"+produit.getDescription()+"', "+produit.getPrix()+")";
				state.executeUpdate(query);
				query = "SELECT PRODUIT_ID FROM PRODUITS"; 
				ResultSet resultUser = state.executeQuery(query);
				if(resultUser.last()) {
					return resultUser.getInt("PRODUIT_ID");
				}
				state.close();
		} catch (Exception e) {
				e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		try {
			Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "DELETE FROM PRODUITS WHERE PRODUIT_ID =" + id;
			state.executeUpdate(query);
			state.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	@Override
	public Produit find(int id) {
		// TODO Auto-generated method stub
		try {
				String query = "SELECT * FROM PRODUITS WHERE PRODUIT_ID = " + id;
				Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet result = state.executeQuery(query);
				if(result.first())
					return new Produit(id, result.getString("PRODUIT_NOM"),result.getDouble("PRODUIT_PRIX"));
				result.close();
				state.close();
		} catch (SQLException e) {
				e.printStackTrace();
		}
		return null;
	}

	@Override
	public void update(int id, Produit produit) {
		// TODO Auto-generated method stub
		try {
				String query = "UPDATE PRODUITS SET 'PRODUIT_NOM = '" + produit.getDescription() + "', PRODUIT_PRIX = '" + produit.getPrix() + "' WHERE PRODUIT_ID = " + id;
				this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeUpdate(query);
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
	
}

