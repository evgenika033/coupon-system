package utils;

import java.sql.Date;
import java.time.LocalDate;

public class DateTimeUtil {
	public static LocalDate convertSQLDate2LocalDate(Date date) {
		return date.toLocalDate();
	}

	public static Date convertLocalDate2SQLDate(LocalDate localDate) {
		return Date.valueOf(localDate);
	}

}
