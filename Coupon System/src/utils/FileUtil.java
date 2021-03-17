package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import dao.CompanyDao;
import dao.CouponDao;
import dao.CustomerDao;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;

public class FileUtil {
	public static final String companyPath = "csvData/companies.csv";
	public static final String customerPath = "csvData/customers.csv";
	public static final String couponPath = "csvData/coupons.csv";
	public static final String customers_vs_couponsPath = "csvData/customersVScoupons.csv";
	private static CompanyDao companyDao = new CompanyDao();
	private static CustomerDao customerDao = new CustomerDao();
	private static CouponDao couponDao = new CouponDao();

	// get data from file and create on DB
	public static void createData() throws IOException, ThreadException, DBException, MisMatchObjectException {
		createCompany(readFromFile(companyPath));
		createCustomer(readFromFile(customerPath));
		createCategory();
		createCoupon(readFromFile(couponPath));
		createCustomersVSCoupon(readFromFile(customers_vs_couponsPath));
	}

	// read data from file, return list of lines
	private static List<String> readFromFile(String path)
			throws IOException, ThreadException, DBException, MisMatchObjectException {
		List<String> lines = new ArrayList<>();
		try (BufferedReader br = Files.newBufferedReader(Path.of(path))) {
			String line = br.readLine();
			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
		} catch (Exception e) {
			System.out.println("read/open file exception: " + e);
		}
		return lines;
	}

	// create companies from list attributes(line ->company)
	private static void createCompany(List<String> companiesStr)
			throws ThreadException, DBException, MisMatchObjectException {
		for (String companyStr : companiesStr) {
			String[] attributes = companyStr.split(",");
			String name = attributes[0];
			String email = attributes[1];
			String password = attributes[2];
			companyDao.add(new Company(name, email, password));
		}

	}

	// create CustomersVSCoupon from list attributes(line ->CustomersVSCoupon)
	private static void createCustomersVSCoupon(List<String> compsVSCopsStr)
			throws NumberFormatException, ThreadException, DBException {
		for (String compVSCopStr : compsVSCopsStr) {
			String[] attributes = compVSCopStr.split(",");
			couponDao.addCouponPurchase(Integer.valueOf(attributes[1]), Integer.valueOf(attributes[0]));
		}

	}

	// create coupons from list attributes(line ->coupon)
	private static void createCoupon(List<String> couponsStr)
			throws DBException, ThreadException, MisMatchObjectException {
		for (String couponStr : couponsStr) {
			String[] attributes = couponStr.split(",");
			int companyID = Integer.valueOf(attributes[0]);
			Category category = Category.valueOf(attributes[1]);
			String title = attributes[2];
			String description = "desc_" + attributes[2];
			LocalDate startDat = DateTimeUtil.convertStringToLocalDate(attributes[3]);
			LocalDate endDate = DateTimeUtil.convertStringToLocalDate(attributes[4]);
			int amount = Integer.valueOf(attributes[5]);
			double price = Double.valueOf(attributes[6]);
			String image = "imagePath";
			System.out.println(title);
			couponDao.add(new Coupon(companyID, category, title, description, startDat, endDate, amount, price, image));
		}

	}

	// create customer from list attributes(line ->customer)
	private static void createCustomer(List<String> customersStr)
			throws DBException, ThreadException, MisMatchObjectException {
		for (String customerStr : customersStr) {
			String[] attributes = customerStr.split(",");
			String firstName = attributes[0];
			String lastName = attributes[1];
			String email = attributes[2];
			String password = attributes[3];
			customerDao.add(new Customer(firstName, lastName, email, password));
		}
	}

	// create categories from Category enum
	private static void createCategory() throws ThreadException, DBException {
		String sql = "insert into `coupons_system`.`category` (`name`) values (?)";
		Map<Integer, Object> parameters;
		for (Category category : Category.values()) {
			parameters = new HashMap<Integer, Object>();
			parameters.put(1, category.name());
			CompanyUtil.executeUpdate(sql, parameters);
		}
	}

}
