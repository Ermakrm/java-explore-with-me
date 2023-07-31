package ru.practicum.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequest {
    @Length(min = 2, max = 250)
    @NotBlank
    String name;
    @Length(min = 6, max = 254)
    @Email
    @NotNull
    String email;
}
