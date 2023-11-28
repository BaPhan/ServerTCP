package org.example.dao;

import org.example.domain.Wine;
import org.example.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class WineDao {
    private static final SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    public void addWine(Wine wine) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(wine);
            transaction.commit();
        }
    }

    public Wine getWine(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Wine.class, id);
        }
    }

    public Wine getWineByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Wine> query = session.createQuery("FROM Wine WHERE name = :name", Wine.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        }
    }
    public Wine getWineByCountryId(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Wine> query = session.createQuery("FROM Wine WHERE CountryId = :countryId", Wine.class);
            query.setParameter("countryId", id);
            return query.uniqueResult();
        }
    }


    public void updateWine(Wine wine) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(wine);
            transaction.commit();
        }
    }

    public void deleteWine(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Wine wine = session.get(Wine.class, id);
            if (wine != null) {
                session.delete(wine);
            }
            transaction.commit();
        }
    }

    public List<Wine> getAllWine() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Wine", Wine.class).list();
        }
    }

}
