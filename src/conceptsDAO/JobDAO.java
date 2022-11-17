package conceptsDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ontology.Database;
import concepts.Job;
import concepts.Machine;

public class JobDAO extends DAO<Job> {
	
	public JobDAO(Connection connection) {
		super(connection);
	}

	@Override
	public int insert(Job job) {
		// TODO Auto-generated method stub
		try {
			Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "INSERT INTO JOBS (JOB_NOM, JOB_PRODUIT_ID, JOB_QUANTITE, JOB_COUT, JOB_DUEDATE) VALUES ('"
											 +job.getNom()+"', "+job.getProduit().getId()+", "+job.getQuantite()+", "+job.getCout()+", "+job.getDueDate()+")";
			state.executeUpdate(query);
			query = "SELECT JOB_ID FROM JOBS"; 
			ResultSet resultUser = state.executeQuery(query);
			if(resultUser.last()) {
				return resultUser.getInt("JOB_ID");
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
			String query = "DELETE FROM JOBS WHERE JOB_ID =" + id;
			state.executeUpdate(query);
			state.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void update(int id, Job obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Job find(int id) {
		// TODO Auto-generated method stub
		try {
			String query = "SELECT * FROM JOBS WHERE JOB_ID = " + id;
			Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = state.executeQuery(query);
			if(result.first())
				return new Job(id, result.getString("JOB_NOM"), result.getInt("JOB_PRODUIT_ID"), result.getInt("JOB_QUANTITE"), result.getString("JOB_ETAT"));
			result.close();
			state.close();
		} catch (SQLException e) {
				e.printStackTrace();
		}
		return null;
	}
}
