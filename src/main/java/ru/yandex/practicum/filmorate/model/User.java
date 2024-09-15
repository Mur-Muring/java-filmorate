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
    private Long id;
    @Email
    @NotBlank(groups = WorkInterface.Create.class)
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9а-яА-Я._-]+$")
    @NotBlank(groups = WorkInterface.Create.class)
    private String login;
    private String name;
    @PastOrPresent
    @NotNull(groups = WorkInterface.Create.class)
    LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();
}
