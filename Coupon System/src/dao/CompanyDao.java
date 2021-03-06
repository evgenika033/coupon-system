package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Company;
import connection.ConnectionPool;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;
import utils.CompanyUtil;
import utils.DBUtils;
import utils.StringHelper;

public class CompanyDao implements ICompanyDao {

	public CompanyDao() {

	}

	@Override
	public void add(Company addObject) throws ThreadException, DBException, MisMatchObjectException {
		String sql = DBUtils.ADD_COMPANY_QUERY;
		boolean fromUpdate = false;
		if (StringHelper.allParametersNotEmpty(addObject, fromUpdate)) {

			// Company not exists
			Connection connection = ConnectionPool.getInstance().getConnection();
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				// "INSERT INTO `company` (`name`, `email`, `password`) VALUES (?,?,?)";
				statement.setString(1, addObject.getName());
				statement.setString(2, addObject.getEmail());
				statement.setString(3, addObject.getPassword());
				System.out.println("add sql: " + statement);
				statement.execute();
				returnConnection(connection);

				System.out.println("The company added: " + addObject.getName());
				// TODO Jeka need continue o add

			} catch (SQLException e) {
				returnConnection(connection);
				throw new DBException(StringHelper.COMPANY_EXCEPTION + e.getCause());
			}
		}

	}

	@Override
	public void update(Company updateObject) throws ThreadException, DBException, MisMatchObjectException {
		boolean fromUpdate = true;
		String sql = DBUtils.UPDATE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CompanyUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CompanyUtil.UPDATE_EMAIL_PASSWORD_PARAMETER);
		if (StringHelper.allParametersNotEmpty(updateObject, fromUpdate)) {

			Connection connection = ConnectionPool.getInstance().getConnection();
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, updateObject.getEmail());
				statement.setString(2, updateObject.getPassword());
				statement.setInt(3, updateObject.getId());
				System.out.println("update sql: " + statement);
				int result = statement.executeUpdate();
				returnConnection(connection);
				if (result > 0) {
					System.out.println("Update company success: " + updateObject.getName());
				}
			} catch (SQLException e) {
				returnConnection(connection);
				throw new DBException(StringHelper.COMPANY_EXCEPTION + e.getCause());
			}
		}
	}

	@Override
	public Company get(int id) throws ThreadException, DBException {
		Company company = null;
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CompanyUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CompanyUtil.PARAMETER_ID);
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			System.out.println("get sql: " + statement);
			ResultSet resultSet = statement.executeQuery();
			returnConnection(connection);
			if (resultSet != null) {
				// System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
				if (resultSet.next()) {
					company = resultSetToCompany(resultSet);

				}
			} else {

				System.out.println(StringHelper.RESULT_SET_ISNULL_MESSAGE);
			}
		} catch (SQLException e) {
			returnConnection(connection);
			throw new DBException(StringHelper.COMPANY_EXCEPTION + e.getCause());
		}
		return company;
	}

	@Override
	public List<Company> get() throws DBException, ThreadException {
		List<Company> companies = new ArrayList<Company>();
		String sql = DBUtils.GET_ALL_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CompanyUtil.TABLE);
		Connection connection = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			System.out.println("get sql: " + statement);
			ResultSet resultSet = statement.executeQuery();
			returnConnection(connection);
			if (resultSet != null) {
				System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
				while (resultSet.next()) {

					Company company = resultSetToCompany(resultSet);
					companies.add(company);
				}
			} else {
				System.out.println(StringHelper.RESULT_SET_ISNULL_MESSAGE);
			}
		} catch (SQLException e) {
			returnConnection(connection);
			throw new DBException(StringHelper.COMPANY_EXCEPTION + e.getCause());
		}

		return companies;

	}

	@Override
	public List<Company> get(String sql) throws DBException, ThreadException {
		List<Company> companies = new ArrayList<Company>();
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			System.out.println("get sql: " + statement);
			ResultSet resultSet = statement.executeQuery();
			returnConnection(connection);
			if (resultSet != null) {
				System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
				while (resultSet.next()) {
					Company company = resultSetToCompany(resultSet);
					companies.add(company);
				}
			} else {
				System.out.println(StringHelper.RESULT_SET_ISNULL_MESSAGE);
			}
		} catch (SQLException e) {
			returnConnection(connection);
			throw new DBException(StringHelper.COMPANY_EXCEPTION + e.getCause());
		}

		return companies;
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

	@Override
	public void delete(int id) throws DBException, ThreadException {
		String sql = DBUtils.DELETE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CompanyUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CompanyUtil.PARAMETER_ID);
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			System.out.println("delete sql: " + statement);
			int result = statement.executeUpdate();
			returnConnection(connection);
			if (result > 0) {
				System.out.println("The company deleted successfully");
			} else {
				System.out.println("The company delete failed.");
			}
		} catch (SQLException e) {
			returnConnection(connection);
			throw new DBException(StringHelper.COMPANY_EXCEPTION + e.getCause());
		}

	}

	private Company resultSetToCompany(ResultSet resultSet) throws DBException {

		try {
			int id = resultSet.getInt(1);
			String name = resultSet.getString(2);
			String email = resultSet.getString(3);
			String password = resultSet.getString(4);
			return new Company(id, name, email, password);
		} catch (SQLException e) {
			throw new DBException("Read resultSet Exception. " + e);

		}

	}

	@Override
	public boolean isExists(String name, String email) throws ThreadException, DBException {
		String sql = DBUtils.IS_COMPANY_EXISTS_QUERY;
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, name);
			statement.setString(2, email);
			System.out.println("isExist sql: " + statement);
			ResultSet resultSet = statement.executeQuery();
			returnConnection(connection);
			if (resultSet != null) {
				// System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
				if (resultSet.next()) {
					if (resultSet.getInt(1) > 0) {
						System.out.println("IsCompany exists with name " + name + ", email " + email);
						return true;
					}
					System.out.println("IsCompany not exists with name " + name + ", email " + email);
					return false;

				}
			} else {
				returnConnection(connection);
				throw new DBException(StringHelper.RESULT_SET_ISNULL_MESSAGE);
			}
		} catch (SQLException e) {
			throw new DBException(StringHelper.COMPANY_EXCEPTION + e.getCause());
		}
		throw new DBException("IsCompany exists Exception");

	}

	@Override
	public void returnConnection(Connection connection) throws DBException {
		if (connection != null) {
			ConnectionPool.getInstance().returnConnection(connection);
			connection = null;
		}

	}

	@Override
	public Company login(String email, String password) throws ThreadException, DBException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CompanyUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CompanyUtil.GET_EMAIL_PASSWORD_PARAMETER);
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, email);
			statement.setString(2, password);
			System.out.println("login sql: " + statement);
			ResultSet resultSet = statement.executeQuery();
			returnConnection(connection);
			if (resultSet != null) {
				// System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
				if (resultSet.next()) {
					return resultSetToCompany(resultSet);

				}
			} else {
				returnConnection(connection);
				throw new DBException(StringHelper.RESULT_SET_ISNULL_MESSAGE);
			}
		} catch (SQLException e) {
			throw new DBException(StringHelper.COMPANY_EXCEPTION + e.getCause());
		}

		return null;
	}

	// update company validator
