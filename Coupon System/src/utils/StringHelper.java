package utils;

import beans.Company;
import beans.Coupon;
import beans.Customer;
import exception.MisMatchObjectException;

public class StringHelper {
	// message section
	public static final String RESULT_SET_ISNULL_MESSAGE = "resultSet is null";
	public static final String RESULT_SET_IS_NOT_NULL_MESSAGE = "resultSet is not null";
	public static final String VALIDATION_PARAMETERS_ERROR_MESSAGE = "validation: some parameters is null or empty";
	public static final String VALIDATION_OK_MESSAGE = "validation ok";
	public static final String COUPON_ADD_MESSAGE = "The coupon  added: ";
	public static final String COUPON_UPDATE_MESSAGE = "Update coupon success: ";
	public static final String COUPON_DELETE_MESSAGE = "The coupon deleted successfully";
	public static final String COUPON_DELETE_FAILED_MESSAGE = "The coupon deleted failed";
	public static final String COUPONPURCHASE_ADD_MESSAGE = "The couponPurchase  added: ";
	public static final String COUPONPURCHASE_DELETE_MESSAGE = "The couponPurchase deleted: ";
	public static final String COUPON_EXIST_MESSAGE = "isCoupon exists with title %s, companyID %s";
	public static final String CUSTOMER_ADD_MESSAGE = "The customer added: ";
	public static final String CUSTOMER_UPDATE_MESSAGE = "Update customer success: ";
	public static final String CUSTOMER_UPDATE_FAILED_MESSAGE = "Update customer failed: ";
	public static final String CUSTOMER_DELETE_MESSAGE = "The customer deleted successfully";
	public static final String CUSTOMER_DELETE_FAILED_MESSAGE = "The customer delete failed.";
	public static final String CUSTOMER_EXISTS_MESSAGE = "IsCustomer exists with email ";
	public static final String CUSTOMER_EXISTS_FALSE_MESSAGE = "IsCustomer not exists with email ";
	public static final String COUPON_NOT_EXISTS_MESSAGE = "isCoupon not exists with title %s, companyID %s";
	public static final String COMPANY_UPDATE_FAILED_MESSAGE = "Update company failed: ";
	public static final String CONNECTIONS_ALL_CLOSE_MESSAGE = "all connection is closed. exit";
	public static final String COMPANY_ADD_MESSAGE = "The company added: ";
	public static final String COMPANY_UPDATE_MESSAGE = "Update company success: ";
	public static final String COMPANY_DELETE_SUCCESS_MESSAGE = "The company deleted successfully";
	public static final String COMPANY_DELETE_FAILED_MESSAGE = "The company delete failed.";
	public static final String COMPANY_DELETE_EXIST_MESSAGE = "IsCompany exists with name ";
	public static final String COMPANY_DELETE_NOT_EXIST_MESSAGE = "IsCompany not exists with name ";
	public static final String COMPANY_OTHER_EXIST_MESSAGE = "is other company exists with id ";
	public static final String COMPANY_OTHER_NOT_EXIST_MESSAGE = "is other company not exists with id ";
	public static final String CUSTOMER_OTHER_EXIST_MESSAGE = "IsOtherCustomer exists with id ";
	public static final String CUSTOMER_OTHER_NOT_EXIST_MESSAGE = "IsOtherCustomer not exists with id ";
	// exception section
	public static final String COMPANY_EXCEPTION = "get company exception. ";
	public static final String CONNECTION_EXCEPTION = "open connection failed. ";
	public static final String WAIT_EXCEPTION = "wait failed. ";
	public static final String COMPANY_VALIDATION_EXCEPTION = "Company is not valid";
	public static final String COUPON_GET_EXCEPTION = "get coupon Exception";
	public static final String COUPON_UPDATE_EXCEPTION = "update coupon Exception";
	public static final String COUPON_DELETE_EXCEPTION = "The coupons delete failed.";
	public static final String RESULTSET_EXCEPTION = "Read resultSet Exception. ";
	public static final String COUPON_ADD_EXCEPTION = "Coupon add Exception";
	public static final String COUPONPURCHASE_ADD_EXCEPTION = "addCouponPurchase Exception";
	public static final String COUPONPURCHASE_DELETE_EXCEPTION = "delete CouponPurchase Exception";

