package facades;

import java.sql.SQLException;
import java.util.List;

import beans.Company;
import beans.Customer;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;
import utils.DBUtils;
import utils.StringHelper;

public class AdminFacade extends ClientFacade {

	public AdminFacade() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean login(String email, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	public void addCompany(Company company) {

	}

	public void updateCompany(Company company) {

	}

	public void deleteCompany(int companyID) {

	}

	public List<Company> getAllCompanies() {
		return null;

	}

	public Company getOneCompany(int companyID) {
		return null;

	}

	public void addCustomer(Customer customer) {

	}

	public void updateCustomer(Customer customer) {

	}

	public void deleteCustomer(int customerID) {

	}

	public List<Customer> getAllCustomers() {
		return null;

	}

	public Customer getOneCustomer(int customerID) {
		return null;

	}

	public boolean isCompanyValid(Company company, boolean fromUpdate)
			throws ThreadException, DBException, MisMatchObjectException, SQLException {
		// check all company field not empty or null
		System.out.println("\r\nstart valiadtion: ");
		if (StringHelper.allParametersNotEmpty(company, fromUpdate)) {
			if (!fromUpdate) {
				if (!companyDao.isExists(company.getName(), company.getEmail())) {
					System.out.println("validation ok");
					return true;
				}
			} else {
				Company existsCompany = companyDao.get(company.getId());
				// check for other company (with different ID)and the same email
				if (existsCompany != null && existsCompany.getName().equals(company.getName())) {
					String sql = ""// DBUtils.IS_OTHER_COMPANY_EXISTS_QUERY
							.replace(DBUtils.EMAIL_PLACE_HOLDER, existsCompany.getEmail())
							.replace(DBUtils.ID_PLACE_HOLDER, String.valueOf(existsCompany.getId()));
					if (!companyDao.isOtherExists(company.getId(), company.getEmail())) {
						System.out.println(StringHelper.VALIDATION_OK_MESSAGE);
						return true;
					}
				}
			}
		} else {
			System.out.println(StringHelper.VALIDATION_PARAMETERS_ERROR_MESSAGE);
		}
		throw new DBException(StringHelper.COMPANY_VALIDATION_EXCEPTION);

	}

}
