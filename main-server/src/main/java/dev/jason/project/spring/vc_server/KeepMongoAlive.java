package dev.jason.project.spring.vc_server;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class KeepMongoAlive {
	
	private static final Logger logger = LoggerFactory.getLogger(KeepMongoAlive.class);
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Scheduled(initialDelay = 2000, fixedRate = 259200000)
	void pingMongoDb() {
		try {
            // Executes a simple low-overhead diagnostic ping
            Document pingResult = mongoTemplate.getDb().runCommand(new Document("ping", 1));
            logger.info("Database keep-alive ping successful: " + pingResult.toJson());
        } catch (Exception e) {
            logger.error("Database keep-alive ping failed: " + e.getMessage());
        }
	}
}
