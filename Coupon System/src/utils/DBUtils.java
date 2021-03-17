package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import connection.ConnectionPool;
import exception.DBException;
import exception.ThreadException;

public class DBUtils {

	// query section
	public static final String GET_ALL_QUERY = "select * from _TABLE_";
	public static final String GET_ONE_QUERY = "select * from _TABLE_ where _PARAMETER_";
	public static final String DELETE_QUERY = "delete from _TABLE_ where _PARAMETER_";
	public static final String ADD_COMPANY_QUERY = "INSERT INTO `coupons_system`.`company` (`name`, `email`, `password`) VALUES (?,?,?)";
	public static final String UPDATE_QUERY = "UPDATE _TABLE_ SET _PARAMETER_ WHERE (`id` = ?)";
	public static final String GET_EMAIL_PASSWORD_PARAMETER = "`email`=? and `password` = ?";
	public static final String ADD_CUSTOMER_QUERY = "INSERT INTO `coupons_system`.`customer` (`first_name`, `last_name`, `email`, `password`) VALUES (?, ?, ?, ?)";
	public static final String GET_CUSTOMERS_COUPONS_QUERY = "select * from `coupons_system`.`coupon` as c join `coupons_system`.`customers_vs_coupons` as cc on c.id=cc.coupon_id	where cc.customer_id=?";
	public static final String GET_CUSTOMERS_COUPONS_BY_MAX_PRICE_QUERY = "select * from `coupons_system`.`coupon` as c join `coupons_system`.`customers_vs_coupons` as cc on c.id=cc.coupon_id	where cc.customer_id=? and cc.price<?";
	public static final String GET_CUSTOMERS_COUPONS_CATEGORY_QUERY = "join `coupons_system`.`customers_vs_coupons` as cc on c.id=cc.coupon_id join category as cat on cat.id=c.category_id where cc.customer_id=? and c.category_id=?";
	public static final String ADD_COUPON_QUERY = "INSERT INTO `coupons_system`.`coupon` (`company_id`, `category_id`, `title`, `description`, `start_date`, `end_date`, `amount`, `price`, `image`) VALUES (?	, ?, ?, ?, ?, ?, ?, ?,?)";
	public static final String IS_COUPON_EXISTS_QUERY = "select * from `coupons_system`.`coupon` where `title`=? and `company_id`=?";
	public static final String ADD_CUSTOMERS_VS_COUPONS_QUERY = "INSERT INTO `coupons_system`.`customers_vs_coupons` (`customer_id`, `coupon_id`) VALUES (?, ?)";
	public static final String DELETE_CUSTOMERS_VS_COUPONS_QUERY = "DELETE FROM `coupons_system`.`customers_vs_coupons` WHERE (`customer_id` = ?) and (`coupon_id` = ?)";
	// place_holders section
	public static final String TABLE_PLACE_HOLDER = "_TABLE_";
	public static final String PARAMETER_PLACE_HOLDER = "_PARAMETER_";
	public static final String EMAIL_PLACE_HOLDER = "_EMAIL_";
	public static final String TITLE_PLACE_HOLDER = "_TITLE_";
	public static final String COMPANY_ID_PLACE_HOLDER = "_COMPANY_ID_";
	public static final String ID_PLACE_HOLDER = "_ID_";
	public static final String CATEGORY_ID_PLACE_HOLDER = "_CATEGORY_ID_";

	// table section
	public static final String CREATE_SCHEMA = "create schema coupons_system";
	public static final String DROP_SCHEMA = "drop schema coupons_system";

	public static void returnConnection(Connection connection) throws DBException {
		if (connection != null) {
			ConnectionPool.getInstance().returnConnection(connection);
			connection = null;
		}
	}

	/**
	 * drop/create schema, create tables
	 * 
	 * @throws DBException
	 * @throws ThreadException
	 * @throws SQLException
	 */
	public static void createTables() throws DBException, ThreadException, SQLException {
		dropSchema();
		createSchema();
		createTableCompany();
		createTableCustomer();
		createTableCategory();
		createTableCoupon();
		createTableCustomers_VS_Coupons();

	}

	/**
	 * drop schema
	 * 
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 */
	private static void dropSchema() throws ThreadException, DBException, SQLException {
		System.out.println("drop schema: coupons_system");
		String sql = DBUtils.DROP_SCHEMA;
		execute(sql);
	}

	/**
	 * create schema
	 * 
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 */
	private static void createSchema() throws ThreadException, DBException, SQLException {
		System.out.println("create schema: coupons_system");
		String sql = DBUtils.CREATE_SCHEMA;
		execute(sql);
	}

	/**
	 * create table company
	 * 
	 * @throws DBException
	 * @throws ThreadException
	 */
	private static void createTableCompany() throws DBException, ThreadException {
		System.out.println("create table: company");
		String sql = "CREATE TABLE `coupons_system`.`company` (\r\n" + "  `id` INT NOT NULL AUTO_INCREMENT,\r\n"
				+ "  `name` VARCHAR(45) NOT NULL,\r\n" + "  `email` VARCHAR(45) NOT NULL,\r\n"
				+ "  `password` VARCHAR(45) NOT NULL,\r\n" + "  PRIMARY KEY (`id`));";
		executeUpdate(sql);
	}

