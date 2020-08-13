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
package cs4347.jdbcGame.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import cs4347.jdbcGame.dao.CreditCardDAO;
import cs4347.jdbcGame.dao.PlayerDAO;
import cs4347.jdbcGame.dao.impl.CreditCardDAOImpl;
import cs4347.jdbcGame.dao.impl.PlayerDAOImpl;
import cs4347.jdbcGame.entity.CreditCard;
import cs4347.jdbcGame.entity.Player;
import cs4347.jdbcGame.services.PlayerService;
import cs4347.jdbcGame.util.DAOException;

// this class is a service for Player
public class PlayerServiceImpl implements PlayerService {
	private DataSource dataSource;

	public PlayerServiceImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// this function inserts player into the table for players
	// it also inserts player's credit cards into the table for credit cards
	@Override
	public Player create(Player player) throws DAOException, SQLException {
		if (player.getCreditCards() == null || player.getCreditCards().size() == 0) {
			throw new DAOException("Player must have at lease one CreditCard");
		}

		PlayerDAO playerDAO = new PlayerDAOImpl();
		CreditCardDAO ccDAO = new CreditCardDAOImpl();

		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			Player p1 = playerDAO.create(connection, player);
			Long playerID = p1.getId();
			for (CreditCard creditCard : player.getCreditCards()) {
				creditCard.setPlayerID(playerID);
				ccDAO.create(connection, creditCard, playerID);
			}
			connection.commit();
			return p1;
		} catch (Exception ex) {
			connection.rollback();
			throw ex;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	// this function retrieves a player and his/her credit cards based on playerID
	@Override
	public Player retrieve(Long playerID) throws DAOException, SQLException {
		PlayerDAO playerDAO = new PlayerDAOImpl();
		CreditCardDAO ccDAO = new CreditCardDAOImpl();
		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			Player p1 = playerDAO.retrieve(connection, playerID);
			List<CreditCard> cards = ccDAO.retrieveCreditCardsForPlayer(connection, playerID);

			if (p1 != null) {
				p1.setCreditCards(cards);
			}
			connection.commit();
			return p1;
		} catch (Exception ex) {
			connection.rollback();
			throw ex;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	// this function updates a player's info and his/her credit cards using the
	// values in player
	@Override
	public int update(Player player) throws DAOException, SQLException {
		if (player.getCreditCards() == null || player.getCreditCards().size() == 0) {
			throw new DAOException("Player must have at lease one CreditCard");
		}

		PlayerDAO playerDAO = new PlayerDAOImpl();
		CreditCardDAO ccDAO = new CreditCardDAOImpl();

		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			int rows = playerDAO.update(connection, player);

			for (CreditCard creditCard : player.getCreditCards()) {

				if (creditCard.getId() == null) {
					ccDAO.create(connection, creditCard, player.getId());
				} else {
					ccDAO.update(connection, creditCard);
				}
			}
			connection.commit();
			return rows;
		} catch (Exception ex) {
			connection.rollback();
			throw ex;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	// this function deletes a player and his/her credit cards based on playerID
	@Override
	public int delete(Long playerID) throws DAOException, SQLException {
		PlayerDAO playerDAO = new PlayerDAOImpl();
		CreditCardDAO ccDAO = new CreditCardDAOImpl();

		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			ccDAO.deleteForPlayer(connection, playerID);
			int rows = playerDAO.delete(connection, playerID);
			connection.commit();
			return rows;
		} catch (Exception ex) {
			connection.rollback();
			throw ex;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	// this function counts the number of players
	@Override
	public int count() throws DAOException, SQLException {
		PlayerDAO playerDAO = new PlayerDAOImpl();
		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			int res = playerDAO.count(connection);
			connection.commit();
			return res;
		} catch (Exception ex) {
			connection.rollback();
			throw ex;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	// this function retrieves players and their credit cards based on when they
	// joined
	@Override
	public List<Player> retrieveByJoinDate(Date start, Date end) throws DAOException, SQLException {
		PlayerDAO playerDAO = new PlayerDAOImpl();
		CreditCardDAO ccDAO = new CreditCardDAOImpl();
		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			List<Player> players = playerDAO.retrieveByJoinDate(connection, start, end);

			for (Player player : players) {
				List<CreditCard> cards = ccDAO.retrieveCreditCardsForPlayer(connection, player.getId());
				player.setCreditCards(cards);
			}
			connection.commit();
			return players;
		} catch (Exception ex) {
			connection.rollback();
			throw ex;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	// this function counts the number of credit cards a certain player has
	/**
	 * Used for debugging and testing purposes.
	 */
	@Override
	public int countCreditCardsForPlayer(Long playerID) throws DAOException, SQLException {
		CreditCardDAO ccDAO = new CreditCardDAOImpl();
		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			List<CreditCard> cards = ccDAO.retrieveCreditCardsForPlayer(connection, playerID);
			connection.commit();
			return cards.size();
		} catch (Exception ex) {
			connection.rollback();
			throw ex;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

}
