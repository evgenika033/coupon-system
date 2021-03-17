package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import beans.Category;
import beans.Coupon;
import connection.ConnectionPool;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;

public class CouponUtil {
	public static final String TABLE = "`coupons_system`.`coupon`";
	public static final String TABLE_CUSTOMERS_VS_COUPONS = "`coupons_system`.`customers_vs_coupons`";
	public static final String PARAMETER_ID = "id=?";
	public static final String UPDATE_PARAMETER = "`company_id` = ?, `category_id` = ?, `title` = ?, `description` = ?, `start_date` = ?, `end_date` =?, `amount` = ?, `price` = ?, `image` = ?";
	public static final String GET_BY_COMPANY_ID = "`company_id` =?";
	public static final String GET_BY_COMPANY_ID_AND_CATEGORY = "`company_id` =?  and `category_id`=?";
	public static final String GET_BY_COMPANY_ID_AND_MAX_PRICE = "`company_id` =?  and `price`<?";
	public static final CharSequence GET_BY_CUSTOMER_ID_AND_MAX_PRICE = "`customer_id` =?  and `price`<?";
	public static final CharSequence GET_BY_COMPANY_AND_CATEGORY = "`company_id` =?  and `category_id`=?";
	public static final String GET_OTHER_COUPON_BY_TITLE_AND_COMPANY = "`title`=? and `company_id`=? and not `id`=?";
	public static final String GET_CUSTOMERS_FROM_CUSTOMERS_VS_COUPONS_BY_COUPON_ID = "select customer_id from `coupons_system`.`customers_vs_coupons` where coupon_id=?";
	public static final String GET_COUPONS_FROM_CUSTOMERS_VS_COUPONS_BY_CUSTOMER_ID = "select coupon_id from `coupons_system`.`customers_vs_coupons` where customer_id=?";
	public static final String GET_COUPONS_BY_END_DATE = "`end_date` <?";
	public static final String GET_FROM_CUSTOMERS_VS_COUPONS_BY_CUSTOMER_AND_COUPON = "`customer_id`=? and `coupon_id`=?";

