package com.lab.lifecycle.singleton;

import org.springframework.stereotype.Service;

@Service
public class ServiceB {

    public ServiceB() {
        System.out.println("[INIT] ServiceB created");
    }

    public void assist() {
        System.out.println("   ServiceB.assist() called");
    }
}
