package dao;

import java.sql.SQLException;

import beans.Company;
import exception.DBException;
import exception.ThreadException;

public interface ICompanyDao extends IDaoCRUD<Company> {
	/**
	 * return isExists if company with email+password exist
	 * 
	 * @param email
	 * @param password
	 * @return boolean
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 */
	boolean isExists(String email, String password) throws ThreadException, DBException, SQLException;

	/**
	 * return Company if company with email+password exist
	 * 
	 * @param email
	 * @param password
	 * @return Company
	 * @throws ThreadException
	 * @throws DBException
	 * @throws SQLException
	 */
	Company login(String email, String password) throws ThreadException, DBException, SQLException;

}
