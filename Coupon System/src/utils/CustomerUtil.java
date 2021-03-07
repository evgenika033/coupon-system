package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import beans.Customer;
import connection.ConnectionPool;
import exception.DBException;
import exception.ThreadException;

public class CustomerUtil {
	public static final String TABLE = "customer";
	public static final String PARAMETER_ID = "id=?";
	public static final String UPDATE_PARAMETER = "`first_name` = ?, `last_name` = ?, `email` = ?, `password` = ?";

	public static List<Customer> executeQuery(String sql, Map<Integer, Object> parameters)
			throws ThreadException, DBException, SQLException {
		List<Customer> customers = new ArrayList<Customer>();
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
				// companies.add(resultSetToCompany(resultSet));
			}

		} catch (SQLException e) {
			DBUtils.returnConnection(connection);
			throw new DBException(StringHelper.COUPON_ADD_EXCEPTION + e);
		}
		return customers;
	}

	public static Customer resultSetToCustomer(ResultSet resultSet) throws DBException {
		try {
			int id = resultSet.getInt(1);
			String firstName = resultSet.getString(2);
			String lastName = resultSet.getString(3);
			String email = resultSet.getString(4);
			String password = resultSet.getString(5);
			return new Customer(id, firstName, lastName, email, password);
		} catch (SQLException e) {
			throw new DBException(StringHelper.RESULTSET_EXCEPTION + e);
		}
	}
}
