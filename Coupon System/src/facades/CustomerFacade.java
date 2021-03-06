package facades;

import java.util.List;

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
	public boolean login(String email, String password) {
		// TODO Auto-generated method stub
		return false;
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
