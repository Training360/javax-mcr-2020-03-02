package training.employees;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HelloServiceTest {

    @Test
    void sayHello() {
        assertThat(new HelloService("HELLO").sayHello()).startsWith("HELLO");
    }
}