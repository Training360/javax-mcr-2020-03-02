package training.employees;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class EventStoreGateway {

    private RestTemplate restTemplate;

    private String url;

    public EventStoreGateway(RestTemplateBuilder restTemplateBuilder,
                             @Value("${employees.eventstore.url}") String url) {
        this.restTemplate = restTemplateBuilder.build();
        this.url = url;
    }

    public void sendEvent(CreateEventCommand event) {
        log.debug("Send event to eventstore");
        restTemplate.postForObject(url, event, String.class);
    }
}
