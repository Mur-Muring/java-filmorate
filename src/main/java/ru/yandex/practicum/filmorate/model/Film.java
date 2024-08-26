package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import utils.NotBeforeDate;
import utils.WorkInterface;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    @NotNull(groups = WorkInterface.Update.class)
    int id;
    @NotEmpty(groups = WorkInterface.Create.class)
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

    public void addLike(final Integer like) {
        likes.add(like);
    }

    public void removeLike(final Integer like) {
        likes.remove(like);
    }
}
