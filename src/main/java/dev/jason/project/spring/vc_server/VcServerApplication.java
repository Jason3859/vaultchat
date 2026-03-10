package dev.jason.project.spring.vc_server;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@SpringBootApplication
@EnableMongoRepositories
public class VcServerApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(VcServerApplication.class, args);
        initFirebase();
    }

    public static void initFirebase() throws IOException {
        InputStream stream = VcServerApplication.class.getClassLoader().getResourceAsStream("vaultchatapp.json");

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(stream))
            .build();

        FirebaseApp.initializeApp(options);
    }
}
