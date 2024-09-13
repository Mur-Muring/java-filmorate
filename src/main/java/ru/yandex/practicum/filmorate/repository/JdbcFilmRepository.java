package ru.yandex.practicum.filmorate.repository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.mappers.FilmsRowMapper;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.repository.mappers.UsersRowMapper;

import java.sql.Date;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcFilmRepository implements FilmRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final FilmRowMapper filmExtractor;
    private final FilmsRowMapper filmsExtractor;

    @Override
    public Map<Long, Film> findAll() {
        String sql = """
                SELECT *
                FROM FILMS AS f
                LEFT JOIN RATING_MPA AS r ON  f.MPA_ID = r.MPA_ID
                LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID
                LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID;
                """;
        return jdbc.query(sql, Map.of(), filmsExtractor);
    }

    @Override
    public Optional<Film> getById(Long id) {
        String sql = """
                SELECT *
                FROM FILMS AS f
                LEFT JOIN RATING_MPA AS r ON  f.MPA_ID = r.MPA_ID
                LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID
                LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID
                WHERE f.FILM_ID = :film_id;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource("film_id", id);
        return Optional.ofNullable(jdbc.query(sql, parameter, filmExtractor));
    }

    @Override
    public void createFilm(Film film) {
        String sql = """
                INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
                VALUES (:name, :description, :release_date, :duration, :mpa_id);
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", Date.valueOf(film.getReleaseDate()))
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpa().getId());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sql, parameter, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        film.setId(id);
    }

    @Override
    public void updateFilm(Film film) {
        String sql = """
                UPDATE FILMS
                SET NAME = :name,
                DESCRIPTION = :description,
                RELEASE_DATE = :release_date,
                DURATION = :duration,
                MPA_ID = :mpa_id
                WHERE FILM_ID = :film_id;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", Date.valueOf(film.getReleaseDate()))
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpa().getId())
                .addValue("film_id", film.getId());
        jdbc.update(sql, parameter);
    }

    @Override
    public void addLike(Long id, Long userId) {
        String sql = """
                INSERT INTO FILM_LIKES (FILM_ID, USER_ID)
                VALUES (:film_id, :user_id)
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("film_id", id)
                .addValue("user_id", userId);
        jdbc.update(sql, parameter);
    }

    @Override
    public void removeLike(Long id, Long userId) {
        String sql = """
                DELETE FROM FILM_LIKES
                WHERE FILM_ID = :film_id
                      AND USER_ID = :user_id;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("film_id", id)
                .addValue("user_id", userId);
        jdbc.update(sql, parameter);
    }

    @Override
    public Map<Long, Film> getPopular(Long count) {
        String sql = """
                SELECT COUNT(p.user_id) AS sum_likes,
                        f.FILM_ID,
                        f.NAME,
                        f.DESCRIPTION,
                        f.RELEASE_DATE,
                        f.DURATION,
                        f.MPA_ID,
                        r.MPA_NAME,
                        fg.GENRE_ID,
                        g.GENRE_NAME
                FROM FILMS AS f
                        LEFT JOIN FILM_LIKES AS p ON f.film_id=p.film_id
                        LEFT JOIN RATING_MPA AS r ON f.MPA_ID = r.MPA_ID
                        LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID
                        LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID
                GROUP BY f.name, f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE,
                                            f.DURATION, f.MPA_ID, r.MPA_NAME, fg.GENRE_ID, g.GENRE_NAME
                ORDER BY COUNT(p.user_id) DESC
                LIMIT :count;
                """;
        SqlParameterSource parameter = new MapSqlParameterSource("count", count);
        return jdbc.query(sql, parameter, filmsExtractor);
    }

    @Override
    public void isFilmNotExists(Long id) {
        if (getById(id).isEmpty()) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }

    @Repository
    @AllArgsConstructor
    public static class JdbcUserRepository implements UserRepository {
        private final NamedParameterJdbcTemplate jdbc;
        private final UserRowMapper userExtractor;
        private final UsersRowMapper usersExtractor;

        @Override
        public Collection<User> findAll() {
            String sql = """
                    SELECT *
                    FROM USERS;
                    """;
            return jdbc.query(sql, usersExtractor);
        }

        @Override
        public Optional<User> getById(Long id) {
            String sql = """
                    SELECT *
                    FROM USERS
                    WHERE USER_ID = :user_id;
                    """;
            SqlParameterSource parameter = new MapSqlParameterSource().addValue("user_id", id);
            System.out.println(jdbc.query(sql, parameter, userExtractor));
            return Optional.ofNullable(jdbc.query(sql, parameter, userExtractor));
        }

        @Override
        public void createUser(User user) {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            String sql = """
                    INSERT INTO USERS ("EMAIL", "LOGIN", "NAME", "BIRTHDAY")
                    VALUES (:email, :login, :name, :birthday);
                    """;
            SqlParameterSource parameter = new MapSqlParameterSource()
                    .addValue("email", user.getEmail())
                    .addValue("login", user.getLogin())
                    .addValue("name", user.getName())
                    .addValue("birthday", user.getBirthday());
            jdbc.update(sql, parameter, keyHolder);
            user.setId(keyHolder.getKeyAs(Long.class));
        }

        @Override
        public void updateUser(User user) {
            String sql = """
                    UPDATE USERS
                    SET "EMAIL" = :email,
                        "LOGIN" = :login,
                        "NAME" = :name,
                        "BIRTHDAY" = :birthday
                    WHERE USER_ID = :user_id;
                    """;
            SqlParameterSource parameter = new MapSqlParameterSource()
                    .addValue("user_id", user.getId())
                    .addValue("email", user.getEmail())
                    .addValue("login", user.getLogin())
                    .addValue("name", user.getName())
                    .addValue("birthday", user.getBirthday());
            jdbc.update(sql, parameter);
        }

        @Override
        public void addToFriends(Long id, Long friendId) {
            String sql = """
                    INSERT INTO FRIENDS (USER_ID, FRIEND_ID)
                    VALUES (:user_id, :friend_id);
                    """;
            SqlParameterSource parameter = new MapSqlParameterSource()
                    .addValue("user_id", id)
                    .addValue("friend_id", friendId);
            jdbc.update(sql, parameter);
        }

        @Override
        public void deleteFromFriends(Long id, Long friendId) {
            String sql = """
                    DELETE FROM FRIENDS
                    WHERE USER_ID = :user_id
                    AND FRIEND_ID = :friend_id;
                    """;
            SqlParameterSource parameter = new MapSqlParameterSource()
                    .addValue("user_id", id)
                    .addValue("friend_id", friendId);
            jdbc.update(sql, parameter);
        }

        @Override
        public Collection<User> findAllFriends(Long id) {
            String sql = """
                    SELECT *
                    FROM USERS AS u
                    WHERE USER_ID IN (
                       SELECT FRIEND_ID
                       FROM FRIENDS
                       WHERE USER_ID = :user_id
                       );
                    """;
            SqlParameterSource parameter = new MapSqlParameterSource()
                    .addValue("user_id", id);
            return jdbc.query(sql, parameter, usersExtractor);
        }

        @Override
        public Collection<User> findCommonFriends(Long id, Long otherId) {
            String sql = """
                    SELECT *
                    FROM USERS
                    WHERE USER_ID IN (
                       SELECT f.friend_id
                       FROM USERS AS u
                       LEFT JOIN FRIENDS AS f ON u.user_id = f.user_id
                       WHERE u.user_id = :user_id AND f.friend_id IN (
                           SELECT fr.friend_id
                           FROM USERS AS us
                           LEFT JOIN FRIENDS AS fr ON us.user_id = fr.user_id
                           WHERE us.user_id = :otherId
                       )
                    );
                    """;
            SqlParameterSource parameter = new MapSqlParameterSource()
                    .addValue("user_id", id)
                    .addValue("otherId", otherId);
            return jdbc.query(sql, parameter, usersExtractor);
        }

        @Override
        public void isUserNotExists(Long id) {
            if (getById(id).isEmpty()) {
                throw new NotFoundException("Пользователь с id = " + id + " не найден");
            }
        }
    }
}
