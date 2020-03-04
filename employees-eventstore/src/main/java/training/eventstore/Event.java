package training.eventstore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String source;

    private LocalDateTime time;

    private String message;

    public Event(String message, String source) {
        this.source = source;
        this.time = LocalDateTime.now();
        this.message = message;
    }
}
