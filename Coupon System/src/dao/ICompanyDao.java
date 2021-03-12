package dao;

import java.sql.SQLException;

import beans.Company;
import exception.DBException;
import exception.ThreadException;

public interface ICompanyDao extends IDaoCRUD<Company> {
	boolean isExists(String email, String password) throws ThreadException, DBException, SQLException;

	Company login(String email, String password) throws ThreadException, DBException, SQLException;

}
