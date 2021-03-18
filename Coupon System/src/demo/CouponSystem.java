package demo;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.security.auth.login.LoginException;

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import connection.ConnectionPool;
import dao.CouponDao;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;
import facades.AdminFacade;
import facades.CompanyFacade;
import facades.CustomerFacade;
import job.CouponsJob;
import login.ClientType;
import login.LoginManager;
import utils.DBUtils;
import utils.FileUtil;

public class CouponSystem {
	private CouponsJob job;
	private Thread thread;
	private CouponDao couponDao;
	private static int sleepTime = 5000;

	public CouponSystem() {
		init();

	}

	private void init() {
		job = new CouponsJob();
		thread = new Thread(job);
		couponDao = new CouponDao();
	}

	public void testAll() throws ThreadException, DBException, SQLException, IOException, MisMatchObjectException,
			InterruptedException, LoginException {
		startApp();
		thread.start();
		System.out.println(sleepTime + " sec sleep   main for readable logs, for text only");
		Thread.sleep(sleepTime);
		System.out.println("\r\n\r\ntest login for facades: ");
		System.out.println("****** start admin facade test ******");
		checkAdminFacade(
				(AdminFacade) LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.ADMINISTRATOR));

		System.out.println("****** start company facade test ******");
		checkCompanyFacade((CompanyFacade) LoginManager.getInstance().login("Angel.Bakeries@company.com", "123456",
				ClientType.COMPANY));

		System.out.println("****** start customer facade test ******");
		checkCustomerFacade((CustomerFacade) LoginManager.getInstance().login("aaron.ahl@customer.com", "123456",
				ClientType.CUSTOMER));

