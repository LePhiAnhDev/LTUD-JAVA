package com.lab.boot.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Value("${app.notification.channel}")
    private String channel;

    @Value("${app.notification.from}")
    private String fromAddress;

    @Value("${app.notification.max-retries:3}") // gia tri mac dinh neu key khong ton tai
    private int maxRetries;

    @Value("${app.notification.enabled:true}")
    private boolean enabled;

    public void send(String message) {
        if (!enabled) {
            System.out.println("[WARN] Notification disabled.");
            return;
        }
        System.out.printf("[%s] From: %s | %s (retry=%d)%n",
                channel.toUpperCase(), fromAddress, message, maxRetries);
    }
}
