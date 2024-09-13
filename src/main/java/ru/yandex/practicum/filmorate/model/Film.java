package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import utils.NotBeforeDate;
import utils.WorkInterface;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Film {
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