		jobStop();
		ConnectionPool.getInstance().closeAllConnections();
	}

	private void checkAdminFacade(AdminFacade facade)
			throws DBException, ThreadException, LoginException, MisMatchObjectException, SQLException {

		// test add new company
		System.out.println("\r\nstart test add new company  ");
		Company company = new Company(56, "Versis@", "Versis@com", "12345");
		// Company company = new Company("Versis@", "Amedei@company.com", "12345");
		facade.addCompany(company);
		System.out.println("end test add new company  \r\n\r\n");

		System.out.println("\r\nstart test(falied) add new company  ");
		Company companyTest = new Company("Versis@", "Amedei@company.com", "12345");
		try {
			facade.addCompany(companyTest);
		} catch (Exception e) {
			System.err.println("failed add company because email exists in other company");
		}
		System.out.println("end test(falied) add new company  \r\n\r\n");

		// test update company
		System.out.println("\r\nstart test update company  ");
		company.setPassword("123456");
		facade.updateCompany(company);
		System.out.println("end test update company  \r\n\r\n");

		System.out.println("\r\nstart test(failed) update company  ");
		company.setName("Versis@1");
		try {
			facade.updateCompany(company);
		} catch (Exception e) {
			System.err.println("update exception: company name changed");
		}
		System.out.println("end test(failed) update  company  \r\n\r\n");

		// test delete company
		System.out.println("start test delete company  ");
		facade.deleteCompany(3);
		System.out.println("end test delete company \r\n\r\n");

		// test get all companies
		System.out.println("start test get all companies ");
		facade.getAllCompanies().forEach(c -> System.out.println(c));
		System.out.println("end test get all companies \r\n\r\n");

		// test get one company
		System.out.println("start test get one company ");
		System.out.println(facade.getOneCompany(10));
		System.out.println("end test get one company \r\n\r\n");

		// test add customer
		System.out.println("start test add customer: ");
		Customer customer = new Customer(109, "Miri", "Regev", "reg@.com", "12345");
		facade.addCustomer(customer);
		System.out.println("end test  add customer \r\n\r\n");

		// test update customer
		System.out.println("start test update customer");
		customer.setLastName("Veger");
		facade.updateCustomer(customer);
		System.out.println("end test update customer \r\n\r\n");

		System.out.println("start test(failed) update customer");
		customer.setEmail("zachary.alfandari@customer.com");
		try {
			facade.updateCustomer(customer);
		} catch (Exception e) {
			System.err.println("update exception: customer email changed");
		}
		System.out.println("end test(failed) update customer \r\n\r\n");

		// test delete customer
		System.out.println("start test delete customer: ");
		facade.deleteCustomer(10);
		System.out.println("end test delete customer \r\n\r\n");

		// test all customers
		System.out.println("start test print all customers ");
		facade.getAllCustomers().forEach(c -> System.out.println(c));
		System.out.println("end test print all customers  \r\n\r\n");

		// test one customer details
		System.out.println("start test print one customer ");
		System.out.println(facade.getOneCustomer(11));
		System.out.println("end test print one customer  \r\n\r\n");

		System.out.println("====== end admin facade test ======\r\n");

	}

	private void checkCompanyFacade(CompanyFacade facade)
			throws ThreadException, DBException, LoginException, SQLException, MisMatchObjectException {

		// test add coupon
		System.out.println("\r\nstart test add coupon ");
		Coupon coupon = new Coupon(40, 4, Category.ACCESSORIES, "title21", "description", LocalDate.of(2021, 02, 02),
				LocalDate.of(2021, 07, 01), 10, 5.10, "eert");
		facade.addCoupon(coupon);
		System.out.println("end test add coupon \r\n");

		// test update coupon
		System.out.println("start test update  coupon ");
		coupon.setDescription("Kuku");
		facade.updateCoupon(coupon);
		System.out.println("end test update  coupon \r\n");

		System.out.println("start test(failed) update  coupon ");
		coupon.setCompanyID(6);
		try {
			facade.updateCoupon(coupon);
		} catch (Exception e) {
			System.err.println("update exception: company id changed");
		}
		System.out.println("end test (failed) update  coupon \r\n");

		// test delete coupon
		System.out.println("start test delete  coupon ");
		facade.deleteCoupon(8);
		System.out.println("end test delete coupon \r\n");

		// test delete coupon
		System.out.println("start test(failed) delete  coupon ");
		facade.deleteCoupon(5);
		System.out.println("end test(failed) delete coupon \r\n");

		// test get all coupons
		System.out.println("start test get all coupons");
		facade.getCompanyCoupons().forEach(c -> System.out.println(c));
		System.out.println("end test get all coupons \r\n");

		// test get all filtered by categories
		System.out.println("start test get all filtered by categories:");
		facade.getCompanyCoupons(Category.ACCESSORIES).forEach(c -> System.out.println(c));
		System.out.println("end test get all filtered by categories: \r\n");

		// test get all filtered by maxPric
		System.out.println("start test get all filtered by maxPrice:");
		double maxPrice = 120.0;
		facade.getCompanyCoupons(maxPrice).forEach(c -> System.out.println(c));
		System.out.println("end test get all filtered by maxPrice: \r\n");

		// test company details
		System.out.println("\r\ntest company Details ");
		Company company = facade.getCompanyDetails();
		System.out.println("\r\n" + company);

		System.out.println("====== end company facade test ======\r\n");
	}

	private void checkCustomerFacade(CustomerFacade customerFacade)
			throws DBException, ThreadException, SQLException, MisMatchObjectException, LoginException {

		// Purchase the coupon (for success test)
		System.out.println("\r\nstart test purchese coupon:  ");
		int couponID = 7;
		Coupon coupon = couponDao.get(couponID);
		System.out.println("coupon amount before buing: " + coupon.getAmount());
		System.out.println("test purchese coupon:  ");
		customerFacade.purchaseCoupon(coupon);
		coupon = couponDao.get(couponID);
		System.out.println("coupon amount after buing: " + coupon.getAmount());
		System.out.println("end test purchese coupon:  \r\n\r\n");

		// try purchase the coupon again (for failed test)
		System.out.println("\r\nstart test(failed) purchese coupon:  ");
		System.out.println("coupon amount before buing: " + coupon.getAmount());
		System.out.println("test purchese coupon:  ");
		customerFacade.purchaseCoupon(coupon);
		coupon = couponDao.get(couponID);
		System.out.println("coupon amount after buing: " + coupon.getAmount());
		System.out.println("end test(failed) purchese coupon:  \r\n\r\n");

		// test get all customer coupons without filter
		System.out.println("start test get all customer coupons:  ");
		customerFacade.getCustomerCoupons().forEach(c -> System.out.println(c));
		System.out.println("end test all customer coupons:  \r\n\r\n");

		// test get all customer coupons filtered by category
		System.out.println("start test get all customer coupons filtered by category:  ");
		customerFacade.getCustomerCoupons(Category.ACCESSORIES).forEach(c -> System.out.println(c));
		System.out.println("end test all customer coupons:  \r\n\r\n");

		// test get all customer coupons less than price
		System.out.println("start test get all customer coupons filtered by maxPrice:  ");
		double maxPrice = 90.0;
		customerFacade.getCustomerCoupons(maxPrice).forEach(c -> System.out.println(c));
		System.out.println("end test all customer coupons:  \r\n\r\n");

		// test customer details
		System.out.println("\r\ntest Customer Details ");
		Customer customer = customerFacade.getCustomerDetails();
		System.out.println("\r\n" + customer);
		System.out.println("====== end customer facade test ======\r\n");
	}

	// drop tables, create tables, fill data
	private void startApp() throws ThreadException, DBException, SQLException, IOException, MisMatchObjectException {
		DBUtils.createTables();
		FileUtil.createData();
	}

	/**
	 * stop coupon job
	 */
	private void jobStop() {
		job.exist();
		for (int i = 0; i < 10; i++) {
			if (job.isExistReaded()) {
				break;
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("thread interupt exception: " + e);
				}
				System.out.println("wait for stopping job: 10/" + i);
			}

		}
		thread.interrupt();
	}

}
