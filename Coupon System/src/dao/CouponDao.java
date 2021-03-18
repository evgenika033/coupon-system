package dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.Category;
import beans.Coupon;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;
import utils.CouponUtil;
import utils.DBUtils;
import utils.StringHelper;

public class CouponDao implements ICouponDao {

	public CouponDao() {

	}

	@Override
	public void add(Coupon addObject) throws DBException, ThreadException, MisMatchObjectException {
		boolean fromUpdate = false;
		String sql = DBUtils.ADD_COUPON_QUERY;
		if (StringHelper.allParametersNotEmpty(addObject, fromUpdate)) {
			Map<Integer, Object> parameters = new HashMap<Integer, Object>();
			parameters.put(1, addObject.getCompanyID());
			parameters.put(2, addObject.getCategory().ordinal() + 1);
			parameters.put(3, addObject.getTitle());
			parameters.put(4, addObject.getDescription());
			parameters.put(5, addObject.getStartDate());
			parameters.put(6, addObject.getEndDate());
			parameters.put(7, addObject.getAmount());
			parameters.put(8, addObject.getPrice());
			parameters.put(9, addObject.getImage());
			CouponUtil.execute(sql, parameters);
		} else {
			throw new DBException(StringHelper.VALIDATION_PARAMETERS_ERROR_MESSAGE);
		}
	}

