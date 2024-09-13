package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.utils.NotBeforeDate;
import ru.yandex.practicum.filmorate.utils.WorkInterface;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @Null(groups = WorkInterface.Create.class)
    @NotNull(groups = WorkInterface.Update.class)
    Long id;
    @NotBlank
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
    @NotNull MpaRating mpa;
    @Builder.Default
    LinkedHashSet<Genre> genres = new LinkedHashSet<>();
}
