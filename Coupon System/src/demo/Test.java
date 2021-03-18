package demo;

import java.io.IOException;
import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;

public class Test {

	public static void main(String[] args) {
		try {
			new CouponSystem().testAll();
		} catch (LoginException | ThreadException | DBException | SQLException | IOException | MisMatchObjectException
				| InterruptedException e) {
			System.out.println("Coupon System exception:" + e);
		}
	}

}
