package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;

import connection.ConnectionPool;
import exception.DBException;

public class DBUtils {

	// query section
	public static final String GET_ALL_QUERY = "select * from _TABLE_";
	public static final String GET_ONE_QUERY = "select * from _TABLE_ where _PARAMETER_";
	public static final String DELETE_QUERY = "delete from _TABLE_ where _PARAMETER_";
	public static final String ADD_COMPANY_QUERY = "INSERT INTO `company` (`name`, `email`, `password`) VALUES (?,?,?)";
	// public static final String IS_COMPANY_EXISTS_QUERY = "SELECT count(*) FROM
	// company where( `name`=?)or (`email`=?)";
	public static final String UPDATE_QUERY = "UPDATE _TABLE_ SET _PARAMETER_ WHERE (`id` = ?)";

	// public static final String IS_OTHER_CUSTOMER_EXISTS_QUERY = "SELECT count(*)
	// FROM customer where `email`=? and not `id`=?";
	public static final String GET_EMAIL_PASSWORD_PARAMETER = "`email`=? and `password` = ?";

	public static final String ADD_CUSTOMER_QUERY = "INSERT INTO `customer` (`first_name`, `last_name`, `email`, `password`) VALUES (?, ?, ?, ?)";
	// public static final String IS_CUSTOMER_EXISTS_QUERY = "SELECT count(*) FROM
	// customer where `email`=?";
	public static final String GET_CUSTOMERS_COUPONS_QUERY = "select * from coupon as c join customers_vs_coupons as cc on c.id=cc.coupon_id	where cc.customer_id=?";
	public static final String GET_CUSTOMERS_COUPONS_BY_MAX_PRICE_QUERY = "select * from coupon as c join customers_vs_coupons as cc on c.id=cc.coupon_id	where cc.customer_id=? and cc.price<?";
	public static final String GET_CUSTOMERS_COUPONS_CATEGORY_QUERY = "join customers_vs_coupons as cc on c.id=cc.coupon_id join category as cat on cat.id=c.category_id where cc.customer_id=? and c.category_id=?";
	public static final String ADD_COUPON_QUERY = "INSERT INTO `coupon` (`company_id`, `category_id`, `title`, `description`, `start_date`, `end_date`, `amount`, `price`, `image`) VALUES (?	, ?, ?, ?, ?, ?, ?, ?,?)";
	public static final String IS_COUPON_EXISTS_QUERY = "select * from `coupon` where `title`=? and `company_id`=?";
//	 public static final String IS_OTHER_COUPON_EXISTS_QUERY = "select * from `coupon` where `title`='_TITLE_' and `company_id`=? and not `id`=?";
	public static final String ADD_CUSTOMERS_VS_COUPONS_QUERY = "INSERT INTO `customers_vs_coupons` (`customer_id`, `coupon_id`) VALUES (?, ?)";
	public static final String DELETE_CUSTOMERS_VS_COUPONS_QUERY = "DELETE FROM `customers_vs_coupons` WHERE (`customer_id` = ?) and (`coupon_id` = ?)";
	// place_holders section
	public static final String TABLE_PLACE_HOLDER = "_TABLE_";
	public static final String PARAMETER_PLACE_HOLDER = "_PARAMETER_";
	public static final String EMAIL_PLACE_HOLDER = "_EMAIL_";
	public static final String TITLE_PLACE_HOLDER = "_TITLE_";
	public static final String COMPANY_ID_PLACE_HOLDER = "_COMPANY_ID_";
	public static final String ID_PLACE_HOLDER = "_ID_";
	public static final String CATEGORY_ID_PLACE_HOLDER = "_CATEGORY_ID_";

	public static void runQuery(String sql) throws DBException {
		Connection connection = null;
		try {
			// STEP 2 - Taking a connection from Connection Pool
			connection = ConnectionPool.getInstance().getConnection();

			// STEP 3 - Run your SQL instruction
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.execute();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Return a connection to the Connection Pool
			ConnectionPool.getInstance().returnConnection(connection);

		}
	}

	public static void returnConnection(Connection connection) throws DBException {
		if (connection != null) {
			ConnectionPool.getInstance().returnConnection(connection);
			connection = null;
		}
	}
}
