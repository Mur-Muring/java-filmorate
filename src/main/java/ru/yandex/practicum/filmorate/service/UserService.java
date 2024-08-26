package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;

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
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.addFriend(friend);
        friend.addFriend(user);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        log.info("Поступил запрос на удаление пользователя {} из друзей {}", userId, friendId);
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.deleteFriend(friend);
        friend.deleteFriend(user);
    }

    public List<User> getFriends(Integer id) {
        log.info("Поступил запрос на получение друзей пользователя {}", id);
        return userStorage.getUserById(id).getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        log.info("Поступил запрос на список общих друзей {} и {}", userId, friendId);

        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        if (user == null || friend == null || user.getFriends() == null || friend.getFriends() == null) {
            return Collections.emptyList();
        }

        Set<Integer> userFriends = user.getFriends();
        userFriends.retainAll(friend.getFriends());

        return userFriends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
