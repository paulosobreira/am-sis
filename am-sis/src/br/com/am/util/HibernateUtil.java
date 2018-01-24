package br.com.am.util;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.internal.SessionFactoryImpl;

/**
 * @author Paulo Sobreira [sowbreira@gmail.com]
 */
public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static Metadata buildMetadata;

	static {

		if (sessionFactory == null) {

			// A SessionFactory is set up once for an application!
			StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
					.configure() // configures settings from
									// hibernate.cfg.xml
					.build();
			try {

				buildMetadata = new MetadataSources(registry).buildMetadata();
				sessionFactory = buildMetadata.buildSessionFactory();
				
			} catch (Exception e) {
				// The registry would be destroyed by the SessionFactory, but we
				// had
				// trouble building the SessionFactory
				// so destroy it manually.
				e.printStackTrace();
				StandardServiceRegistryBuilder.destroy(registry);
			}

		}

	}

	public static SessionFactory getSessionFactory() {
		try {
			SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFactory;
//			Properties props = sessionFactoryImpl.getProperties();
//			String url = props.get("hibernate.connection.datasource").toString();
//			System.out.println("Datasource "+url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sessionFactory;
	}

	public static Metadata getMetadata() {
		return buildMetadata;
	}

}
