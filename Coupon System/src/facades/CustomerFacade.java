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

	/**
	 * empty ctor
	 */
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

	/**
	 * Buying coupon
	 * 
	 * @param coupon
	 * @throws ThreadException
	 * @throws DBException
	 * @throws MisMatchObjectException
	 * @throws SQLException
	 */
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

	/**
	 * get all customer's coupons
	 * 
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	public List<Coupon> getCustomerCoupons()
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		return couponDao.getCustomerCoupons(customerID);
	}

	/**
	 * get all customer's coupons filtered by category
	 * 
	 * @param category
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	public List<Coupon> getCustomerCoupons(Category category)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		return couponDao.getCustomerCoupons(customerID, category);

	}

	/**
	 * get all customer's coupons filtered by maxPrice
	 * 
	 * @param maxPrice
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	public List<Coupon> getCustomerCoupons(double maxPrice)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		return couponDao.getCustomerCoupons(customerID, maxPrice);
	}

	/**
	 * get Customer Details with coupons
	 * 
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	public Customer getCustomerDetails() throws ThreadException, DBException, SQLException, MisMatchObjectException {
		Customer customer = customerDao.get(customerID);
		if (customer != null) {
			customer.setCoupons(getCustomerCoupons());
		}
		return customer;

	}

}
