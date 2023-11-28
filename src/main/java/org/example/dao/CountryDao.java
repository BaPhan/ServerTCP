package org.example.dao;

import org.example.domain.Country;
import org.example.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class CountryDao {
    private static final SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    public void addCountry(Country country) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(country);
            transaction.commit();
        }
    }

    public Country getCountry(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Country.class, id);
        }
    }

    public Country getCountryByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Country> query = session.createQuery("FROM Country WHERE name = :name", Country.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        }
    }


    public void updateCountry(Country country) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(country);
            transaction.commit();
        }
    }

    public void deleteCountry(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Country country = session.get(Country.class, id);
            if (country != null) {
                session.delete(country);
            }
            transaction.commit();
        }
    }

    public List<Country> getAllCountry() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Country", Country.class).list();
        }
    }

}
