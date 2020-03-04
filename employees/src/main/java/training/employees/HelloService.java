package training.employees;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HelloService {

    private String hello;

    public HelloService(@Value("${employees.hello}") String hello) {
        this.hello = hello;
    }

    public String sayHello() {
        return hello + " " + LocalDateTime.now();
    }
}
