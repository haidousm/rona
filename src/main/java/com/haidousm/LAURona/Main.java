package com.haidousm.LAURona;

import com.haidousm.LAURona.entity.User;
import com.haidousm.LAURona.server.Server;
import com.haidousm.LAURona.utils.HibernateUtil;
import org.hibernate.Transaction;


public class Main {
    public static void main(String[] args) {

        User testUser = new User("Moussa", "Haidous", "moussa.haidous@lau.edu", "haidousm", "123456", false, "", "some_file_path");
        Transaction tx = HibernateUtil.beginTransaction();
        HibernateUtil.getSession().save(testUser);
        tx.commit();

        Server server = new Server(1234);
        server.start();
    }
}
