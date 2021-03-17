package job;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import beans.Coupon;
import dao.CouponDao;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;

public class CouponsJob implements Runnable {

	private boolean isExist = false;
	private boolean isExistReaded = false;

	public boolean isExistReaded() {
		return isExistReaded;
	}

	CouponDao couponDao = new CouponDao();

	@Override
	public void run() {
		while (!isExist) {
			try {
				job();
				Thread.sleep(1000 * 60 * 60 * 24);
			} catch (InterruptedException e) {
				System.err.println("job interupt exception: " + e);
			}
		}
		isExistReaded = true;
		System.out.println("job stopped");
	}

	public void exist() {
		this.isExist = true;
	}

	private void job() {
		System.out.println("job started");
		try {
			System.out.println("current time:" + LocalDate.now());
			List<Coupon> coupons = couponDao.getCouponsByEndDate(LocalDate.now());
			System.out.println("coupons to delete by job: " + coupons.size());
			coupons.forEach(c -> System.out.println(c));
			for (Coupon coupon : coupons) {
				System.out.println("get customers list from customers_VS_coupons:");
				List<Integer> customers = couponDao.getCouponPurchaseByCoupon(coupon.getId());
				if (customers.size() > 0) {
					System.out.println("delete customer_VS_coupon: " + customers.size());
					for (int customer_id : customers) {
						System.out.println("delete customer_VS_coupon: " + customer_id + ", " + coupon.getId());
						couponDao.deleteCouponPurchase(customer_id, coupon.getId());
					}
				}
				System.out.println("delete coupon: " + coupon.getTitle());
				couponDao.delete(coupon.getId());

			}
		} catch (ThreadException | DBException | SQLException | MisMatchObjectException e) {
			System.err.println("job exception: " + e);
		}
		System.out.println("job ended");
	}

}
