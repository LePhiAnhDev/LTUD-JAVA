package com.lab.lifecycle.prototype;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RequestProcessor {

    private static int counter = 0;
    private final int id;

    public RequestProcessor() {
        this.id = ++counter;
        System.out.println(" [NEW] RequestProcessor #" + id + " created");
    }

    public void process(String data) {
        System.out.println(" Processor #" + id + " -> " + data);
    }
}
