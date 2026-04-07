package dev.jason.project.spring.vc_server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jason.project.spring.vc_server.domain.Logger;

@RestController
public class VcRestController {

    @GetMapping
    public String home() {
        return "Hello, World!";
    }

    @GetMapping("/stacktrace")
    public String stacktrace() {
        File logFile = Logger.getLogFile();
        StringBuilder stringBuilder = new StringBuilder();

        try (Scanner scanner = new Scanner(logFile)) {
            while (scanner.hasNext()) {
                stringBuilder.append(String.format("%s\n", scanner.nextLine()));
            }

            return new String(stringBuilder);
        } catch (FileNotFoundException ignored) {
            return "Log file not found";
        }
    }
}