	/**
	 * create table customer
	 * 
	 * @throws DBException
	 * @throws ThreadException
	 */
	private static void createTableCustomer() throws DBException, ThreadException {
		System.out.println("create table: customer");
		String sql = "CREATE TABLE `coupons_system`.`customer` (\r\n" + "  `id` INT NOT NULL AUTO_INCREMENT,\r\n"
				+ "  `first_name` VARCHAR(45) NOT NULL,\r\n" + "  `last_name` VARCHAR(45) NOT NULL,\r\n"
				+ "  `email` VARCHAR(45) NOT NULL,\r\n" + "  `password` VARCHAR(45) NOT NULL,\r\n"
				+ "  PRIMARY KEY (`id`));";
		executeUpdate(sql);
	}

	/**
	 * create table category
	 * 
	 * @throws DBException
	 * @throws ThreadException
	 */
	private static void createTableCategory() throws DBException, ThreadException {
		System.out.println("create table: category");
		String sql = "CREATE TABLE `coupons_system`.`category` (\r\n" + "  `id` INT NOT NULL AUTO_INCREMENT,\r\n"
				+ "  `name` VARCHAR(45) NOT NULL,\r\n" + "  PRIMARY KEY (`id`));";
		executeUpdate(sql);
	}

	/**
	 * create table coupon
	 * 
	 * @throws DBException
	 * @throws ThreadException
	 */
	private static void createTableCoupon() throws DBException, ThreadException {
		System.out.println("create table: coupon");
		String sql = "CREATE TABLE `coupons_system`.`coupon` (\r\n" + "  `id` INT NOT NULL AUTO_INCREMENT,\r\n"
				+ "  `company_id` INT NOT NULL,\r\n" + "  `category_id` INT NOT NULL,\r\n"
				+ "  `title` VARCHAR(45) NOT NULL,\r\n" + "  `description` VARCHAR(45) NOT NULL,\r\n"
				+ "  `start_date` DATE NOT NULL,\r\n" + "  `end_date` DATE NOT NULL,\r\n"
				+ "  `amount` INT NOT NULL,\r\n" + "  `price` DOUBLE NOT NULL,\r\n"
				+ "  `image` VARCHAR(45) NOT NULL,\r\n" + "  PRIMARY KEY (`id`),\r\n" + "\r\n"
				+ " INDEX `category_id_idx` (`category_id` ASC) VISIBLE,\r\n"
				+ " INDEX `company_id_idx` (`company_id` ASC) VISIBLE,\r\n" + " CONSTRAINT `company_id`\r\n"
				+ "  FOREIGN KEY (`company_id`)\r\n" + "  REFERENCES `coupons_system`.`company` (`id`)\r\n"
				+ "  ON DELETE NO ACTION\r\n" + "  ON UPDATE NO ACTION,\r\n" + " CONSTRAINT `category_id`\r\n"
				+ "  FOREIGN KEY (`category_id`)\r\n" + "  REFERENCES `coupons_system`.`category` (`id`)\r\n"
				+ "  ON DELETE NO ACTION\r\n" + "  ON UPDATE NO ACTION);";
		executeUpdate(sql);
	}

	/**
	 * create table Customers_VS_Coupons
	 * 
	 * @throws DBException
	 * @throws ThreadException
	 */
	private static void createTableCustomers_VS_Coupons() throws DBException, ThreadException {
		System.out.println("create table: customers_vs_coupons");
		String sql = "CREATE TABLE `coupons_system`.`customers_vs_coupons` (\r\n" + "  `customer_id` INT NOT NULL,\r\n"
				+ "  `coupon_id` INT NOT NULL,\r\n" + "  PRIMARY KEY (`customer_id`, `coupon_id`),\r\n"
				+ "  INDEX `coupon_id_idx` (`coupon_id` ASC) VISIBLE,\r\n" + "  CONSTRAINT `customer_id`\r\n"
				+ "    FOREIGN KEY (`customer_id`)\r\n" + "    REFERENCES `coupons_system`.`customer` (`id`)\r\n"
				+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION,\r\n" + "  CONSTRAINT `coupon_id`\r\n"
				+ "    FOREIGN KEY (`coupon_id`)\r\n" + "    REFERENCES `coupons_system`.`coupon` (`id`)\r\n"
				+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION);";
		executeUpdate(sql);
	}

	/**
	 * for create tables
	 * 
	 * @param sql
	 * @throws DBException
	 * @throws ThreadException
	 */
	public static void executeUpdate(String sql) throws DBException, ThreadException {
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.executeUpdate();
			DBUtils.returnConnection(connection);
		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException("create table exception: " + e);
		}

	}

	/**
	 * for drop, create schema
	 * 
	 * @param sql
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 */
	public static void execute(String sql) throws ThreadException, DBException, SQLException {
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.execute();
			DBUtils.returnConnection(connection);
		}
	}

}
