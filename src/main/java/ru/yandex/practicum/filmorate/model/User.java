package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    int id;
    @Email
    @NonNull
    String email;
    @Pattern(regexp = "^[a-zA-Z0-9а-яА-Я._-]+$")
    @NotBlank
    String login;
    String name;
    @PastOrPresent
    LocalDate birthday;

    public User() {
    }
}
