package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import beans.Coupon;
import connection.ConnectionPool;
import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;
import utils.CouponUtil;
import utils.DBUtils;
import utils.DateTimeUtil;
import utils.StringHelper;

public class CouponDao implements ICouponDao {

	public CouponDao() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void add(Coupon addObject) throws DBException, ThreadException, MisMatchObjectException {
		boolean fromUpdate = false;
		String sql = DBUtils.ADD_COUPON_QUERY;

		if (StringHelper.allParametersNotEmpty(addObject, fromUpdate)) {

			// Coupon not exists
			Connection connection = ConnectionPool.getInstance().getConnection();
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, addObject.getCompanyID());
				statement.setInt(2, addObject.getCategory().ordinal());
				statement.setString(3, addObject.getTitle());
				statement.setString(4, addObject.getDescription());
				statement.setDate(5, DateTimeUtil.convertLocalDate2SQLDate(addObject.getStartDate()));
				statement.setDate(6, DateTimeUtil.convertLocalDate2SQLDate(addObject.getEndDate()));
				statement.setInt(7, addObject.getAmount());
				statement.setDouble(8, addObject.getPrice());
				statement.setString(9, addObject.getImage());
				System.out.println("add sql: " + statement);
				statement.execute();
				DBUtils.returnConnection(connection);

				System.out.println(StringHelper.COUPON_ADD_MESSAGE + addObject.getTitle());

			} catch (SQLException e) {
				DBUtils.returnConnection(connection);
				throw new DBException(StringHelper.COUPON_GET_EXCEPTION + e.getCause());
			}
		}
	}

	@Override
	public void update(Coupon updateObject) throws DBException, ThreadException, MisMatchObjectException {
		boolean fromUpdate = true;
		String sql = DBUtils.UPDATE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.UPDATE_PARAMETER);
		if (StringHelper.allParametersNotEmpty(updateObject, fromUpdate)) {
			Connection connection = ConnectionPool.getInstance().getConnection();
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, updateObject.getCompanyID());
				statement.setInt(2, updateObject.getCategory().ordinal());
				statement.setString(3, updateObject.getTitle());
				statement.setString(4, updateObject.getDescription());
				statement.setDate(5, DateTimeUtil.convertLocalDate2SQLDate(updateObject.getStartDate()));
				statement.setDate(6, DateTimeUtil.convertLocalDate2SQLDate(updateObject.getEndDate()));
				statement.setInt(7, updateObject.getAmount());
				statement.setDouble(8, updateObject.getPrice());
				statement.setString(9, updateObject.getImage());
				statement.setInt(10, updateObject.getId());
				System.out.println("update sql: " + statement);
				int result = statement.executeUpdate();
				DBUtils.returnConnection(connection);
				if (result > 0) {
					System.out.println(StringHelper.COUPON_UPDATE_MESSAGE + updateObject.getDescription() + " "
							+ updateObject.getTitle());
				}
			} catch (SQLException e) {
				DBUtils.returnConnection(connection);
				throw new DBException(StringHelper.COUPON_UPDATE_EXCEPTION + e);

			}
		}
	}

	@Override
	public void delete(int id) throws DBException, ThreadException {
		String sql = DBUtils.DELETE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.PARAMETER_ID);
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			System.out.println("Delete Query: " + statement);
			int result = statement.executeUpdate();
			DBUtils.returnConnection(connection);
			// TODO jeka check return result
			if (result > 0) {
				System.out.println(StringHelper.COUPON_DELETE_MESSAGE);
			} else {
				System.out.println();
			}
		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COUPON_DELETE_EXCEPTION + e);
		}

	}

	public void delete(String sql, Map<Integer, Object> parameters) throws DBException, ThreadException {
		int resultInt = CouponUtil.executeUpdate(sql, parameters);
		if (resultInt > 0) {
			System.out.println(StringHelper.COUPON_DELETE_MESSAGE);
		} else {
			System.out.println(StringHelper.COUPON_DELETE_FAILED_MESSAGE);
		}

	}

	@Override
	public Coupon get(int id) throws DBException, ThreadException {
		Coupon coupon = null;
		String sql = DBUtils.GET_ONE_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE)
				.replace(DBUtils.PARAMETER_PLACE_HOLDER, CouponUtil.PARAMETER_ID);
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			System.out.println("get query: " + statement);
			ResultSet resultSet = statement.executeQuery();
			DBUtils.returnConnection(connection);
			if (resultSet != null) {
				// System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
				if (resultSet.next()) {
					coupon = CouponUtil.resultSetToCoupon(resultSet);

				}
			} else {

				System.out.println(StringHelper.RESULT_SET_ISNULL_MESSAGE);
			}
		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COUPON_GET_EXCEPTION + e);
		}
		return coupon;
	}

	@Override
	public List<Coupon> get() throws DBException, ThreadException {
		List<Coupon> coupons = new ArrayList<Coupon>();
		String sql = DBUtils.GET_ALL_QUERY.replace(DBUtils.TABLE_PLACE_HOLDER, CouponUtil.TABLE);
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			ResultSet resultSet = statement.executeQuery();
			DBUtils.returnConnection(connection);
			if (resultSet != null) {
				System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
				while (resultSet.next()) {
					Coupon coupon = CouponUtil.resultSetToCoupon(resultSet);
					coupons.add(coupon);

				}
			} else {

				System.out.println(StringHelper.RESULT_SET_ISNULL_MESSAGE);
			}
		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COUPON_ADD_EXCEPTION + e);
		}
		return coupons;
	}

	@Override
	public List<Coupon> get(String sql) throws DBException, ThreadException {
		List<Coupon> coupons = new ArrayList<Coupon>();
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			System.out.println("get sql: " + statement);
			ResultSet resultSet = statement.executeQuery();
			DBUtils.returnConnection(connection);
			if (resultSet != null) {
				System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
				while (resultSet.next()) {
					Coupon coupon = CouponUtil.resultSetToCoupon(resultSet);
					coupons.add(coupon);
				}
			} else {
				System.out.println(StringHelper.RESULT_SET_ISNULL_MESSAGE);
			}
		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COUPON_ADD_EXCEPTION + e);
		}
		return coupons;
	}

	public List<Coupon> get(String sql, Map<Integer, Object> parameters)
			throws ThreadException, DBException, SQLException {
		List<Coupon> coupons = CouponUtil.executeQuery(sql, parameters);
		return coupons;
	}

	@Override
	public void addCouponPurchase(int customerID, int couponID) throws ThreadException, DBException {

		String sql = DBUtils.ADD_CUSTOMERS_VS_COUPONS_QUERY;
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, customerID);
			statement.setInt(2, couponID);
			System.out.println("add  addCouponPurchase sql: " + statement);
			statement.execute();
			DBUtils.returnConnection(connection);

			System.out.println(StringHelper.COUPONPURCHASE_ADD_MESSAGE + customerID + "," + couponID);
		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COUPONPURCHASE_ADD_EXCEPTION + e);
		}

	}

	@Override
	public void deleteCouponPurchase(int customerID, int couponID) throws ThreadException, DBException {
		String sql = DBUtils.DELETE_CUSTOMERS_VS_COUPONS_QUERY;
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, customerID);
			statement.setInt(2, couponID);
			System.out.println("delete  CouponPurchase sql: " + statement);
			statement.execute();
			DBUtils.returnConnection(connection);

			System.out.println(StringHelper.COUPONPURCHASE_DELETE_MESSAGE + customerID + "," + couponID);
		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COUPONPURCHASE_DELETE_EXCEPTION + e);
		}

	}

	public boolean isExist(Coupon coupon) throws ThreadException, DBException {
		String sql = DBUtils.IS_COUPON_EXISTS_QUERY;
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, coupon.getTitle());
			statement.setInt(2, coupon.getCompanyID());
			System.out.println("sql: " + statement);
			ResultSet resultSet = statement.executeQuery();
			DBUtils.returnConnection(connection);
			if (resultSet != null) {
				if (resultSet.next()) {
					if (resultSet.getInt(1) > 0) {
						System.out.println(String.format(StringHelper.COUPON_EXIST_MESSAGE, coupon.getTitle(),
								coupon.getCompanyID()));
						return true;
					}
					System.out.println(String.format(StringHelper.COUPON_ISEXISTS_EXCEPTION, coupon.getTitle(),
							coupon.getCompanyID()));
					return false;
				}
			} else {
				throw new DBException(StringHelper.RESULT_SET_ISNULL_MESSAGE);
			}
		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COUPON_GET_EXCEPTION + e);
		}
		throw new DBException(StringHelper.COUPON_ISEXISTS_EXCEPTION);
	}

	@Override
	public int getCount(String sql) throws DBException, ThreadException {
		Connection connection = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			System.out.println("getCount sql: " + statement);
			ResultSet resultSet = statement.executeQuery();
			DBUtils.returnConnection(connection);
			System.out.println(StringHelper.RESULT_SET_IS_NOT_NULL_MESSAGE);
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}

		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COMPANY_EXCEPTION + e);
		}
		return 0;

	}

}
