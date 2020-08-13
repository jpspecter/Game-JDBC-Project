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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcGame.dao.GamesOwnedDAO;
import cs4347.jdbcGame.entity.GamesOwned;
import cs4347.jdbcGame.entity.GamesPlayed;
import cs4347.jdbcGame.entity.GamesOwned;
import cs4347.jdbcGame.util.DAOException;

public class GamesOwnedDAOImpl implements GamesOwnedDAO
{

	// declaring SQL insert query as a string 
	private static final String insertSQL = "INSERT INTO gamesowned(purchase_date, purchase_price, player_id, game_id) "
            + "VALUES(?,?,?,?);";
	
	@Override
	/*
    *   Create method for a new GamesOwned row
    */
	public GamesOwned create(Connection connection, GamesOwned GamesOwned) 
			throws SQLException, DAOException
    {
        // throw DAOException if GamesOwned's ID field is null  
    	if (GamesOwned.getId() != null) {
            throw new DAOException("Trying to insert GamesOwned with NON-NULL ID");
        }
		
		// set PreparedStatement to null 
    	PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

			// setting the fields using the setter and getter functions for the DAO
            ps.setDate(1, new java.sql.Date(GamesOwned.getPurchaseDate().getTime()));
            ps.setFloat(2, GamesOwned.getPurchasePrice());
            ps.setLong(3, GamesOwned.getPlayerID());
            ps.setLong(4, GamesOwned.getGameID());
            ps.executeUpdate();

            // Copy the assigned ID to GamesOwned
            ResultSet keyRS = ps.getGeneratedKeys();
            keyRS.next();
            int lastKey = keyRS.getInt(1);
            GamesOwned.setId((long) lastKey);
            return GamesOwned;
        }
        finally {
			// check if the PreparedStatement is not null and is not closed
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

	// declaring SQL select query as a string 
    private static final String selectSQl = "SELECT id, player_id, game_id, purchase_date, purchase_price FROM gamesowned WHERE id = ?";
	
	@Override
	 /*
    *   RetrieveID method to get a row from the GamesOwned table
    */
	public GamesOwned retrieveID(Connection connection, Long GamesOwnedID) 
			throws SQLException, DAOException
    {
		// throw DAOException if GamesOwned's ID field is null
    	if(GamesOwnedID == null)
    	{
    		throw new DAOException("Cannot search GamesOwned with NON NULL ID");
		}
		
		// set PreparedStatement to null
    	PreparedStatement ps = null;
    	try 
    	{
			ps = connection.prepareStatement(selectSQl);
			
			// setting the ID field using the setter for the DAO
    		ps.setLong(1,GamesOwnedID);
			ResultSet rs = ps.executeQuery();
			
			// if resultSet does not have a row
    		if(!rs.next())
				return null; 
				
			// extracting information from ResultSet
    		GamesOwned GamesOwned = extractFromRS(rs);
    		return GamesOwned;
    	}
    	finally {
			// check if the PreparedStatement is not null and is not closed
    		if(ps != null && !ps.isClosed())
    			ps.close(); 
    	}
    }
	
	// declaring SQL select query as a string
    private static final String selectPlayerGameID = "SELECT id, player_id, game_id, purchase_date, purchase_price FROM gamesowned WHERE game_id = ? AND player_id = ?";
	
	@Override
	/*
    *    retrieveByPlayerGameID method for getting GamesOwned row
    */
    public GamesOwned retrievePlayerGameID(Connection connection, Long playerID, Long gameID)
            throws SQLException, DAOException
    {
		// throw DAOException if Game's ID or Player's ID field is null  
    	if(playerID == null || gameID == null) {
    		throw new DAOException("Cannot search GamesOwned with NULL playerID or gameID"); 
    	}

		 // set PreparedStatement to null    	 
    	 PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectPlayerGameID);
			
			// setting the field using the setter functions for the DAO
			ps.setLong(1,gameID);
			ps.setLong(2,playerID);
			
			ResultSet rs = ps.executeQuery();
			
			// if resultSet does not have a row
			if(!rs.next())
				return null; 
				
			// extracting information from ResultSet
			GamesOwned GamesOwned = extractFromRS(rs);
			return GamesOwned;
		}
		finally {
			// check if the PreparedStatement is not null and is not closed
			if(ps != null && !ps.isClosed())
				ps.close();
		}
	}
	
    // declaring SQL select query as a string 
    private static final String selectGame = "SELECT id, player_id, game_id, purchase_date, purchase_price FROM gamesowned WHERE game_id = ?";
	
	@Override
	/*
    *    retrieveByGame method for getting GamesOwned row
    */
	public List<GamesOwned> retrieveByGame(Connection connection, Long gameID) 
			throws SQLException, DAOException
    {
        // throw DAOException if Game's ID field is null  
    	if(gameID == null) {
    		throw new DAOException("Cannot search GamesOwned with NULL gameID");
    	}
		List<GamesOwned> result = new ArrayList<GamesOwned>();
		
		// set PreparedStatement to null 
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectGame);

			// setting the field using the setter functions for the DAO
			ps.setLong(1,gameID);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				result.add(extractFromRS(rs));
			}
			return result;
		}
		finally {
            // check if the PreparedStatement is not null and is not closed
			if(ps != null && !ps.isClosed())
			ps.close();
		}
    }

	// declaring SQL select query as a string 
    private static final String selectPlayer = "SELECT id, player_id, game_id, purchase_date, purchase_price FROM gamesowned WHERE player_id = ?";
	
	@Override
	/*
    *   RetrieveByPlayer method to get a row from the GamesOwned table
    */
	public List<GamesOwned> retrieveByPlayer(Connection connection, Long playerID) 
			throws SQLException, DAOException
    {
		// throw DAOException if Player's ID field is null  
    	if(playerID == null) {
    		throw new DAOException("Cannot search GamesOwned with NULL PlayerID");
    	}
		List<GamesOwned> result = new ArrayList<GamesOwned>();

		// set PreparedStatement to null
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectPlayer);

			// setting the ID field using the setter for the DAO
			ps.setLong(1,playerID);
			ResultSet rs = ps.executeQuery();

			// while resultSet has a row
			while(rs.next()) {
				result.add(extractFromRS(rs));
			}
			return result;
		}
		finally {
			// check if the PreparedStatement is not null and is not closed
			if(ps != null && !ps.isClosed())
				ps.close();
		}
    }
	
	// declaring SQL update query as a string 
    private static final String update = "UPDATE gamesowned SET player_id = ?, game_id = ?, purchase_date = ?, purchase_price = ? WHERE id = ?";
   
	@Override
	/*
    *   Update method for to update GamesOwned row
    */
	public int update(Connection connection, GamesOwned GamesOwned) 
			throws SQLException, DAOException
    {
		// throw DAOException if GamesOwned's ID field is null  
    	if(GamesOwned == null) {
    		throw new DAOException("Updation cannot be performed on NULL objects");
		}
		
		// set PreparedStatement to null
    	PreparedStatement ps = null;
    	try {
			ps = connection.prepareStatement(update);
			
			// setting the fields using the setter and getter functions for the DAO
    		ps.setLong(1, GamesOwned.getPlayerID());
    		ps.setLong(2,GamesOwned.getGameID());
    		ps.setDate(3, new java.sql.Date(GamesOwned.getPurchaseDate().getTime()));
    		ps.setFloat(4, GamesOwned.getPurchasePrice());
    		ps.setLong(5, GamesOwned.getId());
    		
    		int resultRows = ps.executeUpdate();
    		return resultRows;
    	}
    	finally {
			// check if the PreparedStatement is not null and is not closed
    		if(ps != null && !ps.isClosed())
    			ps.close();
    	}
    }

	// declaring SQL delete query as a string
    private static final String delete = "DELETE FROM gamesowned WHERE id = ?";
	
	@Override
	/*
    *   Delete method for an existing GamesOwned row
    */
	public int delete(Connection connection, Long gameOwnedID) 
			throws SQLException, DAOException
    {
		// throw DAOException if GamesOwned's ID field is null
    	if(gameOwnedID == null) {
        	throw new DAOException("Deletion cannot be performed with NULL ID");
		}
		
		// set PreparedStatement to null 
        PreparedStatement ps = null;
        try {
			ps = connection.prepareStatement(delete);
			
			// setting the field using the setter functions for the DAO
			ps.setLong(1,gameOwnedID);
			
        	int resultRows = ps.executeUpdate();
        	return resultRows;
        }
        finally {
			// check if the PreparedStatement is not null and is not closed
        	if(ps != null && !ps.isClosed())
        		ps.close(); 
        }
    }
	
	// declaring SQL count query as a string 
    private static final String count = "SELECT COUNT(*) FROM gamesowned";
	
	@Override
	/*
    *   Count method to count number of GamesOwned
    */
	public int count(Connection connection) 
			throws SQLException, DAOException
    {
		// set PreparedStatement to null
    	PreparedStatement ps = null;
        try
        {
        	ps = connection.prepareStatement(count);
			ResultSet rs = ps.executeQuery(); 
			
			// if resultSet is empty
        	if(!rs.next())
        		throw new DAOException("Count not found");
			
			return rs.getInt(1);
        }
        finally {
			// check if the PreparedStatement is not null and is not closed
        	if(ps != null && !ps.isClosed())
        		ps.close();
        }
    }
	
	/*
    *   extarctFromRS method to get GamesOwned rows
    */
	private GamesOwned extractFromRS(ResultSet rs) 
			throws SQLException 
    {
    	GamesOwned GamesOwned = new GamesOwned(); 
    	GamesOwned.setId(rs.getLong("id")); 
    	GamesOwned.setPlayerID(rs.getLong("player_id")); 
    	GamesOwned.setGameID(rs.getLong("game_id")); 
    	GamesOwned.setPurchaseDate(rs.getDate("purchase_date")); 
    	GamesOwned.setPurchasePrice(rs.getFloat("purchase_price"));
    	return GamesOwned;
    }

}