	public static final String CUSTOMER_ADD_EXCEPTION = "customer add Exception ";
	public static final String CUSTOMER_UPDATE_EXCEPTION = "update customer Exception";
	public static final String CUSTOMER_DELETE_EXCEPTION = "customer delete Exception";
	public static final String CUSTOMER_GET_EXCEPTION = "customer get Exception";
	public static final String CUSTOMER_EXISTS_EXCEPTION = "IsCustomer exists Exception ";
	public static final String MISMATCH_OBJECT_EXCEPTION = "All parameters exception: object mismatch";
	public static final String LOGIN_EXCEPTION = "Login failed ";
	public static final String CONNECTION_CLOSE_EXCEPTION = "connection close exception: ";
	public static final String FILES_READ_OPEN_EXCEPTION = "read/open file exception: ";

	/**
	 * check all parameters for Coupon/Company/Customer for new or update object.
	 * New object haven't ID. If object unknown throws exception.
	 * 
	 * @param obj
	 * @param fromUpdate
	 * @return
	 * @throws MisMatchObjectException
	 */
	public static boolean allParametersNotEmpty(Object obj, boolean fromUpdate) throws MisMatchObjectException {
		if (obj instanceof Customer) {
			Customer customer = (Customer) obj;
			if (fromUpdate) {
				return customer != null && customer.getId() > 0 && customer.getFirstName() != null
						&& !customer.getFirstName().isEmpty() && customer.getLastName() != null
						&& !customer.getLastName().isEmpty() && customer.getEmail() != null
						&& !customer.getEmail().isEmpty() && customer.getPassword() != null
						&& !customer.getPassword().isEmpty();
			}
			return customer != null && customer.getFirstName() != null && !customer.getFirstName().isEmpty()
					&& customer.getLastName() != null && !customer.getLastName().isEmpty()
					&& customer.getEmail() != null && !customer.getEmail().isEmpty() && customer.getPassword() != null
					&& !customer.getPassword().isEmpty();
		} else if (obj instanceof Coupon) {
			Coupon coupon = (Coupon) obj;
			// check with id
			if (fromUpdate) {
				return coupon != null && coupon.getAmount() > -1 && coupon.getCategory() != null
						&& coupon.getCompanyID() > 0 && coupon.getDescription() != null
						&& !coupon.getDescription().isEmpty() && coupon.getEndDate() != null
						&& coupon.getImage() != null && !coupon.getImage().isEmpty() && coupon.getPrice() > 0
						&& coupon.getStartDate() != null && coupon.getTitle() != null && !coupon.getTitle().isEmpty()
						&& coupon.getId() > 0;
			}
			// check without id
			return coupon != null && coupon.getAmount() > -1 && coupon.getCategory() != null
					&& coupon.getCompanyID() > 0 && coupon.getDescription() != null
					&& !coupon.getDescription().isEmpty() && coupon.getEndDate() != null && coupon.getImage() != null
					&& !coupon.getImage().isEmpty() && coupon.getPrice() > 0 && coupon.getStartDate() != null
					&& coupon.getTitle() != null && !coupon.getTitle().isEmpty();
		} else if (obj instanceof Company) {
			Company company = (Company) obj;
			if (fromUpdate) {
				return company != null && company.getId() > 0 && company.getName() != null
						&& !company.getName().isEmpty() && company.getEmail() != null && !company.getEmail().isEmpty()
						&& company.getPassword() != null && !company.getPassword().isEmpty();
			}
			return company != null && company.getName() != null && !company.getName().isEmpty()
					&& company.getEmail() != null && !company.getEmail().isEmpty() && company.getPassword() != null
					&& !company.getPassword().isEmpty();
		}
		throw new MisMatchObjectException(MISMATCH_OBJECT_EXCEPTION);
	}

}
