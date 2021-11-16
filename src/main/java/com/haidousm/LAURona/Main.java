package com.haidousm.LAURona;

import com.haidousm.LAURona.entity.ConnectionDetails;
import com.haidousm.LAURona.entity.HealthStatus;
import com.haidousm.LAURona.entity.LocationDetails;
import com.haidousm.LAURona.entity.User;
import com.haidousm.LAURona.enums.Health;
import com.haidousm.LAURona.utils.HibernateUtil;
import org.hibernate.Transaction;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        User testUserA = new User("A", "Z",
                "A@AZ.com", "az", "hunter32",
                true, "/src/new/certs/az.pdf", "/src/new/imgs/az.png");
        ConnectionDetails connectionDetailsA = new ConnectionDetails("192.168.32.1", 3200, testUserA);
        LocationDetails locationDetailsA = new LocationDetails(124.5, -12.5, System.currentTimeMillis() / 1000L, testUserA);
        HealthStatus healthStatusA = new HealthStatus(Health.SAFE, System.currentTimeMillis() / 1000L, testUserA);


        User testUserB = new User("B", "Y", "B@AY.com", "by", "hunter32", false, "/src/new/certs/by.pdf", "/src/new/imgs/by.png");
        ConnectionDetails connectionDetailsB = new ConnectionDetails("172.12.255.44", 8080, testUserB);
        LocationDetails locationDetailsB = new LocationDetails(75.5, 32, System.currentTimeMillis() / 1000L, testUserB);
        HealthStatus healthStatusB = new HealthStatus(Health.CONTAGIOUS, System.currentTimeMillis() / 1000L, testUserB);


        Transaction tx;

        tx = HibernateUtil.beginTransaction();
        HibernateUtil.getSession().save(testUserA);
        HibernateUtil.getSession().save(connectionDetailsA);
        HibernateUtil.getSession().save(locationDetailsA);
        HibernateUtil.getSession().save(healthStatusA);
        tx.commit();

        tx = HibernateUtil.beginTransaction();
        HibernateUtil.getSession().save(testUserB);
        HibernateUtil.getSession().save(connectionDetailsB);
        HibernateUtil.getSession().save(locationDetailsB);
        HibernateUtil.getSession().save(healthStatusB);
        tx.commit();

        testUserB.trustUser(testUserA);
        tx = HibernateUtil.beginTransaction();
        HibernateUtil.getSession().update(testUserA);
        HibernateUtil.getSession().update(testUserB);
        tx.commit();

        tx = HibernateUtil.getSession().beginTransaction();
        List<User> users = HibernateUtil.getSession().createQuery("from User", User.class).list();
        List<ConnectionDetails> connectionDetailsList = HibernateUtil.getSession().createQuery("from ConnectionDetails", ConnectionDetails.class).list();
        List<LocationDetails> locationDetailsList = HibernateUtil.getSession().createQuery("from LocationDetails", LocationDetails.class).list();
        List<HealthStatus> healthStatusList = HibernateUtil.getSession().createQuery("from HealthStatus", HealthStatus.class).list();
        tx.commit();

        System.out.println("-------------------");
        users.forEach(System.out::println);
        System.out.println("-------------------");
        connectionDetailsList.forEach(System.out::println);
        System.out.println("-------------------");
        locationDetailsList.forEach(System.out::println);
        System.out.println("-------------------");
        healthStatusList.forEach(System.out::println);
        System.out.println("-------------------");
    }
}
