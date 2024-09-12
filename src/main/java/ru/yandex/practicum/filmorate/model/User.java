package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import utils.WorkInterface;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotNull(groups = WorkInterface.Update.class)
    int id;
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
    Set<Integer> friends;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birthday", birthday);
        return values;
    }
}
