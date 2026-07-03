package com.lab.lifecycle.managed;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class ManagedBean {

    public ManagedBean() {
        System.out.println(" [1] Constructor: object duoc tao");
    }

    @PostConstruct
    public void init() {
        // Chay SAU constructor + DI hoan tat
        // Dung de: khoi tao cache, ket noi DB, validate config...
        System.out.println(" [2] @PostConstruct: bean san sang");
    }

    public void doWork() {
        System.out.println(" [*] doWork() duoc goi");
    }

    @PreDestroy
    public void cleanup() {
        // Chay TRUOC khi bean bi huy (context.close())
        // Dung de: dong connection, flush buffer, giai phong resource...
        System.out.println(" [3] @PreDestroy: don dep truoc khi huy");
    }
}
