package training.eventstore;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    private static final String SOURCE_REST = "REST";

    public EventsService eventsService;

    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @GetMapping
    public List<EventDto> listEvents(String prefix) {
        return eventsService.listEvents();
    }

    @GetMapping("/{id}")
    public EventDto findEventById(@PathVariable("id") long id) {
        return eventsService.findEventById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@RequestBody CreateEventCommand command) {
        return eventsService.createEvent(command, SOURCE_REST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleNotFound() {
        //
    }
}
