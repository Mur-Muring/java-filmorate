package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int idGenerator = 1;

    @Override
    public Film createFilm(Film film) {
        film.setId(idGenerator++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм{}", film);
        return film;
    }


    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (newFilm.getName() != null) {
                oldFilm.setName(newFilm.getName());
            }
            if (newFilm.getDescription() != null) {
                oldFilm.setDescription(newFilm.getDescription());
            }
            if (newFilm.getDuration() != null) {
                oldFilm.setDuration(newFilm.getDuration());
            }
            if (newFilm.getReleaseDate() != null) {
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            }
            films.put(oldFilm.getId(), oldFilm);
            log.info("Обновлен фильм {}", newFilm);
            return oldFilm;
        }
        log.warn("id {} не найден", newFilm.getId());
        throw new ValidationException("id " + newFilm.getId() + " не найден");
    }

    @Override
    public Film deleteFilm(Integer id) {
        log.info("Удален фильм c id {}", id);
        return films.remove(id);
    }

    @Override
    public Film getFilmById(Integer id) {
        log.info("Поступил запрос на поиск фильма с id {}", id);
        Film film = films.get(id);
        if (film == null) {
            log.error("Фильм с id {} не найден", id);
            throw new ValidationException("Фильм с id " + id + " не найден");
        }
        log.info("Фильм с id {} успешно найден", id);
        return film;
    }
}
