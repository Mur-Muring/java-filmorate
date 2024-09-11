package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

@Component
public class RatingStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RatingStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Rating> getAllMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Rating(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description")
        ));
    }

    public Rating getMpaById(Integer id) {
        Rating rating_mpa;
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE id = ?", id);
        if (mpaRows.first()) {
            rating_mpa = new Rating(
                    mpaRows.getInt("id"),
                    mpaRows.getString("name"),
                    mpaRows.getString("description")
            );
        } else {
            throw new NotFoundException("Рейтинг с id = " + id + " не найден!");
        }
        return rating_mpa;
    }
}
