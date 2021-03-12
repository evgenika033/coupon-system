package dao;

import java.sql.SQLException;

import beans.Customer;
import exception.DBException;
import exception.ThreadException;

public interface ICustomerDao extends IDaoCRUD<Customer> {
	boolean isExists(String email) throws ThreadException, DBException, SQLException;

	Customer login(String email, String password) throws ThreadException, DBException, SQLException;

}
