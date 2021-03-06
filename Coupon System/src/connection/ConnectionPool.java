package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

import exception.DBException;
import exception.ThreadException;
import utils.StringHelper;

public class ConnectionPool {
	private static final int NUMBER_OF_CONNECTIONS = 10;
	private static ConnectionPool instance = null;
	private Stack<Connection> connections = new Stack<>();

	private ConnectionPool() throws DBException {
		// System.out.println("ConnectionPool CTOR IN ACTION");
		openAllConnections();
	}

	public static ConnectionPool getInstance() throws DBException {
		if (instance == null) {
			synchronized (ConnectionPool.class) {
				if (instance == null) {
					instance = new ConnectionPool();
				}
			}

		}
		return instance;
	}

	public Connection getConnection() throws ThreadException {

		synchronized (connections) {
			if (connections.empty()) {
				try {
					connections.wait();
				} catch (InterruptedException e) {
					throw new ThreadException(StringHelper.WAIT_EXCEPTION + e.getCause());
				}
			}

			return connections.pop();
		}

	}

	public void returnConnection(Connection connection) {

		synchronized (connections) {
			connections.push(connection);
			connections.notify();
		}
		// System.out.println("connectionPool size: " + connections.size());
	}

	public void openAllConnections() throws DBException {
		for (int i = 0; i < NUMBER_OF_CONNECTIONS; i++) {
			Connection connection;
			try {
				connection = DriverManager.getConnection(DatabaseManager.url, DatabaseManager.username,
						DatabaseManager.password);
			} catch (SQLException e) {
				throw new DBException(StringHelper.CONNECTION_EXCEPTION + e.getCause());
			}

			connections.push(connection);
		}
	}

	public void closeAllConnections() throws InterruptedException {
		synchronized (connections) {
			while (connections.size() < NUMBER_OF_CONNECTIONS) {
				wait();
			}

			// close all connections before remove
			connections.forEach(c -> {
				try {
					c.close();
				} catch (SQLException e) {
					System.out.println("connection close exception: " + e.getMessage());
				}
			});

			connections.removeAllElements();
			System.out.println("all connection is closed. exit");
		}

	}

}
