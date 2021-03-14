package demo;

import java.sql.SQLException;
import java.time.LocalDate;

import javax.security.auth.login.LoginException;

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import connection.ConnectionPool;
import dao.CustomerDao;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;
import facades.AdminFacade;
import facades.CompanyFacade;
import facades.CustomerFacade;
import job.CouponsJob;
import login.ClientType;
import login.LoginManager;

public class Test {

	public static void main(String[] args) throws InterruptedException, SQLException, ThreadException, DBException,
			LoginException, MisMatchObjectException {
		// checkCompany();
		// checkCustomer();
		// checkCouponFacade();
		CouponsJob job = new CouponsJob();
		new Thread(job).start();
		System.out.println("test login: ");
		AdminFacade adminFacade = (AdminFacade) LoginManager.getInstance().login("admin@admin.com", "admin",
				ClientType.ADMINISTRATOR);

		CompanyFacade companyFacade = (CompanyFacade) LoginManager.getInstance().login("Versis@com", "1234",
				ClientType.COMPANY);
		System.out.println(companyFacade.getCompanyDetails());

		CustomerFacade customerFacade = (CustomerFacade) LoginManager.getInstance().login("lavi@", "2255",
				ClientType.CUSTOMER);
		System.out.println(customerFacade.getCustomerDetails());
		job.exist();
		Thread.sleep(1000);
		ConnectionPool.getInstance().closeAllConnections();
//"Arik", "Lavi", "lavi@", "2255"
	}

	private static void checkAdmin(AdminFacade facade)
			throws DBException, ThreadException, LoginException, MisMatchObjectException, SQLException {

		// Company company = new Company("Versis@", "Versis@com", "12345");
		Company company = new Company(7, "Ver", "Verddd@com", "12300");
		Customer customer = new Customer(5, "Miri", "Regev", "reg@.com", "12345");
		// adminFacade.addCompany(company);
		// adminFacade.updateCompany(company);
		System.out.println("test delete company: ");
		facade.deleteCompany(5);
		System.out.println("test getAllCompanies: ");
		facade.getAllCompanies().forEach(c -> System.out.println(c));
		// System.out.println("test add customer: ");
		// adminFacade.addCustomer(customer);
		System.out.println("test update customer: ");
		facade.updateCustomer(customer);
		System.out.println("test All Customers ");
		facade.getAllCustomers().forEach(c -> System.out.println(c));
		System.out.println("test delete customer: ");
		facade.deleteCustomer(1);
//		
//		CompanyFacade companyFacade = new CompanyFacade();
//		System.out.println("test login");
//		System.out.println("login result " + companyFacade.login("Versis@com", "1234"));
//		System.out.println("test company details");
//		Company company = companyFacade.getCompanyDetails();
//		System.out.println(company);
//		Coupon coupon = new Coupon(8, 4, Category.ELECTRICITY, "title21", "description", LocalDate.of(2021, 02, 02),
//				LocalDate.of(2021, 07, 01), 10, 5.10, "eert");
//		// System.out.println("test add coupon ");
//		// companyFacade.addCoupon(coupon);
//		System.out.println("test update  coupon ");
//		companyFacade.updateCoupon(coupon);
//		System.out.println("test delete:");
//		companyFacade.deleteCoupon(10);
//		System.out.println("test get all:");
//		companyFacade.getCompanyCoupons().forEach(c -> System.out.println(c));
//		System.out.println("test get all by categories:");
//		companyFacade.getCompanyCoupons(Category.FOOD).forEach(c -> System.out.println(c));
//		System.out.println("test get all by maxPrice:");
//		companyFacade.getCompanyCoupons(10).forEach(c -> System.out.println(c));
	}

	private static void checkCustomer()
			throws DBException, ThreadException, SQLException, MisMatchObjectException, LoginException {
		CustomerDao customerDao = new CustomerDao();
		Customer customer = new Customer("Arik", "Lavi", "lavi@", "2255");
		// customerDao.add(customer);
		// customer = customerDao.get(2);
		System.out.println(customer);
		customer.setFirstName("Shuki3");
//		customer.setLastName("Mu");
//		customer.setEmail("Shu@");
//		customer.setPassword("2555");
		// customerDao.update(customer);
		// System.out.println(customerDao.isExists("ww@"));
		CustomerFacade customerFacade = new CustomerFacade();
		System.out.println("test customer login ");
		System.out.println("login result " + customerFacade.login("Shu@", "2555"));
		System.out.println("test Customer Details ");
		customer = customerFacade.getCustomerDetails();
		System.out.println(customer);
		System.out.println("test Customer coupons ");
		customerFacade.getCustomerCoupons().forEach(c -> System.out.println(c));
	}

	private static void checkCompany(CompanyFacade facade)
			throws ThreadException, DBException, LoginException, SQLException, MisMatchObjectException {

		System.out.println("test company details");
		Company company = facade.getCompanyDetails();
		System.out.println(company);
		Coupon coupon = new Coupon(8, 4, Category.ELECTRICITY, "title21", "description", LocalDate.of(2021, 02, 02),
				LocalDate.of(2021, 07, 01), 10, 5.10, "eert");
		// System.out.println("test add coupon ");
		// companyFacade.addCoupon(coupon);
		System.out.println("test update  coupon ");
		facade.updateCoupon(coupon);
		System.out.println("test delete:");
		facade.deleteCoupon(10);
		System.out.println("test get all:");
		facade.getCompanyCoupons().forEach(c -> System.out.println(c));
		System.out.println("test get all by categories:");
		facade.getCompanyCoupons(Category.FOOD).forEach(c -> System.out.println(c));
		System.out.println("test get all by maxPrice:");
		facade.getCompanyCoupons(10).forEach(c -> System.out.println(c));
	}

}
