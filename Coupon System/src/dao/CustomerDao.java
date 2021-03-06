package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Customer;
import connection.ConnectionPool;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;
import utils.CustomerUtil;
import utils.DBUtils;
import utils.StringHelper;

public class CustomerDao implements ICustomerDao {

	public CustomerDao() {

	}

	@Override
	public void add(Customer addObject) throws DBException, ThreadException, MisMatchObjectException {
		boolean fromUpdate = false;
		String sql = DBUtils.ADD_CUSTOMER_QUERY;

		// basic validation
		if (StringHelper.allParametersNotEmpty(addObject, fromUpdate)) {
			Connection connection = ConnectionPool.getInstance().getConnection();
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, addObject.getFirstName());
				statement.setString(2, addObject.getLastName());
				statement.setString(3, addObject.getEmail());
				statement.setString(4, addObject.getPassword());
				statement.execute();
				returnConnection(connection);
				System.out.println(
						StringHelper.CUSTOMER_ADD_MESSAGE + addObject.getFirstName() + " " + addObject.getLastName());
			} catch (SQLException e) {
				returnConnection(connection);
				throw new DBException(StringHelper.CUSTOMER_ADD_EXCEPTION + e);
			}
		}

	}

	@Override
	public void update(Customer updateObject) throws DBException, ThreadException, MisMatchObjectException {
		boolean fromUpdate = true;
		String sql = DBUtils.UPDATE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CustomerUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CustomerUtil.UPDATE_PARAMETER);
		if (StringHelper.allParametersNotEmpty(updateObject, fromUpdate)) {

			Connection connection = ConnectionPool.getInstance().getConnection();
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, updateObject.getFirstName());
				statement.setString(2, updateObject.getLastName());
				statement.setString(3, updateObject.getEmail());
				statement.setString(4, updateObject.getPassword());
				statement.setInt(5, updateObject.getId());
				int result = statement.executeUpdate();
				returnConnection(connection);
				if (result > 0) {
					System.out.println(StringHelper.CUSTOMER_UPDATE_MESSAGE + updateObject.getFirstName() + " "
							+ updateObject.getLastName());
				} else {
					System.out.println(StringHelper.CUSTOMER_UPDATE_FAILED_MESSAGE + updateObject.getFirstName() + " "
							+ updateObject.getLastName());
				}
			} catch (SQLException e) {
				returnConnection(connection);
				throw new DBException(StringHelper.CUSTOMER_UPDATE_EXCEPTION + e);
			}
		}

	}

	// update company validator
//	public boolean isValid(Customer customer, boolean fromUpdate) throws ThreadException, DBException {
//		// check all customer field not empty or null
//		System.out.println("\r\nstart valiadtion: ");
//		if (StringHelper.allParametersNotEmpty(customer, fromUpdate)) {
//			if (!fromUpdate) {
//				if (!isExists(customer.getEmail())) {
//					System.out.println("validation ok");
//					return true;
//				}
//			}
//			Customer existsCustomer = get(customer.getId());
//			// check for other customers (with different ID)and the same email
//			if (existsCustomer != null && !isOtherExists(customer.getId(), customer.getEmail())) {
//				System.out.println("validation ok");
//				return true;
//			}
//
//		} else {
//			System.out.println("validation: some parameters is null or empty");
//		}
//		throw new DBException("Customer is not valid");
//
//	}

	// check for other customer (with different ID)and the same email
