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
import dao.CustomerDao;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;
import facades.CompanyFacade;
import facades.CustomerFacade;

public class Test {

	public static void main(String[] args) throws InterruptedException, SQLException, ThreadException, DBException,
			LoginException, MisMatchObjectException {
		// checkCompany();
		// checkCustomer();
		// checkCouponFacade();
		CustomerFacade customerFacade = new CustomerFacade();
		System.out.println("test customer login ");
		System.out.println("login result " + customerFacade.login("Shu@", "2555"));
		System.out.println("test Customer Details ");
		Customer customer = customerFacade.getCustomerDetails();
		System.out.println(customer);
		System.out.println("test Customer coupons ");
		customerFacade.getCustomerCoupons().forEach(c -> System.out.println(c));
		ConnectionPool.getInstance().closeAllConnections();

	}

	private static void checkCouponFacade()
			throws DBException, ThreadException, LoginException, MisMatchObjectException, SQLException {
//		Coupon coupon = new Coupon(4, Category.ELECTRICITY, "title2", "description", LocalDate.of(2021, 02, 01),
//				LocalDate.of(2021, 07, 01), 10, 5.10, "eert");
//		
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
		companyFacade.deleteCoupon(10);
		System.out.println("test get all:");
		companyFacade.getCompanyCoupons().forEach(c -> System.out.println(c));
		System.out.println("test get all by categories:");
		companyFacade.getCompanyCoupons(Category.FOOD).forEach(c -> System.out.println(c));
		System.out.println("test get all by maxPrice:");
		companyFacade.getCompanyCoupons(10).forEach(c -> System.out.println(c));
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
