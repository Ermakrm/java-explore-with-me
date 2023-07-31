package ru.practicum.event.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.model.Category;
import ru.practicum.enums.State;
import ru.practicum.location.model.Location;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String description;
    Boolean paid;
    Integer participantLimit;
    @Enumerated(EnumType.STRING)
    State state;
    String annotation;
    Long confirmedRequests;
    Boolean requestModeration;
    Long views;
    LocalDateTime createdOn;
    LocalDateTime eventDate;
    LocalDateTime publishedOn;
    @OneToOne(targetEntity = Category.class)
    Category category;
    @OneToOne(targetEntity = User.class)
    User initiator;
    @OneToOne(targetEntity = Location.class)
    Location location;

}
