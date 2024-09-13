package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public Collection<User> getAllUsers() {
        log.info("Поступил запрос на получение списка всех пользователей");
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        log.info("Поступил запрос на создание пользователя {}", user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User newUser) {
        log.info("Поступил запрос на обновление пользователя {}", newUser);
        return userStorage.updateUser(newUser);
    }

    public void addFriend(Integer userId, Integer friendId) {
        log.info("Поступил запрос на добавление пользователя {} в друзья {}", userId, friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        log.info("Поступил запрос на удаление пользователя {} из друзей {}", userId, friendId);
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(Integer id) {
        log.info("Поступил запрос на получение друзей пользователя {}", id);
        return friendStorage.getFriends(id);
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        log.info("Поступил запрос на список общих друзей {} и {}", userId, friendId);
        return friendStorage.getCommonFriends(userId, friendId);
    }
}
