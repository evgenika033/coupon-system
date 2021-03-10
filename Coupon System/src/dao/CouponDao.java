package dao;

import java.sql.SQLException;
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
import utils.DateTimeUtil;
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
			parameters.put(1, Integer.valueOf(addObject.getCompanyID()));
			parameters.put(2, Integer.valueOf(addObject.getCategory().ordinal()));
			parameters.put(3, addObject.getTitle());
			parameters.put(4, addObject.getDescription());
			parameters.put(5, DateTimeUtil.convertLocalDate2SQLDate(addObject.getStartDate()));
			parameters.put(6, DateTimeUtil.convertLocalDate2SQLDate(addObject.getEndDate()));
			parameters.put(7, Integer.valueOf(addObject.getAmount()));
			parameters.put(8, Double.valueOf(addObject.getPrice()));
			parameters.put(9, addObject.getImage());
			CouponUtil.execute(sql, parameters);
		}
	}

	@Override
	public void update(Coupon updateObject) throws DBException, ThreadException, MisMatchObjectException {
		boolean fromUpdate = true;
		String sql = DBUtils.UPDATE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.UPDATE_PARAMETER);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, Integer.valueOf(updateObject.getCompanyID()));
		parameters.put(2, Integer.valueOf(updateObject.getCategory().ordinal()));
		parameters.put(3, updateObject.getTitle());
		parameters.put(4, updateObject.getDescription());
		parameters.put(5, DateTimeUtil.convertLocalDate2SQLDate(updateObject.getStartDate()));
		parameters.put(6, DateTimeUtil.convertLocalDate2SQLDate(updateObject.getEndDate()));
		parameters.put(7, Integer.valueOf(updateObject.getAmount()));
		parameters.put(8, Double.valueOf(updateObject.getPrice()));
		parameters.put(9, updateObject.getImage());
		parameters.put(10, Integer.valueOf(updateObject.getId()));
		int result = CouponUtil.executeUpdate(sql, parameters);
		if (result > 0) {
			System.out.println(
					StringHelper.COUPON_UPDATE_MESSAGE + updateObject.getDescription() + " " + updateObject.getTitle());
		}

	}

	@Override
	public void delete(int id) throws DBException, ThreadException {
		String sql = DBUtils.DELETE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.PARAMETER_ID);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, Integer.valueOf(id));
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
		parameters.put(1, Integer.valueOf(id));
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
		parameters.put(1, Integer.valueOf(customerID));
		parameters.put(2, Integer.valueOf(couponID));
		CouponUtil.execute(sql, parameters);
	}

	@Override
	public void deleteCouponPurchase(int customerID, int couponID) throws ThreadException, DBException {
		String sql = DBUtils.DELETE_CUSTOMERS_VS_COUPONS_QUERY;
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, Integer.valueOf(customerID));
		parameters.put(2, Integer.valueOf(couponID));
		CouponUtil.execute(sql, parameters);
	}

	@Override
	public boolean isExist(Coupon coupon) throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.IS_COUPON_EXISTS_QUERY;
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, coupon.getTitle());
		parameters.put(2, Integer.valueOf(coupon.getCompanyID()));
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
	public List<Coupon> get(int companyID, double maxPrice) throws ThreadException, DBException, SQLException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.GET_BY_COMPANY_ID_AND_MAX_PRICE);
		return CouponUtil.executeQuery(sql);
	}

	@Override
	public List<Coupon> getByCompany(int companyID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.GET_BY_COMPANY_ID);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, Integer.valueOf(companyID));
		return CouponUtil.executeQuery(sql, parameters);
	}

	@Override
	public List<Coupon> getByCategory(int companyID, Category category)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.GET_BY_COMPANY_AND_CATEGORY);

		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, Integer.valueOf(companyID));
		parameters.put(2, Integer.valueOf(category.ordinal() + 1));
		return CouponUtil.executeQuery(sql, parameters);
	}

	@Override
	public int getCount(int companyID) throws ThreadException, DBException, SQLException, MisMatchObjectException {
		List<Coupon> coupons = getByCompany(companyID);
		return coupons.size();
	}

	@Override
	public boolean isOtherExist(String title, int companyID, int couponID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.GET_OTHER_COUPON_BY_TITLE_AND_COMPANY);
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, title);
		parameters.put(2, Integer.valueOf(companyID));
		parameters.put(3, Integer.valueOf(couponID));
		List<Coupon> coupons = CouponUtil.executeQuery(sql, parameters);
		// functional programming: return exists if size > 0
		return coupons.size() > 0 ? true : false;
	}

	@Override
	public List<Coupon> getCustomerCoupons(int customerID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_CUSTOMERS_COUPONS_QUERY;
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, Integer.valueOf(customerID));
		return CouponUtil.executeQuery(sql, parameters);
	}

	@Override
	public List<Coupon> getCustomerCoupons(int customerID, double maxPrice)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_CUSTOMERS_COUPONS_QUERY;
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, Integer.valueOf(customerID));
		parameters.put(2, Double.valueOf(maxPrice));
		return CouponUtil.executeQuery(sql, parameters);
	}

	@Override
	public List<Coupon> getCustomerCoupons(int customerID, Category category)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		String sql = DBUtils.GET_CUSTOMERS_COUPONS_CATEGORY_QUERY;
		Map<Integer, Object> parameters = new HashMap<Integer, Object>();
		parameters.put(1, Integer.valueOf(customerID));
		parameters.put(2, Integer.valueOf(category.ordinal() + 1));
		return CouponUtil.executeQuery(sql, parameters);
	}
}
