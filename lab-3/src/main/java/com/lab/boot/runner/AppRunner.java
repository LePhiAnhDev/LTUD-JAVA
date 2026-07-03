package com.lab.boot.runner;

import com.lab.boot.services.NotificationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {

    private final NotificationService notificationService;

    // Constructor Injection: Spring tiem NotificationService vao AppRunner.
    public AppRunner(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void run(String... args) {
        // Chay NGAY sau khi Spring Context san sang.
        System.out.println("\n=== App ready -- running tasks ===");
        notificationService.send("He thong da khoi dong thanh cong!");
        notificationService.send("Tat ca service san sang.");
        System.out.println("=================================\n");
    }
}
