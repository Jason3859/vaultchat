package dev.jason.project.spring.vc_server;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VcServerApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(VcServerApplication.class, args);
    }
}
