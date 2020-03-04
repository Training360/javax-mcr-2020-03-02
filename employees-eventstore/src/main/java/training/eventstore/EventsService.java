package training.eventstore;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventsService {

    private EventsRepository eventsRepository;

    private ModelMapper modelMapper;

    private ApplicationEventPublisher publisher;

    public EventsService(EventsRepository eventsRepository, ModelMapper modelMapper, ApplicationEventPublisher publisher) {
        this.eventsRepository = eventsRepository;
        this.modelMapper = modelMapper;
        this.publisher = publisher;
    }

    public EventDto createEvent(CreateEventCommand command, String source) {
        Event event = new Event(command.getMessage(), source);
        eventsRepository.save(event);
        log.debug("Event has been created with message: {}", event.getMessage());
        publisher.publishEvent(modelMapper.map(event, EventCreated.class));
        return modelMapper.map(event, EventDto.class);
    }

    public List<EventDto> listEvents() {
        return eventsRepository.findAll()
                .stream()
                .map(e -> modelMapper.map(e, EventDto.class))
                .collect(Collectors.toList());
    }

    public EventDto findEventById(long id) {
        return modelMapper.map(eventsRepository.findById(id), EventDto.class);
    }

}
