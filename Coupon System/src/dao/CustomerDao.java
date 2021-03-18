package dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.Customer;
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
		// basic validation
		if (StringHelper.allParametersNotEmpty(addObject, fromUpdate)) {
			String sql = DBUtils.ADD_CUSTOMER_QUERY;
			Map<Integer, Object> parameters = new HashMap<Integer, Object>();
			parameters.put(1, addObject.getFirstName());
			parameters.put(2, addObject.getLastName());
			parameters.put(3, addObject.getEmail());
			parameters.put(4, addObject.getPassword());
			CustomerUtil.execute(sql, parameters);
			System.out.println(
					StringHelper.CUSTOMER_ADD_MESSAGE + addObject.getFirstName() + " " + addObject.getLastName());
		} else {
			throw new DBException(StringHelper.VALIDATION_PARAMETERS_ERROR_MESSAGE);
		}

	}

	@Override
	public void update(Customer updateObject) throws DBException, ThreadException, MisMatchObjectException {
		boolean fromUpdate = true;
		if (StringHelper.allParametersNotEmpty(updateObject, fromUpdate)) {
			String sql = DBUtils.UPDATE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CustomerUtil.TABLE)
					.replace(DBUtils.PARAMETER_PLACE_HOLDER, CustomerUtil.UPDATE_PARAMETER);
			Map<Integer, Object> parameters = new HashMap<Integer, Object>();
			parameters.put(1, updateObject.getFirstName());
			parameters.put(2, updateObject.getLastName());
			parameters.put(3, updateObject.getEmail());
			parameters.put(4, updateObject.getPassword());
			parameters.put(5, updateObject.getId());
			int result = CustomerUtil.executeUpdate(sql, parameters);
			if (result > 0) {
				System.out.println(StringHelper.CUSTOMER_UPDATE_MESSAGE + updateObject.getFirstName() + " "
						+ updateObject.getLastName());
			} else {
				System.out.println(StringHelper.CUSTOMER_UPDATE_FAILED_MESSAGE + updateObject.getFirstName() + " "
						+ updateObject.getLastName());
			}

		} else {
			throw new DBException(StringHelper.VALIDATION_PARAMETERS_ERROR_MESSAGE);
		}

	}

	// check for other customer (with different ID)and the same email
	public boolean isOtherExists(int id, String email) throws ThreadException, DBException, SQLException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CustomerUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CustomerUtil.PARAMETER_BY_EMAIL_AND_NOT_ID);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, email);
		parameters.put(2, id);
		List<Customer> customers = CustomerUtil.executeQuery(sql, parameters);
		if (customers.size() > 0) {
			System.out.println("IsOtherCustomer exists with id " + id + ", email " + email);
			return true;
		}
		System.out.println("IsOtherCustomer not exists with id " + id + ", email " + email);
		return false;

	}

	@Override
	public void delete(int id) throws DBException, ThreadException {
		String sql = DBUtils.DELETE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CustomerUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CustomerUtil.PARAMETER_ID);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, id);
		int result = CustomerUtil.executeUpdate(sql, parameters);
		if (result > 0) {
			System.out.println(StringHelper.CUSTOMER_DELETE_MESSAGE);
		} else {
			System.out.println(StringHelper.CUSTOMER_DELETE_FAILED_MESSAGE);
		}

	}

	@Override
	public Customer get(int id) throws DBException, ThreadException, SQLException {
		Customer customer = null;
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CustomerUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CustomerUtil.PARAMETER_ID);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, id);
		List<Customer> customers = CustomerUtil.executeQuery(sql, parameters);
		// function programming: return first element or null
		return customers.size() > 0 ? customers.get(0) : customer;

	}

	@Override
	public List<Customer> get() throws DBException, ThreadException, SQLException {
		String sql = DBUtils.GET_ALL_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CustomerUtil.TABLE);
		List<Customer> customers = CustomerUtil.executeQuery(sql);
		return customers;

	}

	@Override
	public boolean isExists(String email) throws ThreadException, DBException, SQLException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CustomerUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CustomerUtil.PARAMETER_EMAIL);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, email);
		List<Customer> customers = CustomerUtil.executeQuery(sql, parameters);
		if (customers.size() == 0) {
			return false;
		}
		throw new DBException(StringHelper.CUSTOMER_EXISTS_EXCEPTION);

	}

	@Override
	public Customer login(String email, String password) throws ThreadException, DBException, SQLException {
		Customer customer = null;
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CustomerUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, DBUtils.GET_EMAIL_PASSWORD_PARAMETER);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, email);
		parameters.put(2, password);
		List<Customer> customers = CustomerUtil.executeQuery(sql, parameters);
		return customers.size() > 0 ? customers.get(0) : customer;

	}
}
