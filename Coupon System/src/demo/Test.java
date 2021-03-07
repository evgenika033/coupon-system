package demo;

import java.sql.SQLException;
import java.time.LocalDate;

import javax.security.auth.login.LoginException;

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import connection.ConnectionPool;
import dao.CompanyDao;
import dao.CouponDao;
import dao.CustomerDao;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;
import facades.CompanyFacade;

public class Test {

	public static void main(String[] args) throws InterruptedException, SQLException, ThreadException, DBException,
			LoginException, MisMatchObjectException {
		// checkCompany();
		// checkCustomer();
		// int companyID, Category category, String title, String description, LocalDate
		// startDate,
		// LocalDate endDate, int amount, double price, String image
		CompanyFacade companyFacade = new CompanyFacade();
		System.out.println("test login");
		System.out.println("login result " + companyFacade.login("Versis@com", "1234"));
		System.out.println("test company details");
		Company company = companyFacade.getCompanyDetails();
		System.out.println(company);
		Coupon coupon = new Coupon(8, 4, Category.ELECTRICITY, "title21", "description", LocalDate.of(2021, 02, 02),
				LocalDate.of(2021, 07, 01), 10, 5.10, "eert");
		// System.out.println("test add coupon ");
		// companyFacade.addCoupon(coupon);
		System.out.println("test update  coupon ");
		companyFacade.updateCoupon(coupon);
		System.out.println("test delete:");
		companyFacade.deleteCoupon(6);
		System.out.println("test get all:");
		companyFacade.getCompanyCoupons().forEach(c -> System.out.println(c));
		System.out.println("test get all by categories:");
		companyFacade.getCompanyCoupons(Category.FOOD).forEach(c -> System.out.println(c));
		System.out.println("test get all by maxPrice:");
		companyFacade.getCompanyCoupons(10).forEach(c -> System.out.println(c));
		ConnectionPool.getInstance().closeAllConnections();

	}

	private static void checkCoupon() throws DBException, ThreadException {
		Coupon coupon = new Coupon(4, Category.ELECTRICITY, "title2", "description", LocalDate.of(2021, 02, 01),
				LocalDate.of(2021, 07, 01), 10, 5.10, "eert");
		CouponDao couponDao = new CouponDao();
		// couponDao.add(coupon);
		couponDao.delete(3);
		System.out.println("test get");
		System.out.println(couponDao.get(4));
		System.out.println("test getAll");
		couponDao.get().forEach(c -> System.out.println(c));
		couponDao.deleteCouponPurchase(1, 4);
		System.out.println("test delete CouponPurchase: ");
		System.out.println();
		// couponDao.addCouponPurchase(2, 4);
		// System.out.println("validate: " + couponDao.isValid(coupon, false));
		// System.out.println(Category.ELECTRICITY.ordinal());
	}

	private static void checkCustomer() throws DBException, ThreadException {
		CustomerDao customerDao = new CustomerDao();
		Customer customer = new Customer("Arik", "Lavi", "lavi@", "2255");
		// customerDao.add(customer);
		customer = customerDao.get(2);
		System.out.println(customer);
		customer.setFirstName("Shuki3");
//		customer.setLastName("Mu");
//		customer.setEmail("Shu@");
//		customer.setPassword("2555");
		// customerDao.update(customer);
		// System.out.println(customerDao.isExists("ww@"));
	}

	private static void checkCompany() throws ThreadException, DBException {
		CompanyDao companyDao = new CompanyDao();
		Company company = companyDao.get(1);
		System.out.println(company);
		company.setEmail("change@");
		// companyDao.update(company);
		company = companyDao.get(1);
		System.out.println(company);
		System.out.println(companyDao.get());
//		company = new Company("Versis37", "Versis5@com", null);
//		companyDao.add(company);
		companyDao.delete(2);
	}

}
