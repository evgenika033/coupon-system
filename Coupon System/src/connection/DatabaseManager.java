package connection;

import java.sql.Connection;

import exception.DBException;
import utils.DBUtils;

public class DatabaseManager {
	public static final String url = "jdbc:mysql://localhost:3306/coupons_system?createDatabaseIfNotExist=FALSE&useTimezone=TRUE&serverTimezone=UTC";
	public static final String username = "root";
	public static final String password = "123456";

	private static final String CREATE_SCHEMA = "create schema coupons_system";
	private static final String DROP_SCHEMA = "drop schema coupons_system";
	private static final String CREATE_TABLE_PERSONS = "CREATE TABLE `java129`.`persons` (\r\n"
			+ "  `id` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `name` VARCHAR(30) NOT NULL,\r\n"
			+ "  `city` VARCHAR(45) NOT NULL,\r\n" + "  `birthday` DATE NOT NULL,\r\n"
			+ "  `hobby` VARCHAR(45) NOT NULL,\r\n" + "  PRIMARY KEY (`id`));";
	private static final String DROP_TABLE_PERSONS = "DROP TABLE `java129`.`persons`";

	private static final String CREATE_TABLE_COMPANIES = "CREATE TABLE `java129`.`companies` (\r\n"
			+ "  `id` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `name` VARCHAR(45) NOT NULL,\r\n"
			+ "  `email` VARCHAR(45) NOT NULL,\r\n" + "  `password` VARCHAR(45) NOT NULL,\r\n"
			+ "  PRIMARY KEY (`id`));\r\n" + "";

	private static final String CREATE_TABLE_CUSTOMERS = "CREATE TABLE `java129`.`customers` (\r\n"
			+ "  `id` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `first_name` VARCHAR(45) NOT NULL,\r\n"
			+ "  `last_name` VARCHAR(45) NOT NULL,\r\n" + "  `email` VARCHAR(45) NOT NULL,\r\n"
			+ "  `password` VARCHAR(45) NOT NULL,\r\n" + "  PRIMARY KEY (`id`));\r\n" + "";

	private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE `java129`.`categories` (\r\n"
			+ "  `id` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `name` VARCHAR(45) NOT NULL,\r\n"
			+ "  PRIMARY KEY (`id`));\r\n" + "";

	private static final String CREATE_TABLE_COUPONS = "CREATE TABLE `java129`.`coupons` (\r\n"
			+ "  `id` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `company_id` INT NOT NULL,\r\n"
			+ "  `category_id` INT NOT NULL,\r\n" + "  `title` VARCHAR(45) NOT NULL,\r\n"
			+ "  `description` VARCHAR(45) NOT NULL,\r\n" + "  `start_date` DATE NOT NULL,\r\n"
			+ "  `end_date` DATE NOT NULL,\r\n" + "  `amount` INT NOT NULL,\r\n" + "  `price` DOUBLE NOT NULL,\r\n"
			+ "  `image` VARCHAR(45) NOT NULL,\r\n" + "  PRIMARY KEY (`id`),\r\n"
			+ "  INDEX `company_id_idx` (`company_id` ASC) VISIBLE,\r\n"
			+ "  INDEX `category_id_idx` (`category_id` ASC) VISIBLE,\r\n" + "  CONSTRAINT `company_id`\r\n"
			+ "    FOREIGN KEY (`company_id`)\r\n" + "    REFERENCES `java129`.`companies` (`id`)\r\n"
			+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION,\r\n" + "  CONSTRAINT `category_id`\r\n"
			+ "    FOREIGN KEY (`category_id`)\r\n" + "    REFERENCES `java129`.`categories` (`id`)\r\n"
			+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION);";

	private static final String CREATE_TABLE_CUSTOMERS_VS_COUPONS = "CREATE TABLE `java129`.`customers_vs_coupons` (\r\n"
			+ "  `customer_id` INT NOT NULL,\r\n" + "  `coupon_id` INT NOT NULL,\r\n"
			+ "  PRIMARY KEY (`customer_id`, `coupon_id`),\r\n"
			+ "  INDEX `coupon_id_idx` (`coupon_id` ASC) VISIBLE,\r\n" + "  CONSTRAINT `customer_id`\r\n"
			+ "    FOREIGN KEY (`customer_id`)\r\n" + "    REFERENCES `java129`.`customers` (`id`)\r\n"
			+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION,\r\n" + "  CONSTRAINT `coupon_id`\r\n"
			+ "    FOREIGN KEY (`coupon_id`)\r\n" + "    REFERENCES `java129`.`coupons` (`id`)\r\n"
			+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION);\r\n" + "";

	private static Connection connection;

	public static void createSchema() throws DBException {
		DBUtils.runQuery(CREATE_SCHEMA);
	}

	public static void dropSchema() throws DBException {
		DBUtils.runQuery(DROP_SCHEMA);
	}

	public static void createTablePersons() throws DBException {
		DBUtils.runQuery(CREATE_TABLE_PERSONS);
	}

	public static void dropTablePersons() throws DBException {
		DBUtils.runQuery(DROP_TABLE_PERSONS);
	}
}
