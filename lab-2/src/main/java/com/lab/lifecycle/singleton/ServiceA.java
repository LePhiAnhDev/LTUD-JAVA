package com.lab.lifecycle.singleton;

import org.springframework.stereotype.Service;

@Service
public class ServiceA {

    private final ServiceB serviceB;

    // Constructor Injection: Spring tu dong tiem ServiceB vao day.
    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
        System.out.println("[INIT] ServiceA created (ServiceB injected)");
    }

    public void doWork() {
        System.out.println("ServiceA.doWork() ->");
        serviceB.assist();
    }
}
