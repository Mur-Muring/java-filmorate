package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import utils.NotBeforeDate;
import utils.WorkInterface;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    @NotNull(groups = WorkInterface.Update.class)
    int id;
    @NotBlank(groups = WorkInterface.Create.class)
    String name;
    @NotBlank(groups = WorkInterface.Create.class)
    @Size(max = 200)
    String description;
    @NotBeforeDate
    @NotNull(groups = WorkInterface.Create.class)
    LocalDate releaseDate;
    @NotNull(groups = WorkInterface.Create.class)
    @Positive
    Long duration;
    Set<Integer> likes = new HashSet<>();
    Rating rating_mpa;
    Set<Genre> genres = new HashSet<>();


    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_Date", releaseDate);
        values.put("duration", duration);
        values.put("rating_id", rating_mpa.getId());
        return values;
    }
}
