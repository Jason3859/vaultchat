package dev.jason.project.spring.vc_server.user_microservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.jason.project.spring.vc_server.core.Endpoints;
import dev.jason.project.spring.vc_server.core.model.User;

@FeignClient(name = "messaging-microservice", url = Endpoints.MESSAGING_SERVICE_URL)
public interface MessagingClient {

    @PostMapping(Endpoints.MESSAGING_NOTIFY_STATUS)
    void notifyStatus(@RequestParam String uid, @RequestParam User.Status status);
}
