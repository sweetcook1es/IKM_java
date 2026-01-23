package com.pechenka.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PechenkaRuApplication {

    public static void main(String[] args) {
        SpringApplication.run(PechenkaRuApplication.class, args);
        System.out.println("ПЕЧЕНЬКА.RU - ПАНЕЛЬ УПРАВЛЕНИЯ");
        System.out.println("Приложение запущено!");
        System.out.println("Доступно по адресу: http://localhost:8080");
    }
}
