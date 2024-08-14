package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    UserController userController;
    User user;

    @BeforeEach
    void start() {
        userController = new UserController();
        user = new User();
        user.setName("User");
        user.setLogin("Login");
        user.setEmail("User@gmail.com");
        user.setBirthday(LocalDate.of(1994, 4, 13));
    }

    @Test
    void createFilm() {
        userController.createUser(user);
        assertEquals(userController.getAllUsers().toArray()[0], user);
    }

    @Test
    void updateFilm() {
        userController.createUser(user);
        user.setName("User1");
        userController.updateUser(user);
        assertEquals(userController.getAllUsers().toArray()[0], user);
    }
}

