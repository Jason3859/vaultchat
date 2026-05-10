package dev.jason.project.spring.vc_server.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);
	
	private static final String ADMIN_SDK_FILE_NAME = "vaultchatapp.json";
	private static final String ADMIN_SDK_ENV_KEY = "FIREBASE_ADMIN_SDK_FILE_CONTENT";

	public static void initFirebase() throws IOException {
        InputStream stream;
        
        try {
        	var s = FirebaseConfig.class.getClassLoader().getResourceAsStream(ADMIN_SDK_FILE_NAME);
        	
        	Objects.requireNonNull(s);
        	
        	logger.info("`{}` file found. Initializing Firebase app now", ADMIN_SDK_FILE_NAME);
        	
        	stream = s;        	
        } catch (NullPointerException e) {
        	
        	logger.info("`{}` file not found. trying to fetch from env", ADMIN_SDK_FILE_NAME);
        	
        	String firebaseAdminSdkFileContent = System.getenv(ADMIN_SDK_ENV_KEY);
            
            if (firebaseAdminSdkFileContent == null) {
            	throw new AdminSdkNotFoundException();
            }
            
            logger.info("Found admin sdk in environment variables. Initializing Firebase app");
            
            stream = new ByteArrayInputStream(firebaseAdminSdkFileContent.getBytes());
		}
        
        
        FirebaseOptions options = FirebaseOptions.builder()
        	.setCredentials(GoogleCredentials.fromStream(stream))
        	.build();
        
        FirebaseApp.initializeApp(options);

        logger.info("Initialized Firebase app");
    }
}