	/**
	 * Method for get result like select. Get SQL query and parameters, return
	 * coupons list
	 * 
	 * @param sql
	 * @param parameters
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 */
	public static List<Coupon> executeQuery(String sql, Map<Integer, Object> parameters)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		List<Coupon> coupons = new ArrayList<Coupon>();
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			parameters.entrySet().forEach(p -> {
				if (p.getValue() instanceof String) {
					try {
						statement.setString(p.getKey().intValue(), (String) p.getValue());
					} catch (SQLException e) {
						System.err.println("setString exception: " + e);
					}
				} else if (p.getValue() instanceof Integer) {
					try {
						statement.setInt(p.getKey().intValue(), ((Integer) p.getValue()).intValue());
					} catch (SQLException e) {
						System.err.println("setInt exception: " + e);
					}
				} else if (p.getValue() instanceof Double) {
					try {
						statement.setDouble(p.getKey().intValue(), ((Double) p.getValue()).doubleValue());
					} catch (SQLException e) {
						System.err.println("setDouble exception: " + e);
					}
				} else if (p.getValue() instanceof LocalDate) {
					try {
						statement.setDate(p.getKey().intValue(),
								(DateTimeUtil.convertLocalDate2SQLDate((LocalDate) p.getValue())));
					} catch (SQLException e) {
						System.err.println("setSQL exception: " + e);
					}
				} else {
					// throw new MisMatchObjectException(StringHelper.MISMATCH_OBJECT_EXCEPTION);
				}

			});
			System.out.println("get sql: " + statement);
			ResultSet resultSet = statement.executeQuery();
			DBUtils.returnConnection(connection);
			while (resultSet.next()) {
				coupons.add(resultSetToCoupon(resultSet));
			}

		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COUPON_ADD_EXCEPTION + e);
		}
		return coupons;
	}

	/**
	 * get list of customers id from customers_vs_coupons
	 * 
	 * @param sql
	 * @param parameters
	 * @return
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 * @throws MisMatchObjectException
	 */
	public static List<Integer> executeQuerySpecial(String sql, Map<Integer, Object> parameters)
			throws ThreadException, DBException, SQLException, MisMatchObjectException {
		List<Integer> customers = new ArrayList<>();
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			parameters.entrySet().forEach(p -> {
				if (p.getValue() instanceof String) {
					try {
						statement.setString(p.getKey().intValue(), (String) p.getValue());
					} catch (SQLException e) {
						System.err.println("setString exception: " + e);
					}
				} else if (p.getValue() instanceof Integer) {
					try {
						statement.setInt(p.getKey().intValue(), ((Integer) p.getValue()).intValue());
					} catch (SQLException e) {
						System.err.println("setInt exception: " + e);
					}
				} else if (p.getValue() instanceof Double) {
					try {
						statement.setDouble(p.getKey().intValue(), ((Double) p.getValue()).doubleValue());
					} catch (SQLException e) {
						System.err.println("setDouble exception: " + e);
					}
				} else if (p.getValue() instanceof LocalDate) {
					try {
						statement.setDate(p.getKey().intValue(),
								(DateTimeUtil.convertLocalDate2SQLDate((LocalDate) p.getValue())));
					} catch (SQLException e) {
						System.err.println("setSQL exception: " + e);
					}
				} else {
					// throw new MisMatchObjectException(StringHelper.MISMATCH_OBJECT_EXCEPTION);
				}

			});
			System.out.println("get sql: " + statement);
			ResultSet resultSet = statement.executeQuery();
			DBUtils.returnConnection(connection);
			while (resultSet.next()) {
				customers.add(resultSet.getInt(1));
			}

		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COUPON_ADD_EXCEPTION + e);
		}
		return customers;
	}

	// get list of coupons without parameters
	public static List<Coupon> executeQuery(String sql) throws ThreadException, DBException, SQLException {
		List<Coupon> coupons = new ArrayList<Coupon>();
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			System.out.println("get sql: " + statement);
			ResultSet resultSet = statement.executeQuery();
			DBUtils.returnConnection(connection);
			while (resultSet.next()) {
				coupons.add(resultSetToCoupon(resultSet));
			}

		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COUPON_GET_EXCEPTION + e);
		}
		return coupons;
	}

	public static int executeUpdate(String sql, Map<Integer, Object> parameters) throws ThreadException, DBException {
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			parameters.entrySet().forEach(p -> {
				if (p.getValue() instanceof String) {
					try {
						statement.setString(p.getKey().intValue(), (String) p.getValue());
					} catch (SQLException e) {
						System.err.println("setString exception: " + e);
					}
				} else if (p.getValue() instanceof Integer) {
					try {
						statement.setInt(p.getKey().intValue(), ((Integer) p.getValue()).intValue());
					} catch (SQLException e) {
						System.err.println("setInt exception: " + e);
					}
				} else if (p.getValue() instanceof Double) {
					try {
						statement.setDouble(p.getKey().intValue(), ((Double) p.getValue()).doubleValue());
					} catch (SQLException e) {
						System.err.println("setDouble exception: " + e);
					}
				} else if (p.getValue() instanceof LocalDate) {
					try {
						statement.setDate(p.getKey().intValue(),
								(DateTimeUtil.convertLocalDate2SQLDate((LocalDate) p.getValue())));
					} catch (SQLException e) {
						System.err.println("setSQL exception: " + e);
					}
				}
			});
			System.out.println("get sql: " + statement);
			int resultInt = statement.executeUpdate();
			DBUtils.returnConnection(connection);
			return resultInt;
		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COUPON_ADD_EXCEPTION + e);
		}

	}

	public static void execute(String sql, Map<Integer, Object> parameters) throws DBException, ThreadException {
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			parameters.entrySet().forEach(p -> {
				if (p.getValue() instanceof String) {
					try {
						statement.setString(p.getKey().intValue(), (String) p.getValue());
					} catch (SQLException e) {
						System.err.println("setString exception: " + e);
					}
				} else if (p.getValue() instanceof Integer) {
					try {
						statement.setInt(p.getKey().intValue(), ((Integer) p.getValue()).intValue());
					} catch (SQLException e) {
						System.err.println("setInt exception: " + e);
					}
				} else if (p.getValue() instanceof Double) {
					try {
						statement.setDouble(p.getKey().intValue(), ((Double) p.getValue()).doubleValue());
					} catch (SQLException e) {
						System.err.println("setDouble exception: " + e);
					}
				} else if (p.getValue() instanceof LocalDate) {
					try {
						statement.setDate(p.getKey().intValue(),
								(DateTimeUtil.convertLocalDate2SQLDate((LocalDate) p.getValue())));
					} catch (SQLException e) {
						System.err.println("setSQL exception: " + e);
					}
				}
			});
			System.out.println("add sql: " + statement);
			statement.execute();
			DBUtils.returnConnection(connection);

		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COUPON_ADD_EXCEPTION + e);
		}
	}

	public static Coupon resultSetToCoupon(ResultSet resultSet) throws DBException {
		try {
			int id = resultSet.getInt(1);
			int companyID = resultSet.getInt(2);
			Category category = Category.values()[resultSet.getInt(3)];
			String title = resultSet.getString(4);
			String description = resultSet.getString(5);
			LocalDate startDate = DateTimeUtil.convertSQLDate2LocalDate(resultSet.getDate(6));
			LocalDate endDate = DateTimeUtil.convertSQLDate2LocalDate(resultSet.getDate(7));
			int amount = resultSet.getInt(8);
			double price = resultSet.getDouble(9);
			String image = resultSet.getString(10);
			return new Coupon(id, companyID, category, title, description, startDate, endDate, amount, price, image);
		} catch (SQLException e) {
			throw new DBException(StringHelper.RESULTSET_EXCEPTION + e);

		}
	}

}
