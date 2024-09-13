package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import utils.WorkInterface;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class User {
    @NotNull(groups = WorkInterface.Update.class)
    Long id;
    @Email
    @NotBlank(groups = WorkInterface.Create.class)
    String email;
    @Pattern(regexp = "^[a-zA-Z0-9а-яА-Я._-]+$")
    @NotBlank(groups = WorkInterface.Create.class)
    String login;
    String name;
    @PastOrPresent
    @NotNull(groups = WorkInterface.Create.class)
    LocalDate birthday;
    Set<Integer> friends = new HashSet<>();
}
