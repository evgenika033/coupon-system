package facades;

import java.sql.SQLException;
import java.util.List;

import javax.security.auth.login.LoginException;

import beans.Category;
import beans.Coupon;
import beans.Customer;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;
import utils.StringHelper;

public class CustomerFacade extends ClientFacade {

	private int customerID;

	public CustomerFacade() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean login(String email, String password)
			throws ThreadException, DBException, LoginException, SQLException {
		Customer customer = customerDao.login(email, password);
		if (customer != null) {
			customerID = customer.getId();
			return true;
		}
		throw new LoginException(StringHelper.LOGIN_EXCEPTION);
	}

	public void purchaseCoupon(Coupon coupon) throws ThreadException, DBException, MisMatchObjectException {
		boolean fromUpdate = true;
		if (StringHelper.allParametersNotEmpty(coupon, fromUpdate)) {
			couponDao.addCouponPurchase(customerID, coupon.getId());
		}

	}

	public List<Coupon> getCustomerCoupons()
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		return couponDao.getCustomerCoupons(customerID);
	}

	public List<Coupon> getCustomerCoupons(Category category)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		return couponDao.getCustomerCoupons(customerID, category);

	}

	public List<Coupon> getCustomerCoupons(double maxPrice)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		return couponDao.getCustomerCoupons(customerID, maxPrice);
	}

	public Customer getCustomerDetails() throws ThreadException, DBException, SQLException, MisMatchObjectException {
		Customer customer = customerDao.get(customerID);
		if (customer != null) {
			customer.setCoupons(getCustomerCoupons());
		}
		return customer;

	}

}
