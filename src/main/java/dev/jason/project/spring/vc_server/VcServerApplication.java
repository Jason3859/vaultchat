package dev.jason.project.spring.vc_server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import dev.jason.project.spring.vc_server.exception.VcException.AdminSdkNotFoundException;
import dev.jason.project.spring.vc_server.exception.VcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
public class VcServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(VcServerApplication.class);

    public static void main(@Nonnull String[] args) throws IOException, VcException {
        SpringApplication.run(VcServerApplication.class, args);
        initFirebase();
    }

    private static void initFirebase() throws IOException, AdminSdkNotFoundException {
        InputStream stream = VcServerApplication.class.getClassLoader().getResourceAsStream("vaultchatapp.json");

        try {
            assert stream != null;
            logger.info("`vaultchatapp.json` file found. Initializing Firebase app now");

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(stream))
                .build();

            FirebaseApp.initializeApp(options);
        } catch (NullPointerException ignored) {
            logger.info("`vaultchatapp.json` file not found. trying to fetch from env");

            String firebaseAdminSdkFileContent = System.getenv("FIREBASE_ADMIN_SDK_FILE_CONTENT");

            if (firebaseAdminSdkFileContent == null) {
                throw new AdminSdkNotFoundException();
            }

            logger.info("Found admin sdk in environment variables. Initializing Firebase app");
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(firebaseAdminSdkFileContent.getBytes())))
                .build();

            FirebaseApp.initializeApp(options);
        }

        logger.info("Initialized Firebase app");
    }
}
