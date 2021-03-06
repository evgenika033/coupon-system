package dao;

import beans.Customer;
import exception.DBException;
import exception.ThreadException;

public interface ICustomerDao extends IDaoCRUD<Customer> {
	boolean isExists(String email) throws ThreadException, DBException;
}
