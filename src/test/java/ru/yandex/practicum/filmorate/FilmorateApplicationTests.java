package ru.yandex.practicum.filmorate;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final FilmService filmService;
    private final UserService userService;
    private User firstUser;
    private User secondUser;
    private User thirdUser;
    private Film firstFilm;
    private Film secondFilm;
    private Film thirdFilm;

    @BeforeEach
    public void beforeEach() {
        firstUser = new User();
        firstUser.setName("User 1");
        firstUser.setLogin("Login 1");
        firstUser.setEmail("1@ya.ru");
        firstUser.setBirthday(LocalDate.of(1994, 4, 13));
        secondUser = new User();
        secondUser.setName("User 2");
        secondUser.setLogin("Login 1");
        secondUser.setEmail("2@ya.ru");
        secondUser.setBirthday(LocalDate.of(2009, 4, 17));
        thirdUser = new User();
        thirdUser.setName("User 3");
        thirdUser.setLogin("Login 3");
        thirdUser.setEmail("3@ya.ru");
        thirdUser.setBirthday(LocalDate.of(1992, 12, 22));
        firstFilm = new Film();
        firstFilm.setName("Film 1");
        firstFilm.setDescription("Description 1");
        firstFilm.setReleaseDate(LocalDate.of(1960, 5, 5));
        firstFilm.setDuration(120L);
        firstFilm.setRating_mpa(new Rating(1, "G", "..."));
        firstFilm.setLikes(new HashSet<>());
        firstFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Драма"),
                new Genre(1, "Комедия"))));

        secondFilm = new Film();
        secondFilm.setName("Film 2");
        secondFilm.setDescription("Description 2");
        secondFilm.setReleaseDate(LocalDate.of(2000, 7, 7));
        secondFilm.setDuration(160L);
        secondFilm.setRating_mpa(new Rating(3, "PG-13", "..."));
        secondFilm.setLikes(new HashSet<>());
        secondFilm.setGenres(new HashSet<>(List.of(new Genre(6, "Боевик"))));

        thirdFilm = new Film();
        thirdFilm.setName("Film 3");
        thirdFilm.setDescription("Description 2");
        thirdFilm.setReleaseDate(LocalDate.of(1970, 10, 10));
        thirdFilm.setDuration(130L);
        thirdFilm.setLikes(new HashSet<>());
        thirdFilm.setRating_mpa(new Rating(4, "R", "..."));
        thirdFilm.setGenres(new HashSet<>(List.of(new Genre(2, "Драма"))));
    }

    @Test
    public void testAddUserAndGetUserById() {
        firstUser = userStorage.createUser(firstUser);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(firstUser.getId()));
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", firstUser.getId())
                                .hasFieldOrPropertyWithValue("name", "User 1"));
    }

    @Test
    public void testUpdateUser() {
        firstUser = userStorage.createUser(firstUser);
        User newUser = new User(firstUser.getId(), "new2@ya.ru", "Second", "NewFirstUser",
                LocalDate.of(2000, 7, 7), null);
        Optional<User> testUpdateUser = Optional.ofNullable(userStorage.updateUser(newUser));
        assertThat(testUpdateUser)
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("name", "NewFirstUser")
                );
    }

    @Test
    public void deleteUser() {
        firstUser = userStorage.createUser(firstUser);
        userStorage.deleteUser(firstUser.getId());
        List<User> listUsers = userStorage.getAllUsers();
        assertThat(listUsers).hasSize(0);
    }

    @Test
    public void testGetUsers() {
        firstUser = userStorage.createUser(firstUser);
        secondUser = userStorage.createUser(secondUser);
        List<User> listUsers = userStorage.getAllUsers();
        assertThat(listUsers).hasSize(2);
    }

    @Test
    public void testAddFilmAndGetFilmById() {
        firstFilm = filmStorage.createFilm(firstFilm);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(firstFilm.getId()));
        assertThat(filmOptional)
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("id", firstFilm.getId())
                        .hasFieldOrPropertyWithValue("name", "Film 1")
                );
    }

    @Test
    public void testUpdateFilm() {
        firstFilm = filmStorage.createFilm(firstFilm);
        Film updateFilm = new Film(firstFilm.getId(), "new1", "NewEmpty",
                LocalDate.of(2000, 7, 7), 120L, null, new Rating(1, "G", "..."),
                Set.of(new Genre(2, "Драма")));
        updateFilm.setRating_mpa(new Rating(1, "G", "..."));
        Optional<Film> testUpdateFilm = Optional.ofNullable(filmStorage.updateFilm(updateFilm));
        assertThat(testUpdateFilm)
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "new1")
                                .hasFieldOrPropertyWithValue("description", "NewEmpty")
                );
    }

    @Test
    public void deleteFilm() {
        firstFilm = filmStorage.createFilm(firstFilm);
        filmStorage.deleteFilm(firstFilm.getId());
        List<Film> listFilms = filmStorage.getAllFilms();
        assertThat(listFilms).hasSize(0);
    }

    @Test
    public void testGetFilms() {
        firstFilm = filmStorage.createFilm(firstFilm);
        secondFilm = filmStorage.createFilm(secondFilm);
        List<Film> listFilms = filmStorage.getAllFilms();
        assertThat(listFilms).hasSize(2);
    }

    @Test
    public void testAddLike() {
        firstUser = userStorage.createUser(firstUser);
        firstFilm = filmStorage.createFilm(firstFilm);
        filmService.likeFilm(firstUser.getId(), firstFilm.getId());
        firstFilm = filmStorage.getFilmById(firstFilm.getId());
        assertThat(firstFilm.getLikes()).hasSize(1);
        assertThat(firstFilm.getLikes()).contains(firstUser.getId());
    }

    @Test
    public void testDeleteLike() {
        firstUser = userStorage.createUser(firstUser);
        secondUser = userStorage.createUser(secondUser);
        firstFilm = filmStorage.createFilm(firstFilm);
        filmService.likeFilm(firstUser.getId(), firstFilm.getId());
        filmService.likeFilm(secondUser.getId(), firstFilm.getId());
        filmService.deleteFilmLike(firstUser.getId(), firstFilm.getId());
        firstFilm = filmStorage.getFilmById(firstFilm.getId());
        assertThat(firstFilm.getLikes()).hasSize(1);
        assertThat(firstFilm.getLikes()).contains(secondUser.getId());
    }

    @Test
    public void testGetPopularFilms() {

        firstUser = userStorage.createUser(firstUser);
        secondUser = userStorage.createUser(secondUser);
        thirdUser = userStorage.createUser(thirdUser);

        firstFilm = filmStorage.createFilm(firstFilm);
        filmService.likeFilm(firstUser.getId(), firstFilm.getId());

        secondFilm = filmStorage.createFilm(secondFilm);
        filmService.likeFilm(firstUser.getId(), secondFilm.getId());
        filmService.likeFilm(secondUser.getId(), secondFilm.getId());
        filmService.likeFilm(thirdUser.getId(), secondFilm.getId());

        thirdFilm = filmStorage.createFilm(thirdFilm);
        filmService.likeFilm(firstUser.getId(), thirdFilm.getId());
        filmService.likeFilm(secondUser.getId(), thirdFilm.getId());

        List<Film> listFilms = filmService.getPopularFilms(5);

        assertThat(listFilms).hasSize(3);

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Film 2"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Film 3"));

        assertThat(Optional.of(listFilms.get(2)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Film 1"));
    }

    @Test
    public void testAddFriend() {
        firstUser = userStorage.createUser(firstUser);
        secondUser = userStorage.createUser(secondUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        assertThat(userService.getFriends(firstUser.getId())).hasSize(1);
    }

    @Test
    public void testDeleteFriend() {
        firstUser = userStorage.createUser(firstUser);
        secondUser = userStorage.createUser(secondUser);
        thirdUser = userStorage.createUser(thirdUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        userService.deleteFriend(firstUser.getId(), secondUser.getId());
        assertThat(userService.getFriends(firstUser.getId())).hasSize(1);
    }

    @Test
    public void testGetFriends() {
        firstUser = userStorage.createUser(firstUser);
        secondUser = userStorage.createUser(secondUser);
        thirdUser = userStorage.createUser(thirdUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        assertThat(userService.getFriends(firstUser.getId())).hasSize(2);
    }

    @Test
    public void testGetCommonFriends() {
        firstUser = userStorage.createUser(firstUser);
        secondUser = userStorage.createUser(secondUser);
        thirdUser = userStorage.createUser(thirdUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        userService.addFriend(secondUser.getId(), firstUser.getId());
        userService.addFriend(secondUser.getId(), thirdUser.getId());
        assertThat(userService.getCommonFriends(firstUser.getId(), secondUser.getId())).hasSize(1);
    }
}