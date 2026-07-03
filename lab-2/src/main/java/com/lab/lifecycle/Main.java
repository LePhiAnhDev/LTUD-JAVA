package com.lab.lifecycle;

import com.lab.lifecycle.config.AppConfig;
import com.lab.lifecycle.managed.ManagedBean;
import com.lab.lifecycle.prototype.RequestProcessor;
import com.lab.lifecycle.singleton.ServiceA;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Lab 2 - Debug Vong doi cua Bean.
 *
 * Ba buoc trong tai lieu dung CHUNG mot ApplicationContext: context duoc tao
 * mot lan o Buoc 1 va chi dong o cuoi cung (kich hoat @PreDestroy). Khong the
 * ghep noi 3 doan code roi vi moi doan khai bao lai bien 'ctx' va doan 2 dong
 * context truoc khi doan 3 dung lai no.
 */
public class Main {
    public static void main(String[] args) {

        // ============ Buoc 1: Thu tu khoi tao Bean ============
        // Cac singleton bean (ServiceA, ServiceB, ManagedBean) duoc tao NGAY khi
        // context khoi tao (eager). Spring tinh thu tu theo dependency graph:
        // ServiceA phu thuoc ServiceB  =>  ServiceB phai duoc tao truoc.
        // ManagedBean cung chay Constructor + @PostConstruct ngay tai day.
        // Rieng RequestProcessor la prototype nen CHUA duoc tao (im lang).
        System.out.println("--- Before context ---");
        var ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println("--- After context ---\n");

        ctx.getBean(ServiceA.class).doWork();

        // ============ Buoc 2: @PostConstruct & @PreDestroy ============
        // ManagedBean da san sang tu luc khoi tao context o tren; goi doWork().
        System.out.println("\n=== Vong doi Bean ===");
        var bean = ctx.getBean(ManagedBean.class);
        bean.doWork();

        // ============ Buoc 3: Singleton vs. Prototype ============
        System.out.println("\n=== Singleton ===");         // -> true
        var a1 = ctx.getBean(ServiceA.class);
        var a2 = ctx.getBean(ServiceA.class);
        System.out.println("a1 == a2 ? " + (a1 == a2));

        System.out.println("\n=== Prototype ===");
        var p1 = ctx.getBean(RequestProcessor.class);
        var p2 = ctx.getBean(RequestProcessor.class);
        var p3 = ctx.getBean(RequestProcessor.class);

        p1.process("Request A");
        p2.process("Request B");
        p3.process("Request C");

        System.out.println("p1 == p2 ? " + (p1 == p2));     // -> false
        System.out.println("p1 == p3 ? " + (p1 == p3));     // -> false

        // ============ Dong context -> kich hoat @PreDestroy ============
        System.out.println("\n--- Dong context (ctx.close()) ---");
        ctx.close();
    }
}
