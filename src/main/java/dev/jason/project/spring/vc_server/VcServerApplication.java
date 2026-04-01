package dev.jason.project.spring.vc_server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import dev.jason.project.spring.vc_server.domain.exception.AdminSdkNotFoundException;
import dev.jason.project.spring.vc_server.domain.exception.VcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
@EnableMongoRepositories
public class VcServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(VcServerApplication.class);

    public static void main(String[] args) throws IOException, VcException {
        SpringApplication.run(VcServerApplication.class, args);
        initFirebase();
    }

    private static void initFirebase() throws IOException, VcException {
        InputStream stream = VcServerApplication.class.getClassLoader().getResourceAsStream("vaultchatapp.json");

        try {
            assert stream != null;
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(stream))
                .build();

            FirebaseApp.initializeApp(options);
        } catch (NullPointerException ignored) {
            logger.info("vaultchatapp.json file not found. trying to fetch from env");

            try {
                String firebaseAdminSdkFileContent = System.getenv("FIREBASE_ADMIN_SDK_FILE_CONTENT");

                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(firebaseAdminSdkFileContent.getBytes())))
                    .build();

                FirebaseApp.initializeApp(options);
            } catch (NullPointerException e) {
                throw new AdminSdkNotFoundException();
            }
        }
    }
}
