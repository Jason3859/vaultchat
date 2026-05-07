package dev.jason.project.spring.vc_server.user_microservice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import dev.jason.project.spring.vc_server.user_microservice.exception.UserException.AdminSdkNotFoundException;

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
@EnableFeignClients
public class UserMicroserviceApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(UserMicroserviceApplication.class);

    static void main(String[] args) throws AdminSdkNotFoundException, IOException {
        SpringApplication.run(UserMicroserviceApplication.class, args);
        initFirebase();
    }
    
    private static void initFirebase() throws IOException, AdminSdkNotFoundException {
        InputStream stream = UserMicroserviceApplication.class.getClassLoader().getResourceAsStream("vaultchatapp.json");

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
