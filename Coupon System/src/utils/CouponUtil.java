package utils;

public class CouponUtil {
	public static final String TABLE = "coupon";
	public static final String PARAMETER_ID = "id=?";
	public static final String UPDATE_PARAMETER = "`company_id` = ?, `category_id` = ?, `title` = ?, `description` = ?, `start_date` = ?, `end_date` =?, `amount` = ?, `price` = ?, `image` = ?";
	public static final String GET_BY_COMPANY_ID = "`company_id` = _COMPANY_ID_";
}
