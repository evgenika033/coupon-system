package facades;

import java.util.List;

import javax.security.auth.login.LoginException;

import beans.Category;
import beans.Company;
import beans.Coupon;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;
import utils.CouponUtil;
import utils.DBUtils;
import utils.StringHelper;

public class CompanyFacade extends ClientFacade {
	private int companyID;

	public CompanyFacade() {

	}

	@Override
	public boolean login(String email, String password) throws ThreadException, DBException, LoginException {
		Company company = companyDao.login(email, password);
		if (company != null) {
			companyID = company.getId();
			return true;
		}
		throw new LoginException("Login failed ");
	}

	public void addCoupon(Coupon coupon) throws DBException, ThreadException, MisMatchObjectException {
		boolean fromUpdate = false;// the flag that means coupon (is a Flag that means "i from update" in
									// add=false);
		if (isValid(coupon, fromUpdate)) {
			couponDao.add(coupon);
		}
	}

	public void updateCoupon(Coupon coupon) throws DBException, ThreadException, MisMatchObjectException {
		boolean fromUpdate = true;
		if (isValid(coupon, fromUpdate)) {
			couponDao.update(coupon);

		}
	}

	public void deleteCoupon(int couponID) throws DBException, ThreadException {
		couponDao.delete(couponID);
	}

	public List<Coupon> getCompanyCoupons() throws DBException, ThreadException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.GET_BY_COMPANY_ID)
				.replace(DBUtils.COMPANY_ID_PLACE_HOLDER, String.valueOf(companyID));
		return couponDao.get(sql);
	}

	public List<Coupon> getCompanyCoupons(Category category) {
		return null;
		// return couponDao.get(category);

	}

	public List<Coupon> getCompanyCoupons(double maxPrice) {
		return null;

	}

	public Company getCompanyDetails() throws ThreadException, DBException {
		return companyDao.get(companyID);

	}

	public boolean isValid(Coupon coupon, boolean fromUpdate)
			throws DBException, ThreadException, MisMatchObjectException {
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
				String sql = DBUtils.IS_OTHER_COUPON_EXISTS_QUERY.replace(DBUtils.TITLE_PLACE_HOLDER, coupon.getTitle())
						.replace(DBUtils.ID_PLACE_HOLDER, String.valueOf(coupon.getId()))
						.replace(DBUtils.COMPANY_ID_PLACE_HOLDER, String.valueOf(coupon.getCompanyID()));

				if (couponDao.getCount(sql) == 0) {
					System.out.println("validation ok");
					return true;
				}
			}
		} else {
			System.out.println("validation: some parameters is null or empty");
		}

		throw new DBException("validation: coupon is not valid");
	}

//	public boolean isValid(Company company, boolean fromUpdate) throws ThreadException, DBException, MisMatchObjectException {
//		// check all company field not empty or null
//		System.out.println("\r\nstart valiadtion: ");
//		if (StringHelper.allParametersNotEmpty(company, fromUpdate)) {
//			if (!fromUpdate) {
//				if (!companyDao.isExists(company.getName(), company.getEmail())) {
//					System.out.println("validation ok");
//					return true;
//				}
//			} else {
//				Company existsCompany = getCompanyDetails();
//				// check for other company (with different ID)and the same email
//				if (existsCompany != null && existsCompany.getName().equals(company.getName())) {
//					String sql = DBUtils.IS_OTHER_COMPANY_EXISTS_QUERY
//							.replace(DBUtils.EMAIL_PLACE_HOLDER, existsCompany.getEmail())
//							.replace(DBUtils.ID_PLACE_HOLDER, String.valueOf(existsCompany.getId()));
//					if (companyDao.getCount(sql) == 0) {
//						System.out.println(StringHelper.VALIDATION_OK_MESSAGE);
//						return true;
//					}
//				}
//			}
//		} else {
//			System.out.println(StringHelper.VALIDATION_PARAMETERS_ERROR_MESSAGE);
//		}
//		throw new DBException(StringHelper.COMPANY_VALIDATION_EXCEPTION);
//
//	}

}
