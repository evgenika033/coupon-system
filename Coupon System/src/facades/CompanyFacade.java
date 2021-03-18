package facades;

import java.sql.SQLException;
import java.util.List;

import javax.security.auth.login.LoginException;

import beans.Category;
import beans.Company;
import beans.Coupon;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;
import utils.StringHelper;

public class CompanyFacade extends ClientFacade {
	private int companyID;

	public CompanyFacade() {

	}

	@Override
	public boolean login(String email, String password)
			throws ThreadException, DBException, LoginException, SQLException {
		Company company = companyDao.login(email, password);
		if (company != null) {
			companyID = company.getId();
			return true;
		}
		throw new LoginException("Login failed ");
	}

	public void addCoupon(Coupon coupon) throws DBException, ThreadException, MisMatchObjectException, SQLException {
		boolean fromUpdate = false;// the flag that means coupon (is a Flag that means "i from update" in
									// add=false);
		if (isValid(coupon, fromUpdate)) {
			couponDao.add(coupon);
			System.out.println("add coupon success");
		}
	}

	public void updateCoupon(Coupon coupon) throws DBException, ThreadException, MisMatchObjectException, SQLException {
		boolean fromUpdate = true;
		if (isValid(coupon, fromUpdate)) {
			couponDao.update(coupon);

		}
	}

	public void deleteCoupon(int couponID) throws DBException, ThreadException, SQLException, MisMatchObjectException {
		Coupon coupon = couponDao.get(couponID);
		// check if coupon is from current company
		if (isCompanyCoupon(coupon)) {
			List<Integer> customerIDList = couponDao.getCouponPurchaseByCoupon(couponID);
			for (int customerID : customerIDList) {
				couponDao.deleteCouponPurchase(customerID, couponID);
			}
			couponDao.delete(couponID);
		} else {
			System.err.println("coupon not for current login company. select other coupon");
		}
	}

	/**
	 * check if coupon is from current company
	 * 
	 * @param coupons
	 * @return
	 */
	private boolean isCompanyCoupon(Coupon coupon) {
		return coupon != null && coupon.getCompanyID() == companyID;
	}

	// all coupons of specific company
	public List<Coupon> getCompanyCoupons() throws DBException, ThreadException, SQLException, MisMatchObjectException {
		return couponDao.getByCompany(companyID);
	}

	public List<Coupon> getCompanyCoupons(Category category)
			throws DBException, ThreadException, SQLException, MisMatchObjectException {

		return couponDao.getByCategory(companyID, category);
	}

	public List<Coupon> getCompanyCoupons(double maxPrice)
			throws DBException, ThreadException, SQLException, MisMatchObjectException {
		return couponDao.get(companyID, maxPrice);
	}

	public Company getCompanyDetails() throws ThreadException, DBException, SQLException, MisMatchObjectException {
		Company company = companyDao.get(companyID);
		if (company != null) {
			company.setCoupons(getCompanyCoupons());
		}
		return company;

	}

	public boolean isValid(Coupon coupon, boolean fromUpdate)
			throws DBException, ThreadException, MisMatchObjectException, SQLException {
		System.out.println("\r\nstart validation:");
		if (StringHelper.allParametersNotEmpty(coupon, fromUpdate)) {
			// add validation
			if (!fromUpdate) {
				boolean isCouponExist = couponDao.isExist(coupon);
				if (!isCouponExist) {
					System.out.println("validation ok");
					return true;
				}
				// update validation
			} else {
				// validate coupon by id exist in DB and company id not changed
				Coupon dbCoupon = couponDao.get(coupon.getId());
				if (dbCoupon != null && dbCoupon.getCompanyID() == coupon.getCompanyID()) {
					if (!couponDao.isOtherExist(coupon.getTitle(), coupon.getCompanyID(), coupon.getId())) {
						System.out.println("validation ok");
						return true;
					}

				}

			}
		} else {
			System.out.println("validation: some parameters is null or empty");
		}

		throw new DBException("validation: coupon is not valid");
	}

}
