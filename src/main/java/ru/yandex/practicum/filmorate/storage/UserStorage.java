package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User createUser(User user);

    Collection<User> getAllUsers();

    User updateUser(User user);

    User deleteUser(Integer id);

    User getUserById(Integer id);
}

