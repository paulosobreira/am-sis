package br.com.am.recursos;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import br.com.am.util.Util;

public class Recursos {
	private static Properties properties;

	public static void initProperties() {
		try {
			properties = new Properties();
			properties.load(Recursos.class.getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Properties getProperties() {
		if (properties == null) {
			initProperties();
		}
		return properties;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		System.out.println(Util.md5("am-sis"));
	}

}
