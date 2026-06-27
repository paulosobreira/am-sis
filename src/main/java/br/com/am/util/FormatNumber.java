package br.com.am.util;


import java.text.DecimalFormat;

/**
 * Class: FormatNumber
 * <p>
 * 
 * This class is responsable for all number format and conversions.
 * 
 * @author Anderson Teixeira & Ronaldo Costa <anderson@lojasmaia.com.br,
 *         ronaldo@lojasmaia.com.br>
 * @version 2.2 (18-nov-2003)
 * @since 1.0 (22-oct-2002)
 * 
 */
public class FormatNumber extends Object {

	/**
	 * Method for formating a double number in customized format.<br>
	 * The format patterns can be verified in java.text.DecimalFormat.
	 */
	public static String format(double NUMBER, String FORMAT) {
		if (FORMAT == null || FORMAT.length() == 0) {
			FORMAT = "0.00";
		}
		DecimalFormat df = new DecimalFormat(FORMAT);
		String number = df.format(NUMBER);
		return number;
	}

	/**
	 * Method for formating an int number in customized format.<br>
	 * The format patterns can be verified in java.text.DecimalFormat.
	 */
	public static String format(int NUMBER, String FORMAT) {
		if (FORMAT == null || FORMAT.length() == 0) {
			FORMAT = "000";
		}
		DecimalFormat df = new DecimalFormat(FORMAT);
		String number = df.format(NUMBER);
		return number;
	}

	/**
	 * Method for formating a long number in customized format.<br>
	 * The format patterns can be verified in java.text.DecimalFormat.
	 */
	public static String format(long NUMBER, String FORMAT) {
		if (FORMAT == null || FORMAT.length() == 0) {
			FORMAT = "000";
		}
		DecimalFormat df = new DecimalFormat(FORMAT);
		String number = df.format(NUMBER);
		return number;
	}

	/**
	 * Method for formating a java.lang.Integer number in customized format.<br>
	 * The format patterns can be verified in java.text.DecimalFormat.
	 */
	public static String format(java.lang.Integer NUMBER, String FORMAT) {
		if (NUMBER == null) {
			return "";
		}
		if (FORMAT == null || FORMAT.length() == 0) {
			FORMAT = "000";
		}
		DecimalFormat df = new DecimalFormat(FORMAT);
		String number = df.format(NUMBER);
		return number;
	}

	/**
	 * Method for formating a java.lang.Double number in customized format.<br>
	 * The format patterns can be verified in java.text.DecimalFormat.
	 */
	public static String format(java.lang.Double NUMBER, String FORMAT) {
		if (NUMBER == null) {
			return "";
		}
		if (FORMAT == null || FORMAT.length() == 0) {
			FORMAT = "0.00";
		}
		DecimalFormat df = new DecimalFormat(FORMAT);
		String number = df.format(NUMBER);
		return number;
	}

	/**
	 * Method for formating a java.lang.Float number in customized format.<br>
	 * The format patterns can be verified in java.text.DecimalFormat.
	 */
	public static String format(java.lang.Float NUMBER, String FORMAT) {
		if (NUMBER == null) {
			return "";
		}
		if (FORMAT == null || FORMAT.length() == 0) {
			FORMAT = "0.00";
		}
		DecimalFormat df = new DecimalFormat(FORMAT);
		String number = df.format(NUMBER);
		return number;
	}

	/**
	 * Method for formating a java.lang.Double number in customized format.<br>
	 * The format patterns can be verified in java.text.DecimalFormat.
	 */
	public static String format(java.lang.Byte NUMBER, String FORMAT) {
		if (NUMBER == null) {
			return "";
		}
		if (FORMAT == null || FORMAT.length() == 0) {
			FORMAT = "0";
		}
		DecimalFormat df = new DecimalFormat(FORMAT);
		String number = df.format(NUMBER);
		return number;
	}

	/**
	 * Method for formating a java.lang.Double number in customized format.<br>
	 * The format patterns can be verified in java.text.DecimalFormat.
	 */
	public static String format(java.lang.Short NUMBER, String FORMAT) {
		if (NUMBER == null) {
			return "";
		}
		if (FORMAT == null || FORMAT.length() == 0) {
			FORMAT = "0";
		}
		DecimalFormat df = new DecimalFormat(FORMAT);
		String number = df.format(NUMBER);
		return number;
	}

	/**
	 * Method for formating a java.lang.Long number in customized format.<br>
	 * The format patterns can be verified in java.text.DecimalFormat.
	 */
	public static String format(java.lang.Long NUMBER, String FORMAT) {
		if (NUMBER == null) {
			return "";
		}
		if (FORMAT == null || FORMAT.length() == 0) {
			FORMAT = "000";
		}
		DecimalFormat df = new DecimalFormat(FORMAT);
		String number = df.format(NUMBER);
		return number;
	}