//	public boolean isOtherExists(int id, String email) throws ThreadException, DBException {
//		String sql = DBUtils.IS_OTHER_CUSTOMER_EXISTS_QUERY;
//		System.out.println("IsOtherExists query: " + sql);
//		Connection connection = ConnectionPool.getInstance().getConnection();
//
//		try (PreparedStatement statement = connection.prepareStatement(sql)) {
//			statement.setString(1, email);
//			statement.setInt(2, id);
//			ResultSet resultSet = statement.executeQuery();
//			returnConnection(connection);
//			if (resultSet != null) {
//				System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
//				if (resultSet.next()) {
//					if (resultSet.getInt(1) > 0) {
//						System.out.println("IsOtherCustomer exists with id " + id + ", email " + email);
//						return true;
//					}
//					System.out.println("IsOtherCustomer not exists with id " + id + ", email " + email);
//					return false;
//
//				}
//			} else {
//				returnConnection(connection);
//				throw new DBException(StringHelper.RESULT_SET_ISNULL_MESSAGE);
//			}
//		} catch (SQLException e) {
//			throw new DBException("IsOtherCustomer exists Exception" + e.getCause());
//		}
//		throw new DBException("IsOtherCustomer exists Exception");
//	}

	@Override
	public void delete(int id) throws DBException, ThreadException {
		String sql = DBUtils.DELETE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CustomerUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CustomerUtil.PARAMETER_ID);
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			System.out.println("delete sql: " + statement);
			int result = statement.executeUpdate();
			returnConnection(connection);
			if (result > 0) {
				System.out.println(StringHelper.CUSTOMER_DELETE_MESSAGE);
			} else {
				System.out.println(StringHelper.CUSTOMER_DELETE_FAILED_MESSAGE);
			}
		} catch (SQLException e) {
			returnConnection(connection);
			throw new DBException(StringHelper.CUSTOMER_DELETE_EXCEPTION + e);
		}

	}

	@Override
	public Customer get(int id) throws DBException, ThreadException {
		Customer customer = null;
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CustomerUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CustomerUtil.PARAMETER_ID);
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			returnConnection(connection);
			if (resultSet != null) {
				if (resultSet.next()) {
					customer = resultSetToCustomer(resultSet);
				}
			} else {

				System.out.println(StringHelper.RESULT_SET_ISNULL_MESSAGE);
			}
		} catch (SQLException e) {
			returnConnection(connection);
			throw new DBException(StringHelper.CUSTOMER_GET_EXCEPTION + e);
		}
		return customer;
	}

	@Override
	public int getCount(String sql) throws DBException, ThreadException {
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			System.out.println("getCount sql: " + statement);
			ResultSet resultSet = statement.executeQuery();
			returnConnection(connection);
			System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			throw new DBException(StringHelper.COMPANY_EXCEPTION + e);
		}
		return 0;

	}

	private Customer resultSetToCustomer(ResultSet resultSet) throws DBException {
		try {
			int id = resultSet.getInt(1);
			String firstName = resultSet.getString(2);
			String lastName = resultSet.getString(3);
			String email = resultSet.getString(4);
			String password = resultSet.getString(5);
			return new Customer(id, firstName, lastName, email, password);
		} catch (SQLException e) {
			throw new DBException(StringHelper.RESULTSET_EXCEPTION + e);

		}
	}

	@Override
	public List<Customer> get() throws DBException, ThreadException {
		List<Customer> customers = new ArrayList<Customer>();
		String sql = DBUtils.GET_ALL_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CustomerUtil.TABLE);
		System.out.println("get all Query: " + sql);
		Connection connection = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			ResultSet resultSet = statement.executeQuery();
			returnConnection(connection);
			if (resultSet != null) {
				System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
				while (resultSet.next()) {

					Customer customer = resultSetToCustomer(resultSet);
					customers.add(customer);
				}
			} else {
				System.out.println(StringHelper.RESULT_SET_ISNULL_MESSAGE);
			}
		} catch (SQLException e) {
			returnConnection(connection);
			throw new DBException(StringHelper.CUSTOMER_GET_EXCEPTION + e);
		}
		return customers;

	}

	@Override
	public List<Customer> get(String sql) throws DBException, ThreadException {
		List<Customer> customers = new ArrayList<Customer>();
		System.out.println("get all Query: " + sql);
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			ResultSet resultSet = statement.executeQuery();
			returnConnection(connection);
			if (resultSet != null) {
				System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
				while (resultSet.next()) {
					Customer customer = resultSetToCustomer(resultSet);
					customers.add(customer);
				}
			} else {
				System.out.println(StringHelper.RESULT_SET_ISNULL_MESSAGE);
			}
		} catch (SQLException e) {
			returnConnection(connection);
			throw new DBException(StringHelper.CUSTOMER_GET_EXCEPTION + e);
		}
		return customers;
	}

	@Override
	public void returnConnection(Connection connection) throws DBException {
		if (connection != null) {
			ConnectionPool.getInstance().returnConnection(connection);
			connection = null;
		}
	}

	@Override
	public boolean isExists(String email) throws ThreadException, DBException {

		String sql = DBUtils.IS_CUSTOMER_EXISTS_QUERY;
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, email);
			ResultSet resultSet = statement.executeQuery();
			returnConnection(connection);
			if (resultSet != null) {
				// System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
				if (resultSet.next()) {
					if (resultSet.getInt(1) > 0) {
						System.out.println(StringHelper.CUSTOMER_EXISTS_MESSAGE + email);
						return true;
					}
					System.out.println(StringHelper.CUSTOMER_EXISTS_FALSE_MESSAGE + email);
					return false;

				}
			} else {

				throw new DBException(StringHelper.RESULT_SET_ISNULL_MESSAGE);
			}
		} catch (SQLException e) {
			returnConnection(connection);
			// have to do customer Exception...
			throw new DBException(StringHelper.CUSTOMER_GET_EXCEPTION + e);
		}
		throw new DBException(StringHelper.CUSTOMER_EXISTS_EXCEPTION);

	}

}
