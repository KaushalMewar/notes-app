package io.km.notes.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection="notes")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Note {

    @Id
    private String id;
    private String description;
    private LocalDateTime dateTime;
}
