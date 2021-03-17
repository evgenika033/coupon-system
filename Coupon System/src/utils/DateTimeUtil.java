package utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
	public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static LocalDate convertSQLDate2LocalDate(Date date) {
		return date.toLocalDate();
	}

	public static Date convertLocalDate2SQLDate(LocalDate localDate) {
		return Date.valueOf(localDate);
	}

	public static LocalDate convertStringToLocalDate(String dateStr) {
		return LocalDate.parse(dateStr, dtf);
	}

}
