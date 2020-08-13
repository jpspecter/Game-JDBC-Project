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
import java.util.List;
import cs4347.jdbcGame.dao.CreditCardDAO;
import cs4347.jdbcGame.entity.CreditCard;
import cs4347.jdbcGame.entity.Game;
import cs4347.jdbcGame.util.DAOException;

public class CreditCardDAOImpl implements CreditCardDAO
{   
    // declaring SQL insert query as a string 
    private static final String insertSQL = "INSERT INTO creditcard(cc_name, cc_number, exp_date, security_code, player_id) "
            + "VALUES(?, ?, ?, ?, ?);";

    @Override
    /*
    *   Create method for a new CreditCard row
    */
    public CreditCard create(Connection connection, CreditCard creditCard, Long playerID)
            throws SQLException, DAOException
    {   

        // throw DAOException if Credit Card's ID field is null  
        if (creditCard.getId() != null) {
            throw new DAOException("Trying to insert CreditCard with NON-NULL ID");
        }
        
        // set PreparedStatement to null 
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

            // setting the fields using the setter and getter functions for the DAO
            ps.setString(1, creditCard.getCcName());
            ps.setString(2, creditCard.getCcNumber());
            ps.setString(3, creditCard.getExpDate());
            ps.setInt(4, creditCard.getSecurityCode());
            ps.setLong(5, playerID);
            ps.executeUpdate();

            // Copy the assigned ID to the game instance.
            ResultSet keyRS = ps.getGeneratedKeys();
            keyRS.next();
            int lastKey = keyRS.getInt(1);
            creditCard.setId((long) lastKey);
            creditCard.setPlayerID(playerID);
            return creditCard;
        }
        finally {
            // check if the PreparedStatement is not null and is not closed
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    // declaring SQL select query as a string 
    final static String selectSQL = "SELECT id, cc_name, cc_number, exp_date, security_code, player_id FROM creditcard where id = ?";
    
    @Override
    /*
    *   Retrieve method to get a row from the CreditCard table
    */
    public CreditCard retrieve(Connection connection, Long ccID) 
            throws SQLException, DAOException
    {
        // throw DAOException if Credit Card's ID field is null  
    	if (ccID == null) {
            throw new DAOException("Trying to retrieve credit card with NULL ID");
        }

        // set PreparedStatement to null 
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(selectSQL);

            // setting the ID field using the setter for the DAO
            ps.setLong(1, ccID);
            ResultSet rs = ps.executeQuery();

            // if resultSet does not have a row 
            if (!rs.next()) {
                return null;
            }

            // extracting information from ResultSet
            CreditCard creditCard = extractFromRS(rs);
            return creditCard;
        }
        finally {
            // check if the PreparedStatement is not null and is not closed
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
        
    }

    // declaring SQL select query as a string 
    final static String selectByPlayerSQL = "SELECT id, cc_name, cc_number, exp_date, security_code, player_id FROM creditcard where player_id = ?";
    
    @Override
    /*
    *    retrieveCreditCardsForPlayer method for getting CreditCard row
    */
    public List<CreditCard> retrieveCreditCardsForPlayer(Connection connection, Long playerID)
            throws SQLException, DAOException
    {
        List<CreditCard> result = new ArrayList<CreditCard>();
        
        // set PreparedStatement to null 
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(selectByPlayerSQL);

            // setting the fields using the setter functions for the DAO
            ps.setLong(1, playerID);
            ResultSet rs = ps.executeQuery();

            // loop through the resultSet to store the CC in the list
            while (rs.next()) {
                CreditCard creditCard = extractFromRS(rs);
                result.add(creditCard);
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

    // declaring SQL update query as a string 
    final static String updateSQL = "UPDATE creditcard SET cc_name = ?, cc_number = ?, exp_date = ?, security_code = ? WHERE id = ?;";
    
    @Override
    /*
    *   Update method for to update CreditCard row
    */
    public int update(Connection connection, CreditCard creditCard) 
            throws SQLException, DAOException
    {
        // throw DAOException if Credit Card's ID field is null  
    	Long id = creditCard.getId();
        if (id == null) {
            throw new DAOException("Trying to update Game with NULL ID");
        }

        // set PreparedStatement to null
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(updateSQL);

            // setting the fields using the setter and getter functions for the DAO
            ps.setString(1, creditCard.getCcName());
            ps.setString(2, creditCard.getCcNumber());
            ps.setString(3, creditCard.getExpDate());
            ps.setInt(4, creditCard.getSecurityCode());
            ps.setLong(5, creditCard.getId());

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
    final static String deleteSQL = "delete from creditcard where id = ?;";

    @Override
    /*
    *   Delete method for an existing CreditCard row
    */
    public int delete(Connection connection, Long ccID) 
            throws SQLException, DAOException
    {
        // throw DAOException if Credit Card's ID field is null  
    	if (ccID == null) {
            throw new DAOException("Trying to delete credit card with NULL ID");
        }

        // set PreparedStatement to null 
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(deleteSQL);

            // setting the field using the setter functions for the DAO
            ps.setLong(1, ccID);

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

    // declaring SQL delete by player query as a string 
    final static String deleteByPlayerSQL = "delete from creditcard where player_id = ?;";
    
    @Override
    /*
    *   Delete for player method for a CreditCard row
    */
    public int deleteForPlayer(Connection connection, Long playerID) throws SQLException, DAOException
    {
        // throw DAOException if PlayerID field is null
    	if (playerID == null) {
            throw new DAOException("Trying to delete credit card with NULL player ID");
        }
        
        // set PreparedStatement to null
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(deleteByPlayerSQL);

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
    *   Count method to count number of CreditCards
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
    
    /*
    *   extarctFromRS method to get CreditCard rows
    */
    private CreditCard extractFromRS(ResultSet rs) 
            throws SQLException
    {
        CreditCard creditCard = new CreditCard();

        // setting the fields using the setter functions for the DAO
        creditCard.setId(rs.getLong("id"));
        creditCard.setCcName(rs.getString("cc_name"));
        creditCard.setCcNumber(rs.getString("cc_number"));
        creditCard.setExpDate(rs.getString("exp_date"));
        creditCard.setSecurityCode(rs.getInt("security_code"));
        creditCard.setPlayerID(rs.getLong("player_id"));
        return creditCard;
    }
}
