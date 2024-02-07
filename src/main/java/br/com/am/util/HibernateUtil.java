package br.com.am.util;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.internal.SessionFactoryImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Paulo Sobreira [sowbreira@gmail.com]
 */
public class HibernateUtil {
	private static EntityManagerFactory factory;

	public static Session getSession() {
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory("am-sis-jpa");
		}
		EntityManager entityManager = factory.createEntityManager();
		System.out.println(factory.getProperties());
		return entityManager.unwrap(org.hibernate.Session.class);
	}

}
