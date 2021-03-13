package facades;

import java.sql.SQLException;
import java.util.List;

import beans.Company;
import beans.Coupon;
import beans.Customer;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;
import utils.StringHelper;

public class AdminFacade extends ClientFacade {
	private final String email = "admin@admin.com";
	private final String password = "admin";

	public AdminFacade() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean login(String email, String password) {
		return email == this.email && password == this.password;
	}

	public void addCompany(Company company) throws ThreadException, DBException, MisMatchObjectException, SQLException {
		boolean fromUpdate = false;
		if (isCompanyValid(company, fromUpdate)) {
			companyDao.add(company);
		}

	}

	public void updateCompany(Company company)
			throws ThreadException, DBException, MisMatchObjectException, SQLException {
		boolean fromUpdate = true;
		if (isCompanyValid(company, fromUpdate)) {
			companyDao.update(company);
		}

	}

	// Cascade delete
	public void deleteCompany(int companyID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		System.out.println("get company coupons");
		List<Coupon> coupons = couponDao.getByCompany(companyID);
		System.out.println("coupons amount before delete: " + coupons.size());
		for (Coupon coupon : coupons) {
			System.out.println("get coupon purcheses");
			List<Integer> result = couponDao.getCouponPurchaseByCoupon(coupon.getId());
			System.out.println("customer_VS_coupon amount before delete: " + result.size());
			for (int customerID : result) {
				System.out.println("customer_VS_coupon will be delete: " + customerID + ", " + coupon.getId());
				couponDao.deleteCouponPurchase(customerID, coupon.getId());
			}
			System.out.println("coupon will be delete: " + coupon.getTitle());
			couponDao.delete(coupon.getId());

		}
		System.out.println("company will be delete: " + companyID);
		companyDao.delete(companyID);
	}

	public List<Company> getAllCompanies() {
		return null;

	}

	public Company getOneCompany(int companyID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		Company company = companyDao.get(companyID);
		company.setCoupons(couponDao.getByCompany(companyID));
		return company;
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
