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
		}
	}

	public void updateCoupon(Coupon coupon) throws DBException, ThreadException, MisMatchObjectException, SQLException {
		boolean fromUpdate = true;
		if (isValid(coupon, fromUpdate)) {
			couponDao.update(coupon);

		}
	}

	public void deleteCoupon(int couponID) throws DBException, ThreadException {
		couponDao.delete(couponID);
	}

	// all coupons of specific company
	public List<Coupon> getCompanyCoupons() throws DBException, ThreadException, SQLException, MisMatchObjectException {
		return couponDao.getByCompany(companyID);
	}

	public List<Coupon> getCompanyCoupons(Category category)
			throws DBException, ThreadException, SQLException, MisMatchObjectException {

		return couponDao.getByCategory(companyID, category);
	}

	public List<Coupon> getCompanyCoupons(double maxPrice) throws DBException, ThreadException, SQLException {
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

				if (!couponDao.isOtherExist(coupon.getTitle(), coupon.getCompanyID(), coupon.getId())) {
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
