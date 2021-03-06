package dao;

import beans.Company;
import exception.DBException;
import exception.ThreadException;

public interface ICompanyDao extends IDaoCRUD<Company> {
	boolean isExists(String email, String password) throws ThreadException, DBException;

	Company login(String email, String password) throws ThreadException, DBException;

}
