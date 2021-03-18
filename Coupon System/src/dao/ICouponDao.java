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

	/**
	 * add row to table customers_VS_coupons
	 * 
	 * @param customerID
	 * @param couponID
	 * @throws ThreadException
	 * @throws DBException
	 */
	void addCouponPurchase(int customerID, int couponID) throws ThreadException, DBException;

	/**
	 * return customers_VS_coupons by couponID
	 * 
	 * @param couponID
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	List<Integer> getCouponPurchaseByCoupon(int couponID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	/**
	 * delete customers_VS_coupons row by customerOD and couponID
	 * 
	 * @param customerID
	 * @param couponID
	 * @throws ThreadException
	 * @throws DBException
	 */
	void deleteCouponPurchase(int customerID, int couponID) throws ThreadException, DBException;

	/**
	 * get company coupons by
	 * 
	 * @param companyID
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	List<Coupon> getByCompany(int companyID) throws ThreadException, DBException, SQLException, MisMatchObjectException;

	/**
	 * get company coupons by category and companyID
	 * 
	 * @param companyID
	 * @param category
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	List<Coupon> getByCategory(int companyID, Category category)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	/**
	 * get company coupons by companyID and price
	 * 
	 * @param companyID
	 * @param maxPrice
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	List<Coupon> get(int companyID, double maxPrice)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	/**
	 * check coupon exists
	 * 
	 * @param coupon
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	boolean isExist(Coupon coupon) throws ThreadException, DBException, SQLException, MisMatchObjectException;

	/**
	 * check if other company coupon exists with same title but other id
	 * 
	 * @param title
	 * @param companyID
	 * @param couponID
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	boolean isOtherExist(String title, int companyID, int couponID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	/**
	 * get customer coupons by customerID
	 * 
	 * @param coustomerID
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	List<Coupon> getCustomerCoupons(int coustomerID)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	/**
	 * get customer coupons by customerID and maxPrice
	 * 
	 * @param coustomerID
	 * @param maxPrice
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	List<Coupon> getCustomerCoupons(int coustomerID, double maxPrice)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	/**
	 * get customer coupons by customerID and category
	 * 
	 * @param customerID
	 * @param category
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	List<Coupon> getCustomerCoupons(int customerID, Category category)
			throws ThreadException, DBException, SQLException, MisMatchObjectException;

	/**
	 * get customer coupons by localDate
	 * 
	 * @param localDate
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
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
