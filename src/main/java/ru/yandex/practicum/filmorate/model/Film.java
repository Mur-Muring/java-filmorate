package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import utils.NotBeforeDate;
import utils.WorkInterface;


import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
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
}
