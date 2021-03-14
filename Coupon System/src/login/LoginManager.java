package login;

import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import exception.DBException;
import exception.ThreadException;
import facades.AdminFacade;
import facades.ClientFacade;
import facades.CompanyFacade;
import facades.CustomerFacade;

public class LoginManager {

	private static LoginManager instance;

	private LoginManager() {

	}

	// singelton thread safe with double check
	public static LoginManager getInstance() {
		if (instance == null) {
			synchronized (LoginManager.class) {
				if (instance == null) {
					instance = new LoginManager();
				}
			}
		}
		return instance;
	}

	private ClientFacade adminLogin(String email, String password) {
		AdminFacade adminFacade = new AdminFacade();
		if (adminFacade.login(email, password)) {
			System.out.println("login admin success: " + email);
			return adminFacade;
		}
		System.out.println("login admin failed: " + email);
		return null;
	}

	private ClientFacade companyLogin(String email, String password)
			throws LoginException, ThreadException, DBException, SQLException {
		CompanyFacade companyFacade = new CompanyFacade();
		if (companyFacade.login(email, password)) {
			System.out.println("login company success: " + email);
			return companyFacade;
		}
		System.out.println("login company failed: " + email);
		return null;
	}

	private ClientFacade customerLogin(String email, String password)
			throws LoginException, ThreadException, DBException, SQLException {
		CustomerFacade customerFacade = new CustomerFacade();
		if (customerFacade.login(email, password)) {
			System.out.println("login customer success: " + email);
			return customerFacade;
		}
		System.out.println("login customer failed: " + email);
		return null;
	}

	public ClientFacade login(String email, String password, ClientType clientTipe)
			throws LoginException, ThreadException, DBException, SQLException {
		switch (clientTipe) {
		case ADMINISTRATOR:
			return adminLogin(email, password);
		case COMPANY:
			return companyLogin(email, password);
		case CUSTOMER:
			return customerLogin(email, password);
		default:
			return null;
		}

	}

}
