package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import beans.Company;
import connection.ConnectionPool;
import exception.DBException;
import exception.ThreadException;

public class CompanyUtil {
	public static final String TABLE = "company";
	public static final String PARAMETER_ID = "id=?";
	public static final String PARAMETER_NAME_EMAIL = "`name`=?, `email`=?";
	public static final String PARAMETER_EMAIL_AND_NOT_ID = "`email`=? and not `id`=?";
	public static final String UPDATE_EMAIL_PASSWORD_PARAMETER = "`email`=?,`password` = ?";

	public static List<Company> executeQuery(String sql, Map<Integer, Object> parameters)
			throws ThreadException, DBException, SQLException {
		List<Company> companies = new ArrayList<Company>();
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
			ResultSet resultSet = statement.executeQuery();
			DBUtils.returnConnection(connection);
			while (resultSet.next()) {
				companies.add(resultSetToCompany(resultSet));
			}

		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COUPON_ADD_EXCEPTION + e);
		}
		return companies;
	}

	public static List<Company> executeQuery(String sql) throws ThreadException, DBException, SQLException {
		List<Company> companies = new ArrayList<Company>();
		Connection connection = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			System.out.println("get sql: " + statement);
			ResultSet resultSet = statement.executeQuery();
			DBUtils.returnConnection(connection);
			while (resultSet.next()) {
				companies.add(resultSetToCompany(resultSet));
			}

		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.CUSTOMER_GET_EXCEPTION + e);
		}
		return companies;
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

	public static Company resultSetToCompany(ResultSet resultSet) throws DBException {
		try {
			int id = resultSet.getInt(1);
			String name = resultSet.getString(2);
			String email = resultSet.getString(3);
			String password = resultSet.getString(4);
			return new Company(id, name, email, password);
		} catch (SQLException e) {
			throw new DBException("Read resultSet Exception. " + e);
		}
	}
}
