package com.haidousm.LAURona;

import com.haidousm.LAURona.entity.User;
import com.haidousm.LAURona.utils.HibernateUtil;
import org.hibernate.Transaction;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        User testUser = new User("John", "Doe",
                "jdoe@thedoes.com", "jdoe", "hunter32",
                true, "/src/new/certs/jdoe.pdf", "/src/new/imgs/jdoe.png");

        Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        HibernateUtil.getSessionFactory().getCurrentSession().save(testUser);
        tx.commit();

        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        List<User> users = HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from User",
                User.class).list();
        users.forEach(System.out::println);
    }
}
