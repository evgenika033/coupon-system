package facades;

import java.util.List;

import javax.security.auth.login.LoginException;

import beans.Category;
import beans.Coupon;
import beans.Customer;
import exception.DBException;
import exception.ThreadException;

public class CustomerFacade extends ClientFacade {

	private int customerID;

	public CustomerFacade() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean login(String email, String password) throws ThreadException, DBException, LoginException {
		Customer customer = customerDao.login(email, password);
		if (customer != null) {
			customerID = customer.getId();
			return true;
		}
		throw new LoginException("Login failed ");
	}

	public void purchaseCoupon(Coupon coupon) {

	}

	public List<Coupon> getCustomerCoupons() {
		return null;

	}

	public List<Coupon> getCustomerCoupons(Category category) {
		return null;

	}

	public List<Coupon> getCustomerCoupons(double maxPrice) {
		return null;

	}

	public Customer getCustomerDetails() throws ThreadException, DBException {
		return customerDao.get(customerID);

	}

}
