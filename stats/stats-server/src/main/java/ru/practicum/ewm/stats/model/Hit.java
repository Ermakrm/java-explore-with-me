package ru.practicum.ewm.stats.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String app;
    @Column(nullable = false)
    String uri;
    @Column(nullable = false, length = 15)
    String ip;
    @Column(nullable = false)
    LocalDateTime timestamp;
}
