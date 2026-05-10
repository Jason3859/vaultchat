package dev.jason.project.spring.vc_server.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

	public static void initFirebase() throws IOException {
        InputStream stream = FirebaseConfig.class.getClassLoader().getResourceAsStream("vaultchatapp.json");

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
