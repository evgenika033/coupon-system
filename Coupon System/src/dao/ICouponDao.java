package dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import beans.Category;
import beans.Coupon;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;

public interface ICouponDao extends IDaoCRUD<Coupon> {

	void addCouponPurchase(int customerID, int couponID) throws ThreadException, DBException;

	List<Integer> getCouponPurchaseByCoupon(int couponID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	void deleteCouponPurchase(int customerID, int couponID) throws ThreadException, DBException;

	List<Coupon> getByCompany(int companyID) throws ThreadException, DBException, SQLException, MisMatchObjectException;

	List<Coupon> getByCategory(int companyID, Category category)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	int getCount(int companyID) throws ThreadException, DBException, SQLException, MisMatchObjectException;

	List<Coupon> get(int companyID, double maxPrice) throws ThreadException, DBException, SQLException;

	boolean isExist(Coupon coupon) throws ThreadException, DBException, SQLException, MisMatchObjectException;

	boolean isOtherExist(String title, int companyID, int couponID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	List<Coupon> getCustomerCoupons(int coustomerID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	List<Coupon> getCustomerCoupons(int coustomerID, double maxPrice)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	List<Coupon> getCustomerCoupons(int customerID, Category category)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	List<Integer> getCouponPurchaseByCustomer(int customerID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	List<Coupon> getCouponsByEndDate(LocalDate localDate)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	/**
	 * get CouponPurchase by customerID and couponID
	 * 
	 * @param customerID
	 * @param couponID
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	List<Integer> getCouponPurchase(int customerID, int couponID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;;
}