	/**
	 * Method for formating a java.math.BigDecimal number in customized format.<br>
	 * The format patterns can be verified in java.text.DecimalFormat.
	 */
	public static String format(java.math.BigDecimal NUMBER, String FORMAT) {
		if (NUMBER == null) {
			return "";
		}
		if (FORMAT == null || FORMAT.length() == 0) {
			FORMAT = "0.00";
		}
		DecimalFormat df = new DecimalFormat(FORMAT);
		String number = df.format(NUMBER);
		return number;
	}

	/**
	 * Method for formating a double number in "0.00" format.
	 */
	public static String format(double NUMBER) {
		return format(NUMBER, "0.00");
	}

	/**
	 * Method for formating an int number in "000" format.
	 */
	public static String format(int NUMBER) {
		return format(NUMBER, "000");
	}

	/**
	 * Method for formating a long number in "000" format.
	 */
	public static String format(long NUMBER) {
		return format(NUMBER, "000");
	}

	/**
	 * Method for formating a java.lang.Double number in "0.00" format.
	 */
	public static String format(java.lang.Double NUMBER) {
		return format(NUMBER, "0.00");
	}

	/**
	 * Method for formating a java.lang.Float number in "0.00" format.
	 */
	public static String format(java.lang.Float NUMBER) {
		return format(NUMBER, "0.00");
	}

	/**
	 * Method for formating a java.lang.Integer number in "000" format.
	 */
	public static String format(java.lang.Integer NUMBER) {
		return format(NUMBER, "000");
	}

	/**
	 * Method for formating a java.lang.Byte number in "000" format.
	 */
	public static String format(java.lang.Byte NUMBER) {
		return format(NUMBER, "000");
	}

	/**
	 * Method for formating a java.lang.Short number in "000" format.
	 */
	public static String format(java.lang.Short NUMBER) {
		return format(NUMBER, "000");
	}

	/**
	 * Method for formating a java.lang.Long number in "000" format.
	 */
	public static String format(java.lang.Long NUMBER) {
		return format(NUMBER, "000");
	}

	/**
	 * Method for formating a java.math.BigDecimal number in "0.00" format.
	 */
	public static String format(java.math.BigDecimal NUMBER) {
		return format(NUMBER, "0.00");
	}

	/**
	 * Method for parse an int number.<br>
	 * parameter: String
	 */
	public static int parseInt(String NUMBER) {
		int resultado = 0;
		if (NUMBER != null && NUMBER.length() > 0) {
			NUMBER = NUMBER.trim();
			NUMBER = checkInt(NUMBER);
			if (NUMBER.indexOf(".") > 0) {
				NUMBER = NUMBER.substring(0, NUMBER.indexOf("."));
			}
			try {
				resultado = Integer.parseInt(NUMBER);
			} catch (Exception e) {
				resultado = 0;
			}
		}
		return resultado;
	}

	/**
	 * Method for parse a java.langInteger number.<br>
	 * parameter: String
	 */
	public static Integer parseIntegerWrapper(String NUMBER) {
		Integer resultado = null;
		if (NUMBER != null && NUMBER.length() > 0) {
			NUMBER = NUMBER.trim();
			NUMBER = checkInt(NUMBER);
			if (NUMBER.indexOf(".") > 0) {
				NUMBER = NUMBER.substring(0, NUMBER.indexOf("."));
			}
			try {
				resultado = Integer.valueOf(NUMBER);
			} catch (Exception e) {
				resultado = null;
			}
		}
		return resultado;
	}

	/**
	 * Method for parse an long number.<br>
	 * parameter: String
	 */
	public static long parseLong(String NUMBER) {
		long resultado = 0;
		if (NUMBER != null && NUMBER.length() > 0) {
			NUMBER = NUMBER.trim();
			NUMBER = checkInt(NUMBER);
			if (NUMBER.indexOf(".") > 0) {
				NUMBER = NUMBER.substring(0, NUMBER.indexOf("."));
			}
			try {
				resultado = Long.parseLong(NUMBER);
			} catch (Exception e) {
				resultado = 0;
			}
		}
		return resultado;
	}