	@Override
	public void update(Coupon updateObject) throws DBException, ThreadException, MisMatchObjectException {
		boolean fromUpdate = true;
		if (StringHelper.allParametersNotEmpty(updateObject, fromUpdate)) {
			String sql = DBUtils.UPDATE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
					.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.UPDATE_PARAMETER);
			Map<Integer, Object> parameters = new HashMap<Integer, Object>();
			parameters.put(1, updateObject.getCompanyID());
			parameters.put(2, updateObject.getCategory().ordinal() + 1);
			parameters.put(3, updateObject.getTitle());
			parameters.put(4, updateObject.getDescription());
			parameters.put(5, updateObject.getStartDate());
			parameters.put(6, updateObject.getEndDate());
			parameters.put(7, updateObject.getAmount());
			parameters.put(8, updateObject.getPrice());
			parameters.put(9, updateObject.getImage());
			parameters.put(10, updateObject.getId());
			int result = CouponUtil.executeUpdate(sql, parameters);
			if (result > 0) {
				System.out.println(StringHelper.COUPON_UPDATE_MESSAGE + updateObject.getDescription() + " "
						+ updateObject.getTitle());
			} else {
				throw new DBException(StringHelper.VALIDATION_PARAMETERS_ERROR_MESSAGE);
			}
		}

	}

	@Override
	public void delete(int id) throws DBException, ThreadException {
		String sql = DBUtils.DELETE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.PARAMETER_ID);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, id);
		int result = CouponUtil.executeUpdate(sql, parameters);
		if (result > 0) {
			System.out.println(StringHelper.COUPON_DELETE_MESSAGE);
		} else {
			System.out.println(StringHelper.COUPON_DELETE_FAILED_MESSAGE);
		}

	}

	@Override
	public Coupon get(int id) throws DBException, ThreadException, SQLException, MisMatchObjectException {
		Coupon coupon = null;
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.PARAMETER_ID);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, id);
		List<Coupon> coupons = CouponUtil.executeQuery(sql, parameters);
		// function programming: return first element or null
		return coupons.size() > 0 ? coupons.get(0) : coupon;

	}

	@Override
	public List<Coupon> get() throws DBException, ThreadException, SQLException {
		String sql = DBUtils.GET_ALL_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE);
		return CouponUtil.executeQuery(sql);
	}

	@Override
	public void addCouponPurchase(int customerID, int couponID) throws ThreadException, DBException {
		String sql = DBUtils.ADD_CUSTOMERS_VS_COUPONS_QUERY;
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, customerID);
		parameters.put(2, couponID);
		CouponUtil.execute(sql, parameters);
	}

	@Override
	public List<Integer> getCouponPurchaseByCoupon(int couponID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = CouponUtil.GET_CUSTOMERS_FROM_CUSTOMERS_VS_COUPONS_BY_COUPON_ID;
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, couponID);
		return CouponUtil.executeQuerySpecial(sql, parameters);
	}

	@Override
	public List<Integer> getCouponPurchase(int customerID, int couponID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE_CUSTOMERS_VS_COUPONS)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER,
						CouponUtil.GET_FROM_CUSTOMERS_VS_COUPONS_BY_CUSTOMER_AND_COUPON);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, customerID);
		parameters.put(2, couponID);
		return CouponUtil.executeQuerySpecial(sql, parameters);
	}

	@Override
	public void deleteCouponPurchase(int customerID, int couponID) throws ThreadException, DBException {
		String sql = DBUtils.DELETE_CUSTOMERS_VS_COUPONS_QUERY;
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, customerID);
		parameters.put(2, couponID);
		CouponUtil.execute(sql, parameters);
	}

	@Override
	public boolean isExist(Coupon coupon) throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.IS_COUPON_EXISTS_QUERY;
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, coupon.getTitle());
		parameters.put(2, coupon.getCompanyID());
		List<Coupon> coupons = CouponUtil.executeQuery(sql, parameters);
		if (coupons.size() > 0) {
			System.out.println(
					String.format(StringHelper.COUPON_EXIST_MESSAGE, coupon.getTitle(), coupon.getCompanyID()));
			return true;
		}
		System.out.println(
				String.format(StringHelper.COUPON_NOT_EXISTS_MESSAGE, coupon.getTitle(), coupon.getCompanyID()));
		return false;
	}

	@Override
	public List<Coupon> get(int companyID, double maxPrice)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.GET_BY_COMPANY_ID_AND_MAX_PRICE);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, companyID);
		parameters.put(2, maxPrice);
		return CouponUtil.executeQuery(sql, parameters);
	}

	@Override
	public List<Coupon> getByCompany(int companyID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.GET_BY_COMPANY_ID);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, companyID);
		return CouponUtil.executeQuery(sql, parameters);
	}

	@Override
	public List<Coupon> getByCategory(int companyID, Category category)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.GET_BY_COMPANY_AND_CATEGORY);

		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, companyID);
		parameters.put(2, category.ordinal() + 1);
		return CouponUtil.executeQuery(sql, parameters);
	}

	@Override
	public boolean isOtherExist(String title, int companyID, int couponID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.GET_OTHER_COUPON_BY_TITLE_AND_COMPANY);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, title);
		parameters.put(2, companyID);
		parameters.put(3, couponID);
		List<Coupon> coupons = CouponUtil.executeQuery(sql, parameters);
		// functional programming: return exists if size > 0
		return coupons.size() > 0 ? true : false;
	}

	@Override
	public List<Coupon> getCustomerCoupons(int customerID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_CUSTOMERS_COUPONS_QUERY;
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, customerID);
		return CouponUtil.executeQuery(sql, parameters);
	}

	@Override
	public List<Coupon> getCustomerCoupons(int customerID, double maxPrice)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_CUSTOMERS_COUPONS_BY_MAX_PRICE_QUERY;
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, customerID);
		parameters.put(2, maxPrice);
		return CouponUtil.executeQuery(sql, parameters);
	}

	@Override
	public List<Coupon> getCustomerCoupons(int customerID, Category category)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_CUSTOMERS_COUPONS_CATEGORY_QUERY;
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, customerID);
		System.out.println(category.ordinal() + 1);
		parameters.put(2, category.ordinal() + 1);
		return CouponUtil.executeQuery(sql, parameters);
	}

	@Override
	public List<Coupon> getCouponsByEndDate(LocalDate localDate)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.GET_COUPONS_BY_END_DATE);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, localDate);
		return CouponUtil.executeQuery(sql, parameters);
	}
}
