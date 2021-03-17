package facades;

import java.sql.SQLException;
import java.time.LocalDate;
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

	public void purchaseCoupon(Coupon coupon)
			throws ThreadException, DBException, MisMatchObjectException, SQLException {
		boolean fromUpdate = true;
		if (StringHelper.allParametersNotEmpty(coupon, fromUpdate) && validateBeforePurchase(coupon)) {
			couponDao.addCouponPurchase(customerID, coupon.getId());
			coupon.setAmount(coupon.getAmount() - 1);
			couponDao.update(coupon);
			System.out.println("coupon purchese is done: " + coupon.getTitle());
		} else {
			System.err.println("coupon purchese failed: " + coupon.getTitle());
		}

	}

	// business logic: validate coupon before purchase
	public boolean validateBeforePurchase(Coupon coupon)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
//		return couponDao.getCouponPurchase(customerID, coupon.getId()).size() == 0
//				&& coupon.getAmount() > 0 &&  
//				!coupon.getEndDate().isBefore(LocalDate.now());
		if (couponDao.getCouponPurchase(customerID, coupon.getId()).size() > 0) {
			return false;
		}
		if (coupon.getAmount() == 0) {
			return false;
		}
		if (coupon.getEndDate().isBefore(LocalDate.now())) {
			return false;
		}
		return true;
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
