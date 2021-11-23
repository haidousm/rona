package com.haidousm.rona;

import com.haidousm.rona.entity.User;
import com.haidousm.rona.server.Server;
import com.haidousm.rona.utils.HibernateUtil;
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
