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

	public List<Company> getAllCompanies() throws DBException, ThreadException, SQLException, MisMatchObjectException {
		List<Company> companies = companyDao.get();
		for (Company company : companies) {
			System.out.println("get company coupons");
			List<Coupon> coupons = couponDao.getByCompany(company.getId());
			company.setCoupons(coupons);
		}
		return companies;
	}

	public Company getOneCompany(int companyID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		Company company = companyDao.get(companyID);
		company.setCoupons(couponDao.getByCompany(companyID));
		return company;
	}

	public void addCustomer(Customer customer)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		boolean fromUpdate = false;
		if (isCustomerValid(customer, fromUpdate)) {
			customerDao.add(customer);
		}
	}

	public void updateCustomer(Customer customer)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		boolean fromUpdate = true;
		if (isCustomerValid(customer, fromUpdate)) {
			customerDao.update(customer);
		}
	}

	public void deleteCustomer(int customerID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		System.out.println("get customer coupons");
		List<Coupon> coupons = couponDao.getCustomerCoupons(customerID);
		System.out.println("coupons amount before delete: " + coupons.size());
		for (Coupon coupon : coupons) {
			System.out.println("get coupon purcheses");
			List<Integer> result = couponDao.getCouponPurchaseByCustomer(customerID);
			System.out.println("customer_VS_coupon amount before delete: " + result.size());
			for (int couponID : result) {
				System.out.println("customer_VS_coupon will be delete: " + customerID + ", " + coupon.getId());
				couponDao.deleteCouponPurchase(couponID, coupon.getId());
			}
			System.out.println("coupon will be delete: " + coupon.getTitle());
			couponDao.delete(coupon.getId());

		}
		System.out.println("customer will be delete: " + customerID);
		customerDao.delete(customerID);
	}

	public List<Customer> getAllCustomers() throws DBException, ThreadException, SQLException, MisMatchObjectException {
		List<Customer> customers = customerDao.get();
		for (Customer customer : customers) {
			System.out.println("get customers coupons");
			List<Coupon> coupons = couponDao.getCustomerCoupons(customer.getId());
			customer.setCoupons(coupons);
		}
		return customers;

	}

	public Customer getOneCustomer(int customerID)
			throws DBException, ThreadException, SQLException, MisMatchObjectException {
		Customer customer = customerDao.get(customerID);
		customer.setCoupons(couponDao.getCustomerCoupons(customerID));
		return customer;
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

	// update company validator
	public boolean isCustomerValid(Customer customer, boolean fromUpdate)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		// check all customer field not empty or null
		System.out.println("\r\nstart valiadtion: ");
		if (StringHelper.allParametersNotEmpty(customer, fromUpdate)) {
			if (!fromUpdate) {
				if (!customerDao.isExists(customer.getEmail())) {
					System.out.println("validation ok");
					return true;
				}
			}
			Customer existsCustomer = getOneCustomer(customer.getId());
			// check for other customers (with different ID)and the same email
			if (existsCustomer != null && !customerDao.isOtherExists(customer.getId(), customer.getEmail())) {
				System.out.println("validation ok");
				return true;
			}

		} else {
			System.out.println("validation: some parameters is null or empty");
		}
		throw new DBException("Customer is not valid");

	}

}
