/* NOTICE: All materials provided by this project, and materials derived 
 * from the project, are the property of the University of Texas. 
 * Project materials, or those derived from the materials, cannot be placed 
 * into publicly accessible locations on the web. Project materials cannot 
 * be shared with other project teams. Making project materials publicly 
 * accessible, or sharing with other project teams will result in the 
 * failure of the team responsible and any team that uses the shared materials. 
 * Sharing project materials or using shared materials will also result 
 * in the reporting of all team members for academic dishonesty. 
 */
package cs4347.jdbcGame.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs4347.jdbcGame.dao.PlayerDAO;
import cs4347.jdbcGame.entity.Game;
import cs4347.jdbcGame.entity.Player;
import cs4347.jdbcGame.util.DAOException;

public class PlayerDAOImpl implements PlayerDAO {

	// declaring SQL insert query as a string 
	private static final String insertSQL = "INSERT INTO player (first_name, last_name, join_date, email) VALUES (?, ?, ?, ?);";

	@Override
	/*
    *   Create method for a new Player row
    */
	public Player create(Connection connection, Player player) 
			throws SQLException, DAOException 
	{
		// throw DAOException if Player's ID field is null  
		if (player.getId() != null) {
			throw new DAOException("Trying to insert Player with NON-NULL ID");
		}

		// set PreparedStatement to null 
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

			// setting the fields using the setter and getter functions for the DAO			
			ps.setString(1, player.getFirstName());
			ps.setString(2, player.getLastName());
			ps.setDate(3, new java.sql.Date(player.getJoinDate().getTime()));
			ps.setString(4, player.getEmail());
			ps.executeUpdate();

			// Copy the assigned ID to player
			ResultSet keyRS = ps.getGeneratedKeys();
			keyRS.next();
			int lastKey = keyRS.getInt(1);
			player.setId((long) lastKey);
			return player;
		} finally {
			// check if the PreparedStatement is not null and is not closed
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	// declaring SQL select query as a string 
	final static String selectSQL = "SELECT id, first_name, last_name, join_date, email FROM player where id = ?";

	@Override
	/*
    *   Retrieve method to get a row from the Player table
    */
	public Player retrieve(Connection connection, Long playerID) 
			throws SQLException, DAOException 
	{
		// throw DAOException if Player's ID field is null	
		if (playerID == null) {
			throw new DAOException("Trying to retrieve Game with NULL ID");
		}

		// set PreparedStatement to null
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectSQL);

			// setting the ID field using the setter for the DAO
			ps.setLong(1, playerID);
			ResultSet rs = ps.executeQuery();

			// if resultSet does not have a row
			if (!rs.next()) {
				return null;
			}

			// extracting information from ResultSet
			Player player = extractFromRS(rs);
			return player;
		} 
		finally {
			// check if the PreparedStatement is not null and is not closed
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	// declaring SQL update query as a string 
	final static String updateSQL = "UPDATE player SET first_name = ?, last_name = ?, join_date = ?, email = ? WHERE id = ?;";

	@Override
	/*
    *   Update method for to update Game row
    */
	public int update(Connection connection, Player player) 
			throws SQLException, DAOException 
	{
		Long id = player.getId();
		// throw DAOException if Player's ID field is null  
		if (id == null) {
			throw new DAOException("Trying to update Game with NULL ID");
		}
		
		// set PreparedStatement to null
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updateSQL);

			// setting the fields using the setter and getter functions for the DAO
			ps.setString(1, player.getFirstName());
			ps.setString(2, player.getLastName());
			ps.setDate(3, new java.sql.Date(player.getJoinDate().getTime()));
			ps.setString(4, player.getEmail());
			ps.setLong(5, id);

			int rows = ps.executeUpdate();
			return rows;
		} 
		finally {
			// check if the PreparedStatement is not null and is not closed
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	// declaring SQL delete query as a string
	final static String deleteSQL = "delete from player where id = ?;";

	@Override
	/*
    *   Delete method for an existing Player row
    */
	public int delete(Connection connection, Long playerID) 
			throws SQLException, DAOException 
	{
		// throw DAOException if Player's ID field is null
		if (playerID == null) {
			throw new DAOException("Trying to delete player with NULL ID");
		}
		
		// set PreparedStatement to null
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(deleteSQL);

			// setting the field using the setter functions for the DAO
			ps.setLong(1, playerID);

			int rows = ps.executeUpdate();
			return rows;
		} 
		finally {
			// check if the PreparedStatement is not null and is not closed
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	// declaring SQL count query as a string 
	final static String countSQL = "select count(*) from player";

	@Override
	/*
    *   Count method to count number of Players
    */
	public int count(Connection connection) 
			throws SQLException, DAOException 
	{
		// set PreparedStatement to null
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(countSQL);
			ResultSet rs = ps.executeQuery();
			
			// if resultSet is empty
			if (!rs.next()) {
				throw new DAOException("No Count Returned");
			}
			int count = rs.getInt(1);
			return count;
		} 
		finally {
			// check if the PreparedStatement is not null and is not closed
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}
	
	// declaring SQL select query as a string 
	final static String retrieveByJoiningDate = "select id, first_name, last_name, join_date, email from player where join_date between ? and ?";

	@Override
	/*
    *   RetrieveByJoinDate method to get a row from the Player table
    */
	public List<Player> retrieveByJoinDate(Connection connection, Date start, Date end)
			throws SQLException, DAOException 
	{
		 
		List<Player> result = new ArrayList<Player>();

		// set PreparedStatement to null
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(retrieveByJoiningDate);

			// setting the fields using the setter and getter functions for the DAO
			ps.setDate(1, new java.sql.Date(start.getTime()));
			ps.setDate(2, new java.sql.Date(end.getTime()));
			ResultSet rs = ps.executeQuery();
			
			// while resultSet has a row
			while (rs.next()) {
				Player player = extractFromRS(rs);
				result.add(player);
			}
			return result;
		}
		finally {
			// check if the PreparedStatement is not null and is not closed
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	/*
    *   extarctFromRS method to get Player rows
    */
	private Player extractFromRS(ResultSet rs) 
			throws SQLException 
	{
		Player player = new Player();
		player.setId(rs.getLong("id"));
		player.setFirstName(rs.getString("first_name"));
		player.setLastName(rs.getString("last_name"));
		player.setJoinDate(rs.getDate("join_date"));
		player.setEmail(rs.getString("email"));
		return player;
	}

}
