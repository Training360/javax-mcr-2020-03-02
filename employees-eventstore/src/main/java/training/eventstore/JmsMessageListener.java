package training.eventstore;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class JmsMessageListener {

    private EventsService eventsService;

    public JmsMessageListener(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @JmsListener(destination = "eventsQueue")
    public void processMessage(CreateEventCommand command) {
        eventsService.createEvent(command, "JMS");
    }

}
