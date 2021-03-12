package facades;

import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import dao.CompanyDao;
import dao.CouponDao;
import dao.CustomerDao;
import exception.DBException;
import exception.ThreadException;

public abstract class ClientFacade {
	protected CompanyDao companyDao = new CompanyDao();
	protected CustomerDao customerDao = new CustomerDao();
	protected CouponDao couponDao = new CouponDao();

	public abstract boolean login(String email, String password)
			throws ThreadException, DBException, LoginException, SQLException;
}
