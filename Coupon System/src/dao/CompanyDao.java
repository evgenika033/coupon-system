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

	}

}
