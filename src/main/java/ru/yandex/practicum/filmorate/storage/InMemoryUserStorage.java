package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(idGenerator++);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User updateUser(User newUser) {
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getName() == null || newUser.getName().isBlank()) {
                oldUser.setName(newUser.getLogin());
            } else {
                oldUser.setName(newUser.getName());
            }
            if (newUser.getLogin() != null) {
                oldUser.setLogin(newUser.getLogin());
            }
            if (newUser.getEmail() != null) {
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getBirthday() != null) {
                oldUser.setBirthday(newUser.getBirthday());
            }
            log.info("Информация о пользователе {} обновленв", oldUser);
            return oldUser;
        }
        log.warn("id {} не найден", newUser.getId());
        throw new ValidationException("id " + newUser.getId() + " не найден");
    }

    @Override
    public User deleteUser(Integer id) {
        log.info("Пользователь c id {} удален", id);
        return users.remove(id);
    }

    @Override
    public User getUserById(Integer id) {
        log.info("Поступил запрос на поиск пользователя с id {}", id);
        User user = users.get(id);
        if (user == null) {
            log.error("Пользователь с id {} не найден", id);
            throw new ValidationException("Пользователь с id " + id + " не найден");
        }
        log.info("Пользователь с id {} успешно найден", id);
        return user;
    }
}
