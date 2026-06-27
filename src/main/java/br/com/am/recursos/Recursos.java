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
            java.io.InputStream stream = Recursos.class.getResourceAsStream("/config.properties");
            if (stream == null) {
                throw new IllegalStateException("config.properties nao encontrado no classpath");
            }
            properties = new Properties();
            properties.load(stream);
            stream.close();
        } catch (Exception e) {
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
