package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Collection<User> findAll();

    Optional<User> getById(Long id);

    void createUser(User user);

    void updateUser(User newUser);

    void addToFriends(Long id, Long friendId);

    void deleteFromFriends(Long id, Long friendId);

    Collection<User> findAllFriends(Long id);

    Collection<User> findCommonFriends(Long id, Long otherId);

    void isUserNotExists(Long id);
}
