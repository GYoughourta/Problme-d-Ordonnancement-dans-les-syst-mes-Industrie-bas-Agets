package concepts;

import java.sql.*;
import java.util.ArrayList;

import ontology.Database;

//CTRL + SHIFT + O pour générer les imports
public class User {
	
	private int Id = 0;
	private String Firstname = "";
	private String Lastname = "";
	private String Username = "";
	private String Password = "";
	private String Function = "";
	
	public User(int id, String firstname, String lastname, String username, String password, String function) {
		this.Id = id;
		this.Firstname = firstname;
		this.Lastname = lastname;
		this.Username = username;
		this.Password = password;
		this.Function = function;
	}
	
	public User(){}
	
	public int getId() {
		return Id;
	}
	
	public void setId(int id) {
		this.Id = id;
	}
	
	public String getFirstname() {
		return Firstname;
	}
	
	public void setFirstname(String firstname) {
		this.Firstname = firstname;
	}
	
	public String getLastname() {
		return Lastname;
	}
	
	public void setLastname(String lastname) {
		this.Lastname = lastname;
	}
	
	public String getUsername() {
		return Username;
	}
	
	public void setUsername(String username) {
		this.Username = username;
	}
	
	public String getPassword() {
		return Password;
	}
	
	public void setPassword(String password) {
		this.Password = password;
	}
		
	public String getFunction() {
		return Function;
	}
	
	public void setFunction(String function) {
		this.Function = function;
	}
	
	public boolean equals(User user){
		return this.getId() == user.getId();
	}
	
	/*************************************************************************************************
	 * Fin User by username and password application
	 *************************************************************************************************/
	public static User findUser(String username, String password) {
		try {
				Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				String query = "SELECT * FROM USERS WHERE USER_USERNAME = '"+ username +"' AND USER_PASSWORD = '"+ password +"'";
				ResultSet result = state.executeQuery(query);
				if(result.first())
					return new User(result.getInt("USER_ID"), result.getString("USER_FIRSTNAME"), result.getString ("USER_LASTNAME"), username, password, result.getString ("USER_FUNCTION"));
				result.close();
				state.close();	
		} catch (SQLException e) {
				e.printStackTrace();
		}
		return null;
	}
	
	/*************************************************************************************************
	 * get all users application
	 *************************************************************************************************/
	public ArrayList<User> getAllUsers() {
		ArrayList<User> users = new ArrayList<>();
		try {
			Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = state.executeQuery("SELECT * FROM USERS");
			while (result.next()) {
				User user = new User(result.getInt("USER_ID"), result.getString("USER_FIRSTNAME"), result.getString ("USER_LASTNAME"), result.getString ("USER_USERNAME"), null, null);
				users.add(user);
			}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return users;
	}
	
	/*public User find(int id) {
		try {
			String query = "SELECT * FROM USERS WHERE Id = " + id;
			Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = state.executeQuery(query);
			if(result.first())
				return new User(id, result.getString ("USR_USERNAME"), result.getString ("USR_PASSWORD"), result.getString ("USR_GRADE"));
			result.close();
			state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return null;
	}
	
		
	/*************************************************************************************************
	 * Create add User application
	 *************************************************************************************************/
	/*public static void addUser(String username, String password, String grade) {
		try {
			Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "INSERT INTO USERS (USR_USERNAME, USR_PASSWORD, USR_GRADE)"+" VALUES ('"+username+"', '"+password+"', '" +grade+"')";
			state.executeUpdate(query);
			state.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void DeleteUser(long id) {
		try {
			Statement state = Database.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "DELETE FROM USERS WHERE USR_ID =" + id;
			state.executeUpdate(query);
			state.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
