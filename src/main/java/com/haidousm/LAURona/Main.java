package com.haidousm.LAURona;

import com.haidousm.LAURona.entity.ConnectionDetails;
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

        Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        HibernateUtil.getSessionFactory().getCurrentSession().save(testUser);
        HibernateUtil.getSessionFactory().getCurrentSession().save(connectionDetails);
        tx.commit();

        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        List<ConnectionDetails> connectionDetailsList = HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from ConnectionDetails", ConnectionDetails.class).list();
        connectionDetailsList.forEach(System.out::println);
    }
}
