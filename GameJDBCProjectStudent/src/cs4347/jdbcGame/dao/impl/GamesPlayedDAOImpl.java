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
import cs4347.jdbcGame.dao.GamesPlayedDAO;
import cs4347.jdbcGame.entity.GamesPlayed;
import cs4347.jdbcGame.util.DAOException;

public class GamesPlayedDAOImpl implements GamesPlayedDAO
{
	private static final String createSQL = "INSERT INTO gamesplayed(player_id, game_id, time_finished, score) " + "VALUES (?, ?, ?, ?)"; //creating the SQl statement
   
	@Override
    public GamesPlayed create(Connection connection, GamesPlayed gamesPlayed) throws SQLException, DAOException
    {
    	if(gamesPlayed.getId()!=null)
    	{
    		throw new DAOException("Trying to insert an entity that has a Non-Null key, i.e. it already exists"); //Throwing a new exception if the ID is not null
    	}
    	PreparedStatement query = null; //Setting query to null
    	try
    	{
    		query = connection.prepareStatement(createSQL, Statement.RETURN_GENERATED_KEYS);
    		query.setLong(1,gamesPlayed.getPlayerID()); //Setting playerID
    		query.setLong(2,gamesPlayed.getGameID()); //Setting gameID
    		query.setDate(3,new java.sql.Date(gamesPlayed.getTimeFinished().getTime())); //Setting timeFinished
    		query.setInt(4, gamesPlayed.getScore()); //Setting score
    		query.executeUpdate(); //Executing the query now
    		
    		ResultSet rs = query.getGeneratedKeys(); //Getting the resultset
    		rs.next(); //Moving it to the next step so as to extract valuable data
    		int lastKey = rs.getInt(1);
    		gamesPlayed.setId((long) lastKey); //Setting the gamesPlayed id
    		return gamesPlayed; //Returning the updated object
    	}
    	finally
    	{
    		if(query!=null && !query.isClosed())
    			query.close(); //Closing the query
    			
    	}
    }
    
    private static final String selectSQl = "SELECT id, player_id, game_id, time_finished, score FROM gamesplayed WHERE id = ?";
    @Override
    public GamesPlayed retrieveID(Connection connection, Long gamePlayedID) throws SQLException, DAOException
    {
    	if(gamePlayedID == null)
    	{
    		throw new DAOException("Cannot search a null ID"); //Throwing a new exception if gamePlayedID is null
    	}
    	PreparedStatement query = null;
    	try 
    	{
    		query = connection.prepareStatement(selectSQl); //Preparing the query
    		query.setLong(1,gamePlayedID); //Setting the query
    		ResultSet rs = query.executeQuery(); //Executing the query
    		if(!rs.next())
    			return null; //If the next row doesn't 
    		GamesPlayed gamesPlayed = extractFromRS(rs); //Extracting the result set
    		return gamesPlayed; //Returning the extracted result set
    	}
    	finally
    	{
    		if(query != null && !query.isClosed())
    			query.close(); //Closing the query
    	}
    }
    
    private static final String selectSQLID = "SELECT id, player_id, game_id, time_finished, score FROM gamesplayed WHERE game_id = ? AND player_id = ?";
    @Override
    public List<GamesPlayed> retrieveByPlayerGameID(Connection connection, Long playerID, Long gameID) throws SQLException, DAOException
    {
    	if(playerID == null || gameID == null)
    	{
    		throw new DAOException("Cannot serach the database with null playerID or gameID"); //throwing a new exception if a playerID or gameID is not null
    	}
    	 List<GamesPlayed> result = new ArrayList<GamesPlayed>(); //creating a new ArrayList
    	 PreparedStatement query = null; //Setting PreparedStatement to null
    	 try 
    	 {
    		 query = connection.prepareStatement(selectSQLID); //Preparing the query
    		 query.setLong(1,gameID); //Setting the gameID to the string
    		 query.setLong(2,playerID); //Setting the playerID to the string
    		 ResultSet rs = query.executeQuery(); //Getting the resultset
    		 while(rs.next())
    		 {
    			 result.add(extractFromRS(rs));
    		 }
    		 return result;
    	 }
    		 finally 
    		 {
    			 if(query != null && !query.isClosed())
    				 query.close(); //Closing the query
    		 }
    }

