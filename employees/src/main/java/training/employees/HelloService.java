package training.employees;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

//@Service
public class HelloService {

    @Value("${employees.hello}")
    private String hello;

    public String sayHello() {
        return hello + " " + LocalDateTime.now();
    }
}
