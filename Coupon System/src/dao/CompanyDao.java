package dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.Company;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;
import utils.CompanyUtil;
import utils.CustomerUtil;
import utils.DBUtils;
import utils.StringHelper;

public class CompanyDao implements ICompanyDao {

	public CompanyDao() {

	}

	@Override
	public void add(Company addObject) throws ThreadException, DBException, MisMatchObjectException {
		boolean fromUpdate = false;
		if (StringHelper.allParametersNotEmpty(addObject, fromUpdate)) {
			String sql = DBUtils.ADD_COMPANY_QUERY;
			Map<Integer, Object> parameters = new HashMap<Integer, Object>();
			parameters.put(1, addObject.getName());
			parameters.put(2, addObject.getEmail());
			parameters.put(3, addObject.getPassword());
			CompanyUtil.execute(sql, parameters);
			System.out.println("The company added: " + addObject.getName());

		} else {
			throw new DBException(StringHelper.VALIDATION_PARAMETERS_ERROR_MESSAGE);
		}

	}

	@Override
	public void update(Company updateObject) throws ThreadException, DBException, MisMatchObjectException {
		boolean fromUpdate = true;
		if (StringHelper.allParametersNotEmpty(updateObject, fromUpdate)) {
			String sql = DBUtils.UPDATE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CompanyUtil.TABLE)
					.replace(DBUtils.PARAMETER_PLACE_HOLDER, CompanyUtil.UPDATE_EMAIL_PASSWORD_PARAMETER);
			Map<Integer, Object> parameters = new HashMap<Integer, Object>();
			parameters.put(1, updateObject.getName());
			parameters.put(2, updateObject.getEmail());
			parameters.put(3, updateObject.getPassword());
			parameters.put(4, updateObject.getId());
			int result = CustomerUtil.executeUpdate(sql, parameters);
			if (result > 0) {
				System.out.println("Update company success: " + updateObject.getName());
			} else {
				System.out.println(StringHelper.COMPANY_UPDATE_FAILED_MESSAGE + updateObject.getName());

			}

		} else {
			throw new DBException(StringHelper.VALIDATION_PARAMETERS_ERROR_MESSAGE);
		}
	}

	@Override
	public Company get(int id) throws ThreadException, DBException, SQLException {
		Company company = null;
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CompanyUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CompanyUtil.PARAMETER_ID);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, id);
		List<Company> companies = CompanyUtil.executeQuery(sql, parameters);
		return companies.size() > 0 ? companies.get(0) : company;

	}

	@Override
	public List<Company> get() throws DBException, ThreadException, SQLException {
		String sql = DBUtils.GET_ALL_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CompanyUtil.TABLE);
		List<Company> companies = CompanyUtil.executeQuery(sql);
		return companies;

	}

//	public List<Company> get(String sql) throws DBException, ThreadException {
//		List<Company> companies = new ArrayList<Company>();
//		Connection connection = ConnectionPool.getInstance().getConnection();
//		try (PreparedStatement statement = connection.prepareStatement(sql)) {
//			System.out.println("get sql: " + statement);
//			ResultSet resultSet = statement.executeQuery();
//			DBUtils.returnConnection(connection);
//			if (resultSet != null) {
//				System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
//				while (resultSet.next()) {
//					Company company = CompanyUtil.resultSetToCompany(resultSet);
//					companies.add(company);
//				}
//			} else {
//				System.out.println(StringHelper.RESULT_SET_ISNULL_MESSAGE);
//			}
//		} catch (SQLException e) {
//			DBUtils.returnConnection(connection);
//			throw new DBException(StringHelper.COMPANY_EXCEPTION + e.getCause());
//		}
//
//		return companies;
//	}

//	public int getCount(Company company) throws DBException, ThreadException {
//		Connection connection = ConnectionPool.getInstance().getConnection();
//
//		try (PreparedStatement statement = connection.prepareStatement(sql)) {
//			System.out.println("getCount sql: " + statement);
//			ResultSet resultSet = statement.executeQuery();
//			DBUtils.returnConnection(connection);
//			System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
//			if (resultSet.next()) {
//				return resultSet.getInt(1);
//			}
//
//		} catch (SQLException e) {
//			DBUtils.returnConnection(connection);
//			throw new DBException(StringHelper.COMPANY_EXCEPTION + e);
//		}
//		return 0;
//
//	}

	@Override
	public void delete(int id) throws DBException, ThreadException {
		String sql = DBUtils.DELETE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CompanyUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CompanyUtil.PARAMETER_ID);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, id);
		int result = CompanyUtil.executeUpdate(sql, parameters);
		if (result > 0) {
			System.out.println("The company deleted successfully");
		} else {
			System.out.println("The company delete failed.");
		}
	}

	@Override
	public boolean isExists(String name, String email) throws ThreadException, DBException, SQLException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CompanyUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CompanyUtil.PARAMETER_NAME_EMAIL);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, name);
		parameters.put(2, email);
		List<Company> companies = CompanyUtil.executeQuery(sql, parameters);
		if (companies.size() > 0) {
			System.out.println("IsCompany exists with name " + name + ", email " + email);
			return true;
		} else {
			System.out.println("IsCompany not exists with name " + name + ", email " + email);
			return false;
		}
	}

	@Override
	public Company login(String email, String password) throws ThreadException, DBException, SQLException {
		Company company = null;
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CompanyUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, DBUtils.GET_EMAIL_PASSWORD_PARAMETER);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, email);
		parameters.put(2, password);
		List<Company> companies = CompanyUtil.executeQuery(sql, parameters);
		return companies.size() > 0 ? companies.get(0) : company;

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
	public boolean isOtherExists(int id, String email) throws ThreadException, DBException, SQLException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CompanyUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CompanyUtil.PARAMETER_EMAIL_AND_NOT_ID);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, email);
		parameters.put(2, id);
		List<Company> companies = CompanyUtil.executeQuery(sql, parameters);
		if (companies.size() > 0) {
			System.out.println("IsOtherCompany exists with id " + id + ", email " + email);
			return true;
		} else {
			System.out.println("IsOtherCompany not exists with id " + id + ", email " + email);
			return false;
		}
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

	}

}
