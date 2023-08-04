package ru.practicum.comment.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String text;
    @Column(name = "publish_date")
    LocalDateTime publishDate;
    @Column(name = "update_date")
    LocalDateTime updateDate;
    @ManyToOne(targetEntity = User.class)
    User author;
    @ManyToOne(targetEntity = Event.class)
    Event event;
}
