package dao;

import beans.Coupon;
import exception.DBException;
import exception.ThreadException;

public interface ICouponDao extends IDaoCRUD<Coupon> {

	void addCouponPurchase(int customerID, int couponID) throws ThreadException, DBException;

	void deleteCouponPurchase(int customerID, int couponID) throws ThreadException, DBException;

}