//	public boolean isValid(Company company, boolean fromUpdate) throws ThreadException, DBException {
//		// check all company field not empty or null
//		System.out.println("\r\nstart valiadtion: ");
//		if (CompanyUtil.allParametersNotEmpty(company, fromUpdate)) {
//			if (!fromUpdate) {
//				if (!isExists(company.getName(), company.getEmail())) {
//					System.out.println("validation ok");
//					return true;
//				}
//			} else {
//				Company existsCompany = get(company.getId());
//				// check for other company (with different ID)and the same email
//				if (existsCompany != null && existsCompany.getName().equals(company.getName())
//						&& !isOtherExists(company.getId(), company.getEmail())) {
//					System.out.println("validation ok");
//					return true;
//				}
//			}
//
//		} else {
//			System.out.println("validation: some parameters is null or empty");
//		}
//		throw new DBException("Company is not valid");
//
//	}

// check for other company (with different ID)and the same email
//public boolean isOtherExists(int id, String email) throws ThreadException, DBException {
//	String sql = DBUtils.IS_OTHER_COMPANY_EXISTS_QUERY;
//	Connection connection = ConnectionPool.getInstance().getConnection();
//
//	try (PreparedStatement statement = connection.prepareStatement(sql)) {
//		statement.setString(1, email);
//		statement.setInt(2, id);
//		System.out.println("isOtherExist sql: " + statement);
//		ResultSet resultSet = statement.executeQuery();
//		returnConnection(connection);
//		if (resultSet != null) {
//			System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
//			if (resultSet.next()) {
//				if (resultSet.getInt(1) > 0) {
//					System.out.println("IsOtherCompany exists with id " + id + ", email " + email);
//					return true;
//				}
//				System.out.println("IsOtherCompany not exists with id " + id + ", email " + email);
//				return false;
//
//			}
//		} else {
//			returnConnection(connection);
//			throw new DBException(StringHelper.RESULT_SET_ISNULL_MESSAGE);
//		}
//	} catch (SQLException e) {
//		throw new DBException(StringHelper.COMPANY_EXCEPTION + e.getCause());
//	}
//	throw new DBException("IsOtherCompany exists Exception");
//
//}

}
