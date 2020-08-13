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
import java.util.List;

import javax.sql.DataSource;

import cs4347.jdbcGame.dao.GamesOwnedDAO;
import cs4347.jdbcGame.dao.impl.GamesOwnedDAOImpl;
import cs4347.jdbcGame.entity.GamesOwned;
import cs4347.jdbcGame.services.GamesOwnedService;
import cs4347.jdbcGame.util.DAOException;

// this class is a service for GamesOwned
public class GamesOwnedServiceImpl implements GamesOwnedService {
	private DataSource dataSource;

	public GamesOwnedServiceImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// this function inserts gamesOwned into the table
	@Override
	public GamesOwned create(GamesOwned gamesOwned) throws DAOException, SQLException {
		GamesOwnedDAO gamesOwnedDAO = new GamesOwnedDAOImpl();

		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			GamesOwned g1 = gamesOwnedDAO.create(connection, gamesOwned);
			connection.commit();
			return g1;
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

	// this function retrieves a row from the table based on gamesOwnedID
	@Override
	public GamesOwned retrieveByID(long gamesOwnedID) throws DAOException, SQLException {
		GamesOwnedDAO gamesOwnedDAO = new GamesOwnedDAOImpl();

		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			GamesOwned g1 = gamesOwnedDAO.retrieveID(connection, gamesOwnedID);
			connection.commit();
			return g1;
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

	// this function retrieves a row from the table based on playerID and gameID
	@Override
	public GamesOwned retrievePlayerGameID(long playerID, long gameID) throws DAOException, SQLException {
		GamesOwnedDAO gamesOwnedDAO = new GamesOwnedDAOImpl();

		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			GamesOwned g1 = gamesOwnedDAO.retrievePlayerGameID(connection, playerID, gameID);
			connection.commit();
			return g1;
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

	// this function retrieves rows based on gameID
	@Override
	public List<GamesOwned> retrieveByGame(long gameID) throws DAOException, SQLException {
		GamesOwnedDAO gamesOwnedDAO = new GamesOwnedDAOImpl();

		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			List<GamesOwned> g1 = gamesOwnedDAO.retrieveByGame(connection, gameID);
			connection.commit();
			return g1;
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

	// this function retrieves rows based on playerID
	@Override
	public List<GamesOwned> retrieveByPlayer(long playerID) throws DAOException, SQLException {
		GamesOwnedDAO gamesOwnedDAO = new GamesOwnedDAOImpl();

		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			List<GamesOwned> g1 = gamesOwnedDAO.retrieveByPlayer(connection, playerID);
			connection.commit();
			return g1;
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

	// this function updates a row using the values in gamesOwned
	@Override
	public int update(GamesOwned gamesOwned) throws DAOException, SQLException {
		GamesOwnedDAO gamesOwnedDAO = new GamesOwnedDAOImpl();

		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			int g1 = gamesOwnedDAO.update(connection, gamesOwned);
			connection.commit();
			return g1;
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

	// this function deletes a row based on gameOwnedID
	@Override
	public int delete(long gameOwnedID) throws DAOException, SQLException {
		GamesOwnedDAO gamesOwnedDAO = new GamesOwnedDAOImpl();

		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			int g1 = gamesOwnedDAO.delete(connection, gameOwnedID);
			connection.commit();
			return g1;
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

	// this function counts the number of entries in a table
	@Override
	public int count() throws DAOException, SQLException {
		GamesOwnedDAO gamesOwnedDAO = new GamesOwnedDAOImpl();

		Connection connection = dataSource.getConnection();

		// transaction
		try {
			connection.setAutoCommit(false);
			int g1 = gamesOwnedDAO.count(connection);
			connection.commit();
			return g1;
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
