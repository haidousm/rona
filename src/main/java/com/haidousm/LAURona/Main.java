package com.haidousm.LAURona;

import com.haidousm.LAURona.entity.ConnectionDetails;
import com.haidousm.LAURona.entity.LocationDetails;
import com.haidousm.LAURona.entity.User;
import com.haidousm.LAURona.utils.HibernateUtil;
import org.hibernate.Transaction;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        User testUser = new User("John", "Doe",
                "jdoe@thedoes.com", "jdoe", "hunter32",
                true, "/src/new/certs/jdoe.pdf", "/src/new/imgs/jdoe.png");

        ConnectionDetails connectionDetails = new ConnectionDetails("192.168.32.1", 3200, testUser);

        LocationDetails locationDetails = new LocationDetails(124.5, -12.5, System.currentTimeMillis() / 1000L, testUser);

        Transaction tx;

        tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        HibernateUtil.getSessionFactory().getCurrentSession().save(testUser);
        HibernateUtil.getSessionFactory().getCurrentSession().save(connectionDetails);
        HibernateUtil.getSessionFactory().getCurrentSession().save(locationDetails);
        tx.commit();

        tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        List<User> users = HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from User", User.class).list();
        List<ConnectionDetails> connectionDetailsList = HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from ConnectionDetails", ConnectionDetails.class).list();
        List<LocationDetails> locationDetailsList = HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from LocationDetails", LocationDetails.class).list();
        System.out.println("-------------------");
        users.forEach(System.out::println);
        System.out.println("-------------------");
        connectionDetailsList.forEach(System.out::println);
        System.out.println("-------------------");
        locationDetailsList.forEach(System.out::println);
        tx.commit();
    }
}