	/**
	 * Method for parse an java.lang.Uong number.<br>
	 * parameter: String
	 */
	public static Long parseLongWrapper(String NUMBER) {
		Long resultado = null;
		if (NUMBER != null && NUMBER.length() > 0) {
			NUMBER = NUMBER.trim();
			NUMBER = checkInt(NUMBER);
			if (NUMBER.indexOf(".") > 0) {
				NUMBER = NUMBER.substring(0, NUMBER.indexOf("."));
			}
			try {
				resultado = Long.valueOf(NUMBER);
			} catch (Exception e) {
				resultado = null;
			}
		}
		return resultado;
	}

	/**
	 * Method for parse an long number.<br>
	 * parameter: String
	 */
	public static java.math.BigDecimal parseBigDecimal(String NUMBER) {
		java.math.BigDecimal resultado = null;
		if (NUMBER != null && NUMBER.length() > 0) {
			NUMBER = NUMBER.trim();
			NUMBER = checkDouble(NUMBER);
			try {
				resultado = new java.math.BigDecimal(NUMBER);
			} catch (Exception e) {
				resultado = null;
			}
		}
		return resultado;
	}

	/**
	 * Method for parse a double number.<br>
	 * parameter: String
	 */
	public static double parseDouble(String NUMBER) {
		double resultado = 0;
		if (NUMBER != null && NUMBER.length() > 0) {
			NUMBER = NUMBER.trim();
			NUMBER = checkDouble(NUMBER);
			try {
				resultado = Double.parseDouble(NUMBER);
			} catch (Exception e) {
				resultado = 0;
			}
		}
		return resultado;
	}

	/**
	 * Method for parse a java.lang.Double number.<br>
	 * parameter: String
	 */
	public static Double parseDoubleWrapper(String NUMBER) {
		Double resultado = null;
		if (NUMBER != null && NUMBER.length() > 0) {
			NUMBER = NUMBER.trim();
			NUMBER = checkDouble(NUMBER);
			try {
				resultado = Double.valueOf(NUMBER);
			} catch (Exception e) {
				resultado = null;
			}
		}
		return resultado;
	}

	public static Integer toInteger(String NUMBER) {
		Integer resultado = null;
		if (NUMBER != null && NUMBER.length() > 0) {
			NUMBER = NUMBER.trim();
			NUMBER = checkInt(NUMBER);
			resultado = Integer.valueOf(NUMBER);
		}
		return resultado;
	}

	public static Double toDouble(String NUMBER) {
		Double resultado = null;
		if (NUMBER != null && NUMBER.length() > 0) {
			NUMBER = NUMBER.trim();
			NUMBER = checkDouble(NUMBER);
			resultado = Double.valueOf(NUMBER);
		}
		return resultado;
	}

	public static Long toLong(String NUMBER) {
		Long resultado = null;
		if (NUMBER != null && NUMBER.length() > 0) {
			NUMBER = NUMBER.trim();
			NUMBER = checkInt(NUMBER);
			resultado = Long.valueOf(NUMBER);
		}
		return resultado;
	}

	public static java.math.BigDecimal toBigDecimal(String NUMBER) {
		java.math.BigDecimal resultado = null;
		if (NUMBER != null && NUMBER.length() > 0) {
			NUMBER = NUMBER.trim();
			NUMBER = checkDouble(NUMBER);
			resultado = new java.math.BigDecimal(NUMBER);
		}
		return resultado;
	}

	/**
	 * Method for checking a double number format.
	 */
	private static String checkDouble(String NUMBER) {
		if (NUMBER != null && NUMBER.length() > 0) {
			if (NUMBER.indexOf(",") != -1 || NUMBER.indexOf(".") != -1) {
				int limit = NUMBER.length() - 4;
				String new_number = "";
				for (int i = 0; i < NUMBER.length(); i++) {
					if (NUMBER.charAt(i) == '.' || NUMBER.charAt(i) == ',') {
						if (i > limit) {
							new_number += ".";
						} else if (NUMBER.substring(i + 1).indexOf(".") == -1
								&& NUMBER.substring(i + 1).indexOf(",") == -1) {
							new_number += ".";
						}
					} else {
						new_number += NUMBER.charAt(i);
					}
				}
				NUMBER = new_number;
			}
		}
		return NUMBER;
	}

	/**
	 * Method for checking a int number format.
	 */
	private static String checkInt(String NUMBER) {
		if (NUMBER != null && NUMBER.length() > 0) {
			if (NUMBER.indexOf(",") != -1 || NUMBER.indexOf(".") != -1) {
				int limit = NUMBER.length() - 4;
				String new_number = "";
				for (int i = 0; i < NUMBER.length(); i++) {
					if (!(NUMBER.charAt(i) == '.' || NUMBER.charAt(i) == ',')) {
						new_number += NUMBER.charAt(i);
					}
				}
				NUMBER = new_number;
			}
		}
		return NUMBER;
	}
}
