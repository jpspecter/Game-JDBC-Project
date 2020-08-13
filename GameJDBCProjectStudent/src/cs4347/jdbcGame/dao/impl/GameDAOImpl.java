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

import cs4347.jdbcGame.dao.GameDAO;
import cs4347.jdbcGame.entity.Game;
import cs4347.jdbcGame.util.DAOException;

public class GameDAOImpl implements GameDAO
{

    // declaring SQL insert query as a string 
    private static final String insertSQL = "INSERT INTO game (title, description, release_date, version) VALUES (?, ?, ?, ?);";

    @Override
    /*
    *   Create method for a new Game row
    */
    public Game create(Connection connection, Game game) throws SQLException, DAOException
    {
        // throw DAOException if Game's ID field is null  
        if (game.getId() != null) {
            throw new DAOException("Trying to insert Game with NON-NULL ID");
        }

        // set PreparedStatement to null 
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

            // setting the fields using the setter and getter functions for the DAO
            ps.setString(1, game.getTitle());
            ps.setString(2, game.getDescription());
            ps.setDate(3, new java.sql.Date(game.getReleaseDate().getTime()));
            ps.setString(4, game.getVersion());
            ps.executeUpdate();

            // Copy the assigned ID to the game instance.
            ResultSet keyRS = ps.getGeneratedKeys();
            keyRS.next();
            int lastKey = keyRS.getInt(1);
            game.setId((long) lastKey);
            return game;
        }
        finally {
            // check if the PreparedStatement is not null and is not closed
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    // declaring SQL select query as a string 
    final static String selectSQL = "SELECT id, title, description, release_date, version FROM game where id = ?";

    @Override
    /*
    *   Retrieve method to get a row from the Game table
    */
    public Game retrieve(Connection connection, Long gameID) throws SQLException, DAOException
    {
        // throw DAOException if Game's ID field is null  
        if (gameID == null) {
            throw new DAOException("Trying to retrieve Game with NULL ID");
        }

        // set PreparedStatement to null
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(selectSQL);

            // setting the ID field using the setter for the DAO
            ps.setLong(1, gameID);
            ResultSet rs = ps.executeQuery();

            // if resultSet does not have a row
            if (!rs.next()) {
                return null;
            }

            // extracting information from ResultSet
            Game game = extractFromRS(rs);
            return game;
        }
        finally {
            // check if the PreparedStatement is not null and is not closed
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    // declaring SQL update query as a string 
    final static String updateSQL = "UPDATE game SET title = ?, description = ?, release_date = ?, version = ? WHERE id = ?;";

    @Override
    /*
    *   Update method for to update Game row
    */
    public int update(Connection connection, Game game) throws SQLException, DAOException
    {
        Long id = game.getId();

        // throw DAOException if Game's ID field is null  
        if (id == null) {
            throw new DAOException("Trying to update Game with NULL ID");
        }

        // set PreparedStatement to null
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(updateSQL);
            
            // setting the fields using the setter and getter functions for the DAO
            ps.setString(1, game.getTitle());
            ps.setString(2, game.getDescription());
            ps.setDate(3, new java.sql.Date(game.getReleaseDate().getTime()));
            ps.setString(4, game.getVersion());
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
    final static String deleteSQL = "delete from game where id = ?;";

    @Override
    /*
    *   Delete method for an existing CreditCard row
    */
    public int delete(Connection connection, Long id) 
            throws SQLException, DAOException
    {
        // throw DAOException if Game's ID field is null  
        if (id == null) {
            throw new DAOException("Trying to delete Game with NULL ID");
        }

        // set PreparedStatement to null 
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(deleteSQL);

            // setting the field using the setter functions for the DAO
            ps.setLong(1, id);

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
    final static String countSQL = "select count(*) from game";

    @Override
    /*
    *   Count method to count number of Games
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

            // get count from resultSet
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
    final static String retrieveByTitleSQL = "select id, title, description, release_date, version from game where title like ?";

    @Override
    /*
    *    retrieveByTitle method for getting Game row
    */
    public List<Game> retrieveByTitle(Connection connection, String title) 
            throws SQLException, DAOException
    {
        List<Game> result = new ArrayList<Game>();

        // set PreparedStatement to null 
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(retrieveByTitleSQL);

            // setting the fields using the setter functions for the DAO
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();

            // loop through the resultSet to store the Game in the list
            while (rs.next()) {
                Game game = extractFromRS(rs);
                result.add(game);
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

    // declaring SQL select query as a string 
    final static String retrieveByRelDate = "select id, title, description, release_date, version from game where release_date between ? and ?";

    @Override
    /*
    *    retrieveByReleaseDate method for getting Game row
    */
    public List<Game> retrieveByReleaseDate(Connection connection, Date start, Date end)
            throws SQLException, DAOException
    {
        List<Game> result = new ArrayList<Game>();

        // set PreparedStatement to null 
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(retrieveByRelDate);

            // setting the fields using the setter and getter functions for the DAO
            ps.setDate(1, new java.sql.Date(start.getTime()));
            ps.setDate(2, new java.sql.Date(end.getTime()));
            ResultSet rs = ps.executeQuery();

            // loop through the resultSet to store the Game in the list
            while (rs.next()) {
                Game game = extractFromRS(rs);
                result.add(game);
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
    *   extarctFromRS method to get Game rows
    */
    private Game extractFromRS(ResultSet rs) 
        throws SQLException
    {
        Game game = new Game();
        game.setId(rs.getLong("id"));
        game.setTitle(rs.getString("title"));
        game.setDescription(rs.getString("description"));
        game.setReleaseDate(rs.getDate("release_date"));
        game.setVersion(rs.getString("version"));
        return game;
    }
}