    private static final String selectSQLPlayer = "SELECT id, player_id, game_id, time_finished, score FROM gamesplayed WHERE player_id = ?";
    @Override
    public List<GamesPlayed> retrieveByPlayer(Connection connection, Long playerID) throws SQLException, DAOException
    {
    	if(playerID == null)
    	{
    		throw new DAOException("Cannot search the database with a null PlayerID"); //Throwing a new exception if the playerID is null
    	}
    List<GamesPlayed> result = new ArrayList<GamesPlayed>(); //creating a new ArrayList
   	 PreparedStatement query = null; //Setting PreparedStatement to null
   	 try 
   	 {
   		 query = connection.prepareStatement(selectSQLPlayer); //Preparing the query
   		 query.setLong(1,playerID); //Setting the gameID to the string
   		 ResultSet rs = query.executeQuery(); //Getting the resultset
   		 while(rs.next())
   		 {
   			 result.add(extractFromRS(rs));
   		 }
   		 return result;
   	 }
    finally 
   	 {
   	      if(query != null && !query.isClosed())
   		  query.close(); //Closing the query
   	 }
    }

    private static final String selectSQLGame = "SELECT id, player_id, game_id, time_finished, score FROM gamesplayed WHERE game_id = ?";
    @Override
    public List<GamesPlayed> retrieveByGame(Connection connection, Long gameID) throws SQLException, DAOException
    {
    	if(gameID == null)
    	{
    		throw new DAOException("Cannot search the database with a null gameID"); //Throwing a new exception if the playerID is null
    	}
    List<GamesPlayed> result = new ArrayList<GamesPlayed>(); //creating a new ArrayList
   	 PreparedStatement query = null; //Setting PreparedStatement to null
   	 try 
   	 {
   		 query = connection.prepareStatement(selectSQLGame); //Preparing the query
   		 query.setLong(1,gameID); //Setting the gameID to the string
   		 ResultSet rs = query.executeQuery(); //Getting the resultset
   		 while(rs.next())
   		 {
   			 result.add(extractFromRS(rs));
   		 }
   		 return result;
   	 }
    finally 
   	 {
   	      if(query != null && !query.isClosed())
   		  query.close(); //Closing the query
   	 }
    }

    private static final String updateSQL = "UPDATE gamesplayed SET player_id = ?, game_id = ?, time_finished = ?, score = ? WHERE id = ?";
    @Override
    public int update(Connection connection, GamesPlayed gamesPlayed) throws SQLException, DAOException
    {
    	if(gamesPlayed == null)
    	{
    		throw new DAOException("Null Objects cannot be updated"); //Throwing an Exception
    	}
    	PreparedStatement query = null;
    	try
    	{
    		query = connection.prepareStatement(updateSQL);
    		query.setLong(1, gamesPlayed.getPlayerID());
    		query.setLong(2,gamesPlayed.getGameID());
    		query.setDate(3, new java.sql.Date(gamesPlayed.getTimeFinished().getTime()));
    		query.setInt(4, gamesPlayed.getScore());
    		query.setLong(5, gamesPlayed.getId());
    		
    		int rows = query.executeUpdate();
    		return rows;
    	}
    	finally
    	{
    		if(query != null && !query.isClosed())
    			query.close();
    	}
    }

    private static final String deleteSQL = "DELETE FROM gamesplayed WHERE id = ?";
    @Override
    public int delete(Connection connection, Long gamePlayedID) throws SQLException, DAOException
    {
        if(gamePlayedID == null)
        {
        	throw new DAOException("Trying to delete with Null ID"); //Throwing a new Exception
        }
        PreparedStatement query = null;
        try 
        {
        	query = connection.prepareStatement(deleteSQL); //Preparing the statement
        	query.setLong(1,gamePlayedID); //Setting the SQL query
        	int rows = query.executeUpdate(); //Executing the update
        	return rows;
        }
        finally
        {
        	if(query != null && !query.isClosed())
        		query.close(); //closing the query
        }
    }

    private static final String countSQL = "SELECT COUNT(*) FROM gamesplayed";
    @Override
    public int count(Connection connection) throws SQLException, DAOException
    {
        PreparedStatement query = null;
        try
        {
        	query = connection.prepareStatement(countSQL); //Setting the query
        	ResultSet rs = query.executeQuery(); //Executing the SQL query
        	if(!rs.next())
        		throw new DAOException("No Count found"); //Throwing a new exception
        	return rs.getInt(1); //returning the count
        }
        finally
        {
        	if(query != null && !query.isClosed())
        		query.close(); //Closing the query
        }
    }
    
    private GamesPlayed extractFromRS(ResultSet rs) throws SQLException //Creating a new private function to extract data from result set
    {
    	GamesPlayed gamesPlayed = new GamesPlayed(); //Creating a new GamesPlayed object
    	gamesPlayed.setId(rs.getLong("id")); //Setting the id
    	gamesPlayed.setPlayerID(rs.getLong("player_id")); //Setting the playerID
    	gamesPlayed.setGameID(rs.getLong("game_id")); //Setting the gameID
    	gamesPlayed.setTimeFinished(rs.getDate("time_finished")); //Setting the timeFinished
    	gamesPlayed.setScore(rs.getInt("score")); //Setting the score
    	return gamesPlayed; //Returning the gamesPlayed object
    }

}