package org.week04lab01;

import org.springframework.boot.SpringApplication;

public class TestWeek04Lab01Application {

    public static void main(String[] args) {
        SpringApplication.from(Week04Lab01Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}
