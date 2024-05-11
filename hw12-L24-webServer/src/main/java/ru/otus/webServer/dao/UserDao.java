package ru.otus.webServer.dao;


import ru.otus.webServer.dao.model.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> findById(long id);

    Optional<User> findRandomUser();

    Optional<User> findByLogin(String login);
}
