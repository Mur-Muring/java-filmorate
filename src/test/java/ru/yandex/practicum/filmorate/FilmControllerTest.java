package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ru.yandex.practicum.filmorate.model.Film;
import utils.WorkInterface;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class FilmControllerTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.afterPropertiesSet();
        validator = factoryBean.getValidator();
    }

    @Test
    void shouldPassWhenValidFilm() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120L);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, WorkInterface.Create.class);
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailWhenNameIsEmpty() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120L);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, WorkInterface.Create.class);
        assertThat(violations).hasSize(1);

        ConstraintViolation<Film> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void shouldFailWhenReleaseDateIsTooEarly() {
        Film film = new Film();
        film.setName("ame");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1700, 1, 1)); // Неверная дата
        film.setDuration(120L);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertThat(violations).hasSize(1);
    }

    @Test
    void shouldFailWhenDurationIsNegative() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(-100L); // Неверная длительность

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertThat(violations).hasSize(1);

        ConstraintViolation<Film> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("must be greater than 0");
    }
}