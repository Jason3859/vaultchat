package dev.jason.project.spring.vc_server.user_microservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.jason.project.spring.vc_server.core.model.User;

@FeignClient(name = "messaging-microservice", url = "http://localhost:9001/messaging")
public interface MessagingClient {

    @PostMapping("/notify-status")
    void notifyStatus(@RequestParam String uid, @RequestParam User.Status status);
}
