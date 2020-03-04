package training.eventstore;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventCreated {

    private long id;

    private String source;

    private LocalDateTime time;

    private String message;
}
