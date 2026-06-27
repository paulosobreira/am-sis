package br.com.am.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;

public class HibernateUtil {
    private static EntityManagerFactory factory;

    public static Session getSession() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory("am-sis-jpa");
        }
        EntityManager entityManager = factory.createEntityManager();
        return entityManager.unwrap(Session.class);
    }
}
