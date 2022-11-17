package conceptsDAO;
import java.sql.*;
import ontology.Database;
import concepts.User;

//CTRL + SHIFT + O pour générer les imports
public class UserDAO extends DAO<User> {
	public UserDAO(Connection conn) {
		super(conn);
	}
	
	@Override
	public int insert(User user) {
		// TODO Auto-generated method stub
		try {
				Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				String query = "INSERT INTO USERS (USER_FIRSTNAME, USER_LASTNAME, USER_USERNAME, USER_PASSWORD, USER_FUNCTION) VALUES ('"
												+user.getFirstname()+"', '"+user.getLastname()+"', '"+user.getUsername()+"', '"+user.getPassword()+"', '" +user.getFunction()+"')";
				state.executeUpdate(query);
				query = "SELECT USER_ID FROM USERS"; 
				ResultSet resultUser = state.executeQuery(query);
				if(resultUser.last()) {
					int idUser = resultUser.getInt("USER_ID");
					return idUser;
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
			String query = "DELETE FROM USERS WHERE USER_ID =" + id;
			state.executeUpdate(query);
			state.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	@Override
	public User find(int id) {
		// TODO Auto-generated method stub
		try {
				String query = "SELECT * FROM USERS WHERE Id = " + id;
				Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet result = state.executeQuery(query);
				if(result.first())
					return new User(id, result.getString ("USER_FIRSTNAME"),result.getString ("USER_LASTNAME"), result.getString ("USER_USERNAME"), result.getString ("USER_PASSWORD"), result.getString ("USER_FUNCTION"));
				result.close();
				state.close();
		} catch (SQLException e) {
				e.printStackTrace();
		}
		return null;
	}

	@Override
	public void update(int id, User user) {
		// TODO Auto-generated method stub
		try {
				String query = "UPDATE USERS SET "
											+ "'USER_FIRSTNAME = '" + user.getFirstname()
											+ "', USER_LASTNAME = '" + user.getLastname()
											+ "', USER_USERNAME = '" + user.getUsername()
											+ "', USR_PASSWORD = '" + user.getPassword()
							+ "' WHERE USER_ID = " + id;
				this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeUpdate(query);
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
	
}
