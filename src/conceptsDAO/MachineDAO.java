package conceptsDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import concepts.Machine;
import ontology.Database;

//CTRL + SHIFT + O pour générer les imports
public class MachineDAO extends DAO<Machine> {
	
	public MachineDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	public int insert(Machine machine) {
		// TODO Auto-generated method stub
		try {
				Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				String query = "INSERT INTO MACHINES (MACHINE_NOM, MACHINE_DUREE) VALUES ('" + machine.getNom() + "', " + machine.getDuree()+")";
				state.executeUpdate(query);
				query = "SELECT MACHINE_ID FROM MACHINES"; 
				ResultSet resultUser = state.executeQuery(query);
				if(resultUser.last()) {
					return resultUser.getInt("MACHINE_ID");
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
			String query = "DELETE FROM MACHINES WHERE MACHINE_ID =" + id;
			state.executeUpdate(query);
			state.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	@Override
	public Machine find(int id) {
		// TODO Auto-generated method stub
		try {
				String query = "SELECT * FROM MACHINES WHERE MACHINE_ID = " + id;
				Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet result = state.executeQuery(query);
				if(result.first())
					return new Machine(id, result.getString("MACHINE_NOM"), result.getInt("MACHINE_DUREE"));
				result.close();
				state.close();
		} catch (SQLException e) {
				e.printStackTrace();
		}
		return null;
	}

	@Override
	public void update(int id, Machine machine) {
		// TODO Auto-generated method stub
		try {
				String query = "UPDATE MACHINE SET MACHINE_NOM = '" + machine.getNom() + "', MACHINE_DUREE = " + machine.getDuree() + " WHERE MACHINE_ID = " + id;
				this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeUpdate(query);
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
	
}


