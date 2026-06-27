package br.com.am.util;

import java.text.SimpleDateFormat;

public class FormatDate {

	public static String format(java.sql.Timestamp DATE, String PATTERN) {
		String date = "";
		if (DATE != null) {
			if ((PATTERN != null) && (PATTERN.length() > 0)
					&& !PATTERN.equalsIgnoreCase("null")) {
				SimpleDateFormat df = new SimpleDateFormat(PATTERN);
				date = df.format(DATE);
			} else {
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				date = df.format(DATE);
			}
		}
		return date;
	}

	public static String format(java.sql.Timestamp DATE) {
		return format(DATE, "dd/MM/yyyy HH:mm:ss");
	}

	public static java.util.Date parse(String DATE) throws Exception {
		return parse(DATE, "dd/MM/yyyy");
	}

	public static java.util.Date parse(String DATE, String PATTERN) throws Exception {
		java.util.Date date = null;
		if ((PATTERN != null) && (PATTERN.length() > 0)
				&& !PATTERN.equalsIgnoreCase("null")) {
			try {
				SimpleDateFormat df = new SimpleDateFormat(PATTERN);
				if ((DATE != null) && (DATE.length() > 0)) {
					date = df.parse(DATE);
				}
			} catch (Exception e) {
				throw e;
			}
		} else {
			date = parse(DATE);
		}
		return date;
	}

	public static java.sql.Date parseDate(String DATE) throws Exception {
		return parseDate(DATE, "dd/MM/yyyy");
	}

	public static java.sql.Date parseDate(String DATE, String PATTERN) throws Exception {
		java.sql.Date date = null;
		if ((PATTERN != null) && (PATTERN.length() > 0)
				&& !PATTERN.equalsIgnoreCase("null")) {
			try {
				java.util.Date dt = parse(DATE, PATTERN);
				if (dt != null) {
					date = new java.sql.Date(dt.getTime());
				}
			} catch (Exception e) {
				throw e;
			}
		} else {
			date = parseDate(DATE);
		}
		return date;
	}
}
