package com.haidousm.rona.server.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static StandardServiceRegistry register;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                register = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
                Metadata metadata = new MetadataSources(register).getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Throwable ex) {
                System.err.println("Initial SessionFactory creation failed." + ex);
                if (register != null) {
                    StandardServiceRegistryBuilder.destroy(register);
                }
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (register != null) {
            StandardServiceRegistryBuilder.destroy(register);
        }
    }

}
