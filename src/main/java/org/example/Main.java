package org.example;

import org.example.dao.CountryDao;
import org.example.dao.UserDao;
import org.example.domain.Country;
import org.example.domain.User;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        User newUser = new User();
        newUser.setUsername("john_doe");
        newUser.setPassword("password123");
        userDao.addUser(newUser);
        // Xóa người dùng
        userDao.deleteUser(newUser.getId());

        CountryDao countryDao = new CountryDao();
//        Country country = new Country();
//        country.setName("VN");
//        country.setDescription("nothing!");
//        countryDao.addCountry(country);
        countryDao.deleteCountry(6);
    }
}