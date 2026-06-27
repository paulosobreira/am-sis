package br.com.am.util;

/** Mantido apenas para compatibilidade; BIRT foi substituído por Thymeleaf. */
public class BirtEngine {
    public static synchronized void destroyBirtEngine() {}
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
