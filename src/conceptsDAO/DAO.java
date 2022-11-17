package conceptsDAO;

import java.sql.*;

public abstract class DAO<T> {
	
	protected Connection connect = null;
	
	public DAO(Connection conn){
		this.connect = conn;
	}
	
	/**
	 * Method creation
	 * @param obj
	 * @return boolean
	 */
	public abstract int insert(T obj);
	
	/**
	 * Method for delete
	 * @param obj
	 * @return boolean
	 */
	public abstract void delete(int id);
	
	/**
	 * Method update
	 * @param obj
	 * @return boolean
	 */
	public abstract void update(int id, T obj);
	
	/**
	 * Method search of informations
	 * @param id
	 * @return T
	 */
	public abstract T find(int id);
}
