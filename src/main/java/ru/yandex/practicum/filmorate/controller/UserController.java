package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import utils.WorkInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    @Validated(WorkInterface.Create.class)
    public User createUser(@RequestBody @Valid User user) {
        log.info("Поступил запрос на создание пользователя {}", user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(idGenerator++);
        users.put(user.getId(), user);
        log.info("Отправлен ответ {}", user);
        return user;
    }

    @PutMapping
    @Validated(WorkInterface.Update.class)
    public User updateUser(@RequestBody @Valid User newUser) {
        log.info("Поступил запрос на обновление пользователя {}", newUser);
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
            log.info("Отправлен ответ {}", oldUser);
            return oldUser;
        }
        log.warn("id {} не найден", newUser.getId());
        throw new ValidationException("id " + newUser.getId() + " не найден");
    }
}